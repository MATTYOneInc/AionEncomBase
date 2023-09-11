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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _14025Cooking_Up_Disasters extends QuestHandler
{
    private final static int questId = 14025;
    private final static int[] npcs = {203989, 204020, 203901};
    private final static int[] mobs = {211776, 217090};
	
    public _14025Cooking_Up_Disasters() {
        super(questId);
    }
	
    @Override
    public void register() {
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: mobs) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        }
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 14023, false);
    }
	
    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 5) {
                int[] KrallWarriorHK38Ae = {211776};
                int[] LF2KrallShaQnmd39An = {217090};
                switch (targetId) {
                    case 211776: { //Crack Kaidan Warmonger.
                        return defaultOnKillEvent(env, KrallWarriorHK38Ae, 0, 4, 1);
                    } case 217090: { //Shaman Kalabar.
                        return defaultOnKillEvent(env, LF2KrallShaQnmd39An, 0, 1, 2);
                    }
                }
            }
        }
        return false;
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		int var = qs.getQuestVarById(0);
        int var1 = qs.getQuestVarById(1);
        int var2 = qs.getQuestVarById(2);
		if (qs == null) {
            return false;
        } if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203901) { //Telemachus.
                return sendQuestEndDialog(env);
            }
        } if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 203989) { //Tumblusen.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        } else if (var == 1) {
                            return sendQuestDialog(env, 1352);
                        } else if (var == 2) {
							return sendQuestDialog(env, 1438);
						} else if (var == 4) {
                            return sendQuestDialog(env, 2034);
                        } else if (var == 5 && var1 == 4 && var2 == 1) {
							return sendQuestDialog(env, 2716);
						}
					} case CHECK_COLLECTED_ITEMS: {
                        if (var == 1) {
							if (QuestService.collectItemCheck(env, true)) {
								qs.setQuestVar(2);
								updateQuestStatus(env);
								return sendQuestDialog(env, 1438);
							} else {
								return sendQuestDialog(env, 1353);
							}
						}
					} case STEP_TO_1: {
                        if (var == 0) {
                            return defaultCloseDialog(env, 0, 1);
                        }
					} case STEP_TO_2: {
						if (var == 2) {
							qs.setQuestVar(3);
							updateQuestStatus(env);
							TeleportService2.teleportTo(player, 210020000, 1596f, 1529f, 317f, (byte) 120);
							return closeDialogWindow(env);
						}
					} case STEP_TO_4: {
                        removeQuestItem(env, 182201005, 1);
                        return defaultCloseDialog(env, 4, 5);
					} case STEP_TO_6: {
                        return defaultCloseDialog(env, 5, 6, true, false);
					} case FINISH_DIALOG: {
                        return closeDialogWindow(env);
                    }
                }
            } if (targetId == 204020) { //Mabangtah.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 3) {
                            return sendQuestDialog(env, 1693);
                        }
					} case SELECT_ACTION_1694: {
						if (var == 3) {
							return sendQuestDialog(env, 1694);
						}
					} case STEP_TO_3: {
                        giveQuestItem(env, 182201005, 1);
						TeleportService2.teleportTo(player, 210020000, 1759.697f, 905.983f, 427.812f, (byte) 23);
                        return defaultCloseDialog(env, 3, 4);
                    }
                }
            }
        }
        return false;
    }
}