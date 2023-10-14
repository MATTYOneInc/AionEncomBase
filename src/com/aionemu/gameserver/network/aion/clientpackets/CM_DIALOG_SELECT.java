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
package com.aionemu.gameserver.network.aion.clientpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.ClassChangeService;
import com.aionemu.gameserver.services.QuestService;

public class CM_DIALOG_SELECT extends AionClientPacket {
	private int targetObjectId;
	private int dialogId;
	private int extendedRewardIndex;
	@SuppressWarnings("unused")
	private int lastPage;
	private int questId;
	private int unk;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CM_DIALOG_SELECT.class);

	public CM_DIALOG_SELECT(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		targetObjectId = readD();
		dialogId = readH();
		extendedRewardIndex = readH();
		unk = readH();
		lastPage = readH();
		questId = readD();
		readH();
	}

	@Override
	protected void runImpl() {
		final Player player = getConnection().getActivePlayer();
		QuestTemplate questTemplate = DataManager.QUEST_DATA.getQuestById(questId);
		QuestEnv env = new QuestEnv(null, player, questId, 0);
		if (player.isInPlayerMode(PlayerMode.RIDE)) {
			player.unsetPlayerMode(PlayerMode.RIDE);
		}
		if (player.isTrading()) {
			return;
		}
		if (targetObjectId == 0 || targetObjectId == player.getObjectId()) {
			if (questTemplate != null && !questTemplate.isCannotShare() && (dialogId == 1002 || dialogId == 20000)) {
				QuestService.startQuest(env);
				return;
			}
			if (QuestEngine.getInstance().onDialog(new QuestEnv(null, player, questId, dialogId))) {
				return;
			}
			ClassChangeService.changeClassToSelection(player, dialogId);
			return;
		}
		VisibleObject obj = player.getKnownList().getObject(targetObjectId);
		if (obj != null && obj instanceof Creature) {
			Creature creature = (Creature) obj;
			creature.getController().onDialogSelect(dialogId, player, questId, extendedRewardIndex, unk);
		}
	}
}