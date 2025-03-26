
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("select b from FlightAssignment b where b.flightCrewMember.id = :id")
	Collection<FlightAssignment> findFlightAssignmentsByCrewId(int id);

	// Recupera las asignaciones planeadas: aquellas cuyo scheduledDeparture es mayor al instante actual.
	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :id and fa.flightLeg.scheduledDeparture > CURRENT_TIMESTAMP")
	Collection<FlightAssignment> findPlannedAssignmentsByFlightCrewMemberId(int id);

	// Recupera las asignaciones completadas: aquellas cuyo scheduledDeparture es menor o igual al instante actual.
	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :id and fa.flightLeg.scheduledDeparture <= CURRENT_TIMESTAMP")
	Collection<FlightAssignment> findCompletedAssignmentsByFlightCrewMemberId(int id);

	// Query para el show: se recupera una asignación por su id
	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findOneById(int id);

	@Query("select case when count(fa) > 0 then true else false end from FlightAssignment fa where fa.flightCrewMember.id = :crewMemberId and fa.flightLeg.id = :flightLegId")
	boolean isCrewMemberAssignedSimultaneously(int crewMemberId, int flightLegId);

	@Query("select case when count(fa) > 0 then true else false end from FlightAssignment fa where fa.flightLeg.id = :flightLegId and fa.duty = :duty")
	boolean existsAssignmentForDutyInLeg(int flightLegId, Duty duty);

}
