package quest.brusthonin;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/*
 * author : Aion Gates
 */
public class _4036HeirnirWantstoRest extends QuestHandler {

	private final static int questId = 4036;

	public _4036HeirnirWantstoRest() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(205187).addOnQuestStart(questId);
		qe.registerQuestNpc(205187).addOnTalkEvent(questId);
		qe.registerQuestNpc(205144).addOnTalkEvent(questId);
		qe.registerQuestNpc(205190).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 205187) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
			else if (qs != null && qs.getStatus() == QuestStatus.START) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 2375);
				else if (env.getDialogId() == 1009 && qs.getStatus() != QuestStatus.COMPLETE
					&& qs.getStatus() != QuestStatus.NONE) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return sendQuestEndDialog(env);
			}
			else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
				return sendQuestEndDialog(env);
			}
		}
		else if (targetId == 205144) {
			if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1352);
				else if (env.getDialog() == QuestDialog.STEP_TO_1) {
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (targetId == 205190) {
			if (qs != null) {
				if (env.getDialog() == QuestDialog.START_DIALOG && qs.getStatus() == QuestStatus.START)
					return sendQuestDialog(env, 2375);
				else if (env.getDialogId() == 1009 && qs.getStatus() != QuestStatus.COMPLETE
					&& qs.getStatus() != QuestStatus.NONE) {
					qs.setQuestVar(3);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestEndDialog(env);
				}
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
