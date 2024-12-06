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
package admincommands;

import com.aionemu.gameserver.model.gameobjects.Gatherable;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticObject;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapType;
import com.aionemu.gameserver.world.knownlist.Visitor;

public class ReloadSpawn extends AdminCommand
{
	public ReloadSpawn() {
		super("reload_spawn");
	}
	
	@Override
	public void execute(Player player, String... params) {
		int worldId;
		String destination;
		worldId = 0;
		destination = "null";
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(player, "syntax //reload_spawn <location name | all>");
		}
		else {
			StringBuilder sbDestination = new StringBuilder();
			for(String p : params)
				sbDestination.append(p + " ");
		
			destination = sbDestination.toString().trim();
		//ELYOS.
		if (destination.equalsIgnoreCase("Sanctum"))
			worldId = WorldMapType.SANCTUM.getId();
		else if (destination.equalsIgnoreCase("Kaisinel"))
			worldId = WorldMapType.KAISINEL.getId();
		else if (destination.equalsIgnoreCase("Academy"))
			worldId = WorldMapType.KAISINEL_ACADEMY.getId();
		else if (destination.equalsIgnoreCase("Abbey1"))
			worldId = WorldMapType.WISPLIGHT_ABBEY.getId();
		else if (destination.equalsIgnoreCase("Poeta"))
			worldId = WorldMapType.POETA.getId();
		else if (destination.equalsIgnoreCase("Verteron"))
			worldId = WorldMapType.VERTERON.getId();
		else if (destination.equalsIgnoreCase("Eltnen"))
			worldId = WorldMapType.ELTNEN.getId();
		else if (destination.equalsIgnoreCase("Theobomos"))
			worldId = WorldMapType.THEOBOMOS.getId();
		else if (destination.equalsIgnoreCase("Heiron"))
			worldId = WorldMapType.HEIRON.getId();
		else if (destination.equalsIgnoreCase("Inggison"))
			worldId = WorldMapType.INGGISON_MASTER.getId();
		else if (destination.equalsIgnoreCase("Cygnea"))
			worldId = WorldMapType.CYGNEA.getId();
		else if (destination.equalsIgnoreCase("Idian_L"))
			worldId = WorldMapType.IDIAN_DEPTHS_L.getId();
		else if (destination.equalsIgnoreCase("Iluma"))
			worldId = WorldMapType.ILUMA.getId();
		else if (destination.equalsIgnoreCase("Tower_Of_Eternity_E"))
			worldId = WorldMapType.TOWER_OF_ETERNITY_E.getId();
		//ASMODIANS.
		else if  (destination.equalsIgnoreCase("Pandaemonium"))
			worldId = WorldMapType.PANDAEMONIUM.getId();
		else if (destination.equalsIgnoreCase("Marchutan"))
			worldId = WorldMapType.MARCHUTAN.getId();
		else if (destination.equalsIgnoreCase("Priory"))
			worldId = WorldMapType.MARCHUTAN_PRIORY.getId();
		else if (destination.equalsIgnoreCase("Abbey2"))
			worldId = WorldMapType.FATEBOUND_ABBEY.getId();
		else if (destination.equalsIgnoreCase("Ishalgen"))
			worldId = WorldMapType.ISHALGEN.getId();
		else if (destination.equalsIgnoreCase("Altgard"))
			worldId = WorldMapType.ALTGARD.getId();
		else if (destination.equalsIgnoreCase("Morheim"))
			worldId = WorldMapType.MORHEIM.getId();
		else if (destination.equalsIgnoreCase("Brusthonin"))
			worldId = WorldMapType.BRUSTHONIN.getId();
		else if (destination.equalsIgnoreCase("Beluslan"))
			worldId = WorldMapType.BELUSLAN.getId();
		else if (destination.equalsIgnoreCase("Gelkmaros"))
			worldId = WorldMapType.GELKMAROS_MASTER.getId();
		else if (destination.equalsIgnoreCase("Enshar"))
			worldId = WorldMapType.ENSHAR.getId();
		else if (destination.equalsIgnoreCase("Idian_D"))
			worldId = WorldMapType.IDIAN_DEPTHS_D.getId();
		else if (destination.equalsIgnoreCase("Norsvold"))
			worldId = WorldMapType.NORSVOLD.getId();
		else if (destination.equalsIgnoreCase("Tower_Of_Eternity_A"))
			worldId = WorldMapType.TOWER_OF_ETERNITY_A.getId();
		//Other Zone
		else if (destination.equalsIgnoreCase("Silentera"))
			worldId = WorldMapType.SILENTERA_CANYON_MASTER.getId();
		else if (destination.equalsIgnoreCase("Kaldor"))
			worldId = WorldMapType.KALDOR.getId();
		else if (destination.equalsIgnoreCase("Levinshor"))
			worldId = WorldMapType.LEVINSHOR.getId();
		//Reshanta
		else if (destination.equalsIgnoreCase("Reshanta"))
			worldId = WorldMapType.RESHANTA.getId();
		//Panesterra
		else if (destination.equalsIgnoreCase("Belus"))
			worldId = WorldMapType.BELUS.getId();
		else if (destination.equalsIgnoreCase("Aspida"))
			worldId = WorldMapType.ASPIDA.getId();
		else if (destination.equalsIgnoreCase("Atanatos"))
			worldId = WorldMapType.ATANATOS.getId();
		else if (destination.equalsIgnoreCase("Disillon"))
			worldId = WorldMapType.DISILLON.getId();
		//Housing
		else if (destination.equalsIgnoreCase("Oriel"))
			worldId = WorldMapType.ORIEL.getId();
		else if (destination.equalsIgnoreCase("Pernon"))
			worldId = WorldMapType.PERNON.getId();
		else if (destination.equalsIgnoreCase("All"))
			worldId = 0;
		else
			PacketSendUtility.sendMessage(player, "Could not find the specified map !");
		}
		final String destinationMap = destination;
		if (destination.equalsIgnoreCase("All")) {
			//ELYOS.
			reloadMap(WorldMapType.SANCTUM.getId(), player, "Sanctum");
			reloadMap(WorldMapType.KAISINEL.getId(), player, "Kaisinel");
			reloadMap(WorldMapType.KAISINEL_ACADEMY.getId(), player, "Academy");
			reloadMap(WorldMapType.WISPLIGHT_ABBEY.getId(), player, "Wisplight");
			reloadMap(WorldMapType.POETA.getId(), player, "Poeta");
			reloadMap(WorldMapType.VERTERON.getId(), player, "Verteron");
			reloadMap(WorldMapType.ELTNEN.getId(), player, "Eltnen");
			reloadMap(WorldMapType.THEOBOMOS.getId(), player, "Theobomos");
			reloadMap(WorldMapType.HEIRON.getId(), player, "Heiron");
			reloadMap(WorldMapType.INGGISON_MASTER.getId(), player, "Inggison");
			reloadMap(WorldMapType.CYGNEA.getId(), player, "Cygnea");
			reloadMap(WorldMapType.IDIAN_DEPTHS_L.getId(), player, "Idian_L");
			reloadMap(WorldMapType.ILUMA.getId(), player, "Iluma");
			reloadMap(WorldMapType.TOWER_OF_ETERNITY_E.getId(), player, "Tower_Of_Eternity_E");
			//ASMODIANS.
			reloadMap(WorldMapType.PANDAEMONIUM.getId(), player, "Pandaemonium");
			reloadMap(WorldMapType.MARCHUTAN.getId(), player, "Marchutan");
			reloadMap(WorldMapType.MARCHUTAN_PRIORY.getId(), player, "Priory");
			reloadMap(WorldMapType.FATEBOUND_ABBEY.getId(), player, "Fatebound");
			reloadMap(WorldMapType.ISHALGEN.getId(), player, "Ishalgen");
			reloadMap(WorldMapType.ALTGARD.getId(), player, "Altgard");
			reloadMap(WorldMapType.MORHEIM.getId(), player, "Morheim");
			reloadMap(WorldMapType.BRUSTHONIN.getId(), player, "Brusthonin");
			reloadMap(WorldMapType.BELUSLAN.getId(), player, "Beluslan");
			reloadMap(WorldMapType.GELKMAROS_MASTER.getId(), player, "Gelkmaros");
			reloadMap(WorldMapType.ENSHAR.getId(), player, "Enshar");
			reloadMap(WorldMapType.IDIAN_DEPTHS_D.getId(), player, "Idian_D");
			reloadMap(WorldMapType.NORSVOLD.getId(), player, "Norsvold");
			reloadMap(WorldMapType.TOWER_OF_ETERNITY_A.getId(), player, "Tower_Of_Eternity_A");
			//Reshanta
			reloadMap(WorldMapType.RESHANTA.getId(), player, "Reshanta");
			//Panesterra
			reloadMap(WorldMapType.BELUS.getId(), player, "Belus");
			reloadMap(WorldMapType.ASPIDA.getId(), player, "Aspida");
			reloadMap(WorldMapType.ATANATOS.getId(), player, "Atanatos");
			reloadMap(WorldMapType.DISILLON.getId(), player, "Disillon");
			//Other Zone
			reloadMap(WorldMapType.SILENTERA_CANYON_MASTER.getId(), player, "Silentera");
			reloadMap(WorldMapType.KALDOR.getId(), player, "Kaldor");
			reloadMap(WorldMapType.LEVINSHOR.getId(), player, "Levinshor");
			//Housing
			reloadMap(WorldMapType.ORIEL.getId(), player, "Oriel");
			reloadMap(WorldMapType.PERNON.getId(), player, "Pernon");
		} else {	
			reloadMap(worldId, player, destinationMap);
		}
	}
	
	private void reloadMap (int worldId, Player admin, String destinationMap) {
		final int IdWorld = worldId;
		final Player adm = admin;
		final String dest = destinationMap;
		if (IdWorld != 0) {
			World.getInstance().doOnAllObjects(new Visitor<VisibleObject>() {
				@Override
				public void visit(VisibleObject object) {
					if (object.getWorldId() != IdWorld) {
						return;
					} if (object instanceof Npc || object instanceof Gatherable || object instanceof StaticObject) {
						object.getController().delete();
					}
				}
			});
			SpawnEngine.spawnWorldMap(IdWorld);
			PacketSendUtility.sendMessage(adm, "Spawns for map: " + IdWorld + " (" + dest + ") reloaded succesfully");
		}
	}
	
	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //reload_spawn <location name | all>");
	}
}