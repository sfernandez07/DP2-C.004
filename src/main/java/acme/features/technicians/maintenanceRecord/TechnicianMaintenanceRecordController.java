
package acme.features.technicians.maintenanceRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.realms.Technician;

@GuiController
public class TechnicianMaintenanceRecordController extends AbstractGuiController<Technician, MaintenanceRecord> {

	@Autowired
	protected TechnicianMaintenanceRecordListService	listService;

	@Autowired
	protected TechnicianMaintenanceRecordShowService	showService;

	@Autowired
	protected TechnicianMaintenanceRecordCreateService	createService;

	@Autowired
	protected TechnicianMaintenanceRecordUpdateService	updateService;

	@Autowired
	protected TechnicianMaintenanceRecordPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);  // publish es una extensión de update
	}
}
