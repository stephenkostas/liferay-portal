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
String redirect = ParamUtil.getString(request, "redirect");

DDLRecordSet recordSet = ddlFormAdminDisplayContext.getRecordSet();

long recordSetId = BeanParamUtil.getLong(recordSet, request, "recordSetId");
long groupId = BeanParamUtil.getLong(recordSet, request, "groupId", scopeGroupId);
long ddmStructureId = BeanParamUtil.getLong(recordSet, request, "DDMStructureId");

String defaultLanguageId = LocaleUtil.toLanguageId(LocaleUtil.getSiteDefault());

Locale[] availableLocales = ddlFormAdminDisplayContext.getAvailableLocales();

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

renderResponse.setTitle((recordSet == null) ? LanguageUtil.get(request, "new-form") : LanguageUtil.get(request, "edit-form"));
%>

<div class="loading-animation" id="<portlet:namespace />loader"></div>

<portlet:actionURL name="saveRecordSet" var="saveRecordSetURL">
	<portlet:param name="mvcPath" value="/admin/edit_record_set.jsp" />
</portlet:actionURL>

<div class="hide portlet-forms" id="<portlet:namespace />formContainer">
	<aui:nav-bar cssClass="collapse-basic-search" id="toolbar" markupView="lexicon">
		<aui:nav cssClass="navbar-nav">
			<aui:nav-item id="showForm" label='<%= LanguageUtil.get(request, "builder") %>' selected="<%= true %>" />
			<aui:nav-item id="showRules" label='<%= LanguageUtil.get(request, "rules") %>' />
		</aui:nav>
	</aui:nav-bar>

	<div class="autosave-bar management-bar management-bar-default">
		<div class="container-fluid-1280">
			<div class="toolbar">
				<div class="toolbar-group-field">
				</div>

				<div class="toolbar-group-content">
					<span class="autosave-feedback management-bar-text" id="<portlet:namespace />autosaveMessage"></span>
				</div>

				<div class="toolbar-group-field">
					<button class="btn btn-link publish-icon <%= (recordSet == null) ? "hide" : "" %>" data-original-title="<liferay-ui:message key="copy-url" />" id="<portlet:namespace />publishIcon" type="button" title="<liferay-ui:message key="copy-url" />">
						<svg class="lexicon-icon">
							<use xlink:href="<%= ddlFormAdminDisplayContext.getLexiconIconsPath() %>link" />
						</svg>
					</button>
				</div>
			</div>
		</div>
	</div>

	<div class="container-fluid-1280">
		<aui:translation-manager
			availableLocales="<%= availableLocales %>"
			changeableDefaultLanguage="<%= false %>"
			defaultLanguageId="<%= defaultLanguageId %>"
			id="translationManager"
		/>
	</div>

	<aui:form action="<%= saveRecordSetURL %>" cssClass="ddl-form-builder-form" enctype="multipart/form-data" method="post" name="editForm">
		<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
		<aui:input name="recordSetId" type="hidden" value="<%= recordSetId %>" />
		<aui:input name="groupId" type="hidden" value="<%= groupId %>" />
		<aui:input name="ddmStructureId" type="hidden" value="<%= ddmStructureId %>" />
		<aui:input name="serializedSettingsContext" type="hidden" value="" />

		<%@ include file="/admin/exceptions.jspf" %>

		<aui:fieldset cssClass="ddl-form-basic-info">
			<div class="container-fluid-1280">
				<h1>
					<liferay-ui:input-editor
						autoCreate="<%= false %>"
						contents="<%= HtmlUtil.escape(ddlFormAdminDisplayContext.getFormName()) %>"
						cssClass="ddl-form-name"
						editorName="alloyeditor"
						name="nameEditor"
						placeholder="untitled-form"
						showSource="<%= false %>"
					/>
				</h1>

				<aui:input name="name" type="hidden" />

				<h5>
					<liferay-ui:input-editor
						autoCreate="<%= false %>"
						contents="<%= HtmlUtil.escape(ddlFormAdminDisplayContext.getFormDescription()) %>"
						cssClass="ddl-form-description h5"
						editorName="alloyeditor"
						name="descriptionEditor"
						placeholder="add-a-short-description-for-this-form"
						showSource="<%= false %>"
					/>
				</h5>

				<aui:input name="description" type="hidden" />
			</div>
		</aui:fieldset>

		<aui:fieldset cssClass="container-fluid-1280 ddl-form-builder-app">
			<aui:input name="serializedFormBuilderContext" type="hidden" />

			<div id="<portlet:namespace />formBuilder"></div>
			<div id="<portlet:namespace />ruleBuilder"></div>
		</aui:fieldset>

		<div class="container-fluid-1280">
			<aui:button-row cssClass="ddl-form-builder-buttons">
				<aui:button cssClass="btn-lg btn-primary ddl-button" id="publish" value='<%= ddlFormAdminDisplayContext.isFormPublished() ? "unpublish-form": "publish-form" %>' />

				<aui:button cssClass="btn-lg ddl-button" id="save" value="save-form" />

				<aui:button cssClass="btn-lg btn-link" id="preview" value="preview-form" />
			</aui:button-row>
		</div>

		<liferay-portlet:resourceURL copyCurrentRenderParameters="<%= false %>" id="publishRecordSet" var="publishRecordSetURL" />

		<liferay-portlet:resourceURL copyCurrentRenderParameters="<%= false %>" id="saveRecordSet" var="autoSaveRecordSetURL">
			<portlet:param name="autoSave" value="<%= Boolean.TRUE.toString() %>" />
		</liferay-portlet:resourceURL>

		<liferay-form:ddm-form-builder
			ddmStructureId="<%= ddlFormAdminDisplayContext.getDDMStrucutureId() %>"
			defaultLanguageId="<%= ddlFormAdminDisplayContext.getDefaultLanguageId() %>"
			editingLanguageId="<%= ddlFormAdminDisplayContext.getDefaultLanguageId() %>"
			fieldSetsClassNameId="<%= PortalUtil.getClassNameId(DDLRecordSet.class) %>"
			refererPortletNamespace="<%= liferayPortletResponse.getNamespace() %>"
		/>

		<aui:script>
			Liferay.namespace('DDL').Settings = {
				autosaveInterval: '<%= ddlFormAdminDisplayContext.getAutosaveInterval() %>',
				autosaveURL: '<%= autoSaveRecordSetURL.toString() %>',
				portletNamespace: '<portlet:namespace />',
				publishRecordSetURL: '<%= publishRecordSetURL.toString() %>',
				restrictedFormURL: '<%= ddlFormAdminDisplayContext.getRestrictedFormURL() %>',
				showPagination: true,
				sharedFormURL: '<%= ddlFormAdminDisplayContext.getSharedFormURL() %>'
			};

			var initHandler = Liferay.after(
				'form:registered',
				function(event) {
					if (event.formName === '<portlet:namespace />editForm') {
						initHandler.detach();

						var fieldTypes = <%= ddlFormAdminDisplayContext.getDDMFormFieldTypesJSONArray() %>;

						var systemFieldModules = fieldTypes.filter(
							function(item) {
								return item.system;
							}
						).map(
							function(item) {
								return item.javaScriptModule;
							}
						);

						Liferay.provide(
							window,
							'<portlet:namespace />init',
							function() {
								Liferay.DDM.SoyTemplateUtil.loadModules(
									function() {
										Liferay.DDM.Renderer.FieldTypes.register(fieldTypes);

										<portlet:namespace />registerFormPortlet(event.form);
									}
								);
							},
							['liferay-ddl-portlet','liferay-ddm-soy-template-util'].concat(systemFieldModules)
						);

						<portlet:namespace />init();
					}
				}
			);

			function <portlet:namespace />registerFormPortlet(form) {
				Liferay.component(
					'formPortlet',
					new Liferay.DDL.Portlet(
						{
							localizedDescription: <%= ddlFormAdminDisplayContext.getFormLocalizedDescription() %>,
							localizedName: <%= ddlFormAdminDisplayContext.getFormLocalizedName() %>,
							defaultLanguageId: '<%= ddlFormAdminDisplayContext.getDefaultLanguageId() %>',
							editingLanguageId: '<%= ddlFormAdminDisplayContext.getDefaultLanguageId() %>',
							editForm: form,
							formBuilder: Liferay.component('<portlet:namespace />formBuilder'),
							namespace: '<portlet:namespace />',
							published: !!<%= ddlFormAdminDisplayContext.isFormPublished() %>,
							publishRecordSetURL: '<%= publishRecordSetURL.toString() %>',
							recordSetId: <%= recordSetId %>,
							ruleBuilder: Liferay.component('<portlet:namespace />ruleBuilder'),
							translationManager: Liferay.component('<portlet:namespace />translationManager')
						}
					)
				);
			}
		</aui:script>
	</aui:form>

	<div class="container-fluid-1280 ddl-record-set-settings hide" id="<portlet:namespace />settings">
		<%= request.getAttribute(DDMWebKeys.DYNAMIC_DATA_MAPPING_FORM_HTML) %>
	</div>

	<aui:script use="aui-base">
		Liferay.namespace('DDL').destroySettings = function() {
			var settingsNode = A.one('#<portlet:namespace />settingsModal');

			if (settingsNode) {
				Liferay.Util.getWindow('<portlet:namespace />settingsModal').destroy();
			}
		};

		var clearPortletHandlers = function(event) {
			if (event.portletId === '<%= portletDisplay.getRootPortletId() %>') {
				Liferay.namespace('DDL').destroySettings();

				Liferay.detach('destroyPortlet', clearPortletHandlers);
			}
		};

		Liferay.on('destroyPortlet', clearPortletHandlers);

		Liferay.namespace('DDL').openSettings = function() {
			Liferay.Util.openWindow(
				{
					dialog: {
						cssClass: 'ddl-form-settings-modal',
						height: 620,
						resizable: false,
						'toolbars.footer': [
							{
								cssClass: 'btn-lg btn-primary',
								label: '<liferay-ui:message key="done" />',
								on: {
									click: function() {
										var ddmForm = Liferay.component('settingsDDMForm');

										ddmForm.validate(
											function(hasErrors) {
												if (!hasErrors) {
													Liferay.Util.getWindow('<portlet:namespace />settingsModal').hide();
												}
											}
										);
									}
								}
							},
							{
								cssClass: 'btn-lg btn-link',
								label: '<liferay-ui:message key="cancel" />',
								on: {
									click: function() {
										Liferay.Util.getWindow('<portlet:namespace />settingsModal').hide();
									}
								}
							}
						],
						width: 720
					},
					id: '<portlet:namespace />settingsModal',
					title: '<liferay-ui:message key="form-settings" />'
				},
				function(dialogWindow) {
					var bodyNode = dialogWindow.bodyNode;

					var settingsNode = A.one('#<portlet:namespace />settings');

					settingsNode.show();

					bodyNode.append(settingsNode);
				}
			);
		};
	</aui:script>

	<%@ include file="/admin/copy_form_publish_url.jspf" %>
</div>