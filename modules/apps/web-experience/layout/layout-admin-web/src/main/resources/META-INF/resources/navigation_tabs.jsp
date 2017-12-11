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

<%@ include file="/init.jsp" %>

<aui:nav cssClass="navbar-nav">

	<%
	PortletURL pagesURL = layoutsAdminDisplayContext.getPortletURL();

	pagesURL.setParameter("tabs1", "pages");
	%>

	<aui:nav-item href="<%= pagesURL.toString() %>" label="pages" selected='<%= Objects.equals(layoutsAdminDisplayContext.getTabs1(), "pages") %>' />

	<%
	PortletURL pageTemplatesURL = layoutsAdminDisplayContext.getPortletURL();

	pageTemplatesURL.setParameter("tabs1", "page-templates");
	%>

	<aui:nav-item href="<%= pageTemplatesURL.toString() %>" label="page-templates" selected='<%= Objects.equals(layoutsAdminDisplayContext.getTabs1(), "page-templates") %>' />
</aui:nav>