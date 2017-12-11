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

package com.liferay.vulcan.test.internal.json;

import static com.liferay.vulcan.test.json.JsonMatchers.aJsonBoolean;
import static com.liferay.vulcan.test.json.JsonMatchers.aJsonObjectStringWith;
import static com.liferay.vulcan.test.json.JsonMatchers.aJsonString;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.liferay.vulcan.test.json.Conditions;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class IsJsonObjectStringMatcherTest {

	@Test
	public void testInvalidJsonObjectFails() {
		String json = "{";

		Conditions conditions = _builder.build();

		assertThat(json, is(not(aJsonObjectStringWith(conditions))));
	}

	@Test
	public void testInvalidJsonObjectUpdatesValidDescription() {
		Conditions conditions = _builder.build();

		Matcher<String> stringMatcher = aJsonObjectStringWith(conditions);

		Description description = new StringDescription();

		stringMatcher.describeMismatch("{", description);

		assertThat(
			description.toString(), is(equalTo("was not a JSON object")));
	}

	@Test
	public void testInvalidJsonObjectWithMultiConditionUpdatesDescription() {
		Conditions conditions = _builder.where(
			"vulcan", is(aJsonString(equalTo("Live long and prosper")))
		).where(
			"geek", is(aJsonBoolean(true))
		).build();

		Matcher<String> stringMatcher = aJsonObjectStringWith(conditions);

		Description description = new StringDescription();

		stringMatcher.describeMismatch("{}", description);

		String expected =
			"was a JSON object {\n  vulcan: was null\n  geek: was null\n}";

		assertThat(description.toString(), is(equalTo(expected)));
	}

	@Test
	public void testIsJsonMatcherUpdatesDescription() {
		Conditions conditions = _builder.build();

		Matcher<String> stringMatcher = aJsonObjectStringWith(conditions);

		Description description = new StringDescription();

		stringMatcher.describeTo(description);

		assertThat(
			description.toString(), is(equalTo("a JSON object where {\n}")));
	}

	@Test
	public void testValidEmptyJsonObjectValidates() {
		String json = "{}";

		Conditions conditions = _builder.build();

		assertThat(json, is(aJsonObjectStringWith(conditions)));
	}

	private final Conditions.Builder _builder = new Conditions.Builder();

}