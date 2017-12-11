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

package com.liferay.vulcan.test.internal.result;

import static com.liferay.vulcan.test.result.TryMatchers.aSuccessTry;
import static com.liferay.vulcan.test.result.TryMatchers.aTryWithValueThat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.liferay.vulcan.result.Try;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class ValueTryTest {

	@Test
	public void testFailureTryFails() {
		Try<String> stringTry = Try.fail(new Exception());

		assertThat(
			stringTry, is(not(aTryWithValueThat(is(equalTo("vulcan"))))));
	}

	@Test
	public void testFailureTryUpdatesDescription() {
		Try<String> stringTry = Try.fail(new Exception());

		Description description = new StringDescription();

		aTryWithValueThat(is(equalTo("vulcan"))).describeMismatch(
			stringTry, description);

		String expected = "was a Failure";

		assertThat(description.toString(), is(equalTo(expected)));
	}

	@Test
	public void testInvalidSuccessTryUpdatesDescription() {
		Try<String> stringTry = Try.success("Live long and prosper");

		Description description = new StringDescription();

		aTryWithValueThat(is(equalTo("vulcan"))).describeMismatch(
			stringTry, description);

		String expected =
			"was a Success whose value was \"Live long and prosper\"";

		assertThat(description.toString(), is(equalTo(expected)));
	}

	@Test
	public void testSuccessTryMatcherUpdatesDescription() {
		Description description = new StringDescription();

		aTryWithValueThat(is(equalTo("vulcan"))).describeTo(description);

		String expected = "a Success with a value that is \"vulcan\"";

		assertThat(description.toString(), is(equalTo(expected)));
	}

	@Test
	public void testSuccessTryValidates() {
		Try<String> stringTry = Try.success("Live long and prosper");

		boolean matches = aSuccessTry().matches(stringTry);

		assertThat(matches, is(true));
	}

}