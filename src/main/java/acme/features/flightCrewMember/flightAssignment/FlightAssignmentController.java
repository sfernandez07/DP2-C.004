
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
	private FlightAssignmentListService listService;


	@PostConstruct
	protected void initialise() {
		// Se registra el comando básico "list" para la feature
		super.addBasicCommand("list", this.listService);

		// El comando publish se registra como custom, asignándole la semántica de "update"

	}
}
