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

package com.liferay.vulcan.sample.internal.logger;

import com.liferay.vulcan.logger.VulcanLogger;
import com.liferay.vulcan.result.APIError;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alejandro Hernández
 */
@Component(immediate = true)
public class VulcanLoggerImpl implements VulcanLogger {

	@Override
	public void error(APIError apiError) {
		if (_logger.isDebugEnabled()) {
			_logger.debug(
				_getExceptionMessage(apiError), apiError.getException());
		}
		else {
			_logger.error(_getExceptionMessage(apiError));
		}
	}

	private String _getExceptionMessage(APIError apiError) {
		Optional<String> optional = apiError.getDescription();

		return optional.orElseGet(
			() -> {
				Exception exception = apiError.getException();

				String message = exception.getMessage();

				if ((message != null) && !message.isEmpty()) {
					return message;
				}

				return apiError.toString();
			});
	}

	private static final Logger _logger = LoggerFactory.getLogger(
		VulcanLoggerImpl.class);

}