
package acme.features.flightCrewMember.flightAssignment;

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
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		FlightAssignment assignment = new FlightAssignment();
		assignment.setDraftMode(false);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {

		super.bindObject(assignment, "duty", "status", "remarks", "flightCrewMember", "flightLeg");

	}

	@Override
	public void validate(final FlightAssignment assignment) {

		//		if (assignment.getStatus() != null)
		//			super.state(assignment.getFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE), "flightCrewMember", "Only available crew members can be assigned.");
		//
		//		if (assignment.getFlightLeg() != null) {
		//			boolean alreadyAssigned = this.repository.isCrewMemberAssignedSimultaneously(assignment.getFlightCrewMember().getId(), assignment.getFlightLeg().getId());
		//			super.state(!alreadyAssigned, "flightCrewMember", "The crew member is already assigned to a leg at this time.");
		//		}
		//
		//		if (assignment.getDuty() != null)
		//			if (assignment.getDuty() == Duty.PILOT || assignment.getDuty() == Duty.CO_PILOT) {
		//				boolean roleExists = this.repository.existsAssignmentForDutyInLeg(assignment.getFlightLeg().getId(), assignment.getDuty());
		//				super.state(!roleExists, "duty", "There is already an assignment for this duty in the flight leg.");
		//			}

	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		FlightCrewMember crewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		Dataset dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks");
		//status choices
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		dataset.put("statusChoices", statusChoices);
		//duty choices
		SelectChoices dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);

		// crew choices
		Collection<FlightCrewMember> crews = this.repository.findCrewMembersBySameAirline(crewMember.getAirline().getId());
		SelectChoices choicesCrew = SelectChoices.from(crews, "identity.fullName", null);
		dataset.put("crewChoices", choicesCrew);
		//flightLeg choices
		Collection<FlightLeg> flightLegs = this.repository.findFlightLegsByCrewMemberAirline(crewMember.getAirline().getId());
		SelectChoices legChoices = SelectChoices.from(flightLegs, "flightNumber", null);
		dataset.put("flightLegChoices", legChoices);

		super.getResponse().addData(dataset);
	}

}
