
package acme.features.flightCrewMember.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class FlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	public void authorise() {
		// Permitir la operación, se validará el duty en el validate
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment assignment = new FlightAssignment();
		// Recibimos "masterId" para identificar el FlightLeg
		int flightLegId = super.getRequest().getData("masterId", int.class);
		// No existe FlightLegRepository, por lo que instanciamos un objeto FlightLeg con su id.
		FlightLeg flightLeg = new FlightLeg();
		flightLeg.setId(flightLegId);
		assignment.setFlightLeg(flightLeg);

		// Se espera recibir "crewMemberId" para identificar al FlightCrewMember a asignar.
		int crewMemberId = super.getRequest().getData("crewMemberId", int.class);
		// En un entorno real se recuperaría el FlightCrewMember mediante su repository.
		// Aquí, para el ejemplo, se asume que el principal es el crew member a asignar.
		FlightCrewMember crewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		// Si el id del principal no coincide, se podría lanzar error (aquí se simplifica).
		assignment.setFlightCrewMember(crewMember);

		// Inicialmente, duty se deja sin valor (se asigna en bind) y remarks en blanco.
		assignment.setDuty(null);
		assignment.setRemarks("");
		// Estado inicial: PENDING (no publicada)
		assignment.setStatus(AssignmentStatus.PENDING);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		// Se vinculan los atributos duty y remarks
		super.bindObject(assignment, "duty", "remarks");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		// Restricción: Sólo se permiten operaciones si el duty asignado es LEAD_ATTENDANT.
		super.state(assignment.getDuty() == Duty.LEAD_ATTENDANT, "duty", "Only crew members with duty LEAD_ATTENDANT can perform this operation.");

		// Restricción: El FlightCrewMember asignado debe tener AvailabilityStatus AVAILABLE.
		super.state(assignment.getFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE), "flightCrewMember", "Only available crew members can be assigned.");

		// Restricción: No asignar a un crew member a múltiples legs simultáneamente.
		boolean alreadyAssigned = this.repository.isCrewMemberAssignedSimultaneously(assignment.getFlightCrewMember().getId(), assignment.getFlightLeg().getId());
		super.state(!alreadyAssigned, "flightCrewMember", "The crew member is already assigned to a leg at this time.");

		// Restricción: Si el duty es PILOT o CO_PILOT, verificar que no exista ya esa asignación en el FlightLeg.
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
		dataset.put("masterId", assignment.getFlightLeg().getId());
		super.getResponse().addData(dataset);
	}
}
