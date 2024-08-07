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

public class _26823Reunion_With_Agent_Peregrine extends QuestHandler {

    private final static int questId = 26823;
	private final static int[] npcs = {806289, 806290};
	private final static int[] IDEternity02EventGuardFiLi = {220613, 220614, 220616, 220617};
    public _26823Reunion_With_Agent_Peregrine() {
        super(questId);
    }
	
	@Override
	public void register() {
		for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: IDEternity02EventGuardFiLi) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(220540).addOnKillEvent(questId); //피톤.
		qe.registerOnEnterZone(ZoneName.get("IDETERNITY_02_Q16823_A_301550000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDETERNITY_02_Q16823_B_301550000"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 26822, true);
    }
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.START) {
			if (targetId == 806289) { //페레그란.
				switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 2) {
                            return sendQuestDialog(env, 1694);
                        }
					} case SELECT_ACTION_1695: {
						if (var == 2) {
							return sendQuestDialog(env, 1695);
						}
					} case STEP_TO_3: {
                        changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
                }
			}
		}         
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806290) {
                if (env.getDialogId() == 31) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialogId() == 1009) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
			else { // Bounty Quest made DragonicK?
				// Selected item is not optional.
				env.setDialogId(8);
				env.setExtendedRewardIndex(1);
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(806290, 0));
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
		if (player.getWorldId() == 301550000) { //지�?�?� 정�?.
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
            if (var == 0) {
                int var1 = qs.getQuestVarById(1);
                if (var1 >= 0 && var1 < 4) {
                    return defaultOnKillEvent(env, IDEternity02EventGuardFiLi, var1, var1 + 1, 1);
                } else if (var1 == 4) {
					qs.setQuestVar(1);
					updateQuestStatus(env);
                    return true;
                }
            } else if (var == 4) {
				switch (targetId) {
                    case 220540: { //피톤.
						playQuestMovie(env, 942);
						qs.setQuestVar(5);
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
			if (zoneName == ZoneName.get("IDETERNITY_02_Q16823_A_301550000")) {
				if (var == 1) {
					playQuestMovie(env, 941);
					changeQuestStep(env, 1, 2, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("IDETERNITY_02_Q16823_B_301550000")) {
				if (var == 3) {
					changeQuestStep(env, 3, 4, false);
					return true;
				}
			}
		}
		return false;
	}
}