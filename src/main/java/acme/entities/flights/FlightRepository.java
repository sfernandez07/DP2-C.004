
package acme.entities.flights;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface FlightRepository extends AbstractRepository {

	@Query("select min(l.scheduledDeparture) from FlightLeg l where l.flight.id = :flightId")
	Date findScheduledDeparture(int flightId);

	@Query("select max(l.scheduledArrival) from FlightLeg l where l.flight.id = :flightId")
	Date findScheduledArrival(int flightId);

	@Query("select l.departureAirport.city from FlightLeg l where l.flight.id = :flightId order by l.scheduledDeparture asc")
	String findOriginCity(int flightId);

	@Query("select l.arrivalAirport.city from FlightLeg l where l.flight.id = :flightId order by l.scheduledArrival desc")
	String findDestinationCity(int flightId);

	@Query("select count(l) from FlightLeg l where l.flight.id = :flightId")
	Integer countLegs(int flightId);

}
