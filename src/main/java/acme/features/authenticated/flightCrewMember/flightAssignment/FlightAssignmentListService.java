
package acme.features.authenticated.flightCrewMember.flightAssignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@Service
public class FlightAssignmentListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected FlightAssignmentRepository repository;


	@Override
	public void authorise() {
		// Se permite a cualquier FlightCrewMember acceder a sus asignaciones
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<FlightAssignment> planned = this.repository.findPlannedByFlightCrewMemberId(flightCrewMemberId);
		Collection<FlightAssignment> completed = this.repository.findCompletedByFlightCrewMemberId(flightCrewMemberId);
		// Se almacenan ambas colecciones en atributos globales del response
		super.getResponse().addGlobal("plannedAssignments", planned);
		super.getResponse().addGlobal("completedAssignments", completed);
	}

	@Override
	public void unbind(final Collection<FlightAssignment> assignments) {
		// Se transforman ambas colecciones en listas de Dataset para la vista
		@SuppressWarnings("unchecked")
		Collection<FlightAssignment> planned = (Collection<FlightAssignment>) super.getResponse().getGlobal("plannedAssignments");
		@SuppressWarnings("unchecked")
		Collection<FlightAssignment> completed = (Collection<FlightAssignment>) super.getResponse().getGlobal("completedAssignments");

		List<Dataset> plannedDatasets = new ArrayList<>();
		for (FlightAssignment fa : planned)
			plannedDatasets.add(super.unbindObject(fa, "flightLeg.flightNumber", "flightLeg.scheduledDeparture", "flightLeg.scheduledArrival", "duty", "lastUpdate", "status", "remarks"));
		List<Dataset> completedDatasets = new ArrayList<>();
		for (FlightAssignment fa : completed)
			completedDatasets.add(super.unbindObject(fa, "flightLeg.flightNumber", "flightLeg.scheduledDeparture", "flightLeg.scheduledArrival", "duty", "lastUpdate", "status", "remarks"));
		super.getResponse().addGlobal("plannedAssignments", plannedDatasets);
		super.getResponse().addGlobal("completedAssignments", completedDatasets);
	}
}
