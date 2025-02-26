package quest.heiron;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** DainAvenger
/****/
public class _3920TheSecretofSurkana extends QuestHandler {

	private final static int questId = 3920;
	public _3920TheSecretofSurkana() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(804603).addOnQuestStart(questId);
		qe.registerQuestNpc(730212).addOnTalkEvent(questId);
		qe.registerQuestNpc(804603).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 804603) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else if (env.getDialogId() == 1007) {
					return sendQuestDialog(env, 4);
                }   
				else if (env.getDialogId() == 1002) {
                    giveQuestItem(env, 182206073, 1); 
					return sendQuestStartDialog(env);
				}
				else if (env.getDialogId() == 1003) {
					return closeDialogWindow(env);
				}
			}
        } 
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 730212: {
					if (env.getDialog() == QuestDialog.USE_OBJECT) {
						return sendQuestDialog(env, 1352);
                    }
                    else if (env.getDialog() == QuestDialog.STEP_TO_1) {
                        removeQuestItem(env, 182206073, 1);
                        giveQuestItem(env, 182206074, 1);  
                        qs.setStatus(QuestStatus.REWARD); 
                        qs.setQuestVarById(0, 1);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
            } 
        }
        else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 804603) {
            removeQuestItem(env, 182206074, 1);
		    return sendQuestEndDialog(env);
            }
        }
		return false;
	}
}