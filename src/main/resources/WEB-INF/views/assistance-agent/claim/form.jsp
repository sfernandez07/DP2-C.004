<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-moment code="assistance-agent.claim.form.label.registrationMoment" path="registrationMoment"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.passengerEmail" path="passengerEmail"/>	
	<acme:input-select code="assistance-agent.claim.form.label.type" path="type" choices="${types}"/>
	<acme:input-textarea code="assistance-agent.claim.form.label.description" path="description"/>
	<acme:input-select code="assistance-agent.claim.form.label.status" path="status" choices="${statuses}"/>
	<jstl:if test="${_command == 'show'}">
		<acme:input-textbox code="assistance-agent.claim.form.label.flight-leg" path="flightLeg.flightNumber"/>
	</jstl:if>	
	<jstl:choose>	 
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="assistance-agent.claim.form.label.flight-leg" path="flightLeg"  choices="${flightLegs}"/>
			<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
		</jstl:when>	
			<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="assistance-agent.claim.form.button.update" action="/assistance-agent/claim/update"/>
			<acme:submit code="assistance-agent.claim.form.button.delete" action="/assistance-agent/claim/delete"/>
			<acme:submit code="assistance-agent.claim.form.button.publish" action="/assistance-agent/claim/publish"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>

