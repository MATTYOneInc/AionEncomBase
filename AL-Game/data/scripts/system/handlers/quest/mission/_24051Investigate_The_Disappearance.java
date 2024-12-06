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
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _24051Investigate_The_Disappearance extends QuestHandler
{
    private final static int questId = 24051;
    private final static int[] npcs = {204707, 204749, 204800, 700359};
	
    public _24051Investigate_The_Disappearance() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
        qe.registerQuestItem(182215375, questId);
		qe.registerOnMovieEndQuest(236, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnEnterZone(ZoneName.get("MINE_PORT_220040000"), questId);
        qe.registerOnEnterWorld(questId);
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
        return defaultOnLvlUpEvent(env, 24050, false);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
        if (qs == null) {
            return false;
        }
        Npc target = (Npc) env.getVisibleObject();
        int targetId = target.getNpcId();
        int var = qs.getQuestVarById(0);
        QuestDialog dialog = env.getDialog();
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 204707: { //Mani.
                    if (dialog == QuestDialog.START_DIALOG && var == 0) {
                        return sendQuestDialog(env, 1011);
                    } if (dialog == QuestDialog.START_DIALOG && var == 3) {
                        return sendQuestDialog(env, 2034);
                    } if (dialog == QuestDialog.STEP_TO_1) {
                        return defaultCloseDialog(env, 0, 1);
                    } if (dialog == QuestDialog.STEP_TO_4) {
                        return defaultCloseDialog(env, 3, 4);
                    }
                    break;
                } case 204749: { //Paeru.
                    if (dialog == QuestDialog.START_DIALOG && var == 1) {
                        return sendQuestDialog(env, 1352);
                    } if (dialog == QuestDialog.STEP_TO_2) {
                        return defaultCloseDialog(env, 1, 2, 182215375, 1, 0, 0);
                    }
                    break;
                } case 204800: { //Hammel.
                    if (dialog == QuestDialog.START_DIALOG && var == 4) {
                        return sendQuestDialog(env, 2375);
                    } if (dialog == QuestDialog.STEP_TO_5) {
                        giveQuestItem(env, 182215376, 1);
                        return defaultCloseDialog(env, 4, 5);
                    }
                    break;
                } case 700359: { //Port.
                    if (dialog == QuestDialog.USE_OBJECT && var == 5 && player.getInventory().getItemCountByItemId(182215377) >= 1) {
                        TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), 1757.82f, 1392.94f, 401.75f, (byte) 94);
                        return true;
                    }
                    break;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204707) { //Mani.
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    removeQuestItem(env, 182215376, 1);
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
	
    @Override
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 2) {
                return HandlerResult.fromBoolean(useQuestItem(env, item, 2, 3, false));
            }
        }
        return HandlerResult.FAILED;
    }
	
	@Override
	public boolean onEnterZoneEvent(final QuestEnv env, ZoneName name) {
		Player player = env.getPlayer();
		if (player == null) {
			return false;
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (name == ZoneName.get("MINE_PORT_220040000")) {
				if (var == 5) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							playQuestMovie(env, 236);
						}
					}, 10000);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId != 236) {
			return false;
		}
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			changeQuestStep(env, 5, 5, true);
			removeQuestItem(env, 182215377, 1);
			return true;
		}
		return false;
	}
}