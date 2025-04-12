
package acme.features.airlineManager.flightLeg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.flights.FlightLeg;

@Repository
public interface AirlineManagerFlightLegRepository extends AbstractRepository {

	@Query("SELECT fl FROM FlightLeg fl WHERE fl.flight.id = :flightId ORDER BY fl.scheduledDeparture ASC")
	Collection<FlightLeg> findFlightLegsByFlightId(int flightId);

	@Query("SELECT fl FROM FlightLeg fl WHERE fl.id = :id")
	FlightLeg findFlightLegById(int id);

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("SELECT a FROM Airport a WHERE a.id = :id")
	Airport findAirportById(int id);

	@Query("SELECT a FROM Aircraft a WHERE a.id = :id")
	Aircraft findAircraftById(int id);

	@Query("select fl.flight from FlightLeg fl where fl.id = :id")
	Flight findFlightByFlightLegId(int id);

}
