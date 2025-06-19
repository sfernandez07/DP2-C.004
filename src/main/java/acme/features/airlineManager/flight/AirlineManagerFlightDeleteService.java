
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
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightDeleteService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AirlineManagerFlightRepository flightRepo;

	// Abstract Service methods ---------------------------------------------


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight flightDetails = this.flightRepo.findFlightById(flightId);

		AirlineManager activeManager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();
		boolean isAuthorized = flightDetails != null && flightDetails.getManager().equals(activeManager) && flightDetails.isDraftMode();

		super.getResponse().setAuthorised(isAuthorized);
	}

	@Override
	public void load() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight flightDetails = this.flightRepo.findFlightById(flightId);

		super.getBuffer().addData(flightDetails);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "selfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		int assignments = this.flightRepo.countAssignmentsByFlightId(flight.getId());

		if (assignments > 0)
			super.state(false, "*", "acme.validation.airlineManager.flight.delete-has-assignments");
	}

	@Override
	public void perform(final Flight flight) {
		Collection<FlightLeg> flightLegs;

		flightLegs = this.flightRepo.findFlightLegsByFlightId(flight.getId());
		this.flightRepo.deleteAll(flightLegs);
		this.flightRepo.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		SelectChoices transferChoices;
		Dataset flightData;

		transferChoices = SelectChoices.from(SelfTransfer.class, flight.getSelfTransfer());

		flightData = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");
		flightData.put("selfTransfer", transferChoices.getSelected().getKey());
		flightData.put("selfTransfers", transferChoices);

		super.getResponse().addData(flightData);
	}
}
