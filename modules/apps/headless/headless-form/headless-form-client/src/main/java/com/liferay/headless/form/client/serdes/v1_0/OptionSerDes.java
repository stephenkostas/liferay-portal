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

package com.liferay.headless.form.client.serdes.v1_0;

import com.liferay.headless.form.client.dto.v1_0.Option;
import com.liferay.headless.form.client.json.BaseJSONParser;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class OptionSerDes {

	public static Option toDTO(String json) {
		OptionJSONParser optionJSONParser = new OptionJSONParser();

		return optionJSONParser.parseToDTO(json);
	}

	public static Option[] toDTOs(String json) {
		OptionJSONParser optionJSONParser = new OptionJSONParser();

		return optionJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Option option) {
		if (option == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"id\": ");

		if (option.getId() == null) {
			sb.append("null");
		}
		else {
			sb.append(option.getId());
		}

		sb.append(", ");

		sb.append("\"label\": ");

		if (option.getLabel() == null) {
			sb.append("null");
		}
		else {
			sb.append("\"");

			sb.append(option.getLabel());

			sb.append("\"");
		}

		sb.append(", ");

		sb.append("\"value\": ");

		if (option.getValue() == null) {
			sb.append("null");
		}
		else {
			sb.append("\"");

			sb.append(option.getValue());

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	private static class OptionJSONParser extends BaseJSONParser<Option> {

		protected Option createDTO() {
			return new Option();
		}

		protected Option[] createDTOArray(int size) {
			return new Option[size];
		}

		protected void setField(
			Option option, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					option.setId(Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "label")) {
				if (jsonParserFieldValue != null) {
					option.setLabel((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "value")) {
				if (jsonParserFieldValue != null) {
					option.setValue((String)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}