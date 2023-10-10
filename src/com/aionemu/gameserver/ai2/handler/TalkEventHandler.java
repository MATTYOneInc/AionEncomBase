/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.ai2.handler;

import java.util.List;

import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AISubState;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.TownService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class TalkEventHandler {
	public static void onTalk(NpcAI2 npcAI, Creature creature) {
		onSimpleTalk(npcAI, creature);
		if (creature instanceof Player) {
			Player player = (Player) creature;
			List<Integer> relatedQuests = QuestEngine.getInstance().getQuestNpc(npcAI.getOwner().getNpcId())
					.getOnTalkEvent();
			if (QuestEngine.getInstance().onDialog(new QuestEnv(npcAI.getOwner(), player, 0, -1))) {
				return;
			}
			for (int questId : relatedQuests) {
				if (player.getQuestStateList().hasQuest(questId)) {
					final QuestState qs = player.getQuestStateList().getQuestState(questId);
					if (qs == null || qs.getStatus() != QuestStatus.REWARD) {
						continue;
					}
					PacketSendUtility.sendPacket(player,
							new SM_DIALOG_WINDOW(npcAI.getOwner().getObjectId(), 5, questId));
					return;
				}
			}
			switch (npcAI.getOwner().getObjectTemplate().getTitleId()) {
			case 462877: // Village Trade Broker.
			case 462878: // Village Guestbloom.
				// case 462881: //Village Quest Board.
				// Oriel.
			case 730677:
			case 831198:
			case 831199:
			case 831200:
			case 831201:
			case 831202:
			case 831203:
			case 831204:
			case 831205:
			case 831206:
			case 831207:
			case 831208:
			case 831209:
			case 831211:
			case 831212:
				// Pernon.
			case 730679:
			case 831223:
			case 831224:
			case 831225:
			case 831226:
			case 831227:
			case 831228:
			case 831229:
			case 831230:
			case 831231:
			case 831232:
			case 831233:
			case 831234:
			case 831236:
			case 831237:
				int playerTownId = TownService.getInstance().getTownResidence(player);
				int currentTownId = TownService.getInstance().getTownIdByPosition(player);
				if (playerTownId != currentTownId) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcAI.getOwner().getObjectId(), 44));
					return;
				} else {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcAI.getOwner().getObjectId(), 10));
					return;
				}
			default:
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcAI.getOwner().getObjectId(), 10));
				break;
			}
		}
	}

	public static void onSimpleTalk(NpcAI2 npcAI, Creature creature) {
		if (npcAI.getOwner().getObjectTemplate().isDialogNpc()) {
			npcAI.setSubStateIfNot(AISubState.TALK);
			npcAI.getOwner().setTarget(creature);
		}
	}

	public static void onFinishTalk(NpcAI2 npcAI, Creature creature) {
		Npc owner = npcAI.getOwner();
		if (owner.isTargeting(creature.getObjectId())) {
			if (npcAI.getState() != AIState.FOLLOWING) {
				owner.setTarget(null);
			}
			npcAI.think();
		}
	}

	public static void onSimpleFinishTalk(NpcAI2 npcAI, Creature creature) {
		Npc owner = npcAI.getOwner();
		if (owner.isTargeting(creature.getObjectId()) && npcAI.setSubStateIfNot(AISubState.NONE)) {
			owner.setTarget(null);
		}
	}
}
