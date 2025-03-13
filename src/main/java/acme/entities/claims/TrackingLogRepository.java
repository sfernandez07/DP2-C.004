
package acme.entities.claims;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId AND t.updateMoment < :updateMoment " + "ORDER BY t.updateMoment DESC")
	List<TrackingLog> findPreviousTrackingLogs(int claimId, Date updateMoment);
}
