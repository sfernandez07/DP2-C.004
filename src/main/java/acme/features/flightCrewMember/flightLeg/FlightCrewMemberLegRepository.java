
package acme.features.flightCrewMember.flightLeg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.FlightLeg;

@Repository
public interface FlightCrewMemberLegRepository extends AbstractRepository {

	@Query("select b.flightLeg from FlightAssignment b where b.id = :id")
	Collection<FlightLeg> findFlightLegByAssignmentId(Integer id);

}
