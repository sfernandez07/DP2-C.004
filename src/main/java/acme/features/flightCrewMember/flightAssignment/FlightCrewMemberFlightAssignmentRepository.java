
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignments.FlightAssignment;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select b from FlightAssignment b where b.flightCrewMember.id = :id")
	Collection<FlightAssignment> findFlightAssignmentsByCrewId(int id);

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findOneById(int id);

}
