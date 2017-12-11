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

package com.liferay.site.navigation.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.model.SiteNavigationMenuItem;
import com.liferay.site.navigation.service.base.SiteNavigationMenuLocalServiceBaseImpl;

import java.util.Date;
import java.util.List;

/**
 * @author Pavel Savinov
 */
public class SiteNavigationMenuLocalServiceImpl
	extends SiteNavigationMenuLocalServiceBaseImpl {

	@Override
	public SiteNavigationMenu addSiteNavigationMenu(
			long userId, long groupId, String name,
			ServiceContext serviceContext)
		throws PortalException {

		// Site navigation menu

		User user = userLocalService.getUser(userId);

		long siteNavigationMenuId = counterLocalService.increment();

		SiteNavigationMenu siteNavigationMenu =
			siteNavigationMenuPersistence.create(siteNavigationMenuId);

		siteNavigationMenu.setGroupId(groupId);
		siteNavigationMenu.setCompanyId(user.getCompanyId());
		siteNavigationMenu.setUserId(userId);
		siteNavigationMenu.setUserName(user.getFullName());
		siteNavigationMenu.setCreateDate(
			serviceContext.getCreateDate(new Date()));
		siteNavigationMenu.setName(name);

		siteNavigationMenuPersistence.update(siteNavigationMenu);

		// Resources

		resourceLocalService.addResources(
			siteNavigationMenu.getCompanyId(), siteNavigationMenu.getGroupId(),
			siteNavigationMenu.getUserId(), SiteNavigationMenu.class.getName(),
			siteNavigationMenu.getSiteNavigationMenuId(), false, true, true);

		return siteNavigationMenu;
	}

	@Override
	public SiteNavigationMenu deleteSiteNavigationMenu(
			long siteNavigationMenuId)
		throws PortalException {

		SiteNavigationMenu siteNavigationMenu = getSiteNavigationMenu(
			siteNavigationMenuId);

		return deleteSiteNavigationMenu(siteNavigationMenu);
	}

	@Override
	public SiteNavigationMenu deleteSiteNavigationMenu(
			SiteNavigationMenu siteNavigationMenu)
		throws PortalException {

		// Site navigation menu

		siteNavigationMenuPersistence.remove(
			siteNavigationMenu.getSiteNavigationMenuId());

		// Resources

		resourceLocalService.deleteResource(
			siteNavigationMenu.getCompanyId(),
			SiteNavigationMenuItem.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			siteNavigationMenu.getSiteNavigationMenuId());

		// Site navigation menu items

		List<SiteNavigationMenuItem> siteNavigationMenuItems =
			siteNavigationMenuItemLocalService.getSiteNavigationMenuItems(
				siteNavigationMenu.getSiteNavigationMenuId());

		for (SiteNavigationMenuItem siteNavigationMenuItem :
				siteNavigationMenuItems) {

			siteNavigationMenuItemLocalService.deleteSiteNavigationMenuItem(
				siteNavigationMenuItem.getSiteNavigationMenuItemId());
		}

		return siteNavigationMenu;
	}

	@Override
	public List<SiteNavigationMenu> getSiteNavigationMenus(long groupId) {
		return siteNavigationMenuPersistence.findByGroupId(groupId);
	}

	@Override
	public List<SiteNavigationMenu> getSiteNavigationMenus(
		long groupId, int start, int end, OrderByComparator orderByComparator) {

		return siteNavigationMenuPersistence.findByGroupId(
			groupId, start, end, orderByComparator);
	}

	@Override
	public List<SiteNavigationMenu> getSiteNavigationMenus(
		long groupId, String keywords, int start, int end,
		OrderByComparator orderByComparator) {

		return siteNavigationMenuPersistence.findByG_N(
			groupId, keywords, start, end, orderByComparator);
	}

	@Override
	public int getSiteNavigationMenusCount(long groupId) {
		return siteNavigationMenuPersistence.countByGroupId(groupId);
	}

	@Override
	public int getSiteNavigationMenusCount(long groupId, String keywords) {
		return siteNavigationMenuPersistence.countByG_N(groupId, keywords);
	}

	@Override
	public SiteNavigationMenu updateSiteNavigationMenu(
			long userId, long siteNavigationMenuId, String name,
			ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		SiteNavigationMenu siteNavigationMenu = getSiteNavigationMenu(
			siteNavigationMenuId);

		siteNavigationMenu.setModifiedDate(
			serviceContext.getModifiedDate(new Date()));
		siteNavigationMenu.setUserId(userId);
		siteNavigationMenu.setUserName(user.getFullName());
		siteNavigationMenu.setName(name);

		return siteNavigationMenuPersistence.update(siteNavigationMenu);
	}

}