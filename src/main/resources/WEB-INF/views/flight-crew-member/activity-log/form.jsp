<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="flightCrewMember.activityLog.form.label.registrationMoment" path="registrationMoment"/>
	<acme:input-textbox code="flightCrewMember.activityLog.form.label.typeOfIncident" path="typeOfIncident"/>
	<acme:input-textbox code="flightCrewMember.activitylog.form.label.description" path="description"/>
	<acme:input-textbox code="flightCrewMember.activityLog.form.label.severityLevel" path="severityLevel"/>
	<jstl:choose>	 
		<jstl:when test="${_command == 'show'}">		
		</jstl:when>
	</jstl:choose>
</acme:form>

<%@page%>