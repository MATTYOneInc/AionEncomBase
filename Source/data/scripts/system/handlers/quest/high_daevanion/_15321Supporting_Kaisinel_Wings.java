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
package quest.high_daevanion;

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

public class _15321Supporting_Kaisinel_Wings extends QuestHandler
{
    public static final int questId = 15321;
	//Cygnea
	private final static int[] LF5_P1 = {235829, 235831, 235851};
	private final static int[] LF5_P2 = {235915, 235917, 235920};
	//Inggison [Conquest Offering]
	private final static int[] LF4_Rotation = {236530, 236531, 236532, 236533, 236534, 236535,
	236536, 236537, 236538, 236539, 236540, 236541, 236542, 236543, 236544, 236545, 236546, 236547,
	236548, 236549, 236550, 236551, 236552, 236553};
	//Levinshor
	private final static int[] LDF4_Advance = {233910, 233911, 233912, 233915, 233955, 233916, 234159};
	//Kaldor
	private final static int[] LDF5_Fortress = {234248, 234250, 234251, 234517, 234518, 234519, 234520, 234521, 234522, 234523,
	234524, 234525, 234526, 234527, 234528};
	//Reshanta [Upper Abyss]
	private final static int[] AB1 = {883276, 883277, 883278, 883279, 883280, 883281, 883282, 883283};
    
	public _15321Supporting_Kaisinel_Wings() {
        super(questId);
    }
	
    @Override
    public void register() {
		qe.registerQuestNpc(805330).addOnQuestStart(questId); //Potencia.
        qe.registerQuestNpc(805330).addOnTalkEvent(questId); //Potencia.
		qe.registerQuestNpc(805332).addOnTalkEvent(questId); //Nephoria.
        qe.registerQuestNpc(805333).addOnTalkEvent(questId); //Prestelle.
        qe.registerQuestNpc(805334).addOnTalkEvent(questId); //Somation.
        qe.registerQuestNpc(805335).addOnTalkEvent(questId); //Spintel.
        qe.registerQuestNpc(805336).addOnTalkEvent(questId); //Asteness.
        qe.registerQuestNpc(805337).addOnTalkEvent(questId); //Tallesleon.
		for (int mob: LF5_P1) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
        } for (int mob2: LF5_P2) {
			qe.registerQuestNpc(mob2).addOnKillEvent(questId);
        } for (int mob3: LF4_Rotation) {
			qe.registerQuestNpc(mob3).addOnKillEvent(questId);
        } for (int mob4: LDF4_Advance) {
			qe.registerQuestNpc(mob4).addOnKillEvent(questId);
        } for (int mob5: LDF5_Fortress) {
			qe.registerQuestNpc(mob5).addOnKillEvent(questId);
        } for (int mob6: AB1) {
			qe.registerQuestNpc(mob6).addOnKillEvent(questId);
        }
		qe.registerQuestNpc(805330).addOnAtDistanceEvent(questId);
    }
	
	@Override
	public boolean onAtDistanceEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			QuestService.startQuest(env);
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
		if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 805332) { //Nephoria.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					} case SELECT_ACTION_1012: {
						return sendQuestDialog(env, 1012);
					} case STEP_TO_1: {
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
                }
            } else if (targetId == 805333) { //Prestelle.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1693);
					} case SELECT_ACTION_1694: {
						return sendQuestDialog(env, 1694);
					} case STEP_TO_3: {
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
				}
			} else if (targetId == 805334) { //Somation.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 2375);
					} case SELECT_ACTION_2376: {
						return sendQuestDialog(env, 2376);
					} case STEP_TO_5: {
						changeQuestStep(env, 4, 5, false);
						return closeDialogWindow(env);
					}
				}
			} else if (targetId == 805335) { //Spintel.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 3057);
					} case SELECT_ACTION_3058: {
						return sendQuestDialog(env, 3058);
					} case STEP_TO_7: {
						changeQuestStep(env, 6, 7, false);
						return closeDialogWindow(env);
					}
				}
			} else if (targetId == 805336) { //Asteness.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 3739);
					} case SELECT_ACTION_3740: {
						return sendQuestDialog(env, 3740);
					} case STEP_TO_9: {
						changeQuestStep(env, 8, 9, false);
						return closeDialogWindow(env);
					}
				}
			} else if (targetId == 805337) { //Tallesleon.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 6500);
					} case SELECT_ACTION_6501: {
						return sendQuestDialog(env, 6501);
					} case STEP_TO_11: {
						changeQuestStep(env, 10, 11, false);
						return closeDialogWindow(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 805330) { //Potencia.
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
    public boolean onKillEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int var = qs.getQuestVarById(0);
		int var1 = qs.getQuestVarById(1);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        } if (var == 1 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, LF5_P1, var1, var1 + 1, 1);
		} else if (var == 1 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 1, 2, false);
			updateQuestStatus(env);
			return true;
		} if (var == 3 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, LF5_P2, var1, var1 + 1, 1);
		} else if (var == 3 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 3, 4, false);
			updateQuestStatus(env);
			return true;
		} if (var == 5 && var1 >= 0 && var1 < 9) {
			return defaultOnKillEvent(env, LF4_Rotation, var1, var1 + 1, 1);
		} else if (var == 5 && var1 == 9) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 5, 6, false);
			updateQuestStatus(env);
			return true;
		} if (var == 7 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, LDF4_Advance, var1, var1 + 1, 1);
		} else if (var == 7 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 7, 8, false);
			updateQuestStatus(env);
			return true;
		} if (var == 9 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, LDF5_Fortress, var1, var1 + 1, 1);
		} else if (var == 9 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 9, 10, false);
			updateQuestStatus(env);
			return true;
		} if (var == 11 && var1 >= 0 && var1 < 29) {
			return defaultOnKillEvent(env, AB1, var1, var1 + 1, 1);
		} else if (var == 11 && var1 == 29) {
			qs.setQuestVarById(1, 0);
			qs.setQuestVar(12);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}