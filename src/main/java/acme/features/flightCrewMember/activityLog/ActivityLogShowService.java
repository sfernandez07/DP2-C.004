
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.ActivityLog;
import acme.realms.FlightCrewMember;

@GuiService
public class ActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	//Internal state ---------------------------------------------

	@Autowired
	private ActivityLogRepository repository;

	//AbstractGuiService interface -------------------------------


	@Override
	public void authorise() {
		ActivityLog activityLog;
		int activityLogId;
		int flightCrewMemberId;
		boolean status;

		activityLogId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(activityLogId);
		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = activityLog != null && activityLog.getFlightAssignment().getFlightCrewMember().getId() == flightCrewMemberId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int id;

		id = super.getRequest().getData("id", int.class);

		activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset = super.unbindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("assignmentId", log.getFlightAssignment().getId());
		dataset.put("draftModeFlightAssignment", log.getFlightAssignment().isDraftMode());
		super.getResponse().addData(dataset);
	}

}
