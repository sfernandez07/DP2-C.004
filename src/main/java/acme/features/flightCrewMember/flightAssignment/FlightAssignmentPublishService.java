
package acme.features.flightCrewMember.flightAssignment;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.AssignmentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
@Service
public class FlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findOneById(id);
		// Para publicar:
		// - La asignación no debe estar publicada (status != CONFIRMED)
		// - El FlightLeg asociado debe tener scheduledDeparture en el futuro
		// - El duty de la asignación debe ser LEAD_ATTENDANT.
		FlightCrewMember principal = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		boolean isLeadAttendantOperation = assignment != null && assignment.getDuty() == Duty.LEAD_ATTENDANT;
		boolean status = assignment != null && assignment.getStatus() != AssignmentStatus.CONFIRMED && MomentHelper.isAfter(assignment.getFlightLeg().getScheduledDeparture(), new Date()) && isLeadAttendantOperation;
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
		// Se pueden repetir las validaciones de create/update si se requiere.
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		// Publicar significa cambiar el status a CONFIRMED.
		assignment.setStatus(AssignmentStatus.CONFIRMED);
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks");
		dataset.put("id", assignment.getId());
		super.getResponse().addData(dataset);
	}
}
