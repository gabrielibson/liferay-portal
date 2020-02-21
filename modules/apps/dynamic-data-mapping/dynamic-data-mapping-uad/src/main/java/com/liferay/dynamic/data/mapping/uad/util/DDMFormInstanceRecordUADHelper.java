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

package com.liferay.dynamic.data.mapping.uad.util;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalService;
import com.liferay.petra.string.CharPool;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.xml.SecureXMLFactoryProviderUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.io.StringReader;

import java.lang.reflect.Method;

import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Gabriel Ibson
 * @author Marcos Martins
 */
@Component(immediate = true, service = DDMFormInstanceRecordUADHelper.class)
public class DDMFormInstanceRecordUADHelper {

	public void addGroupIdRestriction(
		DynamicQuery dynamicQuery, long[] groupIds) {

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"groupId", ArrayUtil.toLongArray(groupIds)));
	}

	public DynamicQuery createFormInstanceQuery(
		String keywords, String[] searchableFields, String orderByField,
		String orderByType) {

		DynamicQuery dynamicQuery = _ddmFormInstanceLocalService.dynamicQuery();

		if (Validator.isNotNull(keywords) && (searchableFields.length > 0)) {
			Disjunction disjunction = RestrictionsFactoryUtil.disjunction();

			String quotedKeywords = StringUtil.quote(
				keywords, CharPool.PERCENT);

			Class<?> clazz = DDMFormInstance.class;

			for (String searchableField : searchableFields) {
				try {
					String formattedSearchableField = TextFormatter.format(
						searchableField, TextFormatter.G);

					Method method = clazz.getMethod(
						"get" + formattedSearchableField);

					if (method.getReturnType() == String.class) {
						disjunction.add(
							RestrictionsFactoryUtil.ilike(
								searchableField, quotedKeywords));
					}
				}
				catch (NoSuchMethodException | SecurityException exception) {
					if (_log.isDebugEnabled()) {
						_log.debug(exception, exception);
					}
				}
			}

			dynamicQuery.add(disjunction);
		}

		if (orderByField != null) {
			Order order = null;

			if (Objects.equals(orderByType, "desc")) {
				order = OrderFactoryUtil.desc(orderByField);
			}
			else {
				order = OrderFactoryUtil.asc(orderByField);
			}

			dynamicQuery.addOrder(order);
		}

		return dynamicQuery;
	}

	public Document toXMLDocument(String xml) {
		try {
			DocumentBuilderFactory documentBuilderFactory =
				SecureXMLFactoryProviderUtil.newDocumentBuilderFactory();

			DocumentBuilder builder =
				documentBuilderFactory.newDocumentBuilder();

			return builder.parse(new InputSource(new StringReader(xml)));
		}
		catch (IOException | ParserConfigurationException | SAXException
					exception) {

			_log.error(exception, exception);
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMFormInstanceRecordUADHelper.class);

	@Reference
	private DDMFormInstanceLocalService _ddmFormInstanceLocalService;

}