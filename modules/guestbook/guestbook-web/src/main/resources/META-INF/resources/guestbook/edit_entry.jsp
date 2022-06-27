<%@ include file="../init.jsp" %>


<% 

long entryId = com.liferay.portal.kernel.util.ParamUtil.getLong(renderRequest, "entryId");

com.liferay.docs.guestbook.model.GuestbookEntry entry = null;
if (entryId > 0) {
  entry = com.liferay.docs.guestbook.service.GuestbookEntryLocalServiceUtil.getGuestbookEntry(entryId);
}

long guestbookId = com.liferay.portal.kernel.util.ParamUtil.getLong(renderRequest, "guestbookId");

%>

<portlet:renderURL var="viewURL">
    <portlet:param name="mvcPath" value="/guestbook/view.jsp"></portlet:param>
</portlet:renderURL>

<portlet:actionURL name="addEntry" var="addEntryURL" />

<aui:form action="<%= addEntryURL %>" name="<portlet:namespace />fm">

<aui:model-context bean="<%= entry %>" model="<%= com.liferay.docs.guestbook.model.GuestbookEntry.class %>" />

	<aui:fieldset>

		<aui:input name="name" />
		<aui:input name="email" />
		<aui:input name="message" />
		<aui:input name="entryId" type="hidden" />
		<aui:input name="guestbookId" type="hidden" value='<%= entry == null ? guestbookId : entry.getGuestbookId() %>'/>

	</aui:fieldset>

	<aui:button-row>

		<aui:button type="submit"></aui:button>
		<aui:button type="cancel" onClick="<%= viewURL.toString() %>"></aui:button>

	</aui:button-row>
</aui:form>