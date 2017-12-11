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

package com.liferay.dynamic.data.lists.form.web.internal.portlet.action;

import com.liferay.captcha.util.CaptchaUtil;
import com.liferay.dynamic.data.lists.form.web.internal.constants.DDLFormPortletKeys;
import com.liferay.dynamic.data.lists.form.web.internal.constants.DDLFormWebKeys;
import com.liferay.dynamic.data.lists.form.web.internal.notification.DDLFormEmailNotificationSender;
import com.liferay.dynamic.data.lists.model.DDLRecord;
import com.liferay.dynamic.data.lists.model.DDLRecordConstants;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordSetSettings;
import com.liferay.dynamic.data.lists.service.DDLRecordService;
import com.liferay.dynamic.data.lists.service.DDLRecordSetService;
import com.liferay.dynamic.data.mapping.form.values.factory.DDMFormValuesFactory;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormSuccessPageSettings;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.captcha.CaptchaTextException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + DDLFormPortletKeys.DYNAMIC_DATA_LISTS_FORM,
		"mvc.command.name=addRecord"
	},
	service = MVCActionCommand.class
)
public class AddRecordMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		PortletSession portletSession = actionRequest.getPortletSession();

		long groupId = ParamUtil.getLong(actionRequest, "groupId");

		if (groupId == 0) {
			groupId = GetterUtil.getLong(
				portletSession.getAttribute(DDLFormWebKeys.GROUP_ID));
		}

		long recordSetId = ParamUtil.getLong(actionRequest, "recordSetId");

		if (recordSetId == 0) {
			recordSetId = GetterUtil.getLong(
				portletSession.getAttribute(
					DDLFormWebKeys.DYNAMIC_DATA_LISTS_RECORD_SET_ID));
		}

		DDLRecordSet recordSet = _ddlRecordSetService.getRecordSet(recordSetId);

		validateCaptcha(actionRequest, recordSet);

		DDMForm ddmForm = getDDMForm(recordSet);

		DDMFormValues ddmFormValues = _ddmFormValuesFactory.create(
			actionRequest, ddmForm);

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		_addRecordMVCCommandHelper.updateRequiredFieldsAccordingToVisibility(
			actionRequest, ddmForm, ddmFormValues, themeDisplay.getLocale());

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDLRecord.class.getName(), actionRequest);

		DDLRecord ddlRecord = _ddlRecordService.addRecord(
			groupId, recordSetId, DDLRecordConstants.DISPLAY_INDEX_DEFAULT,
			ddmFormValues, serviceContext);

		if (isEmailNotificationEnabled(recordSet)) {
			_ddlFormEmailNotificationSender.sendEmailNotification(
				actionRequest, ddlRecord);
		}

		if (SessionErrors.isEmpty(actionRequest)) {
			DDLRecordSetSettings recordSetSettings =
				recordSet.getSettingsModel();

			String redirectURL = recordSetSettings.redirectURL();

			if (Validator.isNotNull(redirectURL)) {
				portletSession.setAttribute(
					DDLFormWebKeys.DYNAMIC_DATA_LISTS_RECORD_SET_ID,
					recordSetId);
				portletSession.setAttribute(DDLFormWebKeys.GROUP_ID, groupId);

				sendRedirect(actionRequest, actionResponse, redirectURL);
			}
			else {
				DDMFormSuccessPageSettings ddmFormSuccessPageSettings =
					ddmForm.getDDMFormSuccessPageSettings();

				if (ddmFormSuccessPageSettings.isEnabled()) {
					String portletId = _portal.getPortletId(actionRequest);

					SessionMessages.add(
						actionRequest,
						portletId.concat(
							SessionMessages.
								KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE));
				}
			}
		}
	}

	protected DDMForm getDDMForm(DDLRecordSet recordSet)
		throws PortalException {

		DDMStructure ddmStructure = recordSet.getDDMStructure();

		return ddmStructure.getDDMForm();
	}

	protected boolean isEmailNotificationEnabled(DDLRecordSet recordSet)
		throws PortalException {

		DDLRecordSetSettings recordSettings = recordSet.getSettingsModel();

		return recordSettings.sendEmailNotification();
	}

	@Reference(unbind = "-")
	protected void setDDLFormEmailNotificationSender(
		DDLFormEmailNotificationSender ddlFormEmailNotificationSender) {

		_ddlFormEmailNotificationSender = ddlFormEmailNotificationSender;
	}

	@Reference(unbind = "-")
	protected void setDDLRecordService(DDLRecordService ddlRecordService) {
		_ddlRecordService = ddlRecordService;
	}

	@Reference(unbind = "-")
	protected void setDDLRecordSetService(
		DDLRecordSetService ddlRecordSetService) {

		_ddlRecordSetService = ddlRecordSetService;
	}

	@Reference(unbind = "-")
	protected void setDDMFormValuesFactory(
		DDMFormValuesFactory ddmFormValuesFactory) {

		_ddmFormValuesFactory = ddmFormValuesFactory;
	}

	protected void validateCaptcha(
			ActionRequest actionRequest, DDLRecordSet recordSet)
		throws Exception {

		DDLRecordSetSettings recordSetSettings = recordSet.getSettingsModel();

		if (recordSetSettings.requireCaptcha()) {
			try {
				CaptchaUtil.check(actionRequest);
			}
			catch (CaptchaTextException cte) {
				SessionErrors.add(
					actionRequest, CaptchaTextException.class.getName());

				throw cte;
			}
		}
	}

	@Reference
	private AddRecordMVCCommandHelper _addRecordMVCCommandHelper;

	private DDLFormEmailNotificationSender _ddlFormEmailNotificationSender;
	private DDLRecordService _ddlRecordService;
	private DDLRecordSetService _ddlRecordSetService;
	private DDMFormValuesFactory _ddmFormValuesFactory;

	@Reference
	private Portal _portal;

}