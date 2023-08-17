/*
 * This file is part of Encom.
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.archdaeva;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author (Encom)
/** Source KOR: https://www.youtube.com/watch?v=8Qt-ZODwhoA
/****/

public class _10521Memories_Of_Eternity extends QuestHandler
{
	public static final int questId = 10521;
	private final static int[] npcs = {806075, 806134, 806136, 806137, 703130, 703164, 731667, 731668, 731669};
	
	public _10521Memories_Of_Eternity() {
		super(questId);
	}
	
    @Override
    public void register() {
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnDie(questId);
		qe.registerOnLogOut(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(857783).addOnKillEvent(questId); //IDEternity_Q_Sado_Fi_N_65_An_01.
		qe.registerQuestNpc(857785).addOnKillEvent(questId); //IDEternity_Q_HD_Fire_Li_M_N_65_An.
		qe.registerQuestNpc(857792).addOnKillEvent(questId); //IDEternity_Q_HD_Fire_Li_F_N_65_An.
		qe.registerOnEnterZone(ZoneName.get("ID_ETERNITY_Q_SENSORYAREA_A_301570000"), questId);
		qe.registerOnEnterZone(ZoneName.get("ID_ETERNITY_Q_SENSORYAREA_B_301570000"), questId);
		qe.registerOnEnterZone(ZoneName.get("ID_ETERNITY_Q_SENSORYAREA_C_301570000"), questId);
    }
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 10520, true);
    }
	
    @Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806134) { //Ador.
				switch (env.getDialog()) {
					//Welcome, [%username].
					//Agent Viola mentioned you would have questions regarding this place.
					//I am here to help explain our efforts here, if you have the time to listen.
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					}
					//In all of Elysea, Iluma is the region closest to the Tower.
					//Naturally, as a precaution, access is heavily restricted and monitored.
					//We've established a base here: the site where the first tower fragment was discovered, and we are presently investigating other fragments nearby.
					//The massive fragment you see nearby is the first fragment that fell after the Aetheric Field weakened.
					//Lord Ariel's Agent Viola came here to protect the Tower of Eternity.
					//And that reminds me: she has a task for you regarding the tower fragment.
					case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					}
					//Go on.
					case STEP_TO_1: {
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 806075) { //Weatha.
				switch (env.getDialog()) {
					//Arieluma and the Lady's blessings upon you, [%username].
					//Did Sanctum Keeper Ador explain our purpose here ?
					//And he said you have a task for me.
					case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					}
					//Oh, yes.
					//There's something I need you to do for me, [%username].
					//I believe there is a link between the tower fragment Ador mentioned, and you, [%username].
					//I'd like you to investigate the tower fragment here in the Sanctuary.
					//I'm certain that you'll be able to discover something.
					case SELECT_ACTION_1353: {
						if (var == 1) {
							return sendQuestDialog(env, 1353);
						}
					}
					//Very well.
					case STEP_TO_2: {
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 703164) { //Tower Fragment
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 2) {
							playQuestMovie(env, 999);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									WorldMapInstance ArchivesOfEternity = InstanceService.getNextAvailableInstance(301570000);
									InstanceService.registerPlayerWithInstance(ArchivesOfEternity, player);
									TeleportService2.teleportTo(player, 301570000, ArchivesOfEternity.getInstanceId(), 737, 512, 469);
								}
							}, 20000);
							changeQuestStep(env, 2, 3, false);
							return closeDialogWindow(env);
						}
					}
                }
            } if (targetId == 731667) { //IDEternity_Q_FOBJ_Q10521_A
                switch (env.getDialog()) {
                    //Protector of Atreia
					//Since the Fifth Era, most people regarded Aion as little more than a structure, despite basking in the glorious light of the Tower of Eternity.
					//But there were many who remained deeply devoted to Aion.
					//A hero arose to lead the believers and this hero, by great feats of valor, won victory after victory in the war against the Balaur.
					//But the joy of these victories was short-lived.
					case USE_OBJECT: {
                        if (var == 5) {
							return sendQuestDialog(env, 2716);
						}
					}
					//"Protector of Atreia"
					//As the war continued, the followers of Aion weakened, while the Balaur grew in strength.
					//When all hope seemed lost before the Balaur onslaught, the hero's faith in Aion was rewarded: a pair of glowing wings unfurled from the hero's back, signaling the birth of the Protector of Atreia.
					//Facing the glorious light of Aion for the first time, the Balaur trembled in fear.
					//Since then, a Daeva ascended by an Empyrean Lord was called an ‘Archdaeva', a Protector of Atreia.
					case SELECT_ACTION_2717: {
						if (var == 5) {
							return sendQuestDialog(env, 2717);
						}
					}
					//(Finish reading.)
					case STEP_TO_6: {
                        changeQuestStep(env, 5, 6, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 731668) { //IDEternity_Q_FOBJ_Q10521_B
                switch (env.getDialog()) {
                    //"Empyrean Lord's Proposal"
					//The twelve Empyrean Lords assisted the Archdaeva.
					//Siel and Israphel, the Tower's guardians, created a colossal Aetheric Field around the Tower of Eternity, and the Balaur fled.
					//The Empyrean Lords chose great men and women from among the humans and ascended them into Daevas.
					//They built Temples in the north and south of Atreia to train these Daevas.
					//And thus, the war between Daevas and the Balaur continued for over a thousand years.
					case USE_OBJECT: {
                        if (var == 6) {
							return sendQuestDialog(env, 3057);
						}
					}
					//"Empyrean Lord's Proposal"
					//The seemingly endless war turned when Israphel made a proposal.
					//While walking inside the Tower of Eternity, Israpel came upon an immensely powerful artifact.
					//The artifact was connected with a great multitude of powers that existed within the Tower of Eternity, and it could harness these powers all at one time.
					//Israphel believed the artifact could be used to destroy the Balaur, and proposed a plan to annihilate the enemy to the Archdaeva and other Empyrean Lords.
					//They accepted this proposal, thinking it would bring about the downfall of the Balaur.
					//But little did they know that Israphel intended to betray them and become the sole Empyrean Lord of all Atreia.
					case SELECT_ACTION_3058: {
						if (var == 6) {
							return sendQuestDialog(env, 3058);
						}
					}
					//(Finish reading.)
					case STEP_TO_7: {
                        changeQuestStep(env, 6, 7, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 806136) { //IDEternity_Q_Leibo01_E
				switch (env.getDialog()) {
					//Go away. I'm busy, so don't bother me.
					//Anyway, who are you ?
					//Where are we ?
					case START_DIALOG: {
						if (var == 8) {
							return sendQuestDialog(env, 3739);
						}
					}
					//Wait. How did you get in here ?
					//Important records of the Aion Tower are kept here! Get out of here, Daeva!
					//Guards! There's a suspicious Daeva here! Guards!...
					case SELECT_ACTION_3740: {
						if (var == 8) {
							return sendQuestDialog(env, 3740);
						}
					}
					//Where are we ?
					case STEP_TO_9: {
						ThreadPoolManager.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								QuestService.addNewSpawn(301570000, player.getInstanceId(), 857948, (float) 446.12146, (float) 654.5927, (float) 468.97745, (byte) 19); //IDEternity_Q_Sado_Wi_65_An_02.
								QuestService.addNewSpawn(301570000, player.getInstanceId(), 857903, (float) 451.2063, (float) 654.0501, (float) 468.97745, (byte) 20); //IDEternity_Q_Sado_Wi_N_65_An_01.
								QuestService.addNewSpawn(301570000, player.getInstanceId(), 857948, (float) 453.82755, (float) 650.27997, (float) 468.97745, (byte) 19); //IDEternity_Q_Sado_Wi_65_An_02.
							}
						}, 3000);
						Npc npc = (Npc) env.getVisibleObject();
						npc.getController().onDelete();
						changeQuestStep(env, 8, 9, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 731669) { //IDEternity_Q_FOBJ_Q10521_C
                switch (env.getDialog()) {
                    //"Ancient Memory"
					//On the Day of Peace, the talks seemed to begin smoothly, without incident.
					//But upon the Archdaeva's signal, the gathered might of Aion roared in unison, and the land shook.
					//Throughout the sky the Archdaeva and Empyrean Lords were locked in a fierce battle with Balaur Lords.
					//During the tumult, Israphel stole away from the battle and activated the artifact.
					//But before the energy was unleashed, a shadow fell across Israphel.
					//It was Fregion, who had guessed Israphel's treachery. Fregion's attack destroyed the artifact and
					//caused a tremendous explosion of energy.
					//The Tower of Eternity's core collapsed during the explosion, and the energy of Atreia began to dissipate.
					case USE_OBJECT: {
                        if (var == 9) {
							return sendQuestDialog(env, 4080);
						}
					}
					//"Forgotten Memory"
					//The Archdaeva approached. A powerful light burst from the Tower as the Archdaeva recreated the Aetheric Field.
					//The Field separated the chaos from Atreia, and it seemed that the world would be saved.
					//But inside the Aetheric Field, where the Creator's power ravaged, the Archdaeva's body began to disintegrate.
					//At that moment, Siel appeared before the Archdaeva. Her eyes gleaming with determination and fear,
					//Siel used her power to stop time inside of the Aetheric Field.
					case SELECT_ACTION_4081: {
						if (var == 9) {
							return sendQuestDialog(env, 4081);
						}
					}
					//"Forgotten Memory"
					//You are the Protector of Atreia and the fate of Aion is in your hands.
					//It is true that the decisions we made brought about this tragedy, but it as Aion wills...
					//the destruction of Atreia is not in His plans.
					//Instead, when the time that was stopped flows once again, when Atreia faces grave danger and is in need of your power,
					//you will return to Atreia and protect the world once more, for that is your duty.
					case SELECT_ACTION_4166: {
						if (var == 9) {
							return sendQuestDialog(env, 4166);
						}
					}
					//(Finish examining)
					case STEP_TO_10: {
                        changeQuestStep(env, 9, 10, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 806137) { //IDEternity_Q_Leibo02_E
				switch (env.getDialog()) {
					//Oh! What!
					//You followed me all the way here ?
					//Who is the Protector of Atreia ?
					case START_DIALOG: {
						if (var == 11) {
							return sendQuestDialog(env, 6841);
						}
					}
					//Oh. I'm only a record keeper.
					//I don't know what's IN the records.
					//So stop following me!
					case SELECT_ACTION_6842: {
						if (var == 11) {
							return sendQuestDialog(env, 6842);
						}
					}
					//But what if I want to ?
					case STEP_TO_12: {
						ThreadPoolManager.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								QuestService.addNewSpawn(301570000, player.getInstanceId(), 857915, (float) 346.18872, (float) 516.0532, (float) 468.937, (byte) 119); //IDEternity_Q_Cube_As_65_An.
								QuestService.addNewSpawn(301570000, player.getInstanceId(), 857916, (float) 347.85834, (float) 511.8845, (float) 468.937, (byte) 0); //IDEternity_Q_Energy_Wi_65_An.
								QuestService.addNewSpawn(301570000, player.getInstanceId(), 857915, (float) 346.09894, (float) 507.7084, (float) 468.937, (byte) 119); //IDEternity_Q_Cube_As_65_An.
							}
						}, 3000);
						Npc npc = (Npc) env.getVisibleObject();
						npc.getController().onDelete();
						changeQuestStep(env, 11, 12, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 703130) { //IDEternity_Q_FOBJ_Q10521_D.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 12) {
							switch (player.getGender()) {
								case MALE:
							        PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 923));
									ThreadPoolManager.getInstance().schedule(new Runnable() {
										@Override
										public void run() {
											//You are graced with the aura of Blessed Breath.
											PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403364));
											QuestService.addNewSpawn(301570000, player.getInstanceId(), 857788, (float) 231.63109, (float) 511.9707, (float) 468.80215, (byte) 0); //IDEternity_Q_HD_Wind_Li_M_N_65_An.
										}
									}, 50000);
								break;
								case FEMALE:
									PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 924));
									ThreadPoolManager.getInstance().schedule(new Runnable() {
										@Override
										public void run() {
											//You are graced with the aura of Blessed Breath.
											PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403364));
											QuestService.addNewSpawn(301570000, player.getInstanceId(), 857795, (float) 231.63109, (float) 511.9707, (float) 468.80215, (byte) 0); //IDEternity_Q_HD_Wind_Li_F_N_65_An.
										}
									}, 50000);
								break;
							}
							changeQuestStep(env, 12, 13, false);
							return closeDialogWindow(env);
						}
					}
                }
            }
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            //You're back.
			//I don't know why, but you feel different somehow.
			//What happened with the investigation of the tower fragment ?
			//So it turns out there was this Archdaeva... and it may have been me...
			if (env.getDialog() == QuestDialog.USE_OBJECT) {
				return sendQuestDialog(env, 10002);
			} else {
				return sendQuestEndDialog(env);
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
			if (zoneName == ZoneName.get("ID_ETERNITY_Q_SENSORYAREA_A_301570000")) {
				if (var == 3) {
					changeQuestStep(env, 3, 4, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("ID_ETERNITY_Q_SENSORYAREA_B_301570000")) {
				if (var == 7) {
					changeQuestStep(env, 7, 8, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("ID_ETERNITY_Q_SENSORYAREA_C_301570000")) {
				if (var == 10) {
					changeQuestStep(env, 10, 11, false);
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
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 4) {
				return defaultOnKillEvent(env, 857783, 4, 5); //IDEternity_Q_Sado_Fi_N_65_An_01.
			} if (var == 13) {
				if (env.getTargetId() == 857785 || //IDEternity_Q_HD_Fire_Li_M_N_65_An.
				    env.getTargetId() == 857792) { //IDEternity_Q_HD_Fire_Li_F_N_65_An.
					changeQuestStep(env, 13, 14, false);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (player.getWorldId() == 210100000) { //Iluma.
			if (qs != null && qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVars().getQuestVars();
                if (var == 14) {
                    changeQuestStep(env, 14, 14, true);
					return true;
				}
			}
		}
        return false;
    }
	
	@Override
	public boolean onDieEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVars().getQuestVars();
			if (var >= 3 && var <= 13) {
				qs.setQuestVar(0);
                updateQuestStatus(env);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
				return true;
			}
		}
		return false;
	}
	
	@Override
    public boolean onLogOutEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var >= 3 && var <= 13) {
                qs.setQuestVar(0);
                updateQuestStatus(env);
                return true;
            }
        }
        return false;
    }
}