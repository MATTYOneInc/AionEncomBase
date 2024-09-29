package quest.mission;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

public class _14023Playing_Around_At_The_Temple extends QuestHandler
{
    private final static int questId = 14023;
	
    public _14023Playing_Around_At_The_Temple() {
        super(questId);
    }
	
    @Override
    public void register() {
        int[] npcs = {203965, 203967};
        qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerOnLevelUp(questId);
        for (int npc : npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
    }
	
	@Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 14020, false);
    }
	
    @Override
    public boolean onDialogEvent(final QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        int targetId = env.getTargetId();
        if (qs == null) {
            return false;
        } if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            switch (targetId) {
                case 203965: {
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        } case STEP_TO_1: {
                            return defaultCloseDialog(env, 0, 1);
                        }
                    }
                    break;
                } case 203967: {
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            } else if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            }
                        } case CHECK_COLLECTED_ITEMS: {
                            if (var == 2) {
                                return checkQuestItems(env, 2, 3, true, 10000, 10001);
                            }
						} case STEP_TO_2: {
                            return defaultCloseDialog(env, 1, 2);
                        } case FINISH_DIALOG: {
                            return closeDialogWindow(env);
                        }
                    }
                    break;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203965) {
                if (dialog == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 2034);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
}