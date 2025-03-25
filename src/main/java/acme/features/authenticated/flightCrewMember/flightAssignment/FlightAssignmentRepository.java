
package acme.features.authenticated.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignments.FlightAssignment;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	// Recupera todas las asignaciones de un FlightCrewMember
	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :userId")
	Collection<FlightAssignment> findManyByFlightCrewMemberId(int userId);

	// Recupera una asignación en concreto por su id
	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findOneById(int id);

	// Asignaciones planeadas: el vuelo aún no se ha realizado (salida en el futuro)
	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :userId and fa.flightLeg.scheduledDeparture > CURRENT_TIMESTAMP")
	Collection<FlightAssignment> findPlannedByFlightCrewMemberId(int userId);

	// Asignaciones completadas: el vuelo ya se realizó o está en curso (salida en el pasado o ahora)
	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :userId and fa.flightLeg.scheduledDeparture <= CURRENT_TIMESTAMP")
	Collection<FlightAssignment> findCompletedByFlightCrewMemberId(int userId);
}
