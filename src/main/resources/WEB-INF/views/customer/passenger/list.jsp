<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.label.fullName" path="fullName" width="33%"/>
	<acme:list-column code="customer.passenger.list.label.email" path="email" width="33%"/>
	<acme:list-column code="customer.passenger.list.label.passportNumber" path="passportNumber" width="34%"/>
</acme:list>

<acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>