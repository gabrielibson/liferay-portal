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

package com.liferay.dynamic.data.mapping.uad.display.test;

import com.liferay.dynamic.data.mapping.constants.DDMPortletKeys;
import com.liferay.dynamic.data.mapping.model.DDMContent;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.user.associated.data.display.UADDisplay;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(immediate = true, service = UADDisplay.class)
public class DDMContentUADDisplay extends BaseDDMContentUADDisplay {

	@Override
	public String getEditURL(
			DDMContent ddmContent, LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		DDMFormInstanceRecord ddmFormInstanceRecord =
			ddmFormInstanceRecordLocalService.
				fetchFormInstanceRecordByStorageId(ddmContent.getContentId());

		DDMFormInstance ddmFormInstance =
			ddmFormInstanceRecord.getFormInstance();

		ThemeDisplay themeDisplay = getThemeDisplay(
			liferayPortletRequest.getHttpServletRequest());

		return _getViewFormURL(
			ddmFormInstance, ddmFormInstanceRecord, themeDisplay);
	}

	@Override
	protected DynamicQuery getSearchDynamicQuery(
		long userId, long[] groupIds, String keywords, String orderByField,
		String orderByType) {

		return super.getSearchDynamicQuery(
			userId, null, keywords, orderByField, orderByType);
	}

	protected ThemeDisplay getThemeDisplay(
		HttpServletRequest httpServletRequest) {

		return (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Reference
	protected DDMFormInstanceRecordLocalService
		ddmFormInstanceRecordLocalService;

	private String _getViewFormURL(
			DDMFormInstance ddmFormInstance,
			DDMFormInstanceRecord ddmFormInstanceRecord,
			ThemeDisplay themeDisplay)
		throws PortalException {

		String portletNamespace = _portal.getPortletNamespace(
			DDMPortletKeys.DYNAMIC_DATA_MAPPING_FORM_ADMIN);

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
			new String[] {String.valueOf(ddmFormInstance.getFormInstanceId())}
		).build();

		return _portal.getSiteAdminURL(
			themeDisplay, DDMPortletKeys.DYNAMIC_DATA_MAPPING_FORM_ADMIN,
			params);
	}

	@Reference
	private Portal _portal;

}