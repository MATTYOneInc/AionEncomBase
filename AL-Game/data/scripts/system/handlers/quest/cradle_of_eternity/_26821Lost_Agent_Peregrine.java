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
package quest.cradle_of_eternity;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _26821Lost_Agent_Peregrine extends QuestHandler {

    private final static int questId = 26821;
	private final static int[] npcs = {806286, 806427, 220587};
	private final static int[] IDEternity02All = {220458, 220459, 220460, 220462, 220465, 220467, 220469, 220475, 220476, 220477, 220479, 220670, 220671};
    public _26821Lost_Agent_Peregrine() {
        super(questId);
    }
	
	@Override
	public void register() {
		for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: IDEternity02All) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnEnterWorld(questId);
		qe.registerQuestNpc(220526).addOnKillEvent(questId); //ì‹¬ì•ˆì?˜ ëˆˆë?™ì?.
		qe.registerOnEnterZone(ZoneName.get("IDETERNITY_02_Q16821_A_301550000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDETERNITY_02_Q16821_B_301550000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.START) {
			if (targetId == 806286) { //ì”ê·¸ë¦¬ì¼€.
				switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        }
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case STEP_TO_1: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
                }
			} if (targetId == 806427) { //ë¸Œë ˆê°€íŠ¸.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 1353);
						}
					} case STEP_TO_2: {
                        changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
				}
			}
		} 
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 220587) {
                if (env.getDialogId() == 31) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialogId() == 1009) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
			else { // Bounty Quest made DragonicK?
				// Selected item is not optional. correct for Selected item Reward DainAvenger
				env.setDialogId(8);
				env.setExtendedRewardIndex(8);
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(220587, 0));
				if (QuestService.finishQuest(env)) {
					return closeDialogWindow(env);
				}
			}
		}
		return false;
	}
	
	@Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (player.getWorldId() == 301550000) { //ì§€ì‹?ì?˜ ì •ì›?.
            if (qs == null) {
                env.setQuestId(questId);
                if (QuestService.startQuest(env)) {
					return true;
				}
            }
        }
        return false;
    }
	
	@Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 2) {
                int var1 = qs.getQuestVarById(1);
                if (var1 >= 0 && var1 < 34) {
                    return defaultOnKillEvent(env, IDEternity02All, var1, var1 + 1, 1);
                } else if (var1 == 34) {
					qs.setQuestVar(3);
					updateQuestStatus(env);
                    return true;
                }
            } else if (var == 5) {
				switch (targetId) {
                    case 220526: { //ì‹¬ì•ˆì?˜ ëˆˆë?™ì?.
						qs.setQuestVar(6);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return true;
					}
                }
			}
        }
        return false;
    }
	
	@Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("IDETERNITY_02_Q16821_A_301550000")) {
				if (var == 3) {
					changeQuestStep(env, 3, 4, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("IDETERNITY_02_Q16821_B_301550000")) {
				if (var == 4) {
					playQuestMovie(env, 937);
					changeQuestStep(env, 4, 5, false);
					return true;
				}
			}
		}
		return false;
	}
}