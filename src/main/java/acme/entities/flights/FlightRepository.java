
package acme.entities.flights;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select min(l.scheduledDeparture) from FlightLeg l where l.flight.id = :flightId")
	Date findScheduledDeparture(int flightId);

	@Query("select max(l.scheduledDeparture) from FlightLeg l where l.flight.id = :flightId")
	Date findScheduledArrival(int flightId);

	@Query("SELECT fl.departureAirport.city FROM FlightLeg fl WHERE fl.flight.id = :flightId ORDER BY fl.scheduledDeparture ASC")
	List<String> findOriginCity(int flightId);

	@Query("SELECT fl.arrivalAirport.city FROM FlightLeg fl WHERE fl.flight.id = :flightId ORDER BY fl.scheduledArrival DESC")
	List<String> findDestinationCity(int flightId);

	@Query("select count(l) from FlightLeg l where l.flight.id = :flightId")
	Integer countLegs(int flightId);

}
