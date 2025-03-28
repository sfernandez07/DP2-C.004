
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignments.ActivityLog;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select al from ActivityLog al where al.flightAssignment.id = :assignmentId")
	Collection<ActivityLog> findActivityLogsByAssignmentId(int assignmentId);

}
