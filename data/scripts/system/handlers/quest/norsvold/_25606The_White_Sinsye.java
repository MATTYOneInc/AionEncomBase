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
package quest.norsvold;

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
public class _25606The_White_Sinsye extends QuestHandler {

    public static final int questId = 25606;
    public _25606The_White_Sinsye() {
        super(questId);
    }
		
    @Override
    public void register() {
        qe.registerQuestNpc(806175).addOnQuestStart(questId); //Chaelsean.
		qe.registerQuestNpc(806175).addOnTalkEvent(questId); //Chaelsean.
		qe.registerQuestNpc(806178).addOnTalkEvent(questId); //ë°°íšŒí•˜ëŠ” ì˜?í˜¼.
		qe.registerQuestNpc(806156).addOnTalkEvent(questId); //ê¸°ì–µì?„ ìžƒì?€ ì—¬ì?¸.
		qe.registerQuestNpc(806157).addOnTalkEvent(questId); //ë¦¬ë‹ˆ.
		qe.registerQuestNpc(703140).addOnTalkEvent(questId); //ë¦¬ë‹ˆì?˜ ì†Œí’ˆ ìƒ?ìž?.
        qe.registerQuestNpc(241220).addOnKillEvent(questId); //White Sinsye.
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q25606_A_DYNAMIC_ENV_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q25606_B_DYNAMIC_ENV_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q25606_C_DYNAMIC_ENV_220110000"), questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		final Npc npc = (Npc) env.getVisibleObject();
		if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 806175) { //Chaelsean.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 4762);
					} case SELECT_ACTION_4763: {
                        return sendQuestDialog(env, 4848);
					} case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST_SIMPLE: {
				        return closeDialogWindow(env);
					}
                }
			}
		} else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (targetId == 806178) { //ë°°íšŒí•˜ëŠ” ì˜?í˜¼.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					} case SELECT_ACTION_1353: {
						if (var == 1) {
							return sendQuestDialog(env, 1353);
						}
					} case STEP_TO_2: {
						playQuestMovie(env, 873);
						changeQuestStep(env, 1, 2, false);
						npc.getController().onDelete();
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 806156) { //ê¸°ì–µì?„ ìžƒì?€ ì—¬ì?¸.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 3) {
							return sendQuestDialog(env, 2035);
						}
					} case STEP_TO_4: {
					    changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 806157) { //ë¦¬ë‹ˆ.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 6) {
							return sendQuestDialog(env, 3057);
						}
					} case STEP_TO_7: {
					    changeQuestStep(env, 6, 7, false);
						npc.getController().onDelete();
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 703140) { //ë¦¬ë‹ˆì?˜ ì†Œí’ˆ ìƒ?ìž?.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 7) {
                            giveQuestItem(env, 182216006, 1); //ë¦¬ë‹ˆì?˜ ì?¼ê¸°ìž¥.
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return closeDialogWindow(env);
                        }
					}
                }
            }
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806175) { //Chaelsean.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
					removeQuestItem(env, 182216006, 1); //ë¦¬ë‹ˆì?˜ ì?¼ê¸°ìž¥.
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
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
		final Npc npc = (Npc) env.getVisibleObject();
		if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 5) {
				switch (targetId) {
                    case 241220: { //White Sinsye.
						qs.setQuestVar(6);
					    updateQuestStatus(env);
						QuestService.addNewSpawn(220110000, 1, 806157, npc.getX(), npc.getY(), npc.getZ(), (byte) 0); //ë¦¬ë‹ˆ.
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
			if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q25606_A_DYNAMIC_ENV_220110000")) {
				if (var == 0) {
					QuestService.addNewSpawn(220110000, 1, 806178, player.getX(), player.getY(), player.getZ(), (byte) 0); //ë°°íšŒí•˜ëŠ” ì˜?í˜¼.
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q25606_B_DYNAMIC_ENV_220110000")) {
				if (var == 2) {
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q25606_C_DYNAMIC_ENV_220110000")) {
				if (var == 4) {
					changeQuestStep(env, 4, 5, false);
					return true;
				}
			}
		}
		return false;
	}
}