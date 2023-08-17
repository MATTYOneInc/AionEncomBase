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
package quest.bare_truth;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _14030Retrieved_Memory extends QuestHandler
{
    private final static int questId = 14030;
    private final static int[] npcs = {790001, 700551, 205119, 700552, 203700};
    private final static int[] mobs = {214578, 215396, 215397, 215398, 215399, 215400, 205021, 205022, 215400};
	
    public _14030Retrieved_Memory() {
        super(questId);
    }
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env);
    }
	
    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
        qe.registerOnDie(questId);
        qe.registerOnEnterWorld(questId);
        for (int npc_id : npcs) {
            qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
        } for (int mob : mobs) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        }
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 203700: {
                    switch (env.getDialog()) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        } case STEP_TO_1: {
                            return defaultCloseDialog(env, 0, 1);
                        }
                    }
                    break;
                } case 790001: {
                    switch (env.getDialog()) {
                        case START_DIALOG: {
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            } else if (var == 3) {
                                return sendQuestDialog(env, 2034);
                            }
                        } case STEP_TO_2: {
                            return defaultCloseDialog(env, 1, 2);
                        } case STEP_TO_4: {
                            giveQuestItem(env, 182215387, 1);
							//Teleport To Fissure Of Destiny.
                            TeleportService2.teleportTo(env.getPlayer(), 400010000, 2417.6367f, 2517.138f, 1434.491f, (byte) 37);
                            return defaultCloseDialog(env, 3, 4);
                        }
                    }
                    break;
                } case 700551: {
                    if (env.getDialog() == QuestDialog.USE_OBJECT && var == 4) {
                        WorldMapInstance AtaxiarA = InstanceService.getNextAvailableInstance(310120000);
                        InstanceService.registerPlayerWithInstance(AtaxiarA, player);
                        TeleportService2.teleportTo(player, 310120000, AtaxiarA.getInstanceId(), 52, 174, 229);
                        return true;
                    }
                    break;
                } case 205119: {
                    switch (env.getDialog()) {
                        case START_DIALOG: {
                            if (var == 4) {
                                return sendQuestDialog(env, 2375);
                            }
                        } case STEP_TO_5: {
                            if (var == 4) {
                                removeQuestItem(env, 182215387, 1);
                                SkillEngine.getInstance().applyEffectDirectly(281, player, player, 0);
                                player.setState(CreatureState.FLIGHT_TELEPORT);
                                player.unsetState(CreatureState.ACTIVE);
                                player.setFlightTeleportId(1001);
                                PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 1001, 0));
                                final QuestEnv qe = env;
                                ThreadPoolManager.getInstance().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        changeQuestStep(qe, 4, 5, false);
                                    }
                                }, 43000);
                                return true;
                            }
                        }
                    }
                    break;
                } case 700552: {
                    if (env.getDialog() == QuestDialog.USE_OBJECT && var == 56) {
                        TeleportService2.teleportTo(player, 110010000, 1322.1934f, 1511.148f, 567.909f);
                        return useQuestObject(env, 56, 57, true, false);
                    }
                    break;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203700) {
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }
	
    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 2) {
                return defaultOnKillEvent(env, 214578, 2, 3);
            } else if (var >= 5 && var < 55) {
                int[] npcIds = {215396, 215397, 215398, 215399, 215400, 205021, 205022};
                if (var == 54) {
                    QuestService.addNewSpawn(310120000, player.getInstanceId(), 215400, 299.4378f, 289.15744f, 206.48138f, (byte) 75);
                }
                return defaultOnKillEvent(env, npcIds, 5, 55);
            } else if (var == 55) {
                return defaultOnKillEvent(env, 215400, 55, 56);
            }
        }
        return false;
    }
	
    @Override
    public boolean onDieEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var > 4 && var < 56) {
                changeQuestStep(env, var, 4, false);
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
                return true;
            }
        }
        return false;
    }
	
    @Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (player.getWorldId() != 310120000) {
            if (qs != null && qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVarById(0);
                if (var > 4 && var < 56) {
                    changeQuestStep(env, var, 4, false);
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
                    return true;
                }
            }
        }
        return false;
    }
}