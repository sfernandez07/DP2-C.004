
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getPrincipal().getActiveRealm().getId();
		System.out.println("ID del usuario activo: " + id);
		Collection<FlightAssignment> flightAssignments = this.repository.findFlightAssignmentsByCrewId(id);
		System.out.println("Número de FlightAssignments encontrados: " + flightAssignments.size());
		super.getBuffer().addData(flightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks");

		super.getResponse().addData(dataset);
	}
}
