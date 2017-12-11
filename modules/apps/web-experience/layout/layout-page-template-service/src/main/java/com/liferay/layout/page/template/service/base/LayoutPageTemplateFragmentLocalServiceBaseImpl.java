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

package com.liferay.layout.page.template.service.base;

import aQute.bnd.annotation.ProviderType;

import com.liferay.layout.page.template.model.LayoutPageTemplateFragment;
import com.liferay.layout.page.template.service.LayoutPageTemplateFragmentLocalService;
import com.liferay.layout.page.template.service.persistence.LayoutPageTemplateEntryPersistence;
import com.liferay.layout.page.template.service.persistence.LayoutPageTemplateFragmentPersistence;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the layout page template fragment local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.layout.page.template.service.impl.LayoutPageTemplateFragmentLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.layout.page.template.service.impl.LayoutPageTemplateFragmentLocalServiceImpl
 * @see com.liferay.layout.page.template.service.LayoutPageTemplateFragmentLocalServiceUtil
 * @generated
 */
@ProviderType
public abstract class LayoutPageTemplateFragmentLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements LayoutPageTemplateFragmentLocalService, IdentifiableOSGiService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.layout.page.template.service.LayoutPageTemplateFragmentLocalServiceUtil} to access the layout page template fragment local service.
	 */

	/**
	 * Adds the layout page template fragment to the database. Also notifies the appropriate model listeners.
	 *
	 * @param layoutPageTemplateFragment the layout page template fragment
	 * @return the layout page template fragment that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public LayoutPageTemplateFragment addLayoutPageTemplateFragment(
		LayoutPageTemplateFragment layoutPageTemplateFragment) {
		layoutPageTemplateFragment.setNew(true);

		return layoutPageTemplateFragmentPersistence.update(layoutPageTemplateFragment);
	}

	/**
	 * Creates a new layout page template fragment with the primary key. Does not add the layout page template fragment to the database.
	 *
	 * @param layoutPageTemplateFragmentId the primary key for the new layout page template fragment
	 * @return the new layout page template fragment
	 */
	@Override
	public LayoutPageTemplateFragment createLayoutPageTemplateFragment(
		long layoutPageTemplateFragmentId) {
		return layoutPageTemplateFragmentPersistence.create(layoutPageTemplateFragmentId);
	}

	/**
	 * Deletes the layout page template fragment with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param layoutPageTemplateFragmentId the primary key of the layout page template fragment
	 * @return the layout page template fragment that was removed
	 * @throws PortalException if a layout page template fragment with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public LayoutPageTemplateFragment deleteLayoutPageTemplateFragment(
		long layoutPageTemplateFragmentId) throws PortalException {
		return layoutPageTemplateFragmentPersistence.remove(layoutPageTemplateFragmentId);
	}

	/**
	 * Deletes the layout page template fragment from the database. Also notifies the appropriate model listeners.
	 *
	 * @param layoutPageTemplateFragment the layout page template fragment
	 * @return the layout page template fragment that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public LayoutPageTemplateFragment deleteLayoutPageTemplateFragment(
		LayoutPageTemplateFragment layoutPageTemplateFragment)
		throws PortalException {
		return layoutPageTemplateFragmentPersistence.remove(layoutPageTemplateFragment);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(LayoutPageTemplateFragment.class,
			clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return layoutPageTemplateFragmentPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.layout.page.template.model.impl.LayoutPageTemplateFragmentModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end) {
		return layoutPageTemplateFragmentPersistence.findWithDynamicQuery(dynamicQuery,
			start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.layout.page.template.model.impl.LayoutPageTemplateFragmentModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end, OrderByComparator<T> orderByComparator) {
		return layoutPageTemplateFragmentPersistence.findWithDynamicQuery(dynamicQuery,
			start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return layoutPageTemplateFragmentPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery,
		Projection projection) {
		return layoutPageTemplateFragmentPersistence.countWithDynamicQuery(dynamicQuery,
			projection);
	}

	@Override
	public LayoutPageTemplateFragment fetchLayoutPageTemplateFragment(
		long layoutPageTemplateFragmentId) {
		return layoutPageTemplateFragmentPersistence.fetchByPrimaryKey(layoutPageTemplateFragmentId);
	}

	/**
	 * Returns the layout page template fragment with the primary key.
	 *
	 * @param layoutPageTemplateFragmentId the primary key of the layout page template fragment
	 * @return the layout page template fragment
	 * @throws PortalException if a layout page template fragment with the primary key could not be found
	 */
	@Override
	public LayoutPageTemplateFragment getLayoutPageTemplateFragment(
		long layoutPageTemplateFragmentId) throws PortalException {
		return layoutPageTemplateFragmentPersistence.findByPrimaryKey(layoutPageTemplateFragmentId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(layoutPageTemplateFragmentLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(LayoutPageTemplateFragment.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"layoutPageTemplateFragmentId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		IndexableActionableDynamicQuery indexableActionableDynamicQuery = new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(layoutPageTemplateFragmentLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(LayoutPageTemplateFragment.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"layoutPageTemplateFragmentId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {
		actionableDynamicQuery.setBaseLocalService(layoutPageTemplateFragmentLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(LayoutPageTemplateFragment.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"layoutPageTemplateFragmentId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {
		return layoutPageTemplateFragmentLocalService.deleteLayoutPageTemplateFragment((LayoutPageTemplateFragment)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {
		return layoutPageTemplateFragmentPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the layout page template fragments.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.layout.page.template.model.impl.LayoutPageTemplateFragmentModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of layout page template fragments
	 * @param end the upper bound of the range of layout page template fragments (not inclusive)
	 * @return the range of layout page template fragments
	 */
	@Override
	public List<LayoutPageTemplateFragment> getLayoutPageTemplateFragments(
		int start, int end) {
		return layoutPageTemplateFragmentPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of layout page template fragments.
	 *
	 * @return the number of layout page template fragments
	 */
	@Override
	public int getLayoutPageTemplateFragmentsCount() {
		return layoutPageTemplateFragmentPersistence.countAll();
	}

	/**
	 * Updates the layout page template fragment in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param layoutPageTemplateFragment the layout page template fragment
	 * @return the layout page template fragment that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public LayoutPageTemplateFragment updateLayoutPageTemplateFragment(
		LayoutPageTemplateFragment layoutPageTemplateFragment) {
		return layoutPageTemplateFragmentPersistence.update(layoutPageTemplateFragment);
	}

	/**
	 * Returns the layout page template fragment local service.
	 *
	 * @return the layout page template fragment local service
	 */
	public LayoutPageTemplateFragmentLocalService getLayoutPageTemplateFragmentLocalService() {
		return layoutPageTemplateFragmentLocalService;
	}

	/**
	 * Sets the layout page template fragment local service.
	 *
	 * @param layoutPageTemplateFragmentLocalService the layout page template fragment local service
	 */
	public void setLayoutPageTemplateFragmentLocalService(
		LayoutPageTemplateFragmentLocalService layoutPageTemplateFragmentLocalService) {
		this.layoutPageTemplateFragmentLocalService = layoutPageTemplateFragmentLocalService;
	}

	/**
	 * Returns the layout page template fragment persistence.
	 *
	 * @return the layout page template fragment persistence
	 */
	public LayoutPageTemplateFragmentPersistence getLayoutPageTemplateFragmentPersistence() {
		return layoutPageTemplateFragmentPersistence;
	}

	/**
	 * Sets the layout page template fragment persistence.
	 *
	 * @param layoutPageTemplateFragmentPersistence the layout page template fragment persistence
	 */
	public void setLayoutPageTemplateFragmentPersistence(
		LayoutPageTemplateFragmentPersistence layoutPageTemplateFragmentPersistence) {
		this.layoutPageTemplateFragmentPersistence = layoutPageTemplateFragmentPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the layout page template entry local service.
	 *
	 * @return the layout page template entry local service
	 */
	public com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService getLayoutPageTemplateEntryLocalService() {
		return layoutPageTemplateEntryLocalService;
	}

	/**
	 * Sets the layout page template entry local service.
	 *
	 * @param layoutPageTemplateEntryLocalService the layout page template entry local service
	 */
	public void setLayoutPageTemplateEntryLocalService(
		com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService layoutPageTemplateEntryLocalService) {
		this.layoutPageTemplateEntryLocalService = layoutPageTemplateEntryLocalService;
	}

	/**
	 * Returns the layout page template entry persistence.
	 *
	 * @return the layout page template entry persistence
	 */
	public LayoutPageTemplateEntryPersistence getLayoutPageTemplateEntryPersistence() {
		return layoutPageTemplateEntryPersistence;
	}

	/**
	 * Sets the layout page template entry persistence.
	 *
	 * @param layoutPageTemplateEntryPersistence the layout page template entry persistence
	 */
	public void setLayoutPageTemplateEntryPersistence(
		LayoutPageTemplateEntryPersistence layoutPageTemplateEntryPersistence) {
		this.layoutPageTemplateEntryPersistence = layoutPageTemplateEntryPersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		persistedModelLocalServiceRegistry.register("com.liferay.layout.page.template.model.LayoutPageTemplateFragment",
			layoutPageTemplateFragmentLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.layout.page.template.model.LayoutPageTemplateFragment");
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return LayoutPageTemplateFragmentLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return LayoutPageTemplateFragment.class;
	}

	protected String getModelClassName() {
		return LayoutPageTemplateFragment.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = layoutPageTemplateFragmentPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = LayoutPageTemplateFragmentLocalService.class)
	protected LayoutPageTemplateFragmentLocalService layoutPageTemplateFragmentLocalService;
	@BeanReference(type = LayoutPageTemplateFragmentPersistence.class)
	protected LayoutPageTemplateFragmentPersistence layoutPageTemplateFragmentPersistence;
	@ServiceReference(type = com.liferay.counter.kernel.service.CounterLocalService.class)
	protected com.liferay.counter.kernel.service.CounterLocalService counterLocalService;
	@BeanReference(type = com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService.class)
	protected com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService layoutPageTemplateEntryLocalService;
	@BeanReference(type = LayoutPageTemplateEntryPersistence.class)
	protected LayoutPageTemplateEntryPersistence layoutPageTemplateEntryPersistence;
	@ServiceReference(type = com.liferay.portal.kernel.service.ResourceLocalService.class)
	protected com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.UserLocalService.class)
	protected com.liferay.portal.kernel.service.UserLocalService userLocalService;
	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	@ServiceReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry persistedModelLocalServiceRegistry;
}