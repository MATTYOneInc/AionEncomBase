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
package quest.enshar;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _25084Is_This_A_Trap extends QuestHandler {

    private final static int questId = 25084;
    public _25084Is_This_A_Trap() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerQuestNpc(804926).addOnQuestStart(questId);
        qe.registerQuestNpc(804926).addOnTalkEvent(questId);
        qe.registerQuestNpc(804925).addOnTalkEvent(questId);
		qe.registerQuestNpc(804927).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("DF5_SENSORYAREA_Q25084_220080000"), questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 804926) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (targetId == 804925) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1011);
				} else if (dialog == QuestDialog.CHECK_COLLECTED_ITEMS) {
					if (QuestService.collectItemCheck(env, true)) {
					changeQuestStep(env, 0, 1, false);
                    return closeDialogWindow(env);
					}
                }
            } if (targetId == 804926) { 
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1352);
				} else if (dialog == QuestDialog.STEP_TO_2) {
					changeQuestStep(env, 1, 2, false); 
                    return closeDialogWindow(env);
                }
            }
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 804927) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 2034);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}	
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} switch (targetId) {
			case 220038:
				if (qs.getQuestVarById(0) == 2) {

                    return true;
				}
			break;
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName == ZoneName.get("DF5_SENSORYAREA_Q25084_220080000")) {
			Player player = env.getPlayer();
			if (player == null) {
				return false;
			}
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs != null && qs.getStatus() == QuestStatus.START) {
				int var = qs.getQuestVarById(0);
				if (var == 2) {
					QuestService.addNewSpawn(220080000, player.getInstanceId(), 220038, 1471.4f, 58.2f, 202.3f, (byte) 19);
					QuestService.addNewSpawn(220080000, player.getInstanceId(), 220038, 1475.5f, 58.3f, 202.3f, (byte) 53);
					changeQuestStep(env, 2, 3, false);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}