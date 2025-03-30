
package acme.features.assistanceAgent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claims.Claim;
import acme.realms.AssistanceAgent;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentCompletedClaimListService	listCompletedService;

	@Autowired
	private AssistanceAgentPendingClaimListService		listPendingService;

	@Autowired
	private AssistanceAgentClaimShowService				showService;

	@Autowired
	private AssistanceAgentClaimCreateService			createService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("completed-list", "list", this.listCompletedService);
		super.addCustomCommand("pending-list", "list", this.listPendingService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
	}
}
