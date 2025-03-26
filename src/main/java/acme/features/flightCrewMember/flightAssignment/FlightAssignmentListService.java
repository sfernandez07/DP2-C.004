
package acme.features.flightCrewMember.flightAssignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<FlightAssignment> planned = this.repository.findPlannedAssignmentsByFlightCrewMemberId(id);
		Collection<FlightAssignment> completed = this.repository.findCompletedAssignmentsByFlightCrewMemberId(id);

		Collection<FlightAssignment> allAssignments = new ArrayList<>();
		allAssignments.addAll(planned);
		allAssignments.addAll(completed);

		super.getBuffer().addData(allAssignments);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		// Se extraen solo las propiedades de FlightAssignment: duty, lastUpdate, status y remarks.
		Dataset dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks");

		// Se añade una propiedad adicional para indicar si la asignación es "planned" o "completed".
		// Esta lógica se basa en el scheduledDeparture del flightLeg, sin mostrar sus otros atributos.
		boolean isPlanned = assignment.getFlightLeg().getScheduledDeparture().after(new Date());
		dataset.put("assignmentType", isPlanned ? "planned" : "completed");

		super.getResponse().addData(dataset);
	}
}
