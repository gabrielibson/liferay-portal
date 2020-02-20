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

package com.liferay.dynamic.data.mapping.uad.exporter.test;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.dynamic.data.mapping.model.DDMContent;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.user.associated.data.exporter.UADExporter;

/**
 * @author Brian Wing Shun Chan
 */
@Component(immediate = true, service = UADExporter.class)
public class DDMContentUADExporter extends BaseDDMContentUADExporter {
	
	@Override
	protected String toXmlString(DDMContent ddmContent) {
		StringBundler sb = new StringBundler(19);

		sb.append("<model><model-name>");
		sb.append("com.liferay.dynamic.data.mapping.model.DDMContent");
		sb.append("</model-name>");

		sb.append(_getFormInstanceData(ddmContent.getContentId()));
		sb.append(
			"<column><column-name>contentId</column-name><column-value><![CDATA[");
		sb.append(ddmContent.getContentId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(ddmContent.getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userName</column-name><column-value><![CDATA[");
		sb.append(ddmContent.getUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>description</column-name><column-value><![CDATA[");
		sb.append(ddmContent.getDescription());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>data</column-name><column-value><![CDATA[");
		sb.append(ddmContent.getData());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}
	
	private String _getFormInstanceData(long contentId) {
		DDMFormInstanceRecord ddmFormInstanceRecord = ddmFormInstanceLocalService.fetchFormInstanceRecordByStorageId(contentId);
		StringBundler sb = new StringBundler();
		try {
			DDMFormInstance ddmFormInstance = ddmFormInstanceRecord.getFormInstance();
			
			sb.append(
					"<column><column-name>formName</column-name><column-value><![CDATA[");
				sb.append(ddmFormInstance.getName());
				sb.append("]]></column-value></column>");
				sb.append(
					"<column><column-name>description</column-name><column-value><![CDATA[");
				sb.append(ddmFormInstance.getDescription());
				sb.append("]]></column-value></column>");
			
		}catch(PortalException portalException) {
			new PortalException("Portal Exception: "+ portalException);
		}
	
		return sb.toString();
	}
	
	@Reference
	protected DDMFormInstanceRecordLocalService ddmFormInstanceLocalService;
}