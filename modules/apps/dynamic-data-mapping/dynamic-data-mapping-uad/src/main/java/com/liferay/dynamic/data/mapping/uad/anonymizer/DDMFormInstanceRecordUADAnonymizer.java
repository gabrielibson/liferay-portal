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

package com.liferay.dynamic.data.mapping.uad.anonymizer;

import com.liferay.dynamic.data.mapping.model.DDMContent;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecordVersion;
import com.liferay.dynamic.data.mapping.service.DDMContentLocalService;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordVersionLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.user.associated.data.anonymizer.UADAnonymizer;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(immediate = true, service = UADAnonymizer.class)
public class DDMFormInstanceRecordUADAnonymizer
	extends BaseDDMFormInstanceRecordUADAnonymizer {

	@Override
	public void autoAnonymize(
			DDMFormInstanceRecord ddmFormInstanceRecord, long userId,
			User anonymousUser)
		throws PortalException {

		super.autoAnonymize(ddmFormInstanceRecord, userId, anonymousUser);

		List<DDMFormInstanceRecordVersion> ddmFormInstanceRecordVersions =
			ddmFormInstanceRecordVersionLocalService.
				getFormInstanceRecordVersions(
					ddmFormInstanceRecord.getFormInstanceRecordId(),
					QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		ddmFormInstanceRecordVersions.forEach(
			ddmFormInstanceRecordVersion -> {
				if (ddmFormInstanceRecordVersion.getUserId() == userId) {
					ddmFormInstanceRecordVersion.setUserId(
						anonymousUser.getUserId());

					ddmFormInstanceRecordVersion.setUserName(
						anonymousUser.getFullName());

					ddmFormInstanceRecordVersionLocalService.
						updateDDMFormInstanceRecordVersion(
							ddmFormInstanceRecordVersion);
				}
			});

		DDMContent ddmContent = ddmContentLocalService.fetchDDMContent(
			ddmFormInstanceRecord.getStorageId());

		if ((ddmContent != null) && (ddmContent.getUserId() == userId)) {
			ddmContent.setUserId(anonymousUser.getUserId());

			ddmContent.setUserName(anonymousUser.getFullName());

			ddmContentLocalService.updateDDMContent(ddmContent);
		}
	}

	@Reference
	protected DDMContentLocalService ddmContentLocalService;

	@Reference
	protected DDMFormInstanceRecordVersionLocalService
		ddmFormInstanceRecordVersionLocalService;

}