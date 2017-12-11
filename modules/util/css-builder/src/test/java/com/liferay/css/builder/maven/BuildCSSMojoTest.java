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

package com.liferay.css.builder.maven;

import com.liferay.css.builder.BaseCSSBuilderTestCase;
import com.liferay.css.builder.util.FileTestUtil;
import com.liferay.maven.executor.MavenExecutor;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.ClassRule;

/**
 * @author Andrea Di Giorgi
 */
public class BuildCSSMojoTest extends BaseCSSBuilderTestCase {

	@ClassRule
	public static final MavenExecutor mavenExecutor = new MavenExecutor();

	@Override
	protected void executeCSSBuilder(
			String dirName, Path docrootDirPath, boolean generateSourceMap,
			String outputDirName, Path portalCommonPath, int precision,
			String[] rtlExcludedPathRegexps, String sassCompilerClassName)
		throws Exception {

		_preparePomXml(
			dirName, docrootDirPath, generateSourceMap, outputDirName,
			portalCommonPath, precision, rtlExcludedPathRegexps,
			sassCompilerClassName);

		MavenExecutor.Result result = mavenExecutor.execute(
			docrootDirPath.toFile(), "css-builder:build");

		Assert.assertEquals(result.output, 0, result.exitCode);
	}

	private static void _preparePomXml(
			String dirName, Path docrootDirPath, boolean generateSourceMap,
			String outputDirName, Path portalCommonPath, int precision,
			String[] rtlExcludedPathRegexps, String sassCompilerClassName)
		throws IOException {

		String content = FileTestUtil.read(
			BuildCSSMojoTest.class, "dependencies/pom_xml.tmpl");

		content = _replace(
			content, "[$CSS_BUILDER_VERSION$]", _CSS_BUILDER_VERSION);

		content = _replace(content, "[$CSS_BUILDER_DIR_NAMES$]", dirName);
		content = _replace(
			content, "[$CSS_BUILDER_DOCROOT_DIR_NAME$]",
			String.valueOf(docrootDirPath.toAbsolutePath()));
		content = _replace(
			content, "[$CSS_BUILDER_GENERATE_SOURCE_MAP$]",
			String.valueOf(generateSourceMap));
		content = _replace(
			content, "[$CSS_BUILDER_OUTPUT_DIR_NAME$]", outputDirName);
		content = _replace(
			content, "[$CSS_BUILDER_PORTAL_COMMON_PATH$]",
			String.valueOf(portalCommonPath.toAbsolutePath()));
		content = _replace(
			content, "[$CSS_BUILDER_PRECISION$]", String.valueOf(precision));
		content = _replace(
			content, "[$CSS_BUILDER_RTL_EXCLUDED_PATH_REGEXPS$]",
			StringUtil.merge(rtlExcludedPathRegexps));
		content = _replace(
			content, "[$CSS_BUILDER_SASS_COMPILER_CLASS_NAME$]",
			sassCompilerClassName);

		Path pomXmlPath = docrootDirPath.resolve("pom.xml");

		Files.write(pomXmlPath, content.getBytes(StandardCharsets.UTF_8));
	}

	private static String _replace(String s, String key, String value) {
		if (value == null) {
			value = "";
		}

		return s.replace(key, value);
	}

	private static final String _CSS_BUILDER_VERSION = System.getProperty(
		"css.builder.version");

}