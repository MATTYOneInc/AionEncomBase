package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.PacketLoggerService;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.TradeService;

public class CM_SELL_TERMINATED_ITEMS extends AionClientPacket {
	// private static final Logger log =
	// LoggerFactory.getLogger(CM_SELL_TERMINATED_ITEMS.class);
	private int size;
	private int itemObjectId;

	public CM_SELL_TERMINATED_ITEMS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		PacketLoggerService.getInstance().logPacketCM(this.getPacketName());
		Player player = getConnection().getActivePlayer();
		Storage inventory = player.getInventory();
		size = readH();
		for (int i = 0; i < size; i++) {
			itemObjectId = readD();
			TradeService.terminatedItemToShop(player, itemObjectId);
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();

		if (player == null) {
			return;
		}

	}
}
