<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.label.passportNumber" path="passportNumber" width="40%"/>
</acme:list>
<jstl:choose>
	<jstl:when test="${_command == 'list'}">
		<acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
	</jstl:when>
	<jstl:when test="${_command == 'list-booked'}">
		<acme:button code="customer.passenger.list.button.add" action="/customer/bookingRecord/create"/>
	</jstl:when>
</jstl:choose>