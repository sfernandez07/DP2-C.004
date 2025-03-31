<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.duty" path="duty" choices="${dutyChoices}"/>
	<jstl:if test="${_command == 'show'}">
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.lastUpdate" path="lastUpdate"/>
	</jstl:if>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.status" path="status" choices="${statusChoices}"/>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.crew" path="flightCrewMember" choices="${crewChoices}"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.flightLeg" path="flightLeg" choices="${flightLegChoices}"/>
	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show'}">
			<acme:button code="flightCrewMember.flightAssignment.form.button.flightLeg" action="/flight-crew-member/flight-leg/list?id=${id}"/>
			<acme:button code="flightCrewMember.flightAssignment.form.button.flightCrewMember" action="/flight-crew-member/flight-crew-member/list?id=${id}"/>
			<acme:button code="flightCrewMember.flightAssignment.form.button.activityLog" action="/flight-crew-member/activity-log/list?id=${id}"/>			
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flightCrewMember.flightAssignment.form.button.create" action="/flight-crew-member/flight-assignment/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>