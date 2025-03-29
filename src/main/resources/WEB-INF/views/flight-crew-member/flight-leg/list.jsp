<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-member.flight-leg.list.label.flightNumber" path="flightNumber" width="25%"/>
	<acme:list-column code="flight-crew-member.flight-leg.list.label.scheduledDeparture" path="scheduledDeparture" width="25%"/>
	<acme:list-column code="flight-crew-member.flight-leg.list.label.scheduledArrival" path="scheduledArrival" width="25%"/>
	<acme:list-column code="flight-crew-member.flight-leg.list.label.status" path="status" width="25%"/>
</acme:list>