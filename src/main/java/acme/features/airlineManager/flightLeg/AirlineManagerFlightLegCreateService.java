
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
import acme.entities.flights.Flight;
import acme.entities.flights.FlightLeg;
import acme.entities.flights.LegStatus;
import acme.entities.flights.SelfTransfer;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightLegCreateService extends AbstractGuiService<AirlineManager, FlightLeg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int master = super.getRequest().getData("masterId", int.class);
		Flight flight = this.repository.findFlightById(master);
		AirlineManager current = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();
		boolean status = flight != null && flight.getManager().equals(current) && flight.isDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int master = super.getRequest().getData("masterId", int.class);
		Flight flight = this.repository.findFlightById(master);

		FlightLeg fleg = new FlightLeg();
		fleg.setFlight(flight);
		fleg.setStatus(LegStatus.ON_TIME);
		fleg.setDraftMode(true);
		super.getBuffer().addData(fleg);
		super.getResponse().addGlobal("allowCreate", true);
	}

	@Override
	public void bind(final FlightLeg leg) {
		int dAId = super.getRequest().getData("departureAirport", int.class);
		int aAId = super.getRequest().getData("arrivalAirport", int.class);
		int aId = super.getRequest().getData("aircraft", int.class);

		Airport departure = this.repository.findAirportById(dAId);
		Airport arrival = this.repository.findAirportById(aAId);
		Aircraft aircraft = this.repository.findAircraftById(aId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
		leg.setDepartureAirport(departure);
		leg.setArrivalAirport(arrival);
		leg.setAircraft(aircraft);
	}

	@Override
	public void validate(final FlightLeg leg) {

		if (!super.getBuffer().getErrors().hasErrors("scheduledDeparture")) {
			boolean isPastOrPresent = MomentHelper.isPresentOrPast(leg.getScheduledDeparture());
			super.state(!isPastOrPresent, "scheduledDeparture", "acme.validation.airline-manager.leg.departure-in-the-past");
		}

		if (!super.getBuffer().getErrors().hasErrors("departureAirport")) {
			int flightId = leg.getFlight().getId();
			Collection<FlightLeg> existingfLegs = this.repository.findFlightLegsByFlightId(flightId);

			if (!existingfLegs.isEmpty()) {
				FlightLeg lastfLeg = existingfLegs.stream().max((l1, l2) -> l1.getScheduledDeparture().compareTo(l2.getScheduledDeparture())).orElse(null);

				boolean isConnected = lastfLeg != null && lastfLeg.getArrivalAirport().equals(leg.getDepartureAirport());

				super.state(isConnected, "departureAirport", "acme.validation.airline-manager.leg.not-connected-to-previous");
			}
		}

		if (!super.getBuffer().getErrors().hasErrors("departureAirport")) {
			boolean isNotSelfTransfer = leg.getFlight().getSelfTransfer() == SelfTransfer.NOT_SELF_TRANSFER;
			super.state(!isNotSelfTransfer, "*", "acme.validation.airline-manager.leg.cannot-add-leg-to-no-self-transfer");
		}

		if (!super.getBuffer().getErrors().hasErrors("arrivalAirport") && !super.getBuffer().getErrors().hasErrors("departureAirport")) {
			boolean sameAirport = leg.getDepartureAirport().equals(leg.getArrivalAirport());
			super.state(!sameAirport, "arrivalAirport", "acme.validation.airline-manager.leg.departure-equals-arrival");
		}

	}

	@Override
	public void perform(final FlightLeg leg) {
		super.state(leg != null, "*", "acme.validation.airline-manager.leg.invalid-request");
		super.state(leg.getFlight() != null, "*", "acme.validation.airline-manager.leg.invalid-flight");
		this.repository.save(leg);
	}

	@Override
	public void unbind(final FlightLeg leg) {
		Dataset st;
		SelectChoices statuses;
		SelectChoices dAChoices;
		SelectChoices aAChoices;
		SelectChoices aChoices;

		st = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");

		statuses = SelectChoices.from(LegStatus.class, leg.getStatus());
		st.put("status", statuses.getSelected().getKey());
		st.put("statuses", statuses);

		Collection<Airport> airports = this.repository.findAllAirports();
		Collection<Aircraft> aircrafts = this.repository.findAllAircrafts();

		dAChoices = SelectChoices.from(airports, "name", leg.getDepartureAirport());
		aAChoices = SelectChoices.from(airports, "name", leg.getArrivalAirport());
		aChoices = SelectChoices.from(aircrafts, "model", leg.getAircraft());

		if (leg.getDepartureAirport() != null)
			st.put("departureAirport", dAChoices.getSelected().getKey());

		if (leg.getArrivalAirport() != null)
			st.put("arrivalAirport", aAChoices.getSelected().getKey());

		if (leg.getAircraft() != null)
			st.put("aircraft", aChoices.getSelected().getKey());

		st.put("departureAirportChoices", dAChoices);
		st.put("arrivalAirportChoices", aAChoices);
		st.put("aircraftChoices", aChoices);
		st.put("masterId", leg.getFlight().getId());
		st.put("draftMode", leg.getFlight().isDraftMode());

		super.getResponse().addData(st);
	}
}
