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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
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
public class _25605An_Adventure_In_Time extends QuestHandler {

    public static final int questId = 25605;
    public _25605An_Adventure_In_Time() {
        super(questId);
    }
	
    @Override
    public void register() {
		qe.registerQuestItem(182216005, questId); //ê°„ì?´ ì‹œê³µê°„ ì œì–´ê¸°.
        qe.registerQuestNpc(806174).addOnQuestStart(questId); //Chardelil.
		qe.registerQuestNpc(806174).addOnTalkEvent(questId); //Chardelil.
		qe.registerQuestNpc(806199).addOnTalkEvent(questId); //ì°¨ì›?ì—? ë¬¶ì?¸ ë?°ë°”.
        qe.registerQuestNpc(241217).addOnKillEvent(questId); //Ferson.
		qe.registerOnEnterZone(ZoneName.get("DF6_ITEMUSEAREA_Q25605B_DYNAMIC_ENV"), questId);
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q25605_A_DYNAMIC_ENV_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q25605_B_DYNAMIC_ENV_220110000"), questId);
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
			if (targetId == 806174) { //Chardelil.
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
			if (targetId == 806174) { //Chardelil.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						} else if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
					} case STEP_TO_3: {
						giveQuestItem(env, 182216005, 1); //ê°„ì?´ ì‹œê³µê°„ ì œì–´ê¸°.
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					} case CHECK_COLLECTED_ITEMS: {
						if (QuestService.collectItemCheck(env, true)) {
							changeQuestStep(env, 1, 2, false);
							return sendQuestDialog(env, 10000);
						} else {
							return sendQuestDialog(env, 10001);
						}
					}
				}
			} if (targetId == 806199) { //ì°¨ì›?ì—? ë¬¶ì?¸ ë?°ë°”.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 4) {
							return sendQuestDialog(env, 2375);
						}
					} case STEP_TO_5: {
						changeQuestStep(env, 4, 5, false);
						npc.getController().onDelete();
						removeQuestItem(env, 182216005, 1); //ê°„ì?´ ì‹œê³µê°„ ì œì–´ê¸°.
						return closeDialogWindow(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806174) { //Chardelil.
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
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 6) {
				switch (targetId) {
                    case 241217: { //Ferson.
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
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (!player.isInsideZone(ZoneName.get("DF6_ITEMUSEAREA_Q25605B_DYNAMIC_ENV"))) {
				return HandlerResult.UNKNOWN;
			}
            int var = qs.getQuestVarById(0);
            if (var == 3) {
				QuestService.addNewSpawn(220110000, 1, 806199, player.getX(), player.getY(), player.getZ(), (byte) 0); //ì°¨ì›?ì—? ë¬¶ì?¸ ë?°ë°”.
                return HandlerResult.fromBoolean(useQuestItem(env, item, 3, 4, false));
            }
        }
        return HandlerResult.FAILED;
    }
	
	@Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q25605_A_DYNAMIC_ENV_220110000")) {
				if (var == 0) {
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q25605_B_DYNAMIC_ENV_220110000")) {
				if (var == 5) {
					changeQuestStep(env, 5, 6, false);
					return true;
				}
			}
		}
		return false;
	}
}