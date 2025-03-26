
package acme.features.flightCrewMember.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	public void authorise() {
		int assignmentId = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findOneById(assignmentId);
		// Se autoriza si la asignación pertenece al FlightCrewMember autenticado.
		boolean status = assignment != null && assignment.getFlightCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findOneById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		// Se extraen las propiedades propias de FlightAssignment: duty, lastUpdate, status y remarks.
		Dataset dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks");
		// Se añaden datos asociados:
		// - employee code del FlightCrewMember
		// - flightNumber del FlightLeg
		dataset.put("flightCrewMemberEmployeeCode", assignment.getFlightCrewMember().getEmployeeCode());
		dataset.put("flightLegFlightNumber", assignment.getFlightLeg().getFlightNumber());
		super.getResponse().addData(dataset);
	}
}
