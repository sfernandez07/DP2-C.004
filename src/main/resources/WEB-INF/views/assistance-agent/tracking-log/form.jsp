<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-moment code="assistance-agent.tracking-log.form.label.updateMoment" path="updateMoment"/>
	<acme:input-textbox code="assistance-agent.tracking-log.list.label.step" path="step" />
	<acme:input-double code="assistance-agent.tracking-log.list.label.resolutionPercentage" path="resolutionPercentage"/>
	<acme:input-select code="assistance-agent.tracking-log.list.label.status" path="status" choices="${statuses}" readonly="${_command != 'create'}"/>
	<acme:input-textarea code="assistance-agent.tracking-log.list.label.resolution" path="resolution"/>

	<jstl:choose>	 
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.tracking-log.form.button.create" action="/assistance-agent/tracking-log/create?masterId=${masterId}"/>
		</jstl:when>	
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="assistance-agent.tracking-log.form.button.update" action="/assistance-agent/tracking-log/update"/>
			<acme:submit code="assistance-agent.tracking-log.form.button.delete" action="/assistance-agent/tracking-log/delete"/>
			<jstl:if test="${claimDraftMode == false}">
				<acme:submit code="assistance-agent.tracking-log.form.button.publish" action="/assistance-agent/tracking-log/publish"/>
			</jstl:if>
		</jstl:when>	
	</jstl:choose>
</acme:form>

