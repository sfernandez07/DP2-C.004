
package acme.features.airlineManager.flightLeg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.FlightLeg;
import acme.entities.flights.LegStatus;
import acme.entities.flights.Status;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightLegUpdateService extends AbstractGuiService<AirlineManager, FlightLeg> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AirlineManagerFlightLegRepository legRepo;

	// AbstractGuiService methods -------------------------------------------


	@Override
	public void authorise() {
		int legIdentifier = super.getRequest().getData("id", int.class);
		FlightLeg legDetails = this.legRepo.findFlightLegById(legIdentifier);
		AirlineManager loggedInManager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		boolean isAuthorized = legDetails != null && legDetails.getFlight().getManager().equals(loggedInManager) && legDetails.getFlight().getStatus() == Status.NOT_READY && legDetails.getStatus() == Status.NOT_READY;

		super.getResponse().setAuthorised(isAuthorized);
	}

	@Override
	public void load() {
		int legIdentifier = super.getRequest().getData("id", int.class);
		FlightLeg legDetails = this.legRepo.findFlightLegById(legIdentifier);

		super.getBuffer().addData(legDetails);
	}

	@Override
	public void bind(final FlightLeg legDetails) {
		int departureId = super.getRequest().getData("departureAirport", int.class);
		int arrivalId = super.getRequest().getData("arrivalAirport", int.class);
		int aircraftId = super.getRequest().getData("aircraft", int.class);
		String statusValue = super.getRequest().getData("legStatus", String.class);

		Airport departure = this.legRepo.findAirportById(departureId);
		Airport arrival = this.legRepo.findAirportById(arrivalId);
		Aircraft aircraft = this.legRepo.findAircraftById(aircraftId);

		super.bindObject(legDetails, "flightNumber", "scheduledDeparture", "scheduledArrival");
		legDetails.setDepartureAirport(departure);
		legDetails.setArrivalAirport(arrival);
		legDetails.setAircraft(aircraft);
		legDetails.setLegStatus(LegStatus.valueOf(statusValue));
	}

	@Override
	public void validate(final FlightLeg legDetails) {

		if (!super.getBuffer().getErrors().hasErrors("scheduledDeparture")) {
			boolean isFutureDate = MomentHelper.isPresentOrPast(legDetails.getScheduledDeparture());
			super.state(!isFutureDate, "scheduledDeparture", "acme.validation.airline-manager.leg.departure-in-the-past");
		}

		if (!super.getBuffer().getErrors().hasErrors("arrivalAirport") && !super.getBuffer().getErrors().hasErrors("departureAirport")) {
			boolean sameAirport = legDetails.getDepartureAirport().equals(legDetails.getArrivalAirport());
			super.state(!sameAirport, "arrivalAirport", "acme.validation.airline-manager.leg.departure-equals-arrival");
		}

	}

	@Override
	public void perform(final FlightLeg legDetails) {
		this.legRepo.save(legDetails);
	}

	@Override
	public void unbind(final FlightLeg legDetails) {
		Dataset st;
		SelectChoices statuses;
		SelectChoices dAChoices;
		SelectChoices aAChoices;
		SelectChoices aChoices;

		st = super.unbindObject(legDetails, "flightNumber", "scheduledDeparture", "scheduledArrival");

		statuses = SelectChoices.from(LegStatus.class, legDetails.getLegStatus());
		st.put("status", statuses.getSelected().getKey());
		st.put("statuses", statuses);

		Collection<Airport> airports = this.legRepo.findAllAirports();
		Collection<Aircraft> aircrafts = this.legRepo.findAllAircrafts();

		dAChoices = SelectChoices.from(airports, "name", legDetails.getDepartureAirport());
		aAChoices = SelectChoices.from(airports, "name", legDetails.getArrivalAirport());
		aChoices = SelectChoices.from(aircrafts, "model", legDetails.getAircraft());

		if (legDetails.getDepartureAirport() != null)
			st.put("departureAirport", dAChoices.getSelected().getKey());

		if (legDetails.getArrivalAirport() != null)
			st.put("arrivalAirport", aAChoices.getSelected().getKey());

		if (legDetails.getAircraft() != null)
			st.put("aircraft", aChoices.getSelected().getKey());

		st.put("departureAirportChoices", dAChoices);
		st.put("arrivalAirportChoices", aAChoices);
		st.put("aircraftChoices", aChoices);
		st.put("masterId", legDetails.getFlight().getId());
		st.put("isNotReady", legDetails.getFlight().getStatus() == Status.NOT_READY);

		super.getResponse().addData(st);
	}
}
