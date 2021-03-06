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

package com.liferay.wiki.editor.configuration.internal;

import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType;
import com.liferay.portal.kernel.editor.configuration.EditorConfigContributor;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.wiki.constants.WikiPortletKeys;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ambrín Chaudhary
 */
@Component(
	property = {
		"editor.config.key=contentEditor", "editor.name=alloyeditor",
		"editor.name=ckeditor", "javax.portlet.name=" + WikiPortletKeys.WIKI,
		"javax.portlet.name=" + WikiPortletKeys.WIKI_ADMIN,
		"javax.portlet.name=" + WikiPortletKeys.WIKI_DISPLAY,
		"service.ranking:Integer=100"
	},
	service = EditorConfigContributor.class
)
public class WikiAttachmentImageHTMLEditorConfigContributor
	extends BaseWikiAttachmentImageEditorConfigContributor {

	@Override
	protected String getItemSelectorURL(
		RequestBackedPortletURLFactory requestBackedPortletURLFactory,
		String itemSelectedEventName, long wikiPageResourcePrimKey,
		ThemeDisplay themeDisplay) {

		List<ItemSelectorReturnType> desiredItemSelectorReturnTypes =
			new ArrayList<>();

		desiredItemSelectorReturnTypes.add(
			new FileEntryItemSelectorReturnType());

		ItemSelectorCriterion imageItemSelectorCriterion =
			getImageItemSelectorCriterion(desiredItemSelectorReturnTypes);

		ItemSelectorCriterion urlItemSelectorCriterion =
			getURLItemSelectorCriterion();

		PortletURL itemSelectorURL = null;

		if (wikiPageResourcePrimKey == 0) {
			itemSelectorURL = _itemSelector.getItemSelectorURL(
				requestBackedPortletURLFactory, itemSelectedEventName,
				imageItemSelectorCriterion, urlItemSelectorCriterion);
		}
		else {
			ItemSelectorCriterion attachmentItemSelectorCriterion =
				getWikiAttachmentItemSelectorCriterion(
					wikiPageResourcePrimKey, desiredItemSelectorReturnTypes);

			ItemSelectorCriterion uploadItemSelectorCriterion =
				getUploadItemSelectorCriterion(
					wikiPageResourcePrimKey, themeDisplay,
					requestBackedPortletURLFactory);

			itemSelectorURL = _itemSelector.getItemSelectorURL(
				requestBackedPortletURLFactory, itemSelectedEventName,
				attachmentItemSelectorCriterion, imageItemSelectorCriterion,
				urlItemSelectorCriterion, uploadItemSelectorCriterion);
		}

		return itemSelectorURL.toString();
	}

	@Reference(unbind = "-")
	protected void setItemSelector(ItemSelector itemSelector) {
		_itemSelector = itemSelector;
	}

	private ItemSelector _itemSelector;

}