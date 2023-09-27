/*

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
package com.aionemu.gameserver.services.abyss;

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

public class AbyssService
{
	private static final int[] abyssMapList = {
	////***////
	210020000, //Elten.
	210040000, //Heiron.
	210130000, //Inggison [Master Server].
	210060000, //Theobomos.
	210070000, //Cygnea.
	210090000, //Idian Depths.
	210100000, //Iluma.
	////***////
	220020000, //Morheim.
	220040000, //Beluslan.
	220050000, //Brusthonin.
	220140000, //Gelkmaros [Master Server].
	220080000, //Enshar.
	220100000, //Idian Depths.
	220110000, //Norvsvold.
	////***////
	400010000, //Reshanta.
	//Panesterra//
	400020000, //Belus.
	400040000, //Aspida.
	400050000, //Atanatos.
	400060000, //Disillon.
	////***////
	600110000, //Silentera Canyon [Master Server].
	////4.7////
	600090000, //Kaldor.
	600100000, //Levinshor.
	////5.8////
	600040000, //Tiamaranta's Eye.
	600041000}; //Tiamaranta's Eye [Master Server].
	
	public static final boolean isOnPvpMap(Player player) {
		for (int i: abyssMapList) {
			if (i == player.getWorldId()) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}
	
	public static final void rankedKillAnnounce(final Player victim) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player p) {
				if (p != victim && victim.getWorldId() == p.getWorldId()) {
					PacketSendUtility.sendPacket(p, SM_SYSTEM_MESSAGE.STR_ABYSS_ORDER_RANKER_DIE(victim, AbyssRankEnum.getRankDescriptionId(victim)));
				}
			}
		});
	}
	
	public static final void rankerSkillAnnounce(final Player player, final int nameId) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player p) {
				if (p != player && player.getWorldType() == p.getWorldType() && !p.isInInstance()) {
					PacketSendUtility.sendPacket(p, SM_SYSTEM_MESSAGE.STR_SKILL_ABYSS_SKILL_IS_FIRED(player, new DescriptionId(nameId)));
				}
			}
		});
	}
}