<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-moment code="assistance-agent.claim.form.label.registrationMoment" path="registrationMoment"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.passengerEmail" path="passengerEmail"/>	
	<acme:input-textbox code="assistance-agent.claim.form.label.type" path="type"/>
	<acme:input-textarea code="assistance-agent.claim.form.label.description" path="description"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.status" path="status"/>
	<acme:input-select code="assistance-agent.claim.form.label.flight-leg" path="flightLeg"  choices="${flightLegs}"/>

	<jstl:choose>	 
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>

