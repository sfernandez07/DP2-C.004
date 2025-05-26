
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

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId AND t.creationOrder < :creationOrder " + "ORDER BY t.creationOrder DESC")
	List<TrackingLog> findPreviousTrackingLogs(int claimId, Integer creationOrder);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId AND t.creationOrder > :creationOrder " + "ORDER BY t.creationOrder DESC")
	List<TrackingLog> findNextTrackingLogs(int claimId, Integer creationOrder);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId ORDER BY t.resolutionPercentage DESC, t.creationOrder DESC")
	List<TrackingLog> findTrackingLogOrderedByPercentageByClaimId(int claimId);
}
