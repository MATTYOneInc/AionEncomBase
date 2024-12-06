/*
 * This file is part of Encom.
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.archdaeva;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author (Encom)
/****/

public class _20112 extends QuestHandler
{
    public static final int questId = 20112;
	private final static int[] npcs = {805356, 806780, 703465};
	private final static int[] Ab1EreshFiEvent = {885140}; //ì—?ë ˆìŠˆëž€íƒ€ì?˜ ëˆˆ ìˆ˜í˜¸ë³‘.
	
    public _20112() {
        super(questId);
    }
	
    @Override
    public void register() {
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: Ab1EreshFiEvent) { //ì—?ë ˆìŠˆëž€íƒ€ì?˜ ëˆˆ ìˆ˜í˜¸ë³‘.
		    qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182216238, questId); //ë§ˆì¡± ë?°ë°” êµ¬ê¸‰ìƒ?ìž?.
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(885141).addOnKillEvent(questId); //ì—?ë ˆìŠˆëž€íƒ€ì?˜ ëˆˆ ìˆ˜í˜¸ìž¥êµ?.
    }
	
	@Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 20111, true);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 805356) { //Pontekane.
                switch (env.getDialog()) {
					case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        }
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case STEP_TO_1: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 806780) { //Rahdpel.
                switch (env.getDialog()) {
				    case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						} else if (var == 4) {
							return sendQuestDialog(env, 2375);
						}
					} case SELECT_ACTION_1353: {
						if (var == 1) {
							return sendQuestDialog(env, 1353);
						}
					} case SELECT_ACTION_1354: {
						if (var == 1) {
							return sendQuestDialog(env, 1354);
						}
					} case SELECT_ACTION_2376: {
						if (var == 4) {
							return sendQuestDialog(env, 2376);
						}
					} case STEP_TO_2: {
						return defaultCloseDialog(env, 1, 2, false, false, 182216238, 1, 0, 0); //ë§ˆì¡± ë?°ë°” êµ¬ê¸‰ìƒ?ìž?.
					} case CHECK_COLLECTED_ITEMS: {
						if (QuestService.collectItemCheck(env, true)) {
							qs.setStatus(QuestStatus.REWARD);
						    updateQuestStatus(env);
							changeQuestStep(env, 4, 5, false);
							return sendQuestDialog(env, 10000);
						} else {
							return sendQuestDialog(env, 10001);
						}
					}
				}
            } if (targetId == 703465) { //ë¶€ì„œì§„ ë³´ê¸‰í’ˆ ìƒ?ìž?.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        return closeDialogWindow(env);
					}
                }
            }
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 805356) { //Pontekane.
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
	
	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (player.isInsideZone(ZoneName.get("DIVINE_FORTRESS_400010000"))) {
				int itemId = item.getItemId();
				int var = qs.getQuestVarById(0);
				int var1 = qs.getQuestVarById(1);
				if (itemId == 182216238) { //ë§ˆì¡± ë?°ë°” êµ¬ê¸‰ìƒ?ìž?.
					if (var == 2) {
						if (var1 >= 0 && var1 < 4) {
							changeQuestStep(env, var1, var1 + 1, false, 1);
							return HandlerResult.SUCCESS;
						} else if (var1 == 4) {
							qs.setQuestVar(3);
							updateQuestStatus(env);
							return HandlerResult.SUCCESS;
						}
					}
				}
			}
		}
		return HandlerResult.UNKNOWN;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 3) {
				int[] Ab1EreshFiEvent = {885140};
				switch (targetId) {
					case 885140: { //ì—?ë ˆìŠˆëž€íƒ€ì?˜ ëˆˆ ìˆ˜í˜¸ë³‘.
						return defaultOnKillEvent(env, Ab1EreshFiEvent, 0, 5, 1);
					}
				} switch (targetId) {
				    case 885141: { //ì—?ë ˆìŠˆëž€íƒ€ì?˜ ëˆˆ ìˆ˜í˜¸ìž¥êµ?.
						qs.setQuestVar(4);
						updateQuestStatus(env);
						return true;
					}
				}
			}
		}
		return false;
	}
}