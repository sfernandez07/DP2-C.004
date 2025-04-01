
package acme.features.technicians;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordListService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	protected TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<MaintenanceRecord> records = this.repository.findAllByTechnicianId(technicianId);
		super.getBuffer().addData(records);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset = super.unbindObject(record, "maintenanceMoment", "status", "nextInspectionDueDate", "estimatedCost");
		super.getResponse().addData(dataset);
	}
}
