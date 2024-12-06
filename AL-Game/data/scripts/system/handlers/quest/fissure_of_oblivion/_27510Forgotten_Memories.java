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
package quest.fissure_of_oblivion;

import com.aionemu.gameserver.model.*;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.*;
import com.aionemu.gameserver.questEngine.model.*;
import com.aionemu.gameserver.services.teleport.*;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _27510Forgotten_Memories extends QuestHandler
{
    private final static int questId = 27510;
	
	private final static int[] IDTransformSado = {
		244454, 244455, 244456, 244457, 244495, 244496, 244497, 244498, 244536, 244537,
		244538, 244539, 244577, 244578, 244579, 244580, 244618, 244619, 244620, 244621,
		244659, 244660, 244661, 244662, 244700, 244701, 244702, 244703, 244741, 244742,
		244743, 244744, 244782, 244783, 244784, 244785, 244823, 244824, 244825, 244826};
	
	private final static int[] shadowOfOblivion = {
	    244490, 244491, 244492, 244493, 244494, 244531, 244532, 244533, 244534, 244535, 244572, 244573,
		244574, 244575, 244576, 244613, 244614, 244615, 244616, 244617, 244654, 244655, 244656, 244657,
		244658, 244695, 244696, 244697, 244698, 244699, 244736, 244737, 244738, 244739, 244740, 244777,
		244778, 244779, 244780, 244781, 244818, 244819, 244820, 244821, 244822, 244859, 244860, 244861,
		244862, 244863};
	
    public _27510Forgotten_Memories() {
        super(questId);
    }
	
	@Override
	public void register() {
		qe.registerQuestNpc(806079).addOnTalkEvent(questId); //Peregrine.
		qe.registerQuestNpc(806437).addOnTalkEvent(questId); //Stella.
		qe.registerQuestNpc(834194).addOnTalkEvent(questId); //운명�?� 보주.
		for (int mob: IDTransformSado) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: shadowOfOblivion) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806079) { //Peregrine.
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
			} if (targetId == 806437) { //Stella.
				switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 1) {
                            return sendQuestDialog(env, 1354);
                        }
					} case SELECT_ACTION_1352: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					} case SELECT_ACTION_1353: {
						if (var == 1) {
							return sendQuestDialog(env, 1353);
						}
					} case STEP_TO_2: {
                        changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
                }
			} if (targetId == 834194) { //운명�?� 보주.
				switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 4) {
                            return sendQuestDialog(env, 2377);
                        }
					} case SELECT_ACTION_2376: {
						if (var == 4) {
							return sendQuestDialog(env, 2376);
						}
					} case SET_REWARD: {
						changeQuestStep(env, 4, 5, false);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						TeleportService2.teleportTo(player, 220110000, 1876.4996f, 1952.8883f, 205.12239f, (byte) 68);
						player.getEffectController().removeEffect(4808);
						player.getEffectController().removeEffect(4813);
						player.getEffectController().removeEffect(4818);
						player.getEffectController().removeEffect(4824);
						player.getEffectController().removeEffect(4831);
						player.getEffectController().removeEffect(4834);
						player.getEffectController().removeEffect(4835);
						player.getEffectController().removeEffect(4836);
						return closeDialogWindow(env);
					}
                }
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806079) { //Peregrine.
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
    public boolean onEnterWorldEvent(QuestEnv env) {
        final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (player.getWorldId() == 302100000) { //Fissure Of Oblivion.
			if (qs != null && qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVars().getQuestVars();
                if (var == 2) {
                    changeQuestStep(env, 2, 3, false);
				}
			}
		}
        return false;
    }
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 3) {
                int var1 = qs.getQuestVarById(1);
                if (var1 >= 0 && var1 < 10) {
                    return defaultOnKillEvent(env, IDTransformSado, var1, var1 + 1, 1);
                }
            } if (var == 3) {
				switch (targetId) {
                    case 244490:
					case 244491:
					case 244492:
					case 244493:
					case 244494:
					case 244531:
					case 244532:
					case 244533:
					case 244534:
					case 244535:
					case 244572:
					case 244573:
					case 244574:
					case 244575:
					case 244576:
					case 244613:
					case 244614:
					case 244615:
					case 244616:
					case 244617:
					case 244654:
					case 244655:
					case 244656:
					case 244657:
					case 244658:
					case 244695:
					case 244696:
					case 244697:
					case 244698:
					case 244699:
					case 244736:
					case 244737:
					case 244738:
					case 244739:
					case 244740:
					case 244777:
					case 244778:
					case 244779:
					case 244780:
					case 244781:
					case 244818:
					case 244819:
					case 244820:
					case 244821:
					case 244822:
					case 244859:
					case 244860:
					case 244861:
					case 244862:
					case 244863: {
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