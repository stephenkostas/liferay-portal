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

package com.liferay.fragment.service.impl;

import com.liferay.fragment.constants.FragmentActionKeys;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.fragment.service.base.FragmentCollectionServiceBaseImpl;
import com.liferay.fragment.service.permission.FragmentCollectionPermission;
import com.liferay.fragment.service.permission.FragmentPermission;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jürgen Kappler
 */
public class FragmentCollectionServiceImpl
	extends FragmentCollectionServiceBaseImpl {

	@Override
	public FragmentCollection addFragmentCollection(
			long groupId, String name, String description,
			ServiceContext serviceContext)
		throws PortalException {

		FragmentPermission.check(
			getPermissionChecker(), groupId,
			FragmentActionKeys.ADD_FRAGMENT_COLLECTION);

		return fragmentCollectionLocalService.addFragmentCollection(
			getUserId(), groupId, name, description, serviceContext);
	}

	@Override
	public FragmentCollection deleteFragmentCollection(
			long fragmentCollectionId)
		throws PortalException {

		FragmentCollectionPermission.check(
			getPermissionChecker(), fragmentCollectionId, ActionKeys.DELETE);

		return fragmentCollectionLocalService.deleteFragmentCollection(
			fragmentCollectionId);
	}

	@Override
	public List<FragmentCollection> deleteFragmentCollections(
			long[] fragmentCollectionIds)
		throws PortalException {

		List<FragmentCollection> undeletableFragmentCollections =
			new ArrayList<>();

		for (long fragmentCollectionId : fragmentCollectionIds) {
			try {
				FragmentCollectionPermission.check(
					getPermissionChecker(), fragmentCollectionId,
					ActionKeys.DELETE);

				fragmentCollectionLocalService.deleteFragmentCollection(
					fragmentCollectionId);
			}
			catch (PortalException pe) {
				if (_log.isDebugEnabled()) {
					_log.debug(pe, pe);
				}

				FragmentCollection fragmentCollection =
					fragmentCollectionPersistence.fetchByPrimaryKey(
						fragmentCollectionId);

				undeletableFragmentCollections.add(fragmentCollection);
			}
		}

		return undeletableFragmentCollections;
	}

	@Override
	public FragmentCollection fetchFragmentCollection(long fragmentCollectionId)
		throws PortalException {

		FragmentCollection fragmentCollection =
			fragmentCollectionLocalService.fetchFragmentCollection(
				fragmentCollectionId);

		if (fragmentCollection != null) {
			FragmentCollectionPermission.check(
				getPermissionChecker(), fragmentCollection, ActionKeys.VIEW);
		}

		return fragmentCollection;
	}

	@Override
	public List<FragmentCollection> getFragmentCollections(long groupId)
		throws PortalException {

		return fragmentCollectionPersistence.filterFindByGroupId(groupId);
	}

	@Override
	public List<FragmentCollection> getFragmentCollections(
			long groupId, int start, int end)
		throws PortalException {

		return fragmentCollectionPersistence.filterFindByGroupId(
			groupId, start, end);
	}

	@Override
	public List<FragmentCollection> getFragmentCollections(
			long groupId, int start, int end,
			OrderByComparator<FragmentCollection> orderByComparator)
		throws PortalException {

		return fragmentCollectionPersistence.filterFindByGroupId(
			groupId, start, end, orderByComparator);
	}

	@Override
	public List<FragmentCollection> getFragmentCollections(
			long groupId, String name, int start, int end,
			OrderByComparator<FragmentCollection> orderByComparator)
		throws PortalException {

		return fragmentCollectionPersistence.filterFindByG_LikeN(
			groupId, name, start, end, orderByComparator);
	}

	@Override
	public int getFragmentCollectionsCount(long groupId) {
		return fragmentCollectionPersistence.filterCountByGroupId(groupId);
	}

	@Override
	public int getFragmentCollectionsCount(long groupId, String name) {
		return fragmentCollectionPersistence.filterCountByG_LikeN(
			groupId, name);
	}

	@Override
	public FragmentCollection updateFragmentCollection(
			long fragmentCollectionId, String name, String description)
		throws PortalException {

		FragmentCollectionPermission.check(
			getPermissionChecker(), fragmentCollectionId, ActionKeys.UPDATE);

		return fragmentCollectionLocalService.updateFragmentCollection(
			fragmentCollectionId, name, description);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FragmentCollectionServiceImpl.class);

}