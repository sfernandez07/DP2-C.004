
package acme.features.technicians.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListService extends AbstractGuiService<Technician, Task> {

	@Autowired
	protected TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Task> tasks = this.repository.findAllByTechnicianId(technicianId);
		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset = super.unbindObject(task, "taskType", "description", "priority", "estimatedDuration", "draftMode");
		super.getResponse().addData(dataset);
	}
}
