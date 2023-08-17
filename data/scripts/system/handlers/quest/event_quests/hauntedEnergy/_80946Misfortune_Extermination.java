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
package quest.event_quests.hauntedEnergy;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _80946Misfortune_Extermination extends QuestHandler
{
    private final static int questId = 80946;
	
    public _80946Misfortune_Extermination() {
        super(questId);
    }
	
    public void register() {
		//Huge Misfortune Sealing Charm.
		qe.registerQuestItem(182007418, questId);
        qe.registerQuestNpc(835303).addOnQuestStart(questId);
		qe.registerQuestNpc(835303).addOnTalkEvent(questId);
		qe.registerQuestNpc(246845).addOnTalkEvent(questId);
		qe.registerQuestNpc(835303).addOnAtDistanceEvent(questId);
    }
	
	@Override
	public boolean onAtDistanceEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			QuestService.startQuest(env);
			giveQuestItem(env, 182007418, 3); //Huge Misfortune Sealing Charm.
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
			return true;
		}
		return false;
	}
	
    @Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		QuestDialog dialog = env.getDialog();
		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 246845) { //Huge Misfortune.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
						if (player.getInventory().getItemCountByItemId(182007418) >= 1) {
							//Huge Misfortune Sealing Charm.
							removeQuestItem(env, 182007418, 1);
						    //Huge Sealed Sachet.
						    ItemService.addItem(player, 182007420, 1);
						    Npc npc = (Npc) env.getVisibleObject();
						    npc.getController().scheduleRespawn();
						    npc.getController().onDelete();
						    return closeDialogWindow(env);
						}
					}
                }
            } if (targetId == 835303) { //Exorcistical Tree.
			    switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					} case CHECK_COLLECTED_ITEMS: {
						if (QuestService.collectItemCheck(env, true)) {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 10000);
						} else {
							return sendQuestDialog(env, 10001);
						}
					} case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 835303) { //Exorcistical Tree.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}