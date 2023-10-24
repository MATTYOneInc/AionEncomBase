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
package quest.daevanion;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _2990Making_The_Daevanion_Weapon extends QuestHandler
{
	private final static int questId = 2990;
	
	private int A = 0;
	private int B = 0;
	private int C = 0;
	private int ALL = 0;
	
	public _2990Making_The_Daevanion_Weapon() {
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 2989, true);
	}
	
	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		//int[] mobs = {253696, 253720, 253721, 253682, 253684, 253684};
		int[] mobs = {213011, 213012, 213009, 213010, 213007, 213008};
		
		qe.registerQuestNpc(204146).addOnQuestStart(questId);
		qe.registerQuestNpc(204146).addOnTalkEvent(questId);
		for (int mob: mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204146) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					if (isDaevanionArmorEquipped(player)) {
						return sendQuestDialog(env, 4762);
					} else {
						return sendQuestDialog(env, 4848);
					}
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);
			if (targetId == 204146) {
				switch (dialog) {
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						} else if (var == 2 && var1 == 60) {
							return sendQuestDialog(env, 1693);
						} else if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					} case CHECK_COLLECTED_ITEMS: {
						return checkQuestItems(env, 0, 1, false, 10000, 10001);
					} case SELECT_ACTION_2035: {
						int currentDp = player.getCommonData().getDp();
						int maxDp = player.getGameStats().getMaxDp().getCurrent();
						long burner = player.getInventory().getItemCountByItemId(186000040);
						if (currentDp == maxDp && burner >= 1) {
							removeQuestItem(env, 186000040, 1);
							player.getCommonData().setDp(0);
							changeQuestStep(env, 3, 3, true);
							return sendQuestDialog(env, 5);
						} else {
							return sendQuestDialog(env, 2120);
						}
					} case STEP_TO_2: {
						return defaultCloseDialog(env, 1, 2);
					} case STEP_TO_3: {
						qs.setQuestVar(3);
						updateQuestStatus(env);
						return sendQuestSelectionDialog(env);
					} case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204146) {
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
				switch (env.getTargetId()) {
					case 213011: // Meek Crestlich
					case 213012: // Meek Crestlich
					{ 
						if (A >= 0 && A < 30) {
							++A;
							ALL = C;
							ALL = ALL << 7;
							ALL += B;
							ALL = ALL << 7;
							ALL += A;
							ALL = ALL << 7;
							ALL += 2;
							qs.setQuestVar(ALL);
							updateQuestStatus(env);
						}
						break;
					}
					case 213009: // Blue Jaw Monitor
					case 213010: // Blue Jaw Monitor
					{
						if (B >= 0 && B < 30) {
							++B;
							ALL = C;
							ALL = ALL << 7;
							ALL += B;
							ALL = ALL << 7;
							ALL += A;
							ALL = ALL << 7;
							ALL += 2;
							qs.setQuestVar(ALL);
							updateQuestStatus(env);
						}
						break;
					}
					case 213007: // Plains Vespine
					case 213008: // Plains Vespine
					{ 
						if (C >= 0 && C < 30) {
							++C;
							ALL = C;
							ALL = ALL << 7;
							ALL += B;
							ALL = ALL << 7;
							ALL += A;
							ALL = ALL << 7;
							ALL += 2;
							qs.setQuestVar(ALL);
							updateQuestStatus(env);
						}
						break;
					}
				} if (qs.getQuestVarById(0) == 2 && A == 30 && B == 30 && C == 30) {
					qs.setQuestVarById(1, 60);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isDaevanionArmorEquipped(Player player) {
		int plate = player.getEquipment().itemSetPartsEquipped(9);
		int chain = player.getEquipment().itemSetPartsEquipped(8);
		int leather = player.getEquipment().itemSetPartsEquipped(7);
		int cloth = player.getEquipment().itemSetPartsEquipped(6);
		int gunslinger = player.getEquipment().itemSetPartsEquipped(378);
		if (plate == 5 || chain == 5 || leather == 5 || cloth == 5 || gunslinger == 5) {
			return true;
		}
		return false;
	}
}