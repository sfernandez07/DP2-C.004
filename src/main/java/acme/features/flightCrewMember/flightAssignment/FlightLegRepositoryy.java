
package acme.features.flightCrewMember.flightAssignment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.FlightLeg;

@Repository
public interface FlightLegRepositoryy extends AbstractRepository {

	@Query("select fl from FlightLeg fl where fl.id = :id")
	FlightLeg findById(int id);
}
