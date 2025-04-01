
package acme.features.technicians;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	// Encuentra todos los registros de mantenimiento de un técnico
	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :technicianId")
	List<MaintenanceRecord> findAllByTechnicianId(int technicianId);

	// Encuentra un único registro de mantenimiento por ID
	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findOneMaintenanceRecordById(int id);

	// Encuentra el técnico por ID (para asignarlo al crear un registro)
	@Query("select t from Technician t where t.id = :id")
	Technician findTechnicianById(int id);

	// Encuentra todas las tareas asociadas a un registro de mantenimiento
	@Query("select t from Task t where t.maintenanceRecord.id = :maintenanceRecordId")
	List<Task> findTasksByMaintenanceRecordId(int maintenanceRecordId);

	// Cuenta las tareas publicadas de un registro (si usas un campo 'draftMode' en Task)
	@Query("select count(t) from Task t where t.maintenanceRecord.id = :maintenanceRecordId and t.draftMode = true")
	int countPublishedTasksByMaintenanceRecordId(int maintenanceRecordId);

	// Cuenta las tareas no publicadas de un registro
	@Query("select count(t) from Task t where t.maintenanceRecord.id = :maintenanceRecordId and t.draftMode = false")
	int countUnpublishedTasksByMaintenanceRecordId(int maintenanceRecordId);
}
