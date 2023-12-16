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
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _14123The_Shadow_Of_Vengeance extends QuestHandler {

    private final static int questId = 14123;
	
    public _14123The_Shadow_Of_Vengeance() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerQuestNpc(203933).addOnQuestStart(questId); //Actaeon
        qe.registerQuestNpc(203933).addOnTalkEvent(questId); //Actaeon
		qe.registerQuestNpc(203991).addOnTalkEvent(questId); //Dionera
		qe.registerQuestNpc(206360).addOnKillEvent(questId); //Peddler Hippola
		qe.registerOnEnterZone(ZoneName.get("ELTNEN_OBSERVATORY_210020000"), questId);
    }
	
	@Override
    public boolean onDialogEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203933) { //Actaeon
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1011);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
		} else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (targetId == 203991) { //Dionera
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 1352);
						} else if (var == 1) {
							return sendQuestDialog(env, 2375);
						}
					} case STEP_TO_1: {
						qs.setQuestVar(0);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					} case SELECT_REWARD: {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestEndDialog(env);
					}
				}
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203991) { //Dionera
                return sendQuestEndDialog(env);
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
			if (zoneName == ZoneName.get("ELTNEN_OBSERVATORY_210020000")) {
				if (var == 0) {
					QuestService.addNewSpawn(210020000, 1, 206360, (float) 1768.16, (float) 924.47, (float) 422.02, (byte) 0);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
    public boolean onKillEvent(QuestEnv env) {
        return defaultOnKillEvent(env, 206360, 0, 1);
    }
}