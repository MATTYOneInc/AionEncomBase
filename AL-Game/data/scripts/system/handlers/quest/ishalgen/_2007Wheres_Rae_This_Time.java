/*
 * =====================================================================================*
 * This file is part of Aion-Unique (Aion-Unique Home Software Development)             *
 * Aion-Unique Development is a closed Aion Project that use Old Aion Project Base      *
 * Like Aion-Lightning, Aion-Engine, Aion-Core, Aion-Extreme, Aion-NextGen, ArchSoft,   *
 * Aion-Ger, U3J, Encom And other Aion project, All Credit Content                      *
 * That they make is belong to them/Copyright is belong to them. And All new Content    *
 * that Aion-Unique make the copyright is belong to Aion-Unique                         *
 * You may have agreement with Aion-Unique Development, before use this Engine/Source   *
 * You have agree with all of Term of Services agreement with Aion-Unique Development   *
 * =====================================================================================*
 */
package quest.ishalgen;

import com.aionemu.gameserver.model.EmotionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _2007Wheres_Rae_This_Time extends QuestHandler
{
	private final static int questId = 2007;
	
	public _2007Wheres_Rae_This_Time() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] talkNpcs = {203516, 203519, 203539, 203552, 203554, 700085, 700086, 700087};
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		for (int id: talkNpcs) {
			qe.registerQuestNpc(id).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		int[] ishalgenQuests = {2100, 2001, 2002, 2003, 2004, 2005, 2006};
		return defaultOnZoneMissionEndEvent(env, ishalgenQuests);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		int[] ishalgenQuests = {2100, 2001, 2002, 2003, 2004, 2005, 2006};
		return defaultOnLvlUpEvent(env, ishalgenQuests, false);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203516:
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						break;
						case STEP_TO_1:
							if (var == 0) {
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								closeDialogWindow(env);
								return true;
							}
						break;
					}
				break;
				case 203519:
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						break;		
						case STEP_TO_2:
							if (var == 1) {
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								closeDialogWindow(env);
								return true;
							}
						break;
					}
				break;
				case 203539:
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						break;	
						case SELECT_ACTION_1694:
							playQuestMovie(env, 55);
						break;
						case STEP_TO_3:
							if (var == 2) {
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								closeDialogWindow(env);
								return true;
							}
						break;
					}
				break;
				case 203552:
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						break;
						case STEP_TO_4:
							if (var == 3) {
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								closeDialogWindow(env);
								return true;
							}
						break;	
					}
				break;
				case 203554:
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 4) {
								return sendQuestDialog(env, 2375);
							} else if (var == 8) {
								return sendQuestDialog(env, 2716);
							}
						break;	
						case STEP_TO_5:
							if (var == 4) {
								qs.setQuestVar(5);
								updateQuestStatus(env);
								closeDialogWindow(env);
								return true;
							}
						break;
						case STEP_TO_6:
							if (var == 8) {
								qs.setQuestVar(8);
								updateQuestStatus(env);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								TeleportService2.teleportTo(env.getPlayer(), 220010000, 590.01886f, 2453.0552f, 279.375f, (byte) 79);
								return true;
							}
						break;
					}
				break;
				case 700085:
				if (var == 5) {
                    qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					return false;
				}
				break;
				case 700086:
				if (var == 6) {
                    qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					return false;
				}
				break;
				case 700087:
                if (var == 7) {
                    qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
                    return false;
				}
				break;
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203516) {
				if (env.getDialog() == QuestDialog.USE_OBJECT) {
					playQuestMovie(env, 58);
					return sendQuestDialog(env, 3057);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}	