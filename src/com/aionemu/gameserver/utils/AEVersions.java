/*

 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.utils;

import com.aionemu.commons.utils.AEInfos;
import com.aionemu.commons.versionning.Version;
import com.aionemu.gameserver.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lord_rex
 */
public class AEVersions {

	private static final Logger log = LoggerFactory.getLogger(AEVersions.class);
	private static final Version commons = new Version(AEInfos.class);
	private static final Version gameserver = new Version(GameServer.class);

	private static String getRevisionInfo(Version version) {
		return String.format("%-6s", version.getRevision());
	}

	private static String getBranchInfo(Version version) {
		return String.format("%-6s", version.getBranch());
	}

	private static String getBranchCommitTimeInfo(Version version) {
		return String.format("%-6s", version.getCommitTime());
	}

	private static String getDateInfo(Version version) {
		return String.format("[ %4s ]", version.getDate());
	}

	public static String[] getFullVersionInfo() {
		return new String[] { "Commons Revision: " + getRevisionInfo(commons),
		"Commons Build Date: " + getDateInfo(commons), "GS Revision: " + getRevisionInfo(gameserver),
		"GS Branch: " + getBranchInfo(gameserver), "GS Branch Commit Date: " + getBranchCommitTimeInfo(gameserver),
		"GS Build Date: " + getDateInfo(gameserver), "..................................................",
		".................................................." };
	}

	public static void printFullVersionInfo() {
		for (String line : getFullVersionInfo()) {
			log.info(line);
		}
	}
}