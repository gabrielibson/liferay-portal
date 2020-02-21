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

package com.liferay.dynamic.data.mapping.uad.display;

import com.liferay.dynamic.data.mapping.constants.DDMPortletKeys;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalService;
import com.liferay.dynamic.data.mapping.uad.util.DDMFormInstanceRecordUADHelper;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.user.associated.data.display.UADDisplay;

import java.io.Serializable;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	immediate = true,
	service = {DDMFormInstanceRecordUADDisplay.class, UADDisplay.class}
)
public class DDMFormInstanceRecordUADDisplay
	extends BaseDDMFormInstanceRecordUADDisplay {

	@Override
	public long count(long userId) {
		return 0;
	}

	@Override
	public String getEditURL(
			DDMFormInstanceRecord ddmFormInstanceRecord,
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		String portletNamespace = _portal.getPortletNamespace(
			DDMPortletKeys.DYNAMIC_DATA_MAPPING_FORM_ADMIN);

		HttpServletRequest httpServletRequest =
			liferayPortletRequest.getHttpServletRequest();

		Map<String, String[]> params = HashMapBuilder.put(
			portletNamespace.concat("mvcPath"),
			new String[] {"/admin/view_form_instance_record.jsp"}
		).put(
			portletNamespace.concat("formInstanceRecordId"),
			new String[] {
				String.valueOf(ddmFormInstanceRecord.getFormInstanceRecordId())
			}
		).put(
			portletNamespace.concat("formInstanceId"),
			new String[] {
				String.valueOf(ddmFormInstanceRecord.getFormInstanceId())
			}
		).put(
			portletNamespace.concat("readOnly"),
			new String[] {Boolean.FALSE.toString()}
		).put(
			portletNamespace.concat("redirect"),
			new String[] {_portal.getCurrentURL(httpServletRequest)}
		).build();

		return _portal.getSiteAdminURL(
			getThemeDisplay(httpServletRequest),
			DDMPortletKeys.DYNAMIC_DATA_MAPPING_FORM_ADMIN, params);
	}

	@Override
	public String getName(
		DDMFormInstanceRecord ddmFormInstanceRecord, Locale locale) {

		return LanguageUtil.get(
			ResourceBundleUtil.getBundle(
				locale, DDMFormInstanceRecordUADDisplay.class),
			"ddm-form-instance-record"
		).concat(
			" #"
		).concat(
			String.valueOf(ddmFormInstanceRecord.getFormInstanceRecordId())
		);
	}

	@Override
	public Class<?> getParentContainerClass() {
		return DDMFormInstance.class;
	}

	@Override
	public Serializable getParentContainerId(
		DDMFormInstanceRecord ddmFormInstanceRecord) {

		return ddmFormInstanceRecord.getFormInstanceId();
	}

	@Override
	public boolean isUserOwned(
		DDMFormInstanceRecord formInstanceRecord, long userId) {

		if (formInstanceRecord.getUserId() == userId) {
			return true;
		}

		return false;
	}

	@Override
	public long searchCount(long userId, long[] groupIds, String keywords) {
		return 0;
	}

	@Override
	protected long doCount(DynamicQuery dynamicQuery) {
		return 0;
	}

	@Override
	protected DynamicQuery getSearchDynamicQuery(
		long userId, long[] groupIds, String keywords, String orderByField,
		String orderByType) {

		DynamicQuery dynamicSubquery =
			_ddmFormInstanceRecordUADHelper.createFormInstanceQuery(
				keywords, new String[] {"name", "description"}, orderByField,
				orderByType);

		dynamicSubquery.setProjection(
			ProjectionFactoryUtil.property("formInstanceId"));

		DynamicQuery dynamicQuery =
			ddmFormInstanceRecordLocalService.dynamicQuery();

		Property userIdProperty = PropertyFactoryUtil.forName("userId");
		Property versionUserIdProperty = PropertyFactoryUtil.forName(
			"versionUserId");

		dynamicQuery.add(
			RestrictionsFactoryUtil.or(
				userIdProperty.eq(userId), versionUserIdProperty.eq(userId)));

		if (isSiteScoped() && ArrayUtil.isNotEmpty(groupIds)) {
			_ddmFormInstanceRecordUADHelper.addGroupIdRestriction(
				dynamicQuery, groupIds);
			_ddmFormInstanceRecordUADHelper.addGroupIdRestriction(
				dynamicSubquery, groupIds);
		}

		Property nameProperty = PropertyFactoryUtil.forName("formInstanceId");

		return dynamicQuery.add(nameProperty.in(dynamicSubquery));
	}

	protected ThemeDisplay getThemeDisplay(
		HttpServletRequest httpServletRequest) {

		return (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Reference
	private DDMFormInstanceLocalService _ddmFormInstanceLocalService;

	@Reference
	private DDMFormInstanceRecordUADHelper _ddmFormInstanceRecordUADHelper;

	@Reference
	private DDMFormInstanceUADDisplay _ddmFormInstanceUADDisplay;

	@Reference
	private Portal _portal;

}