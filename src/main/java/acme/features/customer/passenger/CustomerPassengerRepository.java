
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passengers.Passenger;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select br.passenger from BookingRecord br where br.booking.id = :id")
	Collection<Passenger> findPassengersByBookingId(Integer id);

	@Query("select p from Passenger p where p.id = :id")
	Passenger findPassengerById(Integer id);

}
