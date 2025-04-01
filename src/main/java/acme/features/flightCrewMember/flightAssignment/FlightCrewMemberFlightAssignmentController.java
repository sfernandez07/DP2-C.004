
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
	private FlightCrewMemberFlightAssignmentListService	listService;

	@Autowired
	private FlightAssignmentShowService					showService;

	@Autowired
	private FlightAssignmentCreateService				createService;

	@Autowired
	private FlightAssignmentDeleteService				deteteService;


	@PostConstruct
	protected void initialise() {
		System.out.println("Inicializando FlightAssignmentController");
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deteteService);

	}
}
