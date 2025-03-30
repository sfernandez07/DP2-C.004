
package acme.features.flightCrewMember.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.AssignmentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.flights.FlightLeg;
import acme.realms.AvailabilityStatus;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		// Se asume que el principal es el FlightCrewMember a asignar.
		FlightCrewMember crewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		FlightAssignment assignment = new FlightAssignment();
		assignment.setFlightCrewMember(crewMember);

		// Inicialmente, duty se deja sin valor (se asignará en bind) y remarks en blanco.
		assignment.setDuty(null);
		assignment.setRemarks("");
		// Estado inicial: PENDING (no publicada).
		assignment.setStatus(AssignmentStatus.PENDING);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		int flightLegId = super.getRequest().getData("flightLeg", int.class);
		FlightLeg flightLeg = this.repository.findFlightLegById(flightLegId);

		super.bindObject(assignment, "duty", "lastUpdate", "status", "remarks");
		assignment.setFlightLeg(flightLeg);
	}

	@Override
	public void validate(final FlightAssignment assignment) {

		// Restricción: El FlightCrewMember asignado debe tener AvailabilityStatus AVAILABLE.
		super.state(assignment.getFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE), "flightCrewMember", "Only available crew members can be assigned.");

		boolean alreadyAssigned = this.repository.isCrewMemberAssignedSimultaneously(assignment.getFlightCrewMember().getId(), assignment.getFlightLeg().getId());
		super.state(!alreadyAssigned, "flightCrewMember", "The crew member is already assigned to a leg at this time.");

		if (assignment.getDuty() == Duty.PILOT || assignment.getDuty() == Duty.CO_PILOT) {
			boolean roleExists = this.repository.existsAssignmentForDutyInLeg(assignment.getFlightLeg().getId(), assignment.getDuty());
			super.state(!roleExists, "duty", "There is already an assignment for this duty in the flight leg.");
		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks");
		dataset.put("flightLeg", assignment.getFlightLeg().getId());
		super.getResponse().addData(dataset);
	}

}
