package quest.oriel;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class _18830MovingIn extends QuestHandler {

    private static final int questId = 18830;
    private static final Set<Integer> butlers;

    static {
        butlers = new HashSet<Integer>();
        butlers.add(810017);
        butlers.add(810018);
        butlers.add(810019);
        butlers.add(810020);
        butlers.add(810021);
    }

    public _18830MovingIn() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(830584).addOnQuestStart(questId);

        Iterator<Integer> iter = butlers.iterator();
        while (iter.hasNext()) {
            int butlerId = iter.next();
            qe.registerQuestNpc(butlerId).addOnTalkEvent(questId);
        }
        qe.registerQuestNpc(830645).addOnTalkEvent(questId);
        qe.registerQuestHouseItem(3420021, questId);
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestDialog dialog = env.getDialog();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        House house = player.getActiveHouse();

        if (house == null) {
            return false;
        }

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 830584) {
                switch (dialog) {
                    case START_DIALOG:
                        return sendQuestDialog(env, 1011);
                    case ACCEPT_QUEST:
                    case ACCEPT_QUEST_SIMPLE:
                        return sendQuestStartDialog(env);
                    default:
                        break;
                }
            }
        }
        else if (qs.getStatus() == QuestStatus.START && butlers.contains(targetId) && qs.getQuestVarById(0) == 0) {
            if (house.getButler().getNpcId() != targetId) {
                return false;
            }
            switch (dialog) {
                case USE_OBJECT:
                    return sendQuestDialog(env, 1352);
                case STEP_TO_1:
                    return defaultCloseDialog(env, 0, 1);
                default:
                    break;
            }
        }
        else if (qs.getStatus() == QuestStatus.REWARD && targetId == 830645) {
            switch (dialog) {
                case USE_OBJECT:
                    return sendQuestDialog(env, 2375);
                case SELECT_REWARD:
                    return sendQuestDialog(env, 5);
                case SELECT_NO_REWARD:
                    sendQuestEndDialog(env);
                    return true;
                default:
                    break;
            }
        }
        else if (qs.getStatus() == QuestStatus.START && targetId == 830645) {
            switch (dialog) {
                case USE_OBJECT:
                    changeQuestStep(env, 1, 1, true);
                    return sendQuestDialog(env, 2375);
                case SELECT_REWARD:
                    return sendQuestDialog(env, 5);
                case SELECT_NO_REWARD:
                    sendQuestEndDialog(env);
                    return true;
                default:
                    break;
            }
        }

        return false;
    }

    @Override
    public boolean onHouseItemUseEvent(QuestEnv env, int itemId) {
        final Player player = env.getPlayer();
        if (itemId == 3420021) {
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
                changeQuestStep(env, 1, 1, true);
            }
        }
        return false;
    }

}