
package acme.features.flightCrewMember.flightLeg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.FlightLeg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberLegListService extends AbstractGuiService<FlightCrewMember, FlightLeg> {

	@Autowired
	private FlightCrewMemberLegRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		Collection<FlightLeg> legs = this.repository.findFlightLegByAssignmentId(id);
		System.out.println("Número de legs encontrados: " + legs.size());
		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final FlightLeg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");

		super.getResponse().addData(dataset);

	}

}
