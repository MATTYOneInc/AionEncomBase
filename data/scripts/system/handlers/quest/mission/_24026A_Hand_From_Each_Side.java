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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _24026A_Hand_From_Each_Side extends QuestHandler
{
    private final static int questId = 24026;
    private static List<Integer> mobs = new ArrayList<Integer>();
	
    static {
        mobs.add(213576); //Draconute Scout.
		mobs.add(213577); //Chandala Mage.
        mobs.add(213578); //Chandala Scaleguard.
        mobs.add(213579); //Chandala Fangblade.
    }
	
    public _24026A_Hand_From_Each_Side() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnDie(questId);
        qe.registerOnLogOut(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnQuestTimerEnd(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerQuestNpc(204301).addOnTalkEvent(questId); //Aegir.
        qe.registerQuestNpc(204403).addOnTalkEvent(questId); //Taisan. 
        qe.registerQuestNpc(204432).addOnTalkEvent(questId); //Kargate.
		for (int mob: mobs) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        }
		qe.registerQuestNpc(700183).addOnTalkEvent(questId); //Morheim Abyss Gate.
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        int[] quests = {24020, 24021, 24022, 24023, 24024, 24025};
        return defaultOnZoneMissionEndEvent(env, quests);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        int[] quests = {24020, 24021, 24022, 24023, 24024, 24025};
        return defaultOnLvlUpEvent(env, quests, true);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = env.getTargetId();
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 204301: { //Aegir.
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        case STEP_TO_1:
                            giveQuestItem(env, 182215371, 1);
                            TeleportService2.teleportTo(player, 220020000, 2795.9f, 478.37f, 265.86f, (byte) 51, TeleportAnimation.BEAM_ANIMATION);
                            return defaultCloseDialog(env, 0, 1);
                    }
                    break;
                } case 204403: { //Taisan. 
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        case STEP_TO_2:
                            giveQuestItem(env, 182215372, 1);
                            TeleportService2.teleportTo(player, 220020000, 3025.54f, 868.31f, 363.22f, (byte) 14, TeleportAnimation.BEAM_ANIMATION);
                            return defaultCloseDialog(env, 1, 2);
                    }
                    break;
                } case 204432: { //Kargate.
                    switch (env.getDialog()) {
                        case START_DIALOG: {
                            switch (qs.getQuestVarById(0)) {
                                case 2: {
                                    return sendQuestDialog(env, 1693);
                                } case 4: {
                                    return sendQuestDialog(env, 2034);
                                }
                            }
                        } case STEP_TO_3: {
                            qs.setQuestVar(3);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            QuestService.questTimerStart(env, 120);
                            spawn(player);
                            return true;
                        } case STEP_TO_4: {
                            removeQuestItem(env, 182215371, 1);
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            TeleportService2.teleportTo(player, 220020000, 248.52255f, 2398.9722f, 452.81012f, (byte) 48, TeleportAnimation.BEAM_ANIMATION);
                            return true;
                        }
                    }
                    break;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204301) { //Aegir.
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 2375);
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
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 3) {
                int targetId = env.getTargetId();
                if (mobs.contains(targetId)) {
                    spawn(player);
                    return true;
                }
            }
        }
        return false;
    }
	
    private void spawn(Player player) {
        int mobToSpawn = mobs.get(Rnd.get(0, 2));
        float x = 0;
        float y = 0;
        final float z = 217.48f;
        switch (mobToSpawn) {
            case 213576: { //Draconute Scout.
                x = 254.74f;
                y = 236.72f;
                break;
            } case 213577: { //Chandala Mage.
                x = 257.92f;
                y = 237.39f;
                break;
            } case 213578: { //Chandala Scaleguard.
                x = 261.86f;
                y = 237.5f;
                break;
            } case 213579: { //Chandala Fangblade.
                x = 268.86f;
                y = 243.5f;
                break;
            }
        }
        Npc spawn = (Npc) QuestService.spawnQuestNpc(320040000, player.getInstanceId(), mobToSpawn, x, y, z, (byte) 95);
        Collection<Npc> allNpcs = World.getInstance().getNpcs();
        Npc target = null;
        for (Npc npc : allNpcs) {
            if (npc.getNpcId() == 204432) { //Kargate.
                target = npc;
            }
        } if (target != null) {
            spawn.setTarget(target);
            ((AbstractAI) spawn.getAi2()).setStateIfNot(AIState.WALKING);
            spawn.setState(1);
            spawn.getMoveController().moveToTargetObject();
            PacketSendUtility.broadcastPacket(spawn, new SM_EMOTION(spawn, EmotionType.START_EMOTE2, 0, spawn.getObjectId()));
        }
    }
	
    @Override
    public boolean onQuestTimerEndEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 3) {
                qs.setQuestVar(4);
                updateQuestStatus(env);
                return true;
            }
        }
        return false;
    }
	
    @Override
    public boolean onDieEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        } if (qs.getQuestVarById(0) == 3) {
            QuestService.questTimerEnd(env);
            qs.setQuestVar(2);
            updateQuestStatus(env);
            return true;
        }
        return false;
    }
	
    @Override
    public boolean onLogOutEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 3) {
                qs.setQuestVar(2);
                updateQuestStatus(env);
                return true;
            }
        }
        return false;
    }
}