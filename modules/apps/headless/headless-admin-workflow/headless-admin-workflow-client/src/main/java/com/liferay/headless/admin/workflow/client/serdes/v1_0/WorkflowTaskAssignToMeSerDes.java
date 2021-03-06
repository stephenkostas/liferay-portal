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

package com.liferay.headless.admin.workflow.client.serdes.v1_0;

import com.liferay.headless.admin.workflow.client.dto.v1_0.WorkflowTaskAssignToMe;
import com.liferay.headless.admin.workflow.client.json.BaseJSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class WorkflowTaskAssignToMeSerDes {

	public static WorkflowTaskAssignToMe toDTO(String json) {
		WorkflowTaskAssignToMeJSONParser workflowTaskAssignToMeJSONParser =
			new WorkflowTaskAssignToMeJSONParser();

		return workflowTaskAssignToMeJSONParser.parseToDTO(json);
	}

	public static WorkflowTaskAssignToMe[] toDTOs(String json) {
		WorkflowTaskAssignToMeJSONParser workflowTaskAssignToMeJSONParser =
			new WorkflowTaskAssignToMeJSONParser();

		return workflowTaskAssignToMeJSONParser.parseToDTOs(json);
	}

	public static String toJSON(WorkflowTaskAssignToMe workflowTaskAssignToMe) {
		if (workflowTaskAssignToMe == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

		sb.append("\"comment\": ");

		if (workflowTaskAssignToMe.getComment() == null) {
			sb.append("null");
		}
		else {
			sb.append("\"");

			sb.append(workflowTaskAssignToMe.getComment());

			sb.append("\"");
		}

		sb.append(", ");

		sb.append("\"dueDate\": ");

		if (workflowTaskAssignToMe.getDueDate() == null) {
			sb.append("null");
		}
		else {
			sb.append("\"");

			sb.append(
				liferayToJSONDateFormat.format(
					workflowTaskAssignToMe.getDueDate()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	private static class WorkflowTaskAssignToMeJSONParser
		extends BaseJSONParser<WorkflowTaskAssignToMe> {

		protected WorkflowTaskAssignToMe createDTO() {
			return new WorkflowTaskAssignToMe();
		}

		protected WorkflowTaskAssignToMe[] createDTOArray(int size) {
			return new WorkflowTaskAssignToMe[size];
		}

		protected void setField(
			WorkflowTaskAssignToMe workflowTaskAssignToMe,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "comment")) {
				if (jsonParserFieldValue != null) {
					workflowTaskAssignToMe.setComment(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dueDate")) {
				if (jsonParserFieldValue != null) {
					workflowTaskAssignToMe.setDueDate(
						toDate((String)jsonParserFieldValue));
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}