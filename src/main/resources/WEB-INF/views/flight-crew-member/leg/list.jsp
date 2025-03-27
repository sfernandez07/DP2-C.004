<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flightCrewMember.leg.list.label.flightNumber" path="flightNumber" width="20%"/>
	<acme:list-column code="flightCrewMember.leg.list.label.scheduledDeparture" path="scheduledDeparture" width="20%"/>
	<acme:list-column code="flightCrewMember.leg.list.label.scheduledArrival" path="scheduledArrival" width="20%"/>
	<acme:list-column code="flightCrewMember.leg.list.label.status" path="status" width="20%"/>
</acme:list>