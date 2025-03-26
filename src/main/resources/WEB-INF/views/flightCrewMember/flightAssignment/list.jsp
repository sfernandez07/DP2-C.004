<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.duty" path="duty" width="20%"/>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.lastUpdate" path="lastUpdate" width="20%"/>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.status" path="status" width="20%"/>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.remarks" path="remarks" width="20%"/>
</acme:list>
