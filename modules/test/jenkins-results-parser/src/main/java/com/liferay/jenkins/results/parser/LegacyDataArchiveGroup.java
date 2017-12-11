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

package com.liferay.jenkins.results.parser;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Hashimoto
 */
public class LegacyDataArchiveGroup {

	public LegacyDataArchiveGroup(
		LegacyDataArchiveUtil legacyDataArchiveUtil,
		String legacyDataArchiveType, String portalVersion) {

		_legacyDataArchiveUtil = legacyDataArchiveUtil;
		_legacyDataArchiveType = legacyDataArchiveType;
		_portalVersion = portalVersion;
	}

	public void addLegacyDataArchive(LegacyDataArchive legacyDataArchive) {
		_legacyDataArchives.add(legacyDataArchive);
	}

	public void commitLegacyDataArchives() throws IOException {
		for (LegacyDataArchive legacyDataArchive : _legacyDataArchives) {
			if (!legacyDataArchive.isUpdated()) {
				legacyDataArchive.updateLegacyDataArchive();
			}
		}

		GitWorkingDirectory legacyDataGitWorkingDirectory =
			_legacyDataArchiveUtil.getLegacyDataGitWorkingDirectory();

		String status = legacyDataGitWorkingDirectory.status();

		if (!status.contains("nothing to commit") &&
			!status.contains("nothing added to commit")) {

			Commit latestManualCommit =
				_legacyDataArchiveUtil.getLatestManualCommit();

			legacyDataGitWorkingDirectory.commitStagedFilesToCurrentBranch(
				JenkinsResultsParserUtil.combine(
					"archive:ignore Update '", _legacyDataArchiveType,
					"' for '", _portalVersion, "' at ",
					latestManualCommit.getAbbreviatedSHA(), "."));

			String gitLog = legacyDataGitWorkingDirectory.log(1);

			_commit = CommitFactory.newCommit(gitLog);
		}
	}

	public Commit getCommit() {
		return _commit;
	}

	public String getLegacyDataArchiveType() {
		return _legacyDataArchiveType;
	}

	public boolean isUpdated() {
		for (LegacyDataArchive legacyDataArchive : _legacyDataArchives) {
			if (!legacyDataArchive.isUpdated()) {
				return false;
			}
		}

		return true;
	}

	private Commit _commit;
	private List<LegacyDataArchive> _legacyDataArchives = new ArrayList<>();
	private final String _legacyDataArchiveType;
	private final LegacyDataArchiveUtil _legacyDataArchiveUtil;
	private final String _portalVersion;

}