
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.claims.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.id = :id")
	TrackingLog findTrackingLogById(int id);

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogByClaimId(int claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId AND t.updateMoment > :updateMoment " + "ORDER BY t.updateMoment DESC")
	List<TrackingLog> findNextTrackingLogs(int claimId, Date updateMoment);
}
