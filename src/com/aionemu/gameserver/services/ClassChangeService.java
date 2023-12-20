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
package com.aionemu.gameserver.services;

import java.sql.Timestamp;
import java.util.Calendar;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class ClassChangeService {
	public static void showClassChangeDialog(Player player) {
		if (CustomConfig.ENABLE_SIMPLE_2NDCLASS) {
			PlayerClass playerClass = player.getPlayerClass();
			Race playerRace = player.getRace();
			if (player.getLevel() >= 9 && playerClass.isStartingClass()) {
				if (playerRace == Race.ELYOS) {
					switch (playerClass) {
					case WARRIOR:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 2375, 1006));
						break;
					case SCOUT:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 2716, 1006));
						break;
					case MAGE:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3057, 1006));
						break;
					case PRIEST:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3398, 1006));
						break;
					case TECHNIST:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3739, 1006));
						break;
					case MUSE:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 4080, 1006));
						break;
					default:
						break;
					}
				} else if (playerRace == Race.ASMODIANS) {
					switch (playerClass) {
					case WARRIOR:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3057, 2008));
						break;
					case SCOUT:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3398, 2008));
						break;
					case MAGE:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3739, 2008));
						break;
					case PRIEST:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 4080, 2008));
						break;
					case TECHNIST:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3569, 2008));
						break;
					case MUSE:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3910, 2008));
						break;
					default:
						break;
					}
				}
			}
		}
	}

	public static void changeClassToSelection(final Player player, final int dialogId) {
		Race playerRace = player.getRace();
		if (CustomConfig.ENABLE_SIMPLE_2NDCLASS) {
			if (playerRace == Race.ELYOS) {
				switch (dialogId) {
				case 2376:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("1")));
					break;
				case 2461:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("2")));
					break;
				case 2717:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("4")));
					break;
				case 2802:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("5")));
					break;
				case 3058:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("7")));
					break;
				case 3143:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("8")));
					break;
				case 3399:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("10")));
					break;
				case 3484:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("11")));
					break;
				case 3825:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("13")));
					break;
				case 3740:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("14")));
					break;
				case 4081:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("16")));
					break;
				}
				completeQuest(player, 1006);
                /* completeQuest(player, 1007); */
				if (player.havePermission(MembershipConfig.STIGMA_SLOT_QUEST)) {
					completeQuest(player, 1929);
					player.getController().upgradePlayer();
				}
			} else if (playerRace == Race.ASMODIANS) {
				switch (dialogId) {
				case 3058:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("1")));
					break;
				case 3143:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("2")));
					break;
				case 3399:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("4")));
					break;
				case 3484:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("5")));
					break;
				case 3740:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("7")));
					break;
				case 3825:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("8")));
					break;
				case 4081:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("10")));
					break;
				case 4166:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("11")));
					break;
				case 3591:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("13")));
					break;
				case 3570:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("14")));
					break;
				case 3911:
					setClass(player, PlayerClass.getPlayerClassById(Byte.parseByte("16")));
					break;
				}
				completeQuest(player, 2008);
                /* completeQuest(player, 2009); */
				if (player.havePermission(MembershipConfig.STIGMA_SLOT_QUEST)) {
					completeQuest(player, 2900);
					player.getController().upgradePlayer();
				} 
			}
            /* SkillLearnService.addMissingSkills(player); */
		}
	}

	private static void completeQuest(Player player, int questId) {
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		Calendar calendar = Calendar.getInstance();
		Timestamp timeStamp = new Timestamp(calendar.getTime().getTime());
		if (qs == null) {
			player.getQuestStateList().addQuest(questId, new QuestState(questId, QuestStatus.COMPLETE, 0, 1, null, 0, timeStamp));
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, QuestStatus.COMPLETE.value(), 0));
		} else {
			qs.setStatus(QuestStatus.COMPLETE);
			qs.setCompleteCount(qs.getCompleteCount() + 1);
            player.getCommonData().setExp(126150, true); //exp give from quest 1006, 2008 
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
		}
	}

	public static void setClass(Player player, PlayerClass playerClass) {
		if (validateSwitch(player, playerClass)) {
			player.getCommonData().setPlayerClass(playerClass);
			player.getController().upgradePlayer();
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0, 0));
		}
	}

	private static boolean validateSwitch(Player player, PlayerClass playerClass) {
		int level = player.getLevel();
		PlayerClass oldClass = player.getPlayerClass();
		if (level < 9) {
			PacketSendUtility.sendMessage(player, "You can only switch class at level 9");
			return false;
		}
		if (!oldClass.isStartingClass()) {
			PacketSendUtility.sendMessage(player, "You already switched class");
			return false;
		}
		switch (oldClass) {
		case WARRIOR:
			if (playerClass == PlayerClass.GLADIATOR || playerClass == PlayerClass.TEMPLAR)
				break;
		case SCOUT:
			if (playerClass == PlayerClass.ASSASSIN || playerClass == PlayerClass.RANGER)
				break;
		case MAGE:
			if (playerClass == PlayerClass.SORCERER || playerClass == PlayerClass.SPIRIT_MASTER)
				break;
		case PRIEST:
			if (playerClass == PlayerClass.CLERIC || playerClass == PlayerClass.CHANTER)
				break;
		case TECHNIST:
			if (playerClass == PlayerClass.GUNSLINGER || playerClass == PlayerClass.AETHERTECH)
				break;
		case MUSE:
			if (playerClass == PlayerClass.SONGWEAVER)
				break;
		default:
			PacketSendUtility.sendMessage(player, "Invalid class switch chosen");
			return false;
		}
		return true;
	}
}