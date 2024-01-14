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
package quest.iluma;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _15606The_Hall_Of_Zephyr extends QuestHandler {

    public static final int questId = 15606;
    public _15606The_Hall_Of_Zephyr() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(806163).addOnQuestStart(questId); //Thaleia.
		qe.registerQuestNpc(806163).addOnTalkEvent(questId); //Thaleia.
		qe.registerQuestNpc(703147).addOnTalkEvent(questId); //Ancient Daeva Of Zephyr's Chest.
		qe.registerOnEnterZone(ZoneName.get("LF6_SENSORY_AREA_Q15606_A_DYNAMIC_ENV_210100000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF6_SENSORY_AREA_Q15606_B_DYNAMIC_ENV_210100000"), questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 806163) { //Thaleia.
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
		} else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (targetId == 806163) { //Thaleia.
				switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 2) {
                            return sendQuestDialog(env, 1694);
                        } else if (var == 4) {
							return sendQuestDialog(env, 2375);
                        }
					} case SELECT_ACTION_1695: {
						if (var == 2) {
							return sendQuestDialog(env, 1695);
						}
					} case SELECT_ACTION_2376: {
						if (var == 4) {
							return sendQuestDialog(env, 2376);
						}
					} case STEP_TO_3: {
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					} case CHECK_COLLECTED_ITEMS: {
						if (QuestService.collectItemCheck(env, true)) {
							removeQuestItem(env, 182215996, 1); //ì„œí’?ì?˜ ë?°ë°” í›ˆë ¨ ê¸°ë¡?.
							changeQuestStep(env, 4, 5, true);
							return sendQuestDialog(env, 10000);
						} else {
							return sendQuestDialog(env, 10001);
						}
					}
				}
			} if (targetId == 703147) { //Ancient Daeva Of Zephyr's Chest.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 1) {
							giveQuestItem(env, 182215996, 1); //ì„œí’?ì?˜ ë?°ë°” í›ˆë ¨ ê¸°ë¡?.
							changeQuestStep(env, 1, 2, false);
							return closeDialogWindow(env);
                        }
					}
                }
            }
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806163) { //Thaleia.
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
			if (zoneName == ZoneName.get("LF6_SENSORY_AREA_Q15606_A_DYNAMIC_ENV_210100000")) {
				if (var == 0) {
					changeQuestStep(env, 0, 1, false);
					TeleportService2.teleportTo(player, 210100000, 671.3528f, 1609.7289f, 333.86536f, (byte) 72);
					return true;
				}
			} else if (zoneName == ZoneName.get("LF6_SENSORY_AREA_Q15606_B_DYNAMIC_ENV_210100000")) {
				if (var == 3) {
					changeQuestStep(env, 3, 4, false);
					TeleportService2.teleportTo(player, 210100000, 358.1248f, 1679.0687f, 355.41916f, (byte) 60);
					return true;
				}
			}
		}
		return false;
	}
}