
package acme.features.authenticated.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.assistanceAgent.id = :assistanceAgentId and c.status IN (acme.entities.claims.ClaimStatus.ACCEPTED, acme.entities.claims.ClaimStatus.REJECTED)")
	Collection<Claim> findCompletedClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select c from Claim c where c.assistanceAgent.id = :assistanceAgentId and c.status IN (acme.entities.claims.ClaimStatus.PENDING)")
	Collection<Claim> findPendingClaimsByAssistanceAgentId(int assistanceAgentId);
}
