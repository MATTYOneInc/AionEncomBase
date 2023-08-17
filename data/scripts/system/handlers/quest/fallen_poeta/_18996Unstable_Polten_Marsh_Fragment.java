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
package quest.fallen_poeta;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _18996Unstable_Polten_Marsh_Fragment extends QuestHandler
{
    private final static int questId = 18996;
	private final static int[] npcs = {806075, 806252, 834034};
	private final static int[] IDLF1TBarricadeDragon01 = {703290}; //어�?운 �?��?길 철책.
	private final static int[] IDLF1TBarricadeDragon03 = {703292}; //트몰리아 �??광 입구 철책.
	
    public _18996Unstable_Polten_Marsh_Fragment() {
        super(questId);
    }
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
	
    public void register() {
		for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: IDLF1TBarricadeDragon01) {
		    qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: IDLF1TBarricadeDragon03) {
		    qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(243683).addOnKillEvent(questId); //군단장 타하바타.
		qe.registerQuestNpc(243684).addOnKillEvent(questId); //아티팩트를 지배하는 �?�로반.
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.START) {
            if (targetId == 806252) { //Favoni.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 1011);
					} case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST_SIMPLE: {
				        return closeDialogWindow(env);
					} case STEP_TO_1: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 834034) { //베�?�우스.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 1352);
					} case STEP_TO_2: {
                        changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806075) { //Weatha.
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
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 2) {
				int[] IDLF1TBarricadeDragon01 = {703290}; //어�?운 �?��?길 철책.
				int[] IDLF1TBarricadeDragon03 = {703292}; //트몰리아 �??광 입구 철책.
				switch (targetId) {
					case 703290: { //어�?운 �?��?길 철책.
						return defaultOnKillEvent(env, IDLF1TBarricadeDragon01, 0, 1, 1);
					} case 703292: { //트몰리아 �??광 입구 철책.
						qs.setQuestVar(3);
					    updateQuestStatus(env);
						return defaultOnKillEvent(env, IDLF1TBarricadeDragon03, 0, 1, 2);
					}
				}
			} else if (var == 3) {
				switch (targetId) {
                    case 243683: { //군단장 타하바타.
						qs.setQuestVar(4);
						updateQuestStatus(env);
						return true;
					}
                }
			} else if (var == 4) {
				switch (targetId) {
                    case 243684: { //아티팩트를 지배하는 �?�로반.
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