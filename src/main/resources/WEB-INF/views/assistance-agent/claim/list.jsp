<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistance-agent.claim.list.label.registrationMoment" path="registrationMoment" width="20%"/>
	<acme:list-column code="assistance-agent.claim.list.label.passengerEmail" path="passengerEmail" width="30%"/>
	<acme:list-column code="assistance-agent.claim.list.label.type" path="type" width="20%"/>
	<acme:list-column code="assistance-agent.claim.list.label.status" path="status" width="20%"/>
	<jstl:if test="${_command == 'pending-list'}">
		<acme:list-column code="assistance-agent.claim.list.label.flightLeg" path="flightLeg.flightNumber" width="10%"/>
	</jstl:if>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="assistance-agent.claim.list.button.create" action="/assistance-agent/claim/create"/>

