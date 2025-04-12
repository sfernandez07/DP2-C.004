
package acme.features.technicians.task;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiController
public class TechnicianTaskController extends AbstractGuiController<Technician, Task> {

	@Autowired
	protected TechnicianTaskListService		listService;

	@Autowired
	protected TechnicianTaskShowService		showService;

	@Autowired
	protected TechnicianTaskCreateService	createService;

	@Autowired
	protected TechnicianTaskUpdateService	updateService;

	@Autowired
	protected TechnicianTaskPublishService	publishService;


	@PostConstruct
	public void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService); // comando "publish" como acción de tipo "update"
	}
}
