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
package com.liferay.dynamic.data.mapping.uad.anonymizer.test;

import org.junit.runner.RunWith;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalService;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.test.rule.Inject;
import com.liferay.user.associated.data.anonymizer.UADAnonymizer;
import com.liferay.user.associated.data.test.util.BaseUADAnonymizerTestCase;
import com.liferay.user.associated.data.test.util.WhenHasStatusByUserIdField;

/**
 * @author Gabriel Ibson
 *
 */
@RunWith(Arquillian.class)
public class DDMFormInstanceRecordUADAnonymizerTest 
	extends BaseUADAnonymizerTestCase<DDMFormInstanceRecord>
	implements WhenHasStatusByUserIdField<DDMFormInstanceRecord>{

	@Override
	public DDMFormInstanceRecord addBaseModelWithStatusByUserId(long userId, long statusByUserId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected DDMFormInstanceRecord addBaseModel(long userId) throws Exception {
		return addBaseModel(userId, true);
	}

	@Override
	protected DDMFormInstanceRecord addBaseModel(long userId, boolean deleteAfterTestRun) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected UADAnonymizer<DDMFormInstanceRecord> getUADAnonymizer() {
		return _uadAnonymizer;
	}

	@Override
	protected boolean isBaseModelAutoAnonymized(long baseModelPK, User user)
			throws Exception {
		DDMFormInstanceRecord ddmFormInstanceRecord = 
				_ddmFormInstanceRecordLocalService.
					getDDMFormInstanceRecord(baseModelPK);

		String userName = ddmFormInstanceRecord.getUserName();

		if ((ddmFormInstanceRecord.getUserId() != user.getUserId()) 
				&& !userName.equals(user.getFullName())) {

			return true;
		}

		return false;
	}

	@Override
	protected boolean isBaseModelDeleted(long baseModelPK) {
		if (_ddmFormInstanceRecordLocalService.
				fetchDDMFormInstanceRecord(baseModelPK) == null) {

			return true;
		}

		return false;
	}
	
	@Inject
	private DDMFormInstanceRecordLocalService 
		_ddmFormInstanceRecordLocalService;
	
	@Inject(filter = "component.name=*.DDMFormInstanceRecordUADAnonymizer")
	private UADAnonymizer<DDMFormInstanceRecord> _uadAnonymizer;
}
