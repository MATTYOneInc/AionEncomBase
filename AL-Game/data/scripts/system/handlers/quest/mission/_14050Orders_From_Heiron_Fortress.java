package quest.mission;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;
                              
public class _14050Orders_From_Heiron_Fortress extends QuestHandler
{
    private final static int questId = 14050;
	
    public _14050Orders_From_Heiron_Fortress() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerQuestNpc(204500).addOnTalkEvent(questId);
        qe.registerOnEnterZone(ZoneName.get("NEW_HEIRON_GATE_210040000"), questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (targetId != 204500) {
            return false;
        } if (qs.getStatus() == QuestStatus.START) {
            if (env.getDialog() == QuestDialog.START_DIALOG) {
                return sendQuestDialog(env, 10002);
            } else if (env.getDialogId() == 1009) {
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return sendQuestDialog(env, 5);
            }
            return false;
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (env.getDialogId() == 23) {
                int[] ids = {14051, 14052, 14053, 14054};
                for (int id : ids) {
                    QuestEngine.getInstance().onEnterZoneMissionEnd(new QuestEnv(env.getVisibleObject(), env.getPlayer(), id, env.getDialogId()));
                }
            }
            return sendQuestEndDialog(env);
        }
        return false;
    }
	
    @Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        return defaultOnEnterZoneEvent(env, zoneName, ZoneName.get("NEW_HEIRON_GATE_210040000"));
    }
}