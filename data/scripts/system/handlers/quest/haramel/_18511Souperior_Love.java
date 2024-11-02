package quest.haramel;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Remake Rinzler (Encom)
/****/

public class _18511Souperior_Love extends QuestHandler
{
    private static final int questId = 18511;

    public _18511Souperior_Love() {
        super(questId);
    }

    @Override
    public void register() {
        int[] npcs = {799522, 700954, 730359, 790000};
        qe.registerQuestNpc(799522).addOnQuestStart(questId);
        qe.registerGetingItem(182212011, questId);
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 799522) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            switch (targetId) {
                case 700954: {
                    return true;
                } case 730359: { // Huge Cauldron(就是这个大锅的ID标错了)
                    switch (dialog) {
                        case USE_OBJECT: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        } case CHECK_COLLECTED_ITEMS: {
                            return checkQuestItems(env, 0, 0, false, 1352, 10001);
                        } case STEP_TO_2: {
                            giveQuestItem(env, 182212011, 1);
                            return closeDialogWindow(env);
                        } case FINISH_DIALOG: {
                            return closeDialogWindow(env);
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 790000) {
                if (dialog == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onGetItemEvent(QuestEnv env) {
        defaultOnGetItemEvent(env, 0, 1, false);
        changeQuestStep(env, 1, 1, true);
        return true;
    }
}