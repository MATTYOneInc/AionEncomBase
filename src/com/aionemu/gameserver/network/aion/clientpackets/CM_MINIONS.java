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
package com.aionemu.gameserver.network.aion.clientpackets;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.MinionAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.actions.AbstractItemAction;
import com.aionemu.gameserver.model.templates.item.actions.AdoptMinionAction;
import com.aionemu.gameserver.model.templates.item.actions.ItemActions;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.minion.MinionService;
import com.aionemu.gameserver.services.toypet.PetSpawnService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Falke_34, FrozenKiller Reworked by G-Robson26
 */
public class CM_MINIONS extends AionClientPacket {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CM_MINIONS.class);
	List<Integer> material = new ArrayList<>();
	private int actionId;
	private int subAction;
	private int functId;
	private MinionAction action;
	private int isAuto;
	private int minionObjectId;
	private int ItemObjectId;
	private int dopingSlot;
	private int dopingItemId;
	private int activateLoot;
	private int Upgradeslot;
	private int Upgradeslot2;
	private int Upgradeslot3;
	private int Upgradeslot4;
	private int growthtarget;
	private int growthtarget2;
	private int growthtarget3;
	private int growthtarget4;
	private int growthtarget5;
	private int growthtarget6;
	private int growthtarget7;
	private int growthtarget8;
	private int growthtarget9;
	private int growthtarget10;
	private String rename = "";
	private int lock;

	public CM_MINIONS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		actionId = readH();
		action = MinionAction.getActionById(actionId);
		switch (action) {
			case ADOPT:
				ItemObjectId = readD();
				break;
			case RENAME:
				minionObjectId = readD();
				rename = readS();
				break;
			case DELETE:
				minionObjectId = readD();
				break;
			case LOCK:
				minionObjectId = readD();
				lock = readC();
				break;
			case SPAWN:
			case DISMISS:
				minionObjectId = readD();
				break;
			case COMBINE:
				Upgradeslot = readD();
				Upgradeslot2 = readD();
				Upgradeslot3 = readD();
				Upgradeslot4 = readD();
				break;
			case GROWTH:
				material.clear();
				minionObjectId = readD();
				growthtarget = readD();
				growthtarget2 = readD();
				growthtarget3 = readD();
				growthtarget4 = readD();
				growthtarget5 = readD();
				growthtarget6 = readD();
				growthtarget7 = readD();
				growthtarget8 = readD();
				growthtarget9 = readD();
				growthtarget10 = readD();

				material.add(growthtarget);
				material.add(growthtarget2);
				material.add(growthtarget3);
				material.add(growthtarget4);
				material.add(growthtarget5);
				material.add(growthtarget6);
				material.add(growthtarget7);
				material.add(growthtarget8);
				material.add(growthtarget9);
				material.add(growthtarget10);
				break;
			case USE_FUNCTION:
			case STOP_FUNCTION:
				break;
			case FUNCTION_RENEW: //Auto Function
				isAuto = readC();
			case CHARGE:
				break;
			case EVOLVE:
				minionObjectId = readD();
				break;
			case SET_FUNCTION:
				subAction = readD();
				switch (subAction){
					case 0:
						functId = readD();
						if(functId == 0){
							minionObjectId = readD();
							dopingItemId = readD();
							dopingSlot = readD();
						}else if(functId == 1){
							minionObjectId = readD();
							dopingSlot = readD();
							readD();
						}else if(functId == 3){
							minionObjectId = readD();
							dopingItemId = readD();
							readD();
						}
						break;
					case 1:
						minionObjectId = readD();
						activateLoot = readD();
						readD();
						readD();
						break;
				}
				break;
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}

		switch (action) {
			case ADOPT:
				if (player.getMinionList().getMinions().size() >= 200) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404322));
					return;
				}
				Item item = player.getInventory().getItemByObjId(ItemObjectId);
				ItemActions itemActions = item.getItemTemplate().getActions();
				player.getObserveController().notifyItemuseObservers(item);
				for (AbstractItemAction itemAction : itemActions.getItemActions()) {
					if (itemAction instanceof AdoptMinionAction) {
						AdoptMinionAction action = (AdoptMinionAction) itemAction;
						action.act(player, item, item);

					}
				}
				break;
			case RENAME:
				MinionService.getInstance().renameMinion(player, minionObjectId, rename);
				break;
			case DELETE:
				MinionService.getInstance().deleteMinion(player, minionObjectId, false);
				break;
			case LOCK:
				MinionService.getInstance().lockMinion(player, minionObjectId, lock);
				break;
			case SPAWN:
				if (player.getPet() != null) {
					PetSpawnService.dismissPet(player, true);
				}
				MinionService.getInstance().summonMinion(player, minionObjectId, true);
				break;
			case DISMISS:
				MinionService.getInstance().dismissMinion(player,  true);
				break;
			case COMBINE:
				MinionService.getInstance().synthesisMinion(player, Upgradeslot, Upgradeslot2, Upgradeslot3, Upgradeslot4);
				break;
			case GROWTH:
				MinionService.getInstance().growthMinion(player, minionObjectId, material);
				break;
			case CHARGE:
				MinionService.getInstance().chargeSkillPoint(player, false);
				break;
			case SET_FUNCTION:
				if(subAction ==0){
					if(functId == 0){ //add Doping
						MinionService.getInstance().useDoping(player, minionObjectId, functId, dopingItemId, dopingSlot);
					}else if(functId == 1){ //remove doping
						MinionService.getInstance().useDoping(player, minionObjectId, functId, dopingItemId, dopingSlot);
					}else if(functId == 3){ //Buff doping
						MinionService.getInstance().useDoping(player, minionObjectId, functId, dopingItemId, dopingSlot);
					}
				}else if(subAction ==1){
					if(activateLoot == 0){
						MinionService.getInstance().activateLoot(player, minionObjectId, false);
					}else{
						MinionService.getInstance().activateLoot(player, minionObjectId, true);
					}
				}
				break;
			case USE_FUNCTION:
				MinionService.getInstance().useFunction(player);
				break;
			case STOP_FUNCTION:
				MinionService.getInstance().useFunction(player);
				break;
			case FUNCTION_RENEW:
				MinionService.getInstance().autoFunction(player, isAuto != 0);
				break;
			case EVOLVE:
				MinionService.getInstance().evolutionMinion(player, minionObjectId);
				break;
		}
	}
}