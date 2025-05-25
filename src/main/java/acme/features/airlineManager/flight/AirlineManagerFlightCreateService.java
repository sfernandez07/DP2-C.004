
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
public class AirlineManagerFlightCreateService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository flightRepo;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		AirlineManager current = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();
		boolean status = current != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		AirlineManager manager;

		manager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();
		flight = new Flight();
		flight.setDraftMode(true);
		flight.setManager(manager);
		flight.setSelfTransfer(SelfTransfer.SELF_TRANSFER);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "selfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {

	}

	@Override
	public void perform(final Flight flight) {
		this.flightRepo.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset st;

		SelectChoices choices;
		choices = SelectChoices.from(SelfTransfer.class, flight.getSelfTransfer());

		st = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");
		st.put("selfTransfer", choices.getSelected().getKey());
		st.put("selfTransfers", choices);

		super.getResponse().addData(st);
	}
}
