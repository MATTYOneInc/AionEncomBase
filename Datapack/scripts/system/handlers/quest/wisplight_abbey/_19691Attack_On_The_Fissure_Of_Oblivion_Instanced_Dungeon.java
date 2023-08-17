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
package quest.wisplight_abbey;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _19691Attack_On_The_Fissure_Of_Oblivion_Instanced_Dungeon extends QuestHandler
{
    private final static int questId = 19691;
	
    public _19691Attack_On_The_Fissure_Of_Oblivion_Instanced_Dungeon() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806698).addOnQuestStart(questId);
        qe.registerQuestNpc(806698).addOnTalkEvent(questId);
		//Special Forces Commander Gegares
		qe.registerQuestNpc(246200).addOnKillEvent(questId);
		qe.registerQuestNpc(246201).addOnKillEvent(questId);
		qe.registerQuestNpc(246202).addOnKillEvent(questId);
		qe.registerQuestNpc(246203).addOnKillEvent(questId);
		qe.registerQuestNpc(246204).addOnKillEvent(questId);
		qe.registerQuestNpc(246205).addOnKillEvent(questId);
		qe.registerQuestNpc(246206).addOnKillEvent(questId);
		qe.registerQuestNpc(246207).addOnKillEvent(questId);
		qe.registerQuestNpc(246208).addOnKillEvent(questId);
		qe.registerQuestNpc(246209).addOnKillEvent(questId);
		qe.registerQuestNpc(246210).addOnKillEvent(questId);
		qe.registerQuestNpc(246211).addOnKillEvent(questId);
		qe.registerQuestNpc(246212).addOnKillEvent(questId);
		qe.registerQuestNpc(246213).addOnKillEvent(questId);
		qe.registerQuestNpc(246214).addOnKillEvent(questId);
		qe.registerQuestNpc(246215).addOnKillEvent(questId);
		qe.registerQuestNpc(246216).addOnKillEvent(questId);
		qe.registerQuestNpc(246217).addOnKillEvent(questId);
		qe.registerQuestNpc(246218).addOnKillEvent(questId);
		qe.registerQuestNpc(246219).addOnKillEvent(questId);
		qe.registerQuestNpc(246220).addOnKillEvent(questId);
		qe.registerQuestNpc(246221).addOnKillEvent(questId);
		qe.registerQuestNpc(246222).addOnKillEvent(questId);
		qe.registerQuestNpc(246223).addOnKillEvent(questId);
		qe.registerQuestNpc(246224).addOnKillEvent(questId);
		qe.registerQuestNpc(246225).addOnKillEvent(questId);
		qe.registerQuestNpc(246226).addOnKillEvent(questId);
		qe.registerQuestNpc(246227).addOnKillEvent(questId);
		qe.registerQuestNpc(246228).addOnKillEvent(questId);
		qe.registerQuestNpc(246229).addOnKillEvent(questId);
		qe.registerQuestNpc(246230).addOnKillEvent(questId);
		qe.registerQuestNpc(246231).addOnKillEvent(questId);
		qe.registerQuestNpc(246232).addOnKillEvent(questId);
		qe.registerQuestNpc(246233).addOnKillEvent(questId);
		qe.registerQuestNpc(246234).addOnKillEvent(questId);
		qe.registerQuestNpc(246235).addOnKillEvent(questId);
		qe.registerQuestNpc(246236).addOnKillEvent(questId);
		qe.registerQuestNpc(246237).addOnKillEvent(questId);
		qe.registerQuestNpc(246238).addOnKillEvent(questId);
		qe.registerQuestNpc(246239).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806698) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 806698) {
                if (dialog == QuestDialog.START_DIALOG) {
                    if (qs.getQuestVarById(0) == 3) {
                        return sendQuestDialog(env, 2375);
                    }
                } if (dialog == QuestDialog.SELECT_REWARD) {
                    changeQuestStep(env, 3, 4, true);
                    return sendQuestEndDialog(env);
                }
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806698) {
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
	
    public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
			//Special Forces Commander Gegares
				case 246200:
				case 246201:
				case 246202:
				case 246203:
				case 246204:
				case 246205:
				case 246206:
				case 246207:
				case 246208:
				case 246209:
				case 246210:
				case 246211:
				case 246212:
				case 246213:
				case 246214:
				case 246215:
				case 246216:
				case 246217:
				case 246218:
				case 246219:
				case 246220:
				case 246221:
				case 246222:
				case 246223:
				case 246224:
				case 246225:
				case 246226:
				case 246227:
				case 246228:
				case 246229:
				case 246230:
				case 246231:
				case 246232:
				case 246233:
				case 246234:
				case 246235:
				case 246236:
				case 246237:
				case 246238:
				case 246239:
                if (qs.getQuestVarById(1) < 3) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				} if (qs.getQuestVarById(1) >= 3) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
            }
        }
        return false;
    }
}