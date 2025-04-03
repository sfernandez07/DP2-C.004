
package acme.features.airlineManager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.flights.FlightLeg;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select fl from FlightLeg fl where fl.flight.id = :flightId")
	Collection<FlightLeg> findFlightLegsByFlightId(int flightId);

	@Query("SELECT f FROM Flight f WHERE f.manager.id = :id")
	Collection<Flight> findFlightsByAirlineManagerId(int id);

	@Query("select count(fa) from FlightAssignment fa where fa.flightLeg.flight.id = :flightId")
	int countAssignmentsByFlightId(int flightId);

}
