<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.booking.list.label.locatorCode" path="locatorCode" width="20%"/>
	<acme:list-column code="customer.booking.list.label.purchaseMoment" path="purchaseMoment" width="20%"/>
	<acme:list-column code="customer.booking.list.label.travelClass" path="travelClass" width="20%"/>
	<acme:list-column code="customer.booking.list.label.price" path="price" width="20%"/>
	<acme:list-column code="customer.booking.list.label.lastCreditNibble" path="lastCreditNibble" width="20%"/>
</acme:list>

<acme:button code="custormer.booking.list.button.create" action="/customer/booking/create"/>