
package acme.features.airlineManager.flightLeg;

import java.util.Collection;

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
public class AirlineManagerFlightLegDeleteService extends AbstractGuiService<AirlineManager, FlightLeg> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AirlineManagerFlightLegRepository flightLegRepo;

	// AbstractGuiService methods -------------------------------------------


	@Override
	public void authorise() {
		int flightLegId = super.getRequest().getData("id", int.class);
		FlightLeg flightLeg = this.flightLegRepo.findFlightLegById(flightLegId);
		AirlineManager activeManager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		boolean isAuthorized = flightLeg != null && flightLeg.getFlight().getManager().equals(activeManager) && flightLeg.isDraftMode();

		super.getResponse().setAuthorised(isAuthorized);
	}

	@Override
	public void load() {
		int flightLegId = super.getRequest().getData("id", int.class);
		FlightLeg flightLeg = this.flightLegRepo.findFlightLegById(flightLegId);

		super.getBuffer().addData(flightLeg);
	}

	@Override
	public void bind(final FlightLeg flightLeg) {
		int departureAirportId = super.getRequest().getData("departureAirport", int.class);
		int arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		int aircraftId = super.getRequest().getData("aircraft", int.class);
		String statusString = super.getRequest().getData("status", String.class);

		Airport departureAirport = this.flightLegRepo.findAirportById(departureAirportId);
		Airport arrivalAirport = this.flightLegRepo.findAirportById(arrivalAirportId);
		Aircraft aircraft = this.flightLegRepo.findAircraftById(aircraftId);

		super.bindObject(flightLeg, "flightNumber", "scheduledDeparture", "scheduledArrival");
		flightLeg.setDepartureAirport(departureAirport);
		flightLeg.setArrivalAirport(arrivalAirport);
		flightLeg.setAircraft(aircraft);
		flightLeg.setStatus(LegStatus.valueOf(statusString));
	}

	@Override
	public void validate(final FlightLeg flightLeg) {

	}

	@Override
	public void perform(final FlightLeg flightLeg) {
		this.flightLegRepo.delete(flightLeg);
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

		Collection<Airport> airports = this.flightLegRepo.findAllAirports();
		Collection<Aircraft> aircraft = this.flightLegRepo.findAllAircrafts();

		dAChoices = SelectChoices.from(airports, "name", flightLeg.getDepartureAirport());
		aAChoices = SelectChoices.from(airports, "name", flightLeg.getArrivalAirport());
		aChoices = SelectChoices.from(aircraft, "model", flightLeg.getAircraft());

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
