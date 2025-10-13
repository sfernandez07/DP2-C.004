
package acme.features.flightCrewMember.flightCrewMember;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("select fcm from FlightCrewMember fcm where fcm.employeeCode = :employeeCode")
	FlightCrewMember findFlightCrewMemberByEmployeeCode(String employeeCode);
}
