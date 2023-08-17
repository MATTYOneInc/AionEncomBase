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
package com.aionemu.gameserver.questEngine.handlers;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.model.templates.rewards.BonusType;
import com.aionemu.gameserver.questEngine.model.QuestActionType;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

import java.util.List;

public abstract class AbstractQuestHandler
{
	public abstract void register();
	
	public boolean onDialogEvent(QuestEnv questEnv) {
		return false;
	}

	public boolean onEnterWorldEvent(QuestEnv questEnv) {
		return false;
	}

	public boolean onEnterZoneEvent(QuestEnv questEnv, ZoneName zoneName) {
		return false;
	}

	public boolean onLeaveZoneEvent(QuestEnv questEnv, ZoneName zoneName) {
		return false;
	}

	public HandlerResult onItemUseEvent(QuestEnv questEnv, Item item) {
		return HandlerResult.UNKNOWN;
	}

	public boolean onHouseItemUseEvent(QuestEnv env, int itemId) {
		return false;
	}

	public boolean onGetItemEvent(QuestEnv questEnv) {
		return false;
	}

	public boolean onUseSkillEvent(QuestEnv questEnv, int skillId) {
		return false;
	}

	public boolean onKillEvent(QuestEnv questEnv) {
		return false;
	}

	public boolean onAttackEvent(QuestEnv questEnv) {
		return false;
	}

	public boolean onLvlUpEvent(QuestEnv questEnv) {
		return false;
	}

	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return false;
	}

	public boolean onDieEvent(QuestEnv questEnv) {
		return false;
	}

	public boolean onLogOutEvent(QuestEnv env) {
		return false;
	}

	public boolean onNpcReachTargetEvent(QuestEnv env) {
		return false;
	}

	public boolean onNpcLostTargetEvent(QuestEnv env) {
		return false;
	}

	public boolean onMovieEndEvent(QuestEnv questEnv, int movieId) {
		return false;
	}

	public boolean onQuestTimerEndEvent(QuestEnv questEnv) {
		return false;
	}

	public boolean onInvisibleTimerEndEvent(QuestEnv questEnv) {
		return false;
	}

	public boolean onPassFlyingRingEvent(QuestEnv questEnv, String flyingRing) {
		return false;
	}

	public boolean onKillRankedEvent(QuestEnv env) {
		return false;
	}
	
	public boolean onEnterWindStreamEvent(QuestEnv questEnv, int worldId) {
		return false;
	}
	
	public boolean rideAction(QuestEnv questEnv, int rideItemId) {
		return false;
	}

	public boolean onKillInWorldEvent(QuestEnv env) {
		return false;
	}

	public boolean onFailCraftEvent(QuestEnv env, int itemId) {
		return false;
	}

	public boolean onEquipItemEvent(QuestEnv env, int itemId) {
		return false;
	}

	public boolean onCanAct(QuestEnv env, QuestActionType questEventType, Object... objects) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		return (qs != null) && (qs.getStatus() == QuestStatus.START);
	}

	public boolean onAddAggroListEvent(QuestEnv questEnv) {
		return false;
	}

	public boolean onAtDistanceEvent(QuestEnv questEnv) {
		return false;
	}

	public boolean onDredgionRewardEvent(QuestEnv env) {
		return false;
	}
	
	public boolean onKamarRewardEvent(QuestEnv env) {
		return false;
	}
	
	public boolean onOphidanRewardEvent(QuestEnv env) {
		return false;
	}
	
	public boolean onBastionRewardEvent(QuestEnv env) {
		return false;
	}
	
	public HandlerResult onBonusApplyEvent(QuestEnv env, BonusType bonusType, List<QuestItems> rewardItems) {
		return HandlerResult.UNKNOWN;
	}
	
	public boolean onProtectEndEvent(QuestEnv env) {
		return false;
	}
	
	public boolean onProtectFailEvent(QuestEnv env) {
		return false;
	}
	
	public boolean onCreativityPointEvent(QuestEnv questEnv) {
		return false;
	}
}