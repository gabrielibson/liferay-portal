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

import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.portal.kernel.model.User;
import com.liferay.user.associated.data.anonymizer.UADAnonymizer;
import com.liferay.user.associated.data.test.util.BaseUADAnonymizerTestCase;
import com.liferay.user.associated.data.test.util.WhenHasStatusByUserIdField;

/**
 * @author Gabriel Ibson
 *
 */
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected DDMFormInstanceRecord addBaseModel(long userId, boolean deleteAfterTestRun) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected UADAnonymizer<DDMFormInstanceRecord> getUADAnonymizer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isBaseModelAutoAnonymized(long baseModelPK, User user) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isBaseModelDeleted(long baseModelPK) {
		// TODO Auto-generated method stub
		return false;
	}
}
