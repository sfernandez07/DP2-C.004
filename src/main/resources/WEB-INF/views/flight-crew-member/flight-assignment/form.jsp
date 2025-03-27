<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="flightCrewMember.flightAssignment.form.duty" path="duty"/>
	<acme:input-textbox code="flightCrewMember.flightAssignment.form.label.lastUpdate" path="lastUpdate"/>
	<acme:input-textbox code="flightCrewMember.flightAssignment.label.status" path="status"/>
	<acme:input-textbox code="flightCrewMember.flightAssignment.form.label.remarks" path="remarks"/>
	<jstl:choose>	 
		<jstl:when test="${_command == 'show'}">
			<acme:button code="flightCrewMember.flightAssignment.form.button.flightLeg" action="/flight-crew-member/leg/list?id=${id}"/>			
		</jstl:when>
	</jstl:choose>
</acme:form>