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
package quest.cradle_of_eternity;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _16825Escape_From_The_Contaminated_Library extends QuestHandler
{
    private final static int questId = 16825;
	private final static int[] npcs = {806283}; //ë?¼ë”œë¦¬ìŠ¤.
	private final static int[] IDEternity02TowerBoss75Ah = {220534}; //íƒ€ë?½í•œ ë¬¼ì?˜ ëŠ?ë¹Œë¦¼.
	private final static int[] IDEternity02CKeyRa75Ae = {220597}; //ì˜¤ì—¼ë?œ ì œ3 ì„œê³  ìž…êµ¬ ìž ê¸ˆìž¥ì¹˜.
	
    public _16825Escape_From_The_Contaminated_Library() {
        super(questId);
    }
	
    public void register() {
		for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: IDEternity02TowerBoss75Ah) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: IDEternity02CKeyRa75Ae) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnEnterWorld(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs.getStatus() == QuestStatus.REWARD) {
            QuestService.finishQuest(env);
        }
        return false;
    }
	
	@Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (player.getWorldId() == 301550000) { //ì§€ì‹?ì?˜ ì •ì›?.
            if (qs == null || qs.canRepeat()) {
                env.setQuestId(questId);
                if (QuestService.startQuest(env)) {
					return true;
				}
            }
        }
        return false;
    }
	
	public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
                case 220534: //íƒ€ë?½í•œ ë¬¼ì?˜ ëŠ?ë¹Œë¦¼.
				case 220597: //ì˜¤ì—¼ë?œ ì œ3 ì„œê³  ìž…êµ¬ ìž ê¸ˆìž¥ì¹˜.
                if (qs.getQuestVarById(1) < 1) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				} if (qs.getQuestVarById(1) >= 1) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
            }
        }
        return false;
    }
}