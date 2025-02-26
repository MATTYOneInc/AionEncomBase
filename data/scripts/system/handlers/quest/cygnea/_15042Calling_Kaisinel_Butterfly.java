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
package quest.cygnea;

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

public class _15042Calling_Kaisinel_Butterfly extends QuestHandler {

	private final static int questId = 15042;
	public _15042Calling_Kaisinel_Butterfly() {
		super(questId);
	}
	
	public void register() {
		qe.registerQuestNpc(804885).addOnQuestStart(questId); //Telos
		qe.registerQuestNpc(804885).addOnTalkEvent(questId); //Telos
		qe.registerQuestItem(182215676, questId);
		qe.registerQuestItem(182215753, questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 804885) { //Telos
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env, 182215676, 1);
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804885) { //Telos
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 10002);
				}
				removeQuestItem(env, 182215676, 1);
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
        final Player player = env.getPlayer();
        final int id = item.getItemTemplate().getTemplateId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (id != 182215676 || qs.getStatus() == QuestStatus.COMPLETE) {
            return HandlerResult.UNKNOWN;
        } if (!player.isInsideZone(ZoneName.get("DRAGON_LORDS_GARDENS_210070000"))) {
            return HandlerResult.UNKNOWN;
        } if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);
            if (var == 0) {
                if (var1 < 2) {
                    qs.setQuestVarById(1, var1 + 1);
					updateQuestStatus(env);
                } else if (var1 == 2) {
					giveQuestItem(env, 182215753, 1);
					removeQuestItem(env, 182215676, 1);
                    qs.setQuestVar(1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
                    return HandlerResult.SUCCESS;
                }
			}
		}
		return HandlerResult.SUCCESS;
    }
}