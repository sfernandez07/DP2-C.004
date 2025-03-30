
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.flights.FlightLeg;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select b from FlightAssignment b where b.flightCrewMember.id = :id")
	Collection<FlightAssignment> findFlightAssignmentsByCrewId(int id);

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findOneById(int id);

	@Query("select case when count(fa) > 0 then true else false end from FlightAssignment fa where fa.flightCrewMember.id = :crewMemberId and fa.flightLeg.id = :flightLegId")
	Boolean isCrewMemberAssignedSimultaneously(int crewMemberId, int flightLegId);

	@Query("select case when count(fa) > 0 then true else false end from FlightAssignment fa where fa.flightLeg.id = :flightLegId and fa.duty = :duty")
	Boolean existsAssignmentForDutyInLeg(int flightLegId, Duty duty);

	@Query("select c from FlightLeg c where c.id = :flightLegId")
	FlightLeg findFlightLegById(int flightLegId);

}
