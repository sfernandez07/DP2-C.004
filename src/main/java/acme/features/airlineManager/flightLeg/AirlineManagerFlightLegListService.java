
package acme.features.airlineManager.flightLeg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.flights.FlightLeg;
import acme.entities.flights.Status;
import acme.features.airlineManager.flight.AirlineManagerFlightRepository;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightLegListService extends AbstractGuiService<AirlineManager, FlightLeg> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AirlineManagerFlightLegRepository	repository;

	@Autowired
	private AirlineManagerFlightRepository		flightRepository;

	// AbstractGuiService methods -------------------------------------------


	@Override
	public void authorise() {
		boolean isAuthorized;
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("masterId", int.class);
		flight = this.flightRepository.findFlightById(flightId);

		isAuthorized = flight != null && super.getRequest().getPrincipal().hasRealm(flight.getManager());

		super.getResponse().setAuthorised(isAuthorized);
	}

	@Override
	public void load() {
		Collection<FlightLeg> flightLegs;
		int flightId;

		flightId = super.getRequest().getData("masterId", int.class);
		flightLegs = this.repository.findFlightLegsByFlightId(flightId);

		super.getBuffer().addData(flightLegs);
	}

	@Override
	public void unbind(final FlightLeg flightLeg) {
		Dataset flightLegData;

		flightLegData = super.unbindObject(flightLeg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");

		super.getResponse().addData(flightLegData);
	}

	@Override
	public void unbind(final Collection<FlightLeg> flightLegs) {
		int flightId;
		Flight flight;
		final boolean shouldShowCreate;

		flightId = super.getRequest().getData("masterId", int.class);
		flight = this.flightRepository.findFlightById(flightId);
		shouldShowCreate = flight.getStatus() == Status.NOT_READY && super.getRequest().getPrincipal().hasRealm(flight.getManager());

		super.getResponse().addGlobal("masterId", flightId);
		super.getResponse().addGlobal("showCreate", shouldShowCreate);
	}
}
