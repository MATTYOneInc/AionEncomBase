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
package quest.mission;

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

public class _14014Turning_The_Ide extends QuestHandler {

    private final static int questId = 14014;
    private final static int[] npcs = {203098, 203146, 203147, 802045};
    private final static int[] mobs = {210178, 210179, 216892};
    private final static int[] items = {182215314}; //Transformation Potion 4.7
    public _14014Turning_The_Ide() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerOnLevelUp(questId);
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
        for (int item: items) {
            qe.registerQuestItem(item, questId);
        }
        for (int mob: mobs) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        }
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 14010, false);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        QuestDialog dialog = env.getDialog();
        int targetId = env.getTargetId();
        if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            switch (targetId) {
                case 203146: { //Estino.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        } case STEP_TO_1: {
                            giveQuestItem(env, 182215314, 1);
                            return defaultCloseDialog(env, 0, 1);
                        }
                    }
                } case 203147: { //Meteina.
                    switch (dialog) {
                        case USE_OBJECT: {
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            }
                        } case STEP_TO_3: {
                            return defaultCloseDialog(env, 2, 3);
                        }
                    }
                } case 802045: { //Livanon.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 3) {
                                return sendQuestDialog(env, 2034);
                            }
                        } case CHECK_COLLECTED_ITEMS: {
							return checkQuestItems(env, 3, 5, false, 0, 0); // 5
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203098) { //Spatalos.
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
    public HandlerResult onItemUseEvent(QuestEnv env, final Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return HandlerResult.UNKNOWN;
        }
        int var = qs.getQuestVarById(0);
        final int id = item.getItemTemplate().getTemplateId();
        if (id == 182215314) { //Transformation Potion 4.7
            if (var == 1 && player.isInsideZone(ZoneName.get("TURSIN_OUTPOST_ENTRANCE_210030000"))) {
                return HandlerResult.fromBoolean(useQuestItem(env, item, 1, 2, false, 18));
            }
        }
        return HandlerResult.FAILED;
    }
	
    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var >= 5 && var < 7) {
                return defaultOnKillEvent(env, mobs, 5, 7);
            } else if (var == 7) {
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return true;
            }
        }
        return false;
    }
}