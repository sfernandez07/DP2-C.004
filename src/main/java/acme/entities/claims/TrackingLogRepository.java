
package acme.entities.claims;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.id = :id")
	TrackingLog findTrackingLogById(int id);

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogByClaimId(int claimId);

	@Query("SELECT MAX(t.resolutionPercentage) FROM TrackingLog t WHERE t.claim.id = :claimId AND t.draftMode = false")
	Double findLastResolutionPercentagePublished(int claimId);

	@Query("SELECT COUNT(t) FROM TrackingLog t WHERE t.claim.id = :claimId AND t.resolutionPercentage = 100.00 AND t.draftMode = false")
	Long countFinalTrackingLogs(int claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId AND t.draftMode = false ORDER BY t.resolutionPercentage DESC")
	List<TrackingLog> findPublishedTrackingLogOrderedByPercentage(int claimId);
}
