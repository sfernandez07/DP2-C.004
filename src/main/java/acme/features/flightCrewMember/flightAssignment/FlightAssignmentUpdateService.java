
package acme.features.flightCrewMember.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findOneById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {

		super.bindObject(assignment, "duty", "flightLeg", "status", "remarks");

	}

	@Override
	public void validate(final FlightAssignment assignment) {

		//		// Verificar que el FlightCrewMember asignado tenga AvailabilityStatus AVAILABLE.
		//		super.state(assignment.getFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE), "flightCrewMember", "Only available crew members can be assigned.");
		//
		//		// Verificar que el crew member no esté asignado a múltiples FlightLegs simultáneamente.
		//		boolean alreadyAssigned = this.repository.isCrewMemberAssignedSimultaneously(assignment.getFlightCrewMember().getId(), assignment.getFlightLeg().getId());
		//		super.state(!alreadyAssigned, "flightCrewMember", "The crew member is already assigned to a leg at this time.");
		//
		//		// Para roles PILOT y CO_PILOT, verificar que no exista ya asignación para ese rol en el FlightLeg.
		//		if (assignment.getDuty() == Duty.PILOT || assignment.getDuty() == Duty.CO_PILOT) {
		//			boolean roleExists = this.repository.existsAssignmentForDutyInLeg(assignment.getFlightLeg().getId(), assignment.getDuty());
		//			super.state(!roleExists, "duty", "There is already an assignment for this duty in the flight leg.");
		//		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks");
		dataset.put("id", assignment.getId());
		super.getResponse().addData(dataset);
	}

}
