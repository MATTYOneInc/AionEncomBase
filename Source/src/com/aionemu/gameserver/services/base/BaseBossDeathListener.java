/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.services.base;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.eventcallback.OnDieEventCallback;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dao.BaseDAO;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.landing.LandingPointsEnum;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.services.BaseService;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Rinzler
 * @reworked Ranastic
 */

@SuppressWarnings("rawtypes")
public class BaseBossDeathListener extends OnDieEventCallback
{
	private final Base<?> base;
	
	public BaseBossDeathListener(Base base) {
		this.base = base;
	}
	
	@Override
	public void onBeforeDie(AbstractAI obj) {
		Race race = null;
		Npc boss = base.getBoss();
		AionObject winner = base.getBoss().getAggroList().getMostDamage();
		if (winner instanceof Creature) {
			final Creature kill = (Creature) winner;
			applyBaseBuff();
			if (CustomConfig.ENABLE_BASE_REWARDS) {
				giveBaseRewardsToPlayers((Player) kill);
			} if (kill.getRace().isPlayerRace()) {
				base.setRace(kill.getRace());
				race = kill.getRace();
			}
			announceCapture(null, kill);
		} else if (winner instanceof TemporaryPlayerTeam) {
			final TemporaryPlayerTeam team = (TemporaryPlayerTeam) winner;
			applyBaseBuff();
			if (team.getRace().isPlayerRace()) {
				base.setRace(team.getRace());
				race = team.getRace();
			}
			announceCapture(team, null);
		} else {
		   base.setRace(Race.NPC);
		} if (base.getBaseLocation().getWorldId() == 400010000) {
			if (race == Race.ASMODIANS && boss.getRace() == Race.ELYOS) {
				AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
			} if (race == Race.ELYOS && boss.getRace() == Race.ASMODIANS) {
				AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
			}
			landingWinBase(race);
		}
		BaseService.getInstance().capture(base.getId(), base.getRace());
	}
	
	@Override
	public void onAfterDie(AbstractAI obj) {
	}
	
	public void announceCapture(final TemporaryPlayerTeam team, final Creature kill) {
        final String baseName = base.getBaseLocation().getName();
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                if (team != null && kill == null) {
					//%0 succeeded in conquering %1.
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1301039, team.getRace().getRaceDescriptionId(), baseName));
                } else {
					//%0 succeeded in conquering %1.
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1301039, kill.getRace().getRaceDescriptionId(), baseName));
                }
				//Abyss Landing 4.9.1
				switch (player.getWorldId()) {
				    case 400010000: //Reshanta.
				        if (team != null && kill == null) {
							//%0 has occupied %1 Base and the Landing is now enhanced.
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403186, team.getRace().getRaceDescriptionId(), baseName));
						} else {
							//%0 has occupied %1 Base and the Landing is now enhanced.
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403186, kill.getRace().getRaceDescriptionId(), baseName));
						}
					break;
				}
            }
        });
    }
	
	public void applyBaseBuff() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.getCommonData().getRace() == Race.ELYOS) {
				    SkillEngine.getInstance().applyEffectDirectly(12115, player, player, 0); //Kaisinel's Bane.
					//The power of Kaisinel's Protection surrounds you.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_WEAK_RACE_BUFF_LIGHT_GAIN, 5000);
					//Marchutan's Protection has strengthened the opposing faction.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_WEAK_RACE_BUFF_DARK_WARNING, 10000);
				} else if (player.getCommonData().getRace() == Race.ASMODIANS) {
				    SkillEngine.getInstance().applyEffectDirectly(12117, player, player, 0); //Marchutan's Bane.
					//The power of Marchutan's Protection surrounds you.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_WEAK_RACE_BUFF_DARK_GAIN, 5000);
					//Kaisinel's Protection has strengthened the opposing faction.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_WEAK_RACE_BUFF_LIGHT_WARNING, 10000);
				}
			}
		});
	}
	
	protected void giveBaseRewardsToPlayers(Player player) {
		switch (player.getWorldId()) {
			case 210020000: //Eltnen.
			case 210040000: //Heiron.
			case 210130000: //Inggison [Master Server].
			case 220020000: //Morheim.
			case 220040000: //Beluslan.
			case 220140000: //Gelkmaros [Master Server].
		        HTMLService.sendGuideHtml(player, "adventurers_base1");
			break;
			case 600090000: //Kaldor.
			case 600100000: //Levinshor.
		        HTMLService.sendGuideHtml(player, "adventurers_base2");
			break;
			case 400020000: //Belus.
			case 400040000: //Aspida.
			case 400050000: //Atanatos.
			case 400060000: //Disillon.
		        HTMLService.sendGuideHtml(player, "adventurers_base3");
			break;
			case 600050000: //Katalam.
			    HTMLService.sendGuideHtml(player, "adventurers_base4");
			break;
		}
	}
	
	public void landingWinBase(Race race) {
		if (race == Race.ASMODIANS) {
			AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
		} if (race == Race.ELYOS) {
			AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
		}
	}
	
	private BaseDAO getDAO() {
		return DAOManager.getDAO(BaseDAO.class);
	}
}