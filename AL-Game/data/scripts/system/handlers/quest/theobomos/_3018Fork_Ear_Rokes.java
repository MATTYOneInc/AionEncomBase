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
package quest.theobomos;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _3018Fork_Ear_Rokes extends QuestHandler {

    private final static int questId = 3018;
    public _3018Fork_Ear_Rokes() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerQuestNpc(730105).addOnQuestStart(questId);
        qe.registerQuestNpc(730105).addOnTalkEvent(questId);
        qe.registerQuestNpc(798150).addOnTalkEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 730105) {
                switch (dialog) {
                    case USE_OBJECT: {
                        return sendQuestDialog(env, 4762);
                    } case STEP_TO_1: {
                        QuestService.startQuest(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
                        return true;
                    } default:
                        return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (targetId == 798150) {
                switch (dialog) {
                    case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        }
				    } case CHECK_COLLECTED_ITEMS: {
                        return checkQuestItems(env, 0, 1, true, 5, 2716);
                       } 
                    case FINISH_DIALOG: {
                        return sendQuestEndDialog(env);
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 798150) {
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }
}