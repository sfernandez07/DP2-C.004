
package acme.features.airlineManager.flight;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiController
public class AirlineManagerFlightController extends AbstractGuiController<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightListService		listService;

	@Autowired
	private AirlineManagerFlightCreateService	createService;

	@Autowired
	private AirlineManagerFlightUpdateService	updateService;

	@Autowired
	private AirlineManagerFlightDeleteService	deleteService;

	@Autowired
	private AirlineManagerFlightShowService		showService;

	@Autowired
	private AirlineManagerFlightPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("show", this.showService);
		super.addCustomCommand("publish", "update", this.publishService);

	}

}
