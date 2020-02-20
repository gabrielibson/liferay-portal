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

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.osgi.service.component.annotations.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.user.associated.data.display.UADDisplay;

/**
 * @author Brian Wing Shun Chan
 */
@Component(immediate = true, service = {UADDisplay.class, DDMFormInstanceUADDisplay.class})
public class DDMFormInstanceUADDisplay extends BaseDDMFormInstanceUADDisplay {
	
	@Override
	public String getName(DDMFormInstance ddmFormInstance, Locale locale) {

		Document document = _toXMLDocument(ddmFormInstance.getName());
		
		return document.getFirstChild().getChildNodes().item(0).getTextContent();
	}
	
	@Override
	public DDMFormInstance getTopLevelContainer(Class parentContainerClass, Serializable parentContainerId,
			Object childObject) {
		
		if((long)parentContainerId == -1) {
			return null;
		}
		
		try {
			
			if(childObject instanceof DDMFormInstanceRecord) {
				
				DDMFormInstanceRecord ddmFormInstanceRecord = (DDMFormInstanceRecord)childObject;
				
				return ddmFormInstanceRecord.getFormInstance();
			}
			
			
		} catch (PortalException e) {
			_log.error(e, e);
		}
		
		return null;
	}
	
	@Override
	public Class<?> getParentContainerClass() {
		return DDMFormInstance.class;
	}
	
	@Override
	public Serializable getParentContainerId(DDMFormInstance ddmFormInstance) {
		return 0;
	}
	
	@Override
	public boolean isUserOwned(DDMFormInstance ddmFormInstance, long userId) {
		if (ddmFormInstance.getUserId() == userId) {
			return true;
		}

		return false;
	}
	
	private Document _toXMLDocument(String xml) {
		try {
			
			DocumentBuilderFactory documentBuilderFactory =  DocumentBuilderFactory.newInstance();
			
			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
			
			return builder.parse(new InputSource(new StringReader(xml)));
			
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			_log.error(e, e);
		}
		
		return null;
	}
	
	private static final Log _log = LogFactoryUtil.getLog(
			DDMFormInstanceUADDisplay.class);
}