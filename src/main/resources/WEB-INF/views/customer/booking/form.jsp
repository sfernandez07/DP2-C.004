<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="customer.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-textbox code="customer.booking.form.label.purchaseMoment" path="purchaseMoment"/>
	<acme:input-textbox code="customer.booking.label.travelClass" path="travelClass"/>
	<acme:input-textbox code="customer.booking.form.label.price" path="price"/>
	<acme:input-textbox code="customer.booking.form.label.lastCreditNibble" path="lastCreditNibble"/>
	<jstl:choose>	 
		<jstl:when test="${_command == 'show'}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/passenger/list?id=${id}"/>			
		</jstl:when>
	</jstl:choose>
</acme:form>