
package acme.features.flightCrewMember.leg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flights.FlightLeg;
import acme.realms.FlightCrewMember;

@GuiController
public class FlightCrewMemberLegController extends AbstractGuiController<FlightCrewMember, FlightLeg> {

	@Autowired
	private FlightCrewMemberLegListService listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}

}
