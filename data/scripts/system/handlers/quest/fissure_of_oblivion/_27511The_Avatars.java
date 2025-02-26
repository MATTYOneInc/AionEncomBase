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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _27511The_Avatars extends QuestHandler {

    private final static int questId = 27511;
	private final static int[] npcs = {806079};
	private final static int[] IDTransformMobs1 = {
		244458, 244459, 244460, 244461, 244462, 244463, 244464, 244465, 244466, 244467, 244468, 244469,
		244499, 244500, 244501, 244502, 244503, 244504, 244505, 244506, 244507, 244508, 244509, 244510,
	    244540, 244541, 244542, 244543, 244544, 244545, 244546, 244547, 244548, 244549, 244550, 244551,
	    244581, 244582, 244583, 244584, 244585, 244586, 244587, 244588, 244589, 244590, 244591, 244592,
	    244622, 244623, 244624, 244625, 244626, 244627, 244628, 244629, 244630, 244631, 244632, 244633,
	    244663, 244664, 244665, 244666, 244667, 244668, 244669, 244670, 244671, 244672, 244673, 244674,
	    244704, 244705, 244706, 244707, 244708, 244709, 244710, 244711, 244712, 244713, 244714, 244715,
	    244745, 244746, 244747, 244748, 244749, 244750, 244751, 244752, 244753, 244754, 244755, 244756,
	    244786, 244787, 244788, 244789, 244790, 244791, 244792, 244793, 244794, 244795, 244796, 244797,
	    244827, 244828, 244829, 244830, 244831, 244832, 244833, 244834, 244835, 244836, 244837, 244838};
	
	private final static int[] IDTransformMobs2 = {
	    245577, 245578, 245579, 245580, 245581, 245582, 245583, 245584, 245585, 245586, 245587, 245588,
	    245589, 245590, 245591, 245592, 245593, 245594, 245595, 245596, 245597, 245598, 245599, 245600,
	    245601, 245602, 245603, 245604, 245605, 245606, 245607, 245608, 245609, 245610, 245611, 245612,
	    245613, 245614, 245615, 245616, 245617, 245618, 245619, 245620, 245621, 245622, 245623, 245624,
	    245625, 245626, 245627, 245628, 245629, 245630, 245631, 245632, 245633, 245634, 245635, 245636,
	    245637, 245638, 245639, 245640, 245641, 245642, 245643, 245644, 245645, 245646, 245647, 245648,
	    245649, 245650, 245651, 245652, 245653, 245654, 245655, 245656, 245657, 245658, 245659, 245660,
	    245661, 245662, 245663, 245664, 245665, 245666, 245667, 245668, 245669, 245670, 245671, 245672,
	    245673, 245674, 245675, 245676, 245677, 245678, 245679, 245680, 245681, 245682, 245683, 245684,
	    245685, 245686, 245687, 245688, 245689, 245690, 245691, 245692, 245693, 245694, 245695, 245696};
	
	private final static int[] IDTransformMobs3 = {
	    244458, 244459, 244460, 244461, 244462, 244463, 244464, 244465, 244466, 244467, 244468, 244469,
	    244499, 244500, 244501, 244502, 244503, 244504, 244505, 244506, 244507, 244508, 244509, 244510,
	    244540, 244541, 244542, 244543, 244544, 244545, 244546, 244547, 244548, 244549, 244550, 244551,
	    244581, 244582, 244583, 244584, 244585, 244586, 244587, 244588, 244589, 244590, 244591, 244592,
	    244622, 244623, 244624, 244625, 244626, 244627, 244628, 244629, 244630, 244631, 244632, 244633,
	    244663, 244664, 244665, 244666, 244667, 244668, 244669, 244670, 244671, 244672, 244673, 244674,
	    244704, 244705, 244706, 244707, 244708, 244709, 244710, 244711, 244712, 244713, 244714, 244715,
	    244745, 244746, 244747, 244748, 244749, 244750, 244751, 244752, 244753, 244754, 244755, 244756,
	    244786, 244787, 244788, 244789, 244790, 244791, 244792, 244793, 244794, 244795, 244796, 244797,
	    244827, 244828, 244829, 244830, 244831, 244832, 244833, 244834, 244835, 244836, 244837, 244838,
	    245697, 245698, 245699, 245703, 245704, 245705, 245709, 245710, 245711, 245715, 245716, 245717,
	    245721, 245722, 245723, 245727, 245728, 245729, 245733, 245734, 245735, 245739, 245740, 245741,
	    245745, 245746, 245747, 245751, 245752, 245753};
	
	private final static int[] shadowOfOblivion = {
	    244490, 244491, 244492, 244493, 244494, 244531, 244532, 244533, 244534, 244535, 244572, 244573,
		244574, 244575, 244576, 244613, 244614, 244615, 244616, 244617, 244654, 244655, 244656, 244657,
		244658, 244695, 244696, 244697, 244698, 244699, 244736, 244737, 244738, 244739, 244740, 244777,
		244778, 244779, 244780, 244781, 244818, 244819, 244820, 244821, 244822, 244859, 244860, 244861,
		244862, 244863};
	
    public _27511The_Avatars() {
        super(questId);
    }
	
	@Override
	public void register() {
		for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: IDTransformMobs1) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: IDTransformMobs2) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: IDTransformMobs3) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: shadowOfOblivion) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZone(ZoneName.get("IDTRANSFORM_Q17511_A_302100000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDTRANSFORM_Q17511_B_302100000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDTRANSFORM_Q17511_C_302100000"), questId);
	}
	
	@Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
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
            if (qs == null || qs.canRepeat()) {
                env.setQuestId(questId);
                if (QuestService.startQuest(env)) {
					return true;
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
			if (zoneName == ZoneName.get("IDTRANSFORM_Q17511_A_302100000")) {
				if (var == 1) {
					changeQuestStep(env, 1, 4, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("IDTRANSFORM_Q17511_B_302100000")) {
				if (var == 5) {
					changeQuestStep(env, 5, 8, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("IDTRANSFORM_Q17511_C_302100000")) {
				if (var == 9) {
					changeQuestStep(env, 9, 10, false);
					return true;
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
            if (var == 0) {
                int var1 = qs.getQuestVarById(1);
                if (var1 >= 0 && var1 < 4) {
                    return defaultOnKillEvent(env, IDTransformMobs1, var1, var1 + 1, 1);
                } else if (var1 == 4) {
					qs.setQuestVar(1);
					updateQuestStatus(env);
                    return true;
                }
            } else if (var == 4) {
                int var1 = qs.getQuestVarById(1);
                if (var1 >= 0 && var1 < 4) {
                    return defaultOnKillEvent(env, IDTransformMobs2, var1, var1 + 1, 1);
                } else if (var1 == 4) {
					qs.setQuestVar(5);
					updateQuestStatus(env);
                    return true;
                }
            } else if (var == 8) {
                int var1 = qs.getQuestVarById(1);
                if (var1 >= 0 && var1 < 4) {
                    return defaultOnKillEvent(env, IDTransformMobs3, var1, var1 + 1, 1);
                } else if (var1 == 4) {
					qs.setQuestVar(9);
					updateQuestStatus(env);
                    return true;
                }
            } else if (var == 10) {
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
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return true;
					}
                }
            }
        }
        return false;
    }
}