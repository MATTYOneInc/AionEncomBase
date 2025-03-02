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
package quest.ophidan_warpath;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _17505Support_Operation_For_The_Ophidan_Warpath extends QuestHandler {

    private final static int questId = 17505;
    public _17505Support_Operation_For_The_Ophidan_Warpath() {
        super(questId);
    }

	public void register() {
		qe.registerOnEnterWorld(questId);
		qe.registerQuestItem(182216067, questId); //슈고�?� 특수 물약.
		qe.registerQuestNpc(806266).addOnQuestStart(questId); //세베루스.
		qe.registerQuestNpc(806266).addOnTalkEvent(questId); //세베루스.
		qe.registerQuestNpc(806267).addOnTalkEvent(questId); //테루아.
		qe.registerQuestNpc(806270).addOnTalkEvent(questId); //히요린.
		qe.registerOnEnterZone(ZoneName.get("IDLDF5_UNDER_02_WAR_ITEMUSEAREA_17505A"), questId);
	}
	
	@Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 806266) { //세베루스.
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
			if (targetId == 806270) { //히요린.
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
						giveQuestItem(env, 182216067, 1); //슈고�?� 특수 물약.
                        return defaultCloseDialog(env, 0, 1);
					}
                }
			} else if (targetId == 806267) { //테루아.
				switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 3) {
						   return sendQuestDialog(env, 2034);
						}
					} case SELECT_ACTION_2035: {
						if (var == 3) {
							return sendQuestDialog(env, 2035);
						}
					} case STEP_TO_4: {
						playQuestMovie(env, 945);
						return defaultCloseDialog(env, 3, 4);
					}
                }
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806266) { //세베루스.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
		return false;
	}
	
	@Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (player.getWorldId() == 301670000) {
			if (qs != null && qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVars().getQuestVars();
                if (var == 1) {
                    changeQuestStep(env, 1, 2, false);
				}
			}
		}
        return false;
    }
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getQuestVarById(0) == 2) {
			removeQuestItem(env, 182216067, 1); //슈고�?� 특수 물약.
			return HandlerResult.fromBoolean(useQuestItem(env, item, 2, 3, false));
		}
		return HandlerResult.FAILED;
	}
	
	@Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("IDLDF5_UNDER_02_WAR_ITEMUSEAREA_17505A")) {
				if (var == 4) {
					qs.setQuestVar(5);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}