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
package quest.IDAb1_Heroes;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _27551 extends QuestHandler {

    private final static int questId = 27551;
	private final static int[] npcs = {806790, 835783};
	private final static int[] IDAb1HeroesDrakan = {248015, 248016, 248017, 248018, 248019, 248022, 248024};
	private final static int[] IDAb1HeroesWaveDoor = {248440, 248441, 248442, 248443};
	private final static int[] IDAb1HeroesBoss73Ah = {248025};
    public _27551() {
        super(questId);
    }
	
    public void register() {
		for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: IDAb1HeroesDrakan) {
		    qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: IDAb1HeroesWaveDoor) {
		    qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: IDAb1HeroesBoss73Ah) {
		    qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
        qe.registerQuestNpc(806790).addOnQuestStart(questId);
		qe.registerOnEnterZone(ZoneName.get("HARBINGER_LANDING_400010000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDAB1_HEROES_D_Q27551_320160000"), questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 806790) { 
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					} case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST_SIMPLE: {
				        return closeDialogWindow(env);
					}
				}
			}
		}
        if (qs == null || qs == null || qs.getStatus() == QuestStatus.START) {
            if (targetId == 835783) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 1352);
					} case SELECT_ACTION_1353: {
						return sendQuestDialog(env, 1353);
					} case STEP_TO_2: {
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
                }
            }
        } 
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806790) {
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
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("IDAB1_HEROES_D_Q27551_320160000")) {
				if (var == 0) {
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("HARBINGER_LANDING_400010000")) {
				if (var == 4) {
					changeQuestStep(env, 4, 5, true);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
        if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
            if (var == 2) {
				switch (env.getTargetId()) {
					case 248015:
				    case 248016:
				    case 248017:
				    case 248018:
				    case 248019:
				    case 248022:
				    case 248024:
					    if (qs.getQuestVarById(1) < 60) {
					        qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					        updateQuestStatus(env);
				        } if (qs.getQuestVarById(1) >= 60) {
							qs.setQuestVar(3);
							updateQuestStatus(env);
						}
					break;
					case 248440:
					case 248441:
					case 248442:
					case 248443:
			            if (qs.getQuestVarById(2) < 10) {
							qs.setQuestVarById(2, qs.getQuestVarById(2) + 1);
							updateQuestStatus(env);
				        } if (qs.getQuestVarById(2) >= 10) {
							updateQuestStatus(env);
						}
					break;
				}
            } else if (var == 3) {
				switch (targetId) {
					case 248025: {
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