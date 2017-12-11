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

package com.liferay.vulcan.wiring.osgi.util;

import com.liferay.vulcan.result.Try;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Provides methods for skipping problems related to the Java generics system.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
public class GenericUtil {

	/**
	 * Returns the class of the parameterized class's first type argument.
	 *
	 * @param  clazz the parameterized class
	 * @return the class of the parameterized class's first type argument
	 */
	public static <S> Try<Class<S>> getFirstGenericTypeArgumentTry(
		Class<?> clazz) {

		return getGenericTypeArgumentTry(clazz, 0);
	}

	/**
	 * Returns the class of the first type argument in the {@code Type}.
	 *
	 * @param  type the type
	 * @return the class of the type's first type argument
	 */
	public static <S> Try<Class<S>> getFirstGenericTypeArgumentTry(Type type) {
		return getGenericTypeArgumentTry(type, 0);
	}

	/**
	 * Returns the class of the parameterized class's n-th type argument.
	 *
	 * @param  clazz the parameterized class
	 * @param  position the n-th type argument's position in the parameterized
	 *         class
	 * @return the class of the parameterized class's n-th type argument
	 */
	public static <S> Try<Class<S>> getGenericTypeArgumentTry(
		Class<?> clazz, int position) {

		Type[] genericInterfaces = clazz.getGenericInterfaces();

		Try<Class<S>> classTry = Try.fail(
			new IllegalArgumentException(
				"Class " + clazz + " does not implement any interfaces"));

		for (Type genericInterface : genericInterfaces) {
			classTry = classTry.recoverWith(
				throwable -> getGenericTypeArgumentTry(
					genericInterface, position));
		}

		return classTry.recoverWith(
			throwable -> getGenericTypeArgumentTry(
				clazz.getSuperclass(), position));
	}

	/**
	 * Returns the class of the n-th type argument in the {@code Type}.
	 *
	 * @param  type the type
	 * @param  position the type's n-th type argument
	 * @return the class of the type's n-th type argument
	 */
	public static <S> Try<Class<S>> getGenericTypeArgumentTry(
		Type type, int position) {

		Try<Type> typeTry = Try.success(type);

		return typeTry.filter(
			ParameterizedType.class::isInstance
		).map(
			ParameterizedType.class::cast
		).map(
			ParameterizedType::getActualTypeArguments
		).filter(
			typeArguments -> {
				if (typeArguments.length >= 1) {
					return true;
				}

				return false;
			}
		).map(
			typeArguments -> typeArguments[position]
		).map(
			typeArgument -> {
				if (typeArgument instanceof ParameterizedType) {
					return ((ParameterizedType)typeArgument).getRawType();
				}

				return typeArgument;
			}
		).map(
			typeArgument -> (Class<S>)typeArgument
		);
	}

}