
package acme.features.airlineManager.flightLeg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	@Query("SELECT f FROM Flight f WHERE f.id = :id")
	Flight findFlightById(int id);

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

	@Query("select count(c) > 0 from Claim c where c.flightLeg.id = :flightLegId")
	boolean existsClaimsByFlightLegId(@Param("flightLegId") int flightLegId);

	@Query("select fl from FlightLeg fl where fl.id = :flightLegId and fl.departureAirport.id = :departureAirportId and fl.arrivalAirport.id = :arrivalAirportId and fl.aircraft.id = :aircraftId")
	FlightLeg findFlightLegByIdAndFields(@Param("flightLegId") int flightLegId, @Param("departureAirportId") int departureAirportId, @Param("arrivalAirportId") int arrivalAirportId, @Param("aircraftId") int aircraftId);

	@Query("select count(fl) > 0 from FlightLeg fl where fl.flight.id = :flightId and (fl.departureAirport.id = :airportId or fl.arrivalAirport.id = :airportId)")
	boolean existsAirportInFlight(@Param("flightId") int flightId, @Param("airportId") int airportId);

}
