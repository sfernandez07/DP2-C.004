
package acme.features.technicians.maintenanceRecord;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordStatus;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	protected TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		MaintenanceRecord record = new MaintenanceRecord();
		record.setTechnician(this.repository.findTechnicianById(super.getRequest().getPrincipal().getActiveRealm().getId()));
		record.setMaintenanceMoment(new Date());
		record.setStatus(MaintenanceRecordStatus.PENDING);
		super.getBuffer().addData(record);
	}

	@Override
	public void bind(final MaintenanceRecord record) {
		super.bindObject(record, "nextInspectionDueDate", "estimatedCost", "notes", "aircraft");
	}

	@Override
	public void validate(final MaintenanceRecord record) {

		// Validar que la fecha de la siguiente inspección sea posterior al momento de creación
		if (record.getMaintenanceMoment() != null && record.getNextInspectionDueDate() != null) {
			boolean validInspectionDate = record.getNextInspectionDueDate().after(new Date(record.getMaintenanceMoment().getTime() + 60 * 1000)); // +1 minuto
			super.state(validInspectionDate, "nextInspectionDueDate", "La siguiente inspección debe ser al menos un minuto posterior al momento del mantenimiento.");
		}

		// Validar que el coste estimado sea mayor que cero
		if (record.getEstimatedCost() != null) {
			boolean validCost = record.getEstimatedCost().getAmount().doubleValue() > 0;
			super.state(validCost, "estimatedCost", "El coste estimado debe ser un valor positivo.");
		}

		// Validar que el técnico esté presente (normalmente ya lo está porque lo asignas en load)
		super.state(record.getTechnician() != null, "technician", "El técnico debe estar asignado al registro.");

		// Validar que el avión (aircraft) es opcional, pero si se proporciona debe tener datos coherentes
		if (record.getAircraft() != null)
			super.state(record.getAircraft().getModel() != null && !record.getAircraft().getModel().isEmpty(), "aircraft", "El avión debe tener un modelo definido.");
	}

	@Override
	public void perform(final MaintenanceRecord record) {
		record.setMaintenanceMoment(new Date());
		this.repository.save(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset = super.unbindObject(record, "maintenanceMoment", "status", "nextInspectionDueDate", "estimatedCost", "notes", "aircraft");
		super.getResponse().addData(dataset);
	}
}
