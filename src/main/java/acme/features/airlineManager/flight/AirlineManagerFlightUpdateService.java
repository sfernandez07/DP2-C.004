
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
public class AirlineManagerFlightUpdateService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AirlineManagerFlightRepository flightRepo;

	// AbstractGuiService methods -------------------------------------------


	@Override
	public void authorise() {
		int flightIdentifier = super.getRequest().getData("id", int.class);
		Flight flightDetails = this.flightRepo.findFlightById(flightIdentifier);

		AirlineManager loggedInManager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();
		boolean isAuthorized = flightDetails != null && flightDetails.getManager().equals(loggedInManager) && flightDetails.isDraftMode();

		super.getResponse().setAuthorised(isAuthorized);
	}

	@Override
	public void load() {
		int flightIdentifier = super.getRequest().getData("id", int.class);
		Flight flightDetails = this.flightRepo.findFlightById(flightIdentifier);

		super.getBuffer().addData(flightDetails);
	}

	@Override
	public void bind(final Flight flightDetails) {
		super.bindObject(flightDetails, "tag", "selfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flightDetails) {
		if (!super.getBuffer().getErrors().hasErrors("selfTransfer")) {
			Integer layoverCount = flightDetails.getLayovers();
			if (flightDetails.getSelfTransfer() == SelfTransfer.NOT_SELF_TRANSFER && layoverCount > 0)
				super.state(false, "selfTransfer", "acme.validation.airline-manager.flight.invalid-selfTransfer-layovers");
		}
	}

	@Override
	public void perform(final Flight flightDetails) {
		this.flightRepo.save(flightDetails);
	}

	@Override
	public void unbind(final Flight flightDetails) {
		SelectChoices transChoices;
		Dataset st;

		transChoices = SelectChoices.from(SelfTransfer.class, flightDetails.getSelfTransfer());

		st = super.unbindObject(flightDetails, "tag", "selfTransfer", "cost", "description", "draftMode");
		st.put("selfTransfer", transChoices.getSelected().getKey());
		st.put("selfTransfers", transChoices);

		super.getResponse().addData(st);
	}
}
