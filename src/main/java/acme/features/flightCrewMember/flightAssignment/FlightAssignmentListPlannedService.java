
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentListPlannedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	//Internal state ---------------------------------------------

	@Autowired
	private FlightAssignmentRepository repository;

	//AbstractGuiService interface -------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> flightAssignments;
		int flightCrewMemberId;

		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		flightAssignments = this.repository.findPlannedFlightAssignmentsByMemberId(flightCrewMemberId);

		super.getBuffer().addData(flightAssignments);

	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		dataset = super.unbindObject(flightAssignment, "duty", "lastUpdateMoment", "currentStatus", "publish");

		super.addPayload(dataset, flightAssignment, "publish");

		super.getResponse().addData(dataset);
	}

}
