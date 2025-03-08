
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	// Last five destinations assigned to the crew member
	private String[]			lastFiveDestinations;

	// Number of flight legs with incidents, categorized by severity level
	private Integer				lowSeverityIncidents; // 0 - 3
	private Integer				mediumSeverityIncidents; // 4 - 7
	private Integer				highSeverityIncidents; // 8 - 10

	// Crew members assigned with them in their last flight leg
	private String[]			crewMembersLastLeg;

	// Number of flight assignments grouped by status
	private Integer				confirmedAssignments;
	private Integer				pendingAssignments;
	private Integer				cancelledAssignments;

	// Statistics on the number of flight assignments in the last month
	private Double				averageAssignmentsLastMonth;
	private Integer				minAssignmentsLastMonth;
	private Integer				maxAssignmentsLastMonth;
	private Double				stdDevAssignmentsLastMonth;

}
