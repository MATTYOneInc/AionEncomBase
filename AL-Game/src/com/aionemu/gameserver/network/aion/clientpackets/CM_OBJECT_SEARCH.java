package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnSearchResult;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SHOW_NPC_ON_MAP;
import com.aionemu.gameserver.services.teleport.TeleportService2;

public class CM_OBJECT_SEARCH extends AionClientPacket {

	private int npcId;

	public CM_OBJECT_SEARCH(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);

	}

	@Override
	protected void readImpl() {
		this.npcId = readD();
	}

	@Override
	protected void runImpl() {
		SpawnSearchResult searchResult = DataManager.SPAWNS_DATA2.getFirstSpawnByNpcId(0, npcId);
		Player player = getConnection().getActivePlayer();
		if (searchResult != null) {
			sendPacket(new SM_SHOW_NPC_ON_MAP(npcId, searchResult.getWorldId(), searchResult.getSpot().getX(),
					searchResult.getSpot().getY(), searchResult.getSpot().getZ()));
			if (player.isGM()) {
				TeleportService2.teleportToNpc(player, this.npcId);
			}
		}
	}
}
