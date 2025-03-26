
package acme.features.flightCrewMember.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiController
public class FlightAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentListService	listService;

	@Autowired
	private FlightAssignmentShowService	showService;


	@PostConstruct
	protected void initialise() {
		// Se registra el comando básico "list" para la feature
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);

	}
}
