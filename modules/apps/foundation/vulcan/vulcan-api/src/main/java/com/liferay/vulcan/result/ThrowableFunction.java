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

package com.liferay.vulcan.result;

import java.util.Objects;

/**
 * Defines a {@code java.util.function.Function} that can throw an exception.
 *
 * @author Alejandro Hernández
 */
@FunctionalInterface
@SuppressWarnings("unused")
public interface ThrowableFunction<T, R> {

	/**
	 * Returns a composed function that first applies the current {@code
	 * ThrowableFunction} instance to its input, and then applies the {@code
	 * throwableFunction} to the result.
	 *
	 * @param  throwableFunction the function to apply after applying the
	 *         current {@code ThrowableFunction} instance
	 * @return the composed function
	 * @see    #compose(ThrowableFunction)
	 */
	public default <V> ThrowableFunction<T, V> andThen(
		ThrowableFunction<? super R, ? extends V> throwableFunction) {

		Objects.requireNonNull(throwableFunction);

		return (T t) -> throwableFunction.apply(apply(t));
	}

	/**
	 * Returns the result of applying the current {@code ThrowableFunction}
	 * instance to the argument.
	 *
	 * @param  t the argument
	 * @return the result of applying the current {@code ThrowableFunction}
	 *         instance to the argument
	 */
	public R apply(T t) throws Exception;

	/**
	 * Returns a composed function that first applies the {@code
	 * throwableFunction} to its input, and then applies the current {@code
	 * ThrowableFunction} instance to the result.
	 *
	 * @param  throwableFunction the function to apply before applying the
	 *         current {@code ThrowableFunction}  instance
	 * @return the composed function
	 * @see    #andThen(ThrowableFunction)
	 */
	public default <V> ThrowableFunction<V, R> compose(
		ThrowableFunction<? super V, ? extends T> throwableFunction) {

		Objects.requireNonNull(throwableFunction);

		return (V v) -> apply(throwableFunction.apply(v));
	}

}