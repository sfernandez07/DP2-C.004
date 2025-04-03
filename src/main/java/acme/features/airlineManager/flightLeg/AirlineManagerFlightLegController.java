
package acme.features.airlineManager.flightLeg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flights.FlightLeg;
import acme.realms.AirlineManager;

@GuiController
public class AirlineManagerFlightLegController extends AbstractGuiController<AirlineManager, FlightLeg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightLegListService		listService;

	@Autowired
	private AirlineManagerFlightLegCreateService	createService;

	@Autowired
	private AirlineManagerFlightLegUpdateService	updateService;

	@Autowired
	private AirlineManagerFlightLegDeleteService	deleteService;

	@Autowired
	private AirlineManagerFlightLegShowService		showService;

	@Autowired
	private AirlineManagerFlightLegPublishService	publishService;

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
