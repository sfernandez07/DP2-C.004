
package acme.features.airlineManager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightListService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AirlineManagerFlightRepository flightRepo;

	// AbstractGuiService methods -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Flight> availableFlights;
		int airlineManagerId;

		airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		availableFlights = this.flightRepo.findFlightsByAirlineManagerId(airlineManagerId);

		super.getBuffer().addData(availableFlights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset flightData;

		flightData = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description");

		super.getResponse().addData(flightData);
	}
}
