
package acme.features.airlineManager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.flights.SelfTransfer;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightShowService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AirlineManagerFlightRepository flightRepo;

	// AbstractGuiService methods -------------------------------------------


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight currentFlight = this.flightRepo.findFlightById(flightId);

		AirlineManager loggedInManager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();
		boolean isAuthorized = currentFlight != null && currentFlight.getManager().equals(loggedInManager);

		super.getResponse().setAuthorised(isAuthorized);
	}

	@Override
	public void load() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight currentFlight = this.flightRepo.findFlightById(flightId);

		super.getBuffer().addData(currentFlight);
	}

	@Override
	public void unbind(final Flight currentFlight) {
		Dataset st;

		SelectChoices transOptions = SelectChoices.from(SelfTransfer.class, currentFlight.getSelfTransfer());

		st = super.unbindObject(currentFlight, "tag", "selfTransfer", "cost", "description", "status");
		st.put("selfTransfer", transOptions.getSelected().getKey());
		st.put("selfTransfers", transOptions);
		st.put("scheduledDeparture", currentFlight.getScheduledDeparture());
		st.put("scheduledArrival", currentFlight.getScheduledArrival());
		st.put("originCity", currentFlight.getOriginCity());
		st.put("destinationCity", currentFlight.getDestinationCity());
		st.put("layovers", currentFlight.getLayovers());
		st.put("id", currentFlight.getId());
		super.getResponse().addData(st);
	}
}
