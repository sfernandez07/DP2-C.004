
package acme.features.flightCrewMember.flightCrewMember;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.FlightCrewMember;

@GuiController
public class CrewController extends AbstractGuiController<FlightCrewMember, FlightCrewMember> {

	@Autowired
	private CrewListService listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);

	}

}
