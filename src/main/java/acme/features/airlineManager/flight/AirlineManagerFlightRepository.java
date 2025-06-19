
package acme.features.airlineManager.flight;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.flights.FlightLeg;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query(value = "SELECT * FROM flight WHERE id = :id LIMIT 1", nativeQuery = true)
	Flight findFlightById(@Param("id") int id);

	@Query("select fl from FlightLeg fl where fl.flight.id = :flightId")
	Collection<FlightLeg> findFlightLegsByFlightId(int flightId);

	@Query("SELECT f FROM Flight f WHERE f.manager.id = :id")
	Collection<Flight> findFlightsByAirlineManagerId(int id);

	@Query("select count(fa) from FlightAssignment fa where fa.flightLeg.flight.id = :flightId")
	int countAssignmentsByFlightId(int flightId);

	@Query("SELECT MIN(fl.scheduledDeparture) FROM FlightLeg fl WHERE fl.flight.id = :flightId")
	Date findMinScheduledDeparture(@Param("flightId") int flightId);

	@Query("SELECT MAX(fl.scheduledArrival) FROM FlightLeg fl WHERE fl.flight.id = :flightId")
	Date findMaxScheduledArrival(@Param("flightId") int flightId);

	@Query("SELECT fl.departureAirport.city FROM FlightLeg fl WHERE fl.flight.id = :flightId ORDER BY fl.scheduledDeparture ASC")
	List<String> findOriginCity(@Param("flightId") int flightId);

	@Query("SELECT fl.arrivalAirport.city FROM FlightLeg fl WHERE fl.flight.id = :flightId ORDER BY fl.scheduledArrival DESC")
	List<String> findDestinationCity(@Param("flightId") int flightId);

	@Query("SELECT COUNT(fl) FROM FlightLeg fl WHERE fl.flight.id = :flightId")
	int countLegs(@Param("flightId") int flightId);

}
