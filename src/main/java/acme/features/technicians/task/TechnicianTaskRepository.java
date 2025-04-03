
package acme.features.technicians.task;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	// Todas las tareas creadas por un técnico
	@Query("select t from Task t where t.technician.id = :technicianId")
	List<Task> findAllByTechnicianId(int technicianId);

	// Una única tarea por su ID
	@Query("select t from Task t where t.id = :id")
	Task findOneTaskById(int id);

	// Buscar técnico por ID (para asignar en creación)
	@Query("select tech from Technician tech where tech.id = :id")
	Technician findTechnicianById(int id);
}
