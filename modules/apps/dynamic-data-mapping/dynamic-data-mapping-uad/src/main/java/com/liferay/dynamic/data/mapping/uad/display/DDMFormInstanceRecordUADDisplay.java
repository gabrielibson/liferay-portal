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

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.dynamic.data.mapping.constants.DDMPortletKeys;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.user.associated.data.display.UADDisplay;

/**
 * @author Brian Wing Shun Chan
 */
@Component(immediate = true, service = {UADDisplay.class, DDMFormInstanceRecordUADDisplay.class})
public class DDMFormInstanceRecordUADDisplay
	extends BaseDDMFormInstanceRecordUADDisplay {

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
	public String getName(DDMFormInstanceRecord ddmFormInstanceRecord, Locale locale) {
		return ddmFormInstanceRecord.getVersion();
	}

	@Override
	public Class<?> getParentContainerClass() {
		return DDMFormInstance.class;
	}
	
	@Override
	public Serializable getParentContainerId(DDMFormInstanceRecord ddmFormInstanceRecord) {
		return ddmFormInstanceRecord.getFormInstanceId();
	}
	
	@Override
	public boolean isUserOwned(DDMFormInstanceRecord formInstanceRecord, long userId) {
		if (formInstanceRecord.getUserId() == userId) {
			return true;
		}

		return false;
	}
	
	protected ThemeDisplay getThemeDisplay(
		HttpServletRequest httpServletRequest) {

		return (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Reference
	private Portal _portal;

}