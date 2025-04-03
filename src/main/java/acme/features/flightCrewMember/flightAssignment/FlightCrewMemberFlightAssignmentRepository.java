
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignments.ActivityLog;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.flights.FlightLeg;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select b from FlightAssignment b where b.flightCrewMember.id = :id")
	Collection<FlightAssignment> findFlightAssignmentsByCrewId(int id);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :id and fa.flightLeg.scheduledArrival > :currentMoment")
	Collection<FlightAssignment> findPlannedAssignmentsByCrewId(int id, Date currentMoment);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :id and fa.flightLeg.scheduledArrival <= :currentMoment")
	Collection<FlightAssignment> findCompletedAssignmentsByCrewId(int id, Date currentMoment);

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findOneById(int id);

	@Query("select case when count(fa) > 0 then true else false end from FlightAssignment fa where fa.flightCrewMember.id = :crewMemberId and fa.flightLeg.id = :flightLegId")
	Boolean isCrewMemberAssignedSimultaneously(int crewMemberId, int flightLegId);

	@Query("select case when count(fa) > 0 then true else false end from FlightAssignment fa where fa.flightLeg.id = :flightLegId and fa.duty = :duty")
	Boolean existsAssignmentForDutyInLeg(int flightLegId, Duty duty);

	@Query("select c from FlightLeg c where c.id = :flightLegId")
	FlightLeg findFlightLegById(int flightLegId);

	@Query("select l from FlightLeg l where l.status IN(acme.entities.flights.LegStatus.LANDED)")
	Collection<FlightLeg> findLegsThatOccurred();

	@Query("select f from FlightCrewMember f where f.airline.id = :airlineId")
	Collection<FlightCrewMember> findCrewMembersBySameAirline(int airlineId);

	@Query("select fl from FlightLeg fl where fl.flight.airline.id = :airlineId")
	Collection<FlightLeg> findFlightLegsByCrewMemberAirline(int airlineId);

	@Query("SELECT a FROM ActivityLog a WHERE a.flightAssignment.id = :flightAssignmentId")
	List<ActivityLog> findAllActivityLogs(int flightAssignmentId);
}
