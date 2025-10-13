
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignments.ActivityLog;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.flights.FlightLeg;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightLeg.scheduledArrival < CURRENT_TIMESTAMP")
	Collection<FlightAssignment> findCompletedFlightAssignments();

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightLeg.scheduledDeparture > CURRENT_TIMESTAMP")
	Collection<FlightAssignment> findPlannedFlightAssignments();

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :flightCrewMemberId AND fa.flightLeg.scheduledArrival < CURRENT_TIMESTAMP")
	Collection<FlightAssignment> findCompletedFlightAssignmentsByMemberId(final int flightCrewMemberId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :flightCrewMemberId AND fa.flightAssignmentLeg.scheduledDeparture > CURRENT_TIMESTAMP")
	Collection<FlightAssignment> findPlannedFlightAssignmentsByMemberId(final int flightCrewMemberId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT p FROM FlightLeg p")
	Collection<FlightLeg> findAllLegs();

	@Query("SELECT l FROM Leg l WHERE l.departure > CURRENT_TIMESTAMP")
	Collection<FlightLeg> findAllFutureLegs();

	@Query("SELECT fcm FROM FlightCrewMember fcm")
	Collection<FlightCrewMember> findAllFlightCrewMembers();

	@Query("SELECT fcm FROM FlightCrewMember fcm WHERE fcm.id = :flightCrewMemberId")
	FlightCrewMember findFlightCrewMemberById(int flightCrewMemberId);

	@Query("SELECT COUNT(fa) FROM FlightAssignment fa WHERE fa.flightLeg.id = :legId AND fa.duty = :duty AND fa.id != :id AND fa.draftMode = true")
	int hasDutyAssigned(int legId, Duty duty, int id);

	@Query("SELECT fa  FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :id AND fa.flightLeg.scheduledDeparture< :arrival AND fa.flightLeg.scheduledArrival> :departure AND fa.draftMode = true")
	Collection<FlightAssignment> findFlightAssignmentsByFlightCrewMemberInRange(int id, Date departure, Date arrival);

	@Query("SELECT al FROM ActivityLog al WHERE al.flightAssignment.id = :activityLogAssignmentId")
	Collection<ActivityLog> findAllLogsByAssignmentId(int activityLogAssignmentId);

	@Query("SELECT l FROM FlightLeg l WHERE l.id = :legId")
	FlightLeg findLegById(int legId);

	@Query("SELECT l FROM FlightLeg l WHERE l.id = :legId")
	FlightLeg findPublishedLegById(int legId);
}
