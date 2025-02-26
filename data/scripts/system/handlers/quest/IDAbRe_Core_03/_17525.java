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
package quest.IDAbRe_Core_03;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _17525 extends QuestHandler {

    private final static int questId = 17525;
	private final static int[] IDAbReCore03A2Witch = {248423, 248424, 248426};
    public _17525() {
        super(questId);
    }
	
    public void register() {
		qe.registerQuestNpc(805351).addOnQuestStart(questId); 
		qe.registerQuestNpc(805351).addOnTalkEvent(questId); 
        for (int mob: IDAbReCore03A2Witch) {
		    qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} 
		qe.registerQuestNpc(248013).addOnKillEvent(questId);
		qe.registerQuestNpc(248382).addOnKillEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("IDABRE_CORE_03_Q17525_A_301720000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDABRE_CORE_03_Q17525_B_301720000"), questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 805351) { 
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					} case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST_SIMPLE: {
				        return closeDialogWindow(env);
					}
				}
			}
		}
		if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 805351) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
        return false;
    }
	
	@Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("IDABRE_CORE_03_Q17525_A_301720000")) {
				if (var == 0) {
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("IDABRE_CORE_03_Q17525_B_301720000")) {
				if (var == 3) {
					changeQuestStep(env, 3, 4, false);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
        if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
            if (var == 1) {
				switch (env.getTargetId()) {
					case 248382:
						if (qs.getQuestVarById(1) < 1) {
					        qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					        updateQuestStatus(env);
				        } if (qs.getQuestVarById(1) >= 1) {
							qs.setQuestVar(2);
							updateQuestStatus(env);
						}
					break;
                }
			} else if (var == 2) {
				switch (env.getTargetId()) {
					case 248423:
					case 248424:
					case 248426:
						if (qs.getQuestVarById(2) < 1) {
					        qs.setQuestVarById(2, qs.getQuestVarById(2) + 1);
					        updateQuestStatus(env);
				        } if (qs.getQuestVarById(2) >= 1) {
							qs.setQuestVar(3);
							updateQuestStatus(env);
						}
					break;
                }
			} else if (var == 4) {
				switch (targetId) {
					case 248013: {
						qs.setStatus(QuestStatus.REWARD);
						changeQuestStep(env, 4, 5, false);
						updateQuestStatus(env);
						return true;
					}
                }
			}
        }
        return false;
    }
}