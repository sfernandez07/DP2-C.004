
package acme.features.airlineManager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.flights.FlightLeg;
import acme.entities.flights.SelfTransfer;
import acme.entities.flights.Status;
import acme.features.airlineManager.flightLeg.AirlineManagerFlightLegRepository;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightPublishService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AirlineManagerFlightRepository		repository;

	@Autowired
	private AirlineManagerFlightLegRepository	legRepository;

	// AbstractGuiService methods -------------------------------------------


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		AirlineManager currentUser = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		boolean isAuthorized = flight != null && flight.getManager().equals(currentUser) && flight.getStatus() == Status.NOT_READY;

		super.getResponse().setAuthorised(isAuthorized);
	}

	@Override
	public void load() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(flightId);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "selfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		int flightId = flight.getId();

		Collection<FlightLeg> flightLegs = this.legRepository.findFlightLegsByFlightId(flightId);
		super.state(!flightLegs.isEmpty(), "*", "acme.validation.airline-manager.flight.no-legs");

		boolean allLegsPublished = flightLegs.stream().allMatch(leg -> leg.getStatus() == Status.READY);
		super.state(allLegsPublished, "*", "acme.validation.airline-manager.flight.legs-not-published");

		if (!super.getBuffer().getErrors().hasErrors("selfTransfer")) {
			Integer layovers = flight.getLayovers();
			if (flight.getSelfTransfer() == SelfTransfer.NOT_SELF_TRANSFER && layovers > 0)
				super.state(false, "selfTransfer", "acme.validation.airline-manager.flight.invalid-selfTransfer-layovers");
		}
	}

	@Override
	public void perform(final Flight flight) {
		flight.setStatus(Status.READY);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset st;

		SelectChoices transChoices = SelectChoices.from(SelfTransfer.class, flight.getSelfTransfer());

		st = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "status");
		st.put("selfTransfer", transChoices.getSelected().getKey());
		st.put("selfTransfers", transChoices);

		st.put("scheduledDeparture", flight.getScheduledDeparture());
		st.put("scheduledArrival", flight.getScheduledArrival());
		st.put("originCity", flight.getOriginCity());
		st.put("destinationCity", flight.getDestinationCity());
		st.put("layovers", flight.getLayovers());

		st.put("id", flight.getId());
		super.getResponse().addData(st);
	}

}
