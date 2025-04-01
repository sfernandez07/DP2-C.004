
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.claims.TrackingLog;
import acme.entities.flights.FlightLeg;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	@Query("select c from Claim c where c.assistanceAgent.id = :assistanceAgentId and c.status IN (acme.entities.claims.ClaimStatus.ACCEPTED, acme.entities.claims.ClaimStatus.REJECTED)")
	Collection<Claim> findCompletedClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select c from Claim c where c.assistanceAgent.id = :assistanceAgentId and c.status IN (acme.entities.claims.ClaimStatus.PENDING)")
	Collection<Claim> findPendingClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select l from FlightLeg l where l.id = :legId")
	FlightLeg findLegById(int legId);

	@Query("select l from FlightLeg l")
	Collection<FlightLeg> findAllFlightLegs();

	@Query("select l from FlightLeg l where l.status IN(acme.entities.flights.LegStatus.LANDED)")
	Collection<FlightLeg> findLegsThatOccurred();
}
