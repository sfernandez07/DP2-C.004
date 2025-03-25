
package acme.features.authenticated.flightCrewMember.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@Service
public class FlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected FlightAssignmentRepository repository;


	@Override
	public void authorise() {
		int assignmentId = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findOneById(assignmentId);
		// Se autoriza si la asignación pertenece al FlightCrewMember autenticado
		boolean status = assignment != null && assignment.getFlightCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
		;
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
		// Se incluyen detalles del tramo (FlightLeg) y del miembro de la tripulación (FlightCrewMember)
		Dataset dataset = super.unbindObject(assignment, "flightLeg.flightNumber", "flightLeg.scheduledDeparture", "flightLeg.scheduledArrival", "flightLeg.status", "flightCrewMember.employeeCode", "flightCrewMember.phoneNumber",
			"flightCrewMember.languageSkills", "duty", "lastUpdate", "status", "remarks");
		super.getResponse().addData(dataset);
	}
}
