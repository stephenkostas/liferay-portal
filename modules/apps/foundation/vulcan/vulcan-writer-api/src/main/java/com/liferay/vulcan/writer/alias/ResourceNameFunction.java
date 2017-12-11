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

package com.liferay.vulcan.writer.alias;

import java.util.Optional;
import java.util.function.Function;

/**
 * Defines a type alias for a function that receives the class name of a model
 * and returns an {@code Optional} of the name of its {@code Representor}.
 *
 * @author Alejandro Hernández
 * @review
 */
@FunctionalInterface
public interface ResourceNameFunction
	extends Function<String, Optional<String>> {
}