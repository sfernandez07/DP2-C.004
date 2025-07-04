
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airports.Airport;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.flights.Flight;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findAllBookingsByCustomerId(int customerId);

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

	@Query("select b from Booking b where b.locatorCode= :locatorCode")
	Booking findBookingByLocator(String locatorCode);

	@Query("select f from Flight f where f.draftMode = false")
	Collection<Flight> findAllPublishedFlights();

	@Query("select br from BookingRecord br where br.booking.id = :bookingId")
	Collection<BookingRecord> findAllBookingRecordsByBookingId(int bookingId);

	@Query("select f from Flight f where f.id =:id")
	Flight getFlightById(int id);

	@Query("SELECT fl.arrivalAirport FROM FlightLeg fl WHERE fl.flight.id = :flightId ORDER BY fl.scheduledDeparture ASC")
	Airport findDestinationAirportByFlightId(Integer flightId);

}
