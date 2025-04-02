
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.AssignmentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.flights.FlightLeg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findOneById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "flightCrewMember", "flightLeg", "draftMode");
		//status choices
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		dataset.put("statusChoices", statusChoices);
		//duty choices
		SelectChoices dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);

		// crew choices
		Collection<FlightCrewMember> crews = this.repository.findCrewMembersBySameAirline(assignment.getFlightCrewMember().getAirline().getId());
		SelectChoices choicesCrew = SelectChoices.from(crews, "identity.fullName", assignment.getFlightCrewMember());
		dataset.put("crewChoices", choicesCrew);
		//flightLeg choices
		Collection<FlightLeg> flightLegs = this.repository.findFlightLegsByCrewMemberAirline(assignment.getFlightCrewMember().getAirline().getId());
		SelectChoices legChoices = SelectChoices.from(flightLegs, "flightNumber", assignment.getFlightLeg());
		dataset.put("flightLegChoices", legChoices);

		super.getResponse().addData(dataset);
	}

}
