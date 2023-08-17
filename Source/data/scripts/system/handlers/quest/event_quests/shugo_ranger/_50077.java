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
package quest.event_quests.shugo_ranger;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _50077 extends QuestHandler
{
    private final static int questId = 50077;
	
	private final static int[] Q50077 = {
		241815, 241816, 241817, 241818, 241819, 241820, 241821, 241822, 241823,
		241743, 241744, 241745, 241746, 241747, 241748, 241749, 241750, 241751,
		242667, 242671, 242675, 242679, 242683, 242687, 242691, 242695, 242699,
		242703, 242707, 242711, 242715, 242719, 242723, 242807, 242811, 242815,
		242819, 242823, 242827, 242831, 242835, 242839, 242843, 242847, 242851,
		242855, 242859, 242863, 243207, 243211, 243215, 243219, 243223, 243227,
		243231, 243235, 243239, 243243, 243247, 243251, 243255, 243259, 243263,
		241776, 241777, 241778, 241779, 241780, 241781, 241782, 241783, 241784,
		241785, 241786, 241787, 243007, 243011, 243015, 243019, 243023, 243027,
		243031, 243035, 243039, 243043, 243047, 243051, 243055, 243059, 243063};
	
    public _50077() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(835578).addOnQuestStart(questId);
        qe.registerQuestNpc(835578).addOnTalkEvent(questId);
		for (int mob: Q50077) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        int targetId = env.getTargetId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 835578) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 835578) {
                if (dialog == QuestDialog.START_DIALOG) {
                    if (qs.getQuestVarById(0) == 30) {
                        return sendQuestDialog(env, 2375);
                    }
                } if (dialog == QuestDialog.SELECT_REWARD) {
                    changeQuestStep(env, 30, 31, true);
                    return sendQuestEndDialog(env);
                }
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 835578) {
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
	
    public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
                case 241815:
				case 241816:
				case 241817:
				case 241818:
				case 241819:
				case 241820:
				case 241821:
				case 241822:
				case 241823:
				case 241743:
				case 241744:
				case 241745:
				case 241746:
				case 241747:
				case 241748:
				case 241749:
				case 241750:
				case 241751:
				case 242667:
				case 242671:
				case 242675:
				case 242679:
				case 242683:
				case 242687:
				case 242691:
				case 242695:
				case 242699:
				case 242703:
				case 242707:
				case 242711:
				case 242715:
				case 242719:
				case 242723:
				case 242807:
				case 242811:
				case 242815:
				case 242819:
				case 242823:
				case 242827:
				case 242831:
				case 242835:
				case 242839:
				case 242843:
				case 242847:
				case 242851:
				case 242855:
				case 242859:
				case 242863:
				case 243207:
				case 243211:
				case 243215:
				case 243219:
				case 243223:
				case 243227:
				case 243231:
				case 243235:
				case 243239:
				case 243243:
				case 243247:
				case 243251:
				case 243255:
				case 243259:
				case 243263:
				case 241776:
				case 241777:
				case 241778:
				case 241779:
				case 241780:
				case 241781:
				case 241782:
				case 241783:
				case 241784:
				case 241785:
				case 241786:
				case 241787:
				case 243007:
				case 243011:
				case 243015:
				case 243019:
				case 243023:
				case 243027:
				case 243031:
				case 243035:
				case 243039:
				case 243043:
				case 243047:
				case 243051:
				case 243055:
				case 243059:
				case 243063:
                if (qs.getQuestVarById(1) < 30) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				} if (qs.getQuestVarById(1) >= 30) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
            }
        }
        return false;
    }
}