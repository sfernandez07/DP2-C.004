
package acme.features.flightCrewMember.flightAssignment;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.AssignmentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.flights.FlightLeg;
import acme.realms.AvailabilityStatus;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {

		FlightAssignment flightAssignment;
		int flightAssignmentId;
		int flightCrewMemberId;
		boolean status;

		flightAssignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = flightAssignment != null && !flightAssignment.isDraftMode() && flightAssignment.getFlightCrewMember().getId() == flightCrewMemberId;

		if (status) {
			String method;

			method = super.getRequest().getMethod();

			if (method.equals("GET"))
				status = true;
			else {
				String duty;
				String currentStatus;
				boolean correctDuty;
				boolean correctStatus;
				int legId;

				FlightLeg leg;

				legId = super.getRequest().getData("flightLeg", int.class);
				leg = this.repository.findPublishedLegById(legId);

				duty = super.getRequest().getData("duty", String.class);
				currentStatus = super.getRequest().getData("status", String.class);

				correctDuty = "0".equals(duty) || Arrays.stream(Duty.values()).map(Duty::name).anyMatch(name -> name.equals(duty));
				correctStatus = "0".equals(currentStatus) || Arrays.stream(AssignmentStatus.values()).map(AssignmentStatus::name).anyMatch(name -> name.equals(currentStatus));

				status = (legId == 0 || leg != null) && correctDuty && correctStatus;
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		super.bindObject(flightAssignment, "duty", "status", "remarks", "flightLeg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		int flightCrewMemberId;

		boolean completedLeg;
		boolean availableMember;
		boolean legsOverlap;

		FlightLeg leg;

		Collection<FlightAssignment> OverlappedLegs;

		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		leg = flightAssignment.getFlightLeg();

		if (leg != null) {
			completedLeg = leg.getScheduledArrival().before(MomentHelper.getCurrentMoment());
			super.state(!completedLeg, "*", "acme.validation.flightassignment.leg.completed.message");
		}

		availableMember = this.repository.findFlightCrewMemberById(flightCrewMemberId).getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
		super.state(availableMember, "*", "acme.validation.flightassignment.flightcrewmember.available.message");

		if (flightAssignment.getDuty() != null && flightAssignment.getFlightLeg() != null)
			if (flightAssignment.getDuty() == Duty.PILOT) {
				int count = this.repository.hasDutyAssigned(flightAssignment.getFlightLeg().getId(), flightAssignment.getDuty(), flightAssignment.getId());
				super.state(count == 0, "*", "acme.validation.flightassignment.duty.pilot.message");
			} else if (flightAssignment.getDuty() == Duty.CO_PILOT) {
				int count = this.repository.hasDutyAssigned(flightAssignment.getFlightLeg().getId(), flightAssignment.getDuty(), flightAssignment.getId());
				super.state(count == 0, "*", "acme.validation.flightassignment.duty.copilot.message");
			}

		if (leg != null) {
			OverlappedLegs = this.repository.findFlightAssignmentsByFlightCrewMemberInRange(flightCrewMemberId, leg.getScheduledDeparture(), leg.getScheduledArrival());
			legsOverlap = OverlappedLegs.isEmpty();
			super.state(legsOverlap, "*", "acme.validation.flightassignment.leg.overlap.message");
		}
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		flightAssignment.setDraftMode(true);
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;
		SelectChoices dutyChoice;
		SelectChoices currentStatusChoice;

		SelectChoices legChoice;
		Collection<FlightLeg> legs;

		SelectChoices flightCrewMemberChoice;
		Collection<FlightCrewMember> flightCrewMembers;

		dutyChoice = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		currentStatusChoice = SelectChoices.from(AssignmentStatus.class, flightAssignment.getStatus());

		legs = this.repository.findAllLegs();
		legChoice = SelectChoices.from(legs, "flightNumber", flightAssignment.getFlightLeg());

		flightCrewMembers = this.repository.findAllFlightCrewMembers();
		flightCrewMemberChoice = SelectChoices.from(flightCrewMembers, "employeeCode", flightAssignment.getFlightCrewMember());

		dataset = super.unbindObject(flightAssignment, "duty", "lastUpdat", "status", "remarks", "draftMode", "flightLeg", "flightCrewMember");
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("legChoice", legChoice);
		dataset.put("flightCrewMemberChoice", flightCrewMemberChoice);

		super.getResponse().addData(dataset);
	}

}
