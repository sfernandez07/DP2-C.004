<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list navigable="false">
	<acme:list-column code="assistance-agent.claim.completed-list.label.registrationMoment" path="registrationMoment" width="30%"/>
	<acme:list-column code="assistance-agent.claim.completed-list.label.passengerEmail" path="passengerEmail" width="30%"/>
	<acme:list-column code="assistance-agent.claim.completed-list.label.type" path="type" width="20%"/>
	<acme:list-column code="assistance-agent.claim.completed-list.label.status" path="status" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>
