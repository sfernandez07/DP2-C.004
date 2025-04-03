
package acme.features.technicians.maintenanceRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordStatus;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		MaintenanceRecord record = this.repository.findOneMaintenanceRecordById(id);
		boolean isOwner = record != null && record.getTechnician().equals(super.getRequest().getPrincipal().getActiveRealm());
		super.getResponse().setAuthorised(isOwner);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		MaintenanceRecord record = this.repository.findOneMaintenanceRecordById(id);
		super.getBuffer().addData(record);
	}

	@Override
	public void bind(final MaintenanceRecord record) {
		super.bindObject(record, "maintenanceMoment", "status", "nextInspectionDueDate", "estimatedCost", "notes", "aircraft");
	}

	@Override
	public void validate(final MaintenanceRecord record) {
		List<Task> tasks = this.repository.findTasksByMaintenanceRecordId(record.getId());

		boolean hasAtLeastOnePublished = tasks.stream().anyMatch(Task::isDraftMode);
		boolean hasUnpublishedTasks = tasks.stream().anyMatch(t -> !t.isDraftMode());

		super.state(hasAtLeastOnePublished, "*", "Debe existir al menos una tarea publicada para poder publicar el mantenimiento.");
		super.state(!hasUnpublishedTasks, "*", "No puede haber tareas sin publicar para publicar el mantenimiento.");
	}

	@Override
	public void perform(final MaintenanceRecord record) {
		record.setStatus(MaintenanceRecordStatus.COMPLETED); // o PUBLISHED, si usas otro estado
		this.repository.save(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset = super.unbindObject(record, "maintenanceMoment", "status", "nextInspectionDueDate", "estimatedCost", "notes", "aircraft");
		super.getResponse().addData(dataset);
	}
}
