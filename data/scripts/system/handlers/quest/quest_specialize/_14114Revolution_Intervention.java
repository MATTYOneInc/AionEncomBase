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
package quest.quest_specialize;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _14114Revolution_Intervention extends QuestHandler {
    private final static int questId = 14114;
	
    public _14114Revolution_Intervention() {
        super(questId);
    }
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
	
    @Override
    public void register() {
		qe.registerOnLevelUp(questId);
        qe.registerOnMovieEndQuest(23, questId);
		qe.registerQuestNpc(203098).addOnQuestStart(questId); //Spatalos
        qe.registerQuestNpc(203098).addOnTalkEvent(questId); //Spatalos
        qe.registerQuestNpc(203183).addOnTalkEvent(questId); //Khidia
        qe.registerOnEnterZone(ZoneName.get("LF1A_SENSORY_AREA_Q14114_210030000"), questId);
    }
	
	@Override
    public boolean onDialogEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
		int var = qs.getQuestVarById(0);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203098) { //Spatalos
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 203183) { //Khidia
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						} else if (var == 2) {
							return sendQuestDialog(env, 1352);
						} else if (var == 3) {
							return sendQuestDialog(env, 1693);
						}
					} case STEP_TO_1: {
						playQuestMovie(env, 30);
                        SkillEngine.getInstance().applyEffectDirectly(8197, player, player, 0); //Transforming Plumis.
                        updateQuestStatus(env);
                        return defaultCloseDialog(env, 0, 1);
                    } case STEP_TO_2: {
						player.getEffectController().removeEffect(8197); //Transforming Plumis.
						return defaultCloseDialog(env, 2, 3);
					} case STEP_TO_3: {
						removeQuestItem(env, 182215457, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					} case CHECK_COLLECTED_ITEMS: {
						return checkQuestItems(env, 3, 4, false, 10000, 10001);
					} case FINISH_DIALOG: {
						if (var == 4) {
							defaultCloseDialog(env, 4, 5);
						} else if (var == 3) {
							defaultCloseDialog(env, 3, 3);
						}
					}
				}
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203098) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 2375);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
    @Override
    public boolean onMovieEndEvent(QuestEnv env, int movieId) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && movieId == 23) {
			qs.setQuestVar(2);
            updateQuestStatus(env);
            return true;
        }
        return false;
    }
	
    @Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("LF1A_SENSORY_AREA_Q14114_210030000")) {
				if (var == 1) {
					playQuestMovie(env, 23);
					return true;
				}
			}
		}
		return false;
	}
}