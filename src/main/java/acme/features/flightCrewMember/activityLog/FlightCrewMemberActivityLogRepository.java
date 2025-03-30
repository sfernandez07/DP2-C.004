
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignments.ActivityLog;
import acme.entities.flightAssignments.FlightAssignment;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select al from ActivityLog al where al.flightAssignment.id = :assignmentId")
	Collection<ActivityLog> findActivityLogsByAssignmentId(int assignmentId);

	@Query("select ac from ActivityLog ac where ac.id = :id")
	ActivityLog findOneById(int id);

	@Query("select a from FlightAssignment a where a.id = :id")
	FlightAssignment findAssignmentById(int id);

}
