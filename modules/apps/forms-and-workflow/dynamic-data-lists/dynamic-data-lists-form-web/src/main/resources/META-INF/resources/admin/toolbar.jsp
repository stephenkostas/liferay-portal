<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/admin/init.jsp" %>

<%
PortletURL portletURL = ddlFormAdminDisplayContext.getPortletURL();

String currentTab = ParamUtil.getString(request, "currentTab", "forms");
%>

<liferay-frontend:management-bar
	includeCheckBox="<%= true %>"
	searchContainerId="<%= ddlFormAdminDisplayContext.getSearchContainerId() %>"
>
	<liferay-frontend:management-bar-buttons>
		<liferay-util:include page="/admin/display_style_buttons.jsp" servletContext="<%= application %>" />
	</liferay-frontend:management-bar-buttons>

	<liferay-frontend:management-bar-filters>
		<liferay-frontend:management-bar-navigation
			navigationKeys='<%= new String[] {"all"} %>'
			portletURL="<%= portletURL %>"
		/>

		<liferay-frontend:management-bar-sort
			orderByCol="<%= ddlFormAdminDisplayContext.getOrderByCol() %>"
			orderByType="<%= ddlFormAdminDisplayContext.getOrderByType() %>"
			orderColumns='<%= new String[] {"create-date", "modified-date", "name"} %>'
			portletURL="<%= portletURL %>"
		/>
	</liferay-frontend:management-bar-filters>

	<liferay-frontend:management-bar-action-buttons>
		<c:choose>
			<c:when test='<%= currentTab.equals("forms") %>'>
				<liferay-frontend:management-bar-button href='<%= "javascript:" + renderResponse.getNamespace() + "deleteRecordSets();" %>' icon="trash" label="delete" />
			</c:when>
			<c:when test='<%= currentTab.equals("field-set") %>'>
				<liferay-frontend:management-bar-button href='<%= "javascript:" + renderResponse.getNamespace() + "deleteStructures();" %>' icon="trash" label="delete" />
			</c:when>
		</c:choose>
	</liferay-frontend:management-bar-action-buttons>
</liferay-frontend:management-bar>

<c:choose>
	<c:when test='<%= currentTab.equals("forms") %>'>
		<aui:script>
			function <portlet:namespace />deleteRecordSets() {
				if (confirm('<%= UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-this") %>')) {
					var form = AUI.$(document.<portlet:namespace />searchContainerForm);

					var searchContainer = AUI.$('#<portlet:namespace /><%= ddlFormAdminDisplayContext.getSearchContainerId() %>', form);

					form.attr('method', 'post');
					form.fm('deleteRecordSetIds').val(Liferay.Util.listCheckedExcept(searchContainer, '<portlet:namespace />allRowIds'));

					submitForm(form, '<portlet:actionURL name="deleteRecordSet"><portlet:param name="mvcPath" value="/admin/view.jsp" /><portlet:param name="redirect" value="<%= currentURL %>" /></portlet:actionURL>');
				}
			}
		</aui:script>
	</c:when>
	<c:when test='<%= currentTab.equals("field-set") %>'>
		<aui:script>
			function <portlet:namespace />deleteStructures() {
				if (confirm('<%= UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-this") %>')) {
					var form = AUI.$(document.<portlet:namespace />searchContainerForm);

					var searchContainer = AUI.$('#<portlet:namespace /><%= ddlFormAdminDisplayContext.getSearchContainerId() %>', form);

					form.attr('method', 'post');
					form.fm('deleteStructureIds').val(Liferay.Util.listCheckedExcept(searchContainer, '<portlet:namespace />allRowIds'));

					submitForm(form, '<portlet:actionURL name="deleteStructure"><portlet:param name="mvcPath" value="/admin/view.jsp" /><portlet:param name="currentTab" value="field-set" /><portlet:param name="redirect" value="<%= currentURL %>" /></portlet:actionURL>');
				}
			}
		</aui:script>
	</c:when>
</c:choose>