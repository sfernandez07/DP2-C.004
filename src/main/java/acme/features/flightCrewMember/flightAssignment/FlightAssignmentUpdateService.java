
package acme.features.flightCrewMember.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.AssignmentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.AvailabilityStatus;
import acme.realms.FlightCrewMember;

@GuiService
@Service
public class FlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findOneById(id);
		// Sólo se permite actualizar si la asignación no está publicada (status != CONFIRMED)
		// y si el duty asignado es LEAD_ATTENDANT.
		boolean status = assignment != null && assignment.getStatus() != AssignmentStatus.CONFIRMED && assignment.getDuty() == Duty.LEAD_ATTENDANT;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findOneById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "remarks");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		// Se verifica que el duty actualizado sea LEAD_ATTENDANT.
		super.state(assignment.getDuty() == Duty.LEAD_ATTENDANT, "duty", "Only crew members with duty LEAD_ATTENDANT can update assignments.");

		// Verificar disponibilidad
		super.state(assignment.getFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE), "flightCrewMember", "Only available crew members can be assigned.");

		// Verificar asignación simultánea
		boolean alreadyAssigned = this.repository.isCrewMemberAssignedSimultaneously(assignment.getFlightCrewMember().getId(), assignment.getFlightLeg().getId());
		super.state(!alreadyAssigned, "flightCrewMember", "The crew member is already assigned to a leg at this time.");

		// Para PILOT/CO_PILOT se verifica que no exista ya asignación en el FlightLeg.
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
		dataset.put("id", assignment.getId());
		super.getResponse().addData(dataset);
	}
}
