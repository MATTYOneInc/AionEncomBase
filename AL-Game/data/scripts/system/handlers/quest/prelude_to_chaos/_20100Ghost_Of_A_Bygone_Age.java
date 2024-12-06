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
package quest.prelude_to_chaos;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _20100Ghost_Of_A_Bygone_Age extends QuestHandler
{
    private final static int questId = 20100;
	
    public _20100Ghost_Of_A_Bygone_Age() {
        super(questId);
    }
	
	@Override
	public void register() {
		int[] npcIds = {804719, 802363, 802463};
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182215449, questId);
		for (int npcId: npcIds) {
			qe.registerQuestNpc(npcId).addOnTalkEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("PINNACLE_CATARACT_OUTPOST_600100000"),questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
	
	@Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 804719: { //Haldor.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        } case STEP_TO_1: {
							TeleportService2.teleportTo(player, 600100000, 1786.3436f, 1738.9655f, 298.82288f, (byte) 13, TeleportAnimation.BEAM_ANIMATION);
							changeQuestStep(env, 0, 1, false);
							return closeDialogWindow(env);
                        }
                    }
                    break;
                } case 802363: { //Girtan.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            }
                        } case STEP_TO_3: {
                            return defaultCloseDialog(env, 2, 3);
                        }
                    }
                    break;
                } case 802463: { //Kahrun.
                    switch (dialog) {
                        case START_DIALOG: {
							if (var == 3) {
                                return sendQuestDialog(env, 2034);
                            }
                        } case STEP_TO_4: {
                            return defaultCloseDialog(env, 3, 4, 182215449, 1, 0, 0);
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 802463) { //Kahrun.
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getQuestVarById(0) == 4) {
			return HandlerResult.fromBoolean(useQuestItem(env, item, 4, 4, true));
		}
		return HandlerResult.FAILED;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName == ZoneName.get("PINNACLE_CATARACT_OUTPOST_600100000")) {
			Player player = env.getPlayer();
			if (player == null)
				return false;
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs != null && qs.getStatus() == QuestStatus.START) {
				int var = qs.getQuestVarById(0);
				if (var == 1) {
					changeQuestStep(env, 1, 2, false);
					return true;
				}
			}
		}
		return false;
	}
}