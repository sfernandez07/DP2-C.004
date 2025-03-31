<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="flight-crew-member.activity-log.form.label.registrationMoment" path="registrationMoment"/>
	<acme:input-textbox code="flight-crew-member.activity-log.form.label.typeOfIncident" path="typeOfIncident"/>
	<acme:input-textbox code="flight-crew-member.activity-log.form.label.description" path="description"/>
	<acme:input-textbox code="flight-crew-member.activity-log.form.label.severityLevel" path="severityLevel"/>
	<jstl:choose>	 
		<jstl:when test="${_command == 'show'}">		
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.activity-log.form.button.create" action="/flight-crew-member/activity-log/create?masterId=${masterId}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>

<%@page%>