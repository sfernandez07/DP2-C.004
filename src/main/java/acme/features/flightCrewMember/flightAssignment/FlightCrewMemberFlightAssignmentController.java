
package acme.features.flightCrewMember.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiController
public class FlightCrewMemberFlightAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentCompletedLegsAssignmentListService	listCompletedService;

	@Autowired
	private FlightAssignmentPlannedLegsAssignmentListService	listPlannedService;

	@Autowired
	private FlightAssignmentShowService							showService;

	@Autowired
	private FlightAssignmentCreateService						createService;

	@Autowired
	private FlightAssignmentDeleteService						deteteService;

	@Autowired
	private FlightAssignmentUpdateService						updateService;

	@Autowired
	private FlightAssignmentPublishService						publishService;


	@PostConstruct
	protected void initialise() {
		System.out.println("Inicializando FlightAssignmentController");
		super.addCustomCommand("completed-list", "list", this.listCompletedService);
		super.addCustomCommand("planned-list", "list", this.listPlannedService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deteteService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);

	}
}
