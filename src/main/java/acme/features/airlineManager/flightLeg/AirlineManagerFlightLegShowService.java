
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
import acme.entities.flights.Status;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightLegShowService extends AbstractGuiService<AirlineManager, FlightLeg> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AirlineManagerFlightLegRepository flightLegRepo;

	// AbstractGuiService methods -------------------------------------------


	@Override
	public void authorise() {
		int legIdentifier = super.getRequest().getData("id", int.class);
		FlightLeg flightLeg = this.flightLegRepo.findFlightLegById(legIdentifier);
		AirlineManager loggedInManager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		boolean isAuthorized = flightLeg != null && flightLeg.getFlight().getManager().equals(loggedInManager);

		super.getResponse().setAuthorised(isAuthorized);
	}

	@Override
	public void load() {
		int legIdentifier = super.getRequest().getData("id", int.class);
		FlightLeg flightLeg = this.flightLegRepo.findFlightLegById(legIdentifier);

		super.getBuffer().addData(flightLeg);
	}

	@Override
	public void unbind(final FlightLeg flightLeg) {
		Dataset st;

		st = super.unbindObject(flightLeg, "flightNumber", "scheduledDeparture", "scheduledArrival");

		SelectChoices flegStatuses = SelectChoices.from(LegStatus.class, flightLeg.getLegStatus());
		st.put("status", flegStatuses.getSelected().getKey());
		st.put("statuses", flegStatuses);

		Collection<Airport> allAirports = this.flightLegRepo.findAllAirports();
		SelectChoices depOptions = SelectChoices.from(allAirports, "name", flightLeg.getDepartureAirport());
		SelectChoices arrivalOptions = SelectChoices.from(allAirports, "name", flightLeg.getArrivalAirport());

		st.put("departureAirport", depOptions.getSelected().getKey());
		st.put("departureAirportChoices", depOptions);
		st.put("arrivalAirport", arrivalOptions.getSelected().getKey());
		st.put("arrivalAirportChoices", arrivalOptions);

		Collection<Aircraft> allAircrafts = this.flightLegRepo.findAllAircrafts();
		SelectChoices aOptions = SelectChoices.from(allAircrafts, "model", flightLeg.getAircraft());

		st.put("aircraft", aOptions.getSelected().getKey());
		st.put("aircraftChoices", aOptions);
		st.put("durationHours", flightLeg.getDurationHours());
		st.put("masterId", flightLeg.getFlight().getId());
		st.put("isNotReady", flightLeg.getFlight().getStatus() == Status.NOT_READY);

		super.getResponse().addData(st);
	}
}
