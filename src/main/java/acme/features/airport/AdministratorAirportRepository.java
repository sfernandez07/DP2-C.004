
package acme.features.airport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airports.Airport;

@Repository
public interface AdministratorAirportRepository extends AbstractRepository {

	@Query("select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("select a from Airport a where a.id = :id")
	Airport findAirportById(int id);

}
