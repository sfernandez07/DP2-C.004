
package acme.features.airlineManager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerRepository extends AbstractRepository {

	@Query("SELECT COUNT(am) > 0 FROM AirlineManager am WHERE am.identifierNumber = :identifierNumber")
	boolean existsByIdentifierNumber(String identifierNumber);

	@Query("SELECT am FROM AirlineManager am WHERE am.identifierNumber = :identifierNumber")
	AirlineManager findByIdentifierNumber(String identifierNumber);

}
