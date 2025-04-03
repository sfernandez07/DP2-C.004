
package acme.features.technicians.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskPublishService extends AbstractGuiService<Technician, Task> {

	@Autowired
	protected TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		int taskId = super.getRequest().getData("id", int.class);
		Task task = this.repository.findOneTaskById(taskId);
		boolean isOwner = task != null && task.getTechnician().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean isDraft = task != null && task.isDraftMode();
		super.getResponse().setAuthorised(isOwner && isDraft);
	}

	@Override
	public void load() {
		int taskId = super.getRequest().getData("id", int.class);
		Task task = this.repository.findOneTaskById(taskId);
		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		super.bindObject(task, "taskType", "description", "priority", "estimatedDuration");
	}

	@Override
	public void validate(final Task task) {
		boolean hasValidDescription = task.getDescription() != null && !task.getDescription().trim().isEmpty();
		boolean hasValidDuration = task.getEstimatedDuration() != null && task.getEstimatedDuration() > 0;
		boolean validPriority = task.getPriority() >= 0 && task.getPriority() <= 10;

		super.state(hasValidDescription, "description", "La descripción no puede estar vacía");
		super.state(hasValidDuration, "estimatedDuration", "La duración debe ser mayor que cero");
		super.state(validPriority, "priority", "La prioridad debe estar entre 0 y 10");
	}

	@Override
	public void perform(final Task task) {
		task.setDraftMode(false);
		this.repository.save(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset = super.unbindObject(task, "taskType", "description", "priority", "estimatedDuration", "draftMode");
		super.getResponse().addData(dataset);
	}
}
