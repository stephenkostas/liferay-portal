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

package com.liferay.adaptive.media.image.mime.type;

/**
 * Provides the supported adaptive media image mime types.
 *
 * @author Sergio González
 */
public interface AMImageMimeTypeProvider {

	/**
	 * Returns the supported mime types that generate adaptive media images.
	 *
	 * @return the supported mime types that generate adaptive media images
	 */
	public String[] getSupportedMimeTypes();

	/**
	 * Returns whether the provided mime type generates adaptive media images.
	 *
	 * @param mimeType the mime type to check wheter it generates adaptive media
	 *                    images.
	 *
	 * @return <code>true</code> if the mime type generates adaptive media
	 * images; <code>false</code> otherwise
	 *
	 * @review
	 */
	public boolean isMimeTypeSupported(String mimeType);

}