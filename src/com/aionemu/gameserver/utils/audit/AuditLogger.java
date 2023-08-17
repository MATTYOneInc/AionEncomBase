/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.utils.audit;

import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.configs.main.PunishmentConfig;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MrPoke
 */
public class AuditLogger {

	private static final Logger log = LoggerFactory.getLogger("AUDIT_LOG");

	public static final void info(Player player, String message) {
		Preconditions.checkNotNull(player, "Player should not be null or use different info method");
		if (LoggingConfig.LOG_AUDIT) {
			info(player.getName(), player.getObjectId(), message);
		}
		if (PunishmentConfig.PUNISHMENT_ENABLE) {
			AutoBan.punishment(player, message);
		}
	}

	public static final void info(String playerName, int objectId, String message) {
		message += " Player name: " + playerName + " objectId: " + objectId;
		log.info(message);

		if (SecurityConfig.GM_AUDIT_MESSAGE_BROADCAST) {
			GMService.getInstance().broadcastMesage(message);
		}
	}
}