
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
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {

		String method;
		boolean status;

		method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = true;
		else {
			String duty;
			String currentStatus;
			boolean correctDuty;
			boolean correctStatus;
			int legId;
			int version;
			int id;

			FlightLeg leg;

			legId = super.getRequest().getData("flightLeg", int.class);
			leg = this.repository.findPublishedLegById(legId);

			version = super.getRequest().getData("version", int.class);
			id = super.getRequest().getData("id", int.class);

			duty = super.getRequest().getData("duty", String.class);
			currentStatus = super.getRequest().getData("status", String.class);

			correctDuty = "0".equals(duty) || Arrays.stream(Duty.values()).map(Duty::name).anyMatch(name -> name.equals(duty));
			correctStatus = "0".equals(currentStatus) || Arrays.stream(AssignmentStatus.values()).map(AssignmentStatus::name).anyMatch(name -> name.equals(currentStatus));

			status = (legId == 0 || leg != null) && id == 0 && version == 0 && correctDuty && correctStatus;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		flightAssignment = new FlightAssignment();
		flightAssignment.setFlightCrewMember(flightCrewMember);
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
		flightAssignment.setDraftMode(false);
		super.getBuffer().addData(flightAssignment);

	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {

		super.bindObject(flightAssignment, "duty", "status", "remarks", "flightLeg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {

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

		legs = this.repository.findAllFutureLegs();
		legChoice = SelectChoices.from(legs, "flightNumber", flightAssignment.getFlightLeg());

		flightCrewMembers = this.repository.findAllFlightCrewMembers();
		flightCrewMemberChoice = SelectChoices.from(flightCrewMembers, "employeeCode", flightAssignment.getFlightCrewMember());

		dataset = super.unbindObject(flightAssignment, "duty", "lastUpdate", "status", "remarks", "draftmode", "flightLeg", "flightCrewMember");
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("legChoice", legChoice);
		dataset.put("flightCrewMemberChoice", flightCrewMemberChoice);

		super.getResponse().addData(dataset);
	}

}
