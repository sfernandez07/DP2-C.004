
package acme.features.flightCrewMember.flightCrewMember;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.FlightCrewMember;

@Repository
public interface CrewRepository extends AbstractRepository {

	@Query("select b.flightCrewMember from FlightAssignment b where b.id = :id")
	Collection<FlightCrewMember> findCrewByAssignmentId(Integer id);

}
