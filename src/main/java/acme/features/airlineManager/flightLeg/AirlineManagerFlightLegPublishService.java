
package acme.features.airlineManager.flightLeg;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.FlightLeg;
import acme.entities.flights.LegStatus;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightLegPublishService extends AbstractGuiService<AirlineManager, FlightLeg> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AirlineManagerFlightLegRepository repository;

	// AbstractGuiService methods -------------------------------------------


	@Override
	public void authorise() {
		int flightLegId = super.getRequest().getData("id", int.class);
		FlightLeg flightLeg = this.repository.findFlightLegById(flightLegId);
		AirlineManager loggedInManager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		boolean isAuthorized = flightLeg != null && flightLeg.getFlight().getManager().equals(loggedInManager) && flightLeg.getFlight().isDraftMode() && flightLeg.isDraftMode();

		super.getResponse().setAuthorised(isAuthorized);
	}

	@Override
	public void load() {
		int flightLegId = super.getRequest().getData("id", int.class);
		FlightLeg flightLeg = this.repository.findFlightLegById(flightLegId);
		super.getBuffer().addData(flightLeg);
	}

	@Override
	public void bind(final FlightLeg flightLeg) {
		int departureId = super.getRequest().getData("departureAirport", int.class);
		int arrivalId = super.getRequest().getData("arrivalAirport", int.class);
		int aircraftId = super.getRequest().getData("aircraft", int.class);
		String statusString = super.getRequest().getData("status", String.class);

		Airport departure = this.repository.findAirportById(departureId);
		Airport arrival = this.repository.findAirportById(arrivalId);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(flightLeg, "flightNumber", "scheduledDeparture", "scheduledArrival");
		flightLeg.setDepartureAirport(departure);
		flightLeg.setArrivalAirport(arrival);
		flightLeg.setAircraft(aircraft);
		flightLeg.setStatus(LegStatus.valueOf(statusString));
	}

	@Override
	public void validate(final FlightLeg flightLeg) {
		boolean isValidStatus = flightLeg.getStatus() == LegStatus.ON_TIME || flightLeg.getStatus() == LegStatus.DELAYED;
		super.state(isValidStatus, "status", "acme.validation.airline-manager.leg.invalid-status-on-publish");

		List<FlightLeg> flightLegs = this.repository.findFlightLegsByFlightId(flightLeg.getFlight().getId()).stream().toList();

		for (int i = 0; i < flightLegs.size() - 1; i++) {
			FlightLeg currentLeg = flightLegs.get(i);
			FlightLeg nextLeg = flightLegs.get(i + 1);

			boolean areConnected = currentLeg.getArrivalAirport().equals(nextLeg.getDepartureAirport());
			super.state(areConnected, "*", "acme.validation.airline-manager.leg.legs-not-connected");
		}
	}

	@Override
	public void perform(final FlightLeg flightLeg) {
		flightLeg.setDraftMode(false);
		this.repository.save(flightLeg);
	}

	@Override
	public void unbind(final FlightLeg flightLeg) {
		Dataset st;
		SelectChoices statuses;
		SelectChoices dAChoices;
		SelectChoices aAChoices;
		SelectChoices aChoices;

		st = super.unbindObject(flightLeg, "flightNumber", "scheduledDeparture", "scheduledArrival");

		statuses = SelectChoices.from(LegStatus.class, flightLeg.getStatus());
		st.put("status", statuses.getSelected().getKey());
		st.put("statuses", statuses);

		Collection<Airport> airports = this.repository.findAllAirports();
		Collection<Aircraft> aircrafts = this.repository.findAllAircrafts();

		dAChoices = SelectChoices.from(airports, "name", flightLeg.getDepartureAirport());
		aAChoices = SelectChoices.from(airports, "name", flightLeg.getArrivalAirport());
		aChoices = SelectChoices.from(aircrafts, "model", flightLeg.getAircraft());

		if (flightLeg.getDepartureAirport() != null)
			st.put("departureAirport", dAChoices.getSelected().getKey());

		if (flightLeg.getArrivalAirport() != null)
			st.put("arrivalAirport", aAChoices.getSelected().getKey());

		if (flightLeg.getAircraft() != null)
			st.put("aircraft", aChoices.getSelected().getKey());

		st.put("departureAirportChoices", dAChoices);
		st.put("arrivalAirportChoices", aAChoices);
		st.put("aircraftChoices", aChoices);
		st.put("masterId", flightLeg.getFlight().getId());
		st.put("draftMode", flightLeg.isDraftMode());

		super.getResponse().addData(st);
	}

}
