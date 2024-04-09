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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.sql.Timestamp;
import java.util.Collection;

import com.aionemu.gameserver.model.gameobjects.Minion;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.MinionCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34, FrozenKiller Reworked by G-Robson26
 */
public class SM_MINIONS extends AionServerPacket {

	private int actionId;
	private int functionId;
	private int subFunctionId;
	private Minion minion;

	private Collection<MinionCommonData> minions;
	private MinionCommonData minionsCommonData;
	private boolean isActing;
	private int lootNpcId;
	private int dopeItemId;
	private int dopeSlot;
	private int GrowthPoint;
	private Timestamp functionTime;
	private boolean isAutoRenewFunc;
	private boolean asMaterial;
	private int energy;


	public SM_MINIONS(int actionId, Collection<MinionCommonData> minions) {
		this.actionId = actionId;
		this.minions = minions;
	}

	public SM_MINIONS(int actionId, MinionCommonData minion) {
		this.actionId = actionId;
		this.minionsCommonData = minion;
	}

	public SM_MINIONS(int actionId, int functionId, MinionCommonData minion) {
		this.actionId = actionId;
		this.functionId = functionId;
		this.minionsCommonData = minion;
	}

	public SM_MINIONS(int actionId, int functionId) {
		this.actionId = actionId;
		this.functionId = functionId;
	}

	public SM_MINIONS(int actionId, MinionCommonData minion, boolean asMaterial) {
		this.actionId = actionId;
		this.minionsCommonData = minion;
		this.asMaterial = asMaterial;
	}

	public SM_MINIONS(int actionId, Minion minion) {
		this.actionId = actionId;
		this.minion = minion;
	}

	public SM_MINIONS(MinionCommonData minion, boolean isLooting) {
		this.actionId = 8;
		this.minionsCommonData = minion;
		this.isActing = isLooting;
		this.functionId = 1;
		this.subFunctionId = 0;
	}

	public SM_MINIONS(Npc lootNpc) {
		this.actionId = 8;
		this.functionId = 1;
		this.subFunctionId = 1;
		this.lootNpcId = lootNpc.getObjectId();
	}

	public SM_MINIONS(int action, MinionCommonData minionCommonData, int functionId, int subFunctionId, int itemId, int slot) {
		this.actionId = action;
		this.minionsCommonData = minionCommonData;
		this.functionId = functionId;
		this.subFunctionId = subFunctionId;
		this.dopeItemId = itemId;
		this.dopeSlot = slot;
	}

	public SM_MINIONS(int action, int energy, boolean isAutoRenewFunc) {
		this.actionId = action;
		this.energy = energy;
		this.isAutoRenewFunc = isAutoRenewFunc;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();

		if(player == null){
			return;
		}

		writeH(actionId);

		switch (actionId) {
			case 0: //Minion List
				writeC(0); // unk
				if (minions == null) {
					writeH(0);
				} else {
					writeH(minions.size());
					for (MinionCommonData minionCommonData : minions) {
						writeD(minionCommonData.getObjectId());
						writeD(minionCommonData.getMinionId());
						writeD(0); // unk
						writeD(minionCommonData.getMasterObjectId());
						writeD(minionCommonData.getMinionId());
						writeS(minionCommonData.getName());
						writeD(minionCommonData.getBirthday());
						writeD(0);
						writeD(minionCommonData.getGrowthPoint());
						writeC(minionCommonData.isLocked() ? 1 : 0);

						if (minionCommonData.getDopingBag() == null) {
							writeB(new byte[24]);
						} else {
							int[] scrollBag = minionCommonData.getDopingBag().getScrollsUsed();
							writeD(minionCommonData.getDopingBag().getFoodItem());
							writeD(minionCommonData.getDopingBag().getDrinkItem());
							if (scrollBag.length == 0) {
								writeB(new byte[16]);
							} else {
								writeD(scrollBag[0]);
								writeD(scrollBag.length > 1 ? scrollBag[1] : 0);
								writeD(scrollBag.length > 2 ? scrollBag[2] : 0);
								writeD(scrollBag.length > 3 ? scrollBag[3] : 0);
							}
						}
						writeC(0); //unk
					}
				}
				break;
			case 1: // Adopt Minion
				if (minionsCommonData == null) {
					return;
				}
				writeH(functionId);
				switch (functionId) {
					case 0: //Adopt
						writeD(0); // unk
						writeD(0); // unk
						writeD(minionsCommonData.getObjectId());
						writeD(182648);
						writeD(0); // unk
						writeD(minionsCommonData.getMasterObjectId());
						writeD(minionsCommonData.getMinionId());
						writeS(minionsCommonData.getName()); // unk
						writeD(minionsCommonData.getBirthday());
						writeB(new byte[34]);
						break;
					case 1:
						writeD(0); // unk
						writeD(0); // unk
						writeD(minionsCommonData.getObjectId());
						writeD(0);
						writeD(0); // unk
						writeD(minionsCommonData.getMasterObjectId());
						writeD(minionsCommonData.getMinionId());
						writeS(minionsCommonData.getName()); // unk
						writeD(minionsCommonData.getBirthday());
						writeB(new byte[34]);
						break;
					case 2:
					case 3:
						writeD(0); // unk
						writeD(0); // unk
						writeD(minionsCommonData.getObjectId());
						writeD(0);
						writeD(0); // unk
						writeD(minionsCommonData.getMasterObjectId());
						writeD(minionsCommonData.getMinionId());
						writeS(minionsCommonData.getName()); // unk
						writeD(minionsCommonData.getBirthday());
						writeB(new byte[34]);
						break;

				}
				break;
			case 2: // Delete minion
				if (minionsCommonData == null) {
					return;
				}
				writeH(asMaterial ? 1 : 0);
				writeD(minionsCommonData.getObjectId()); // object id
				break;
			case 3: // Rename
				if (minionsCommonData == null) {
					return;
				}
				writeD(minionsCommonData.getObjectId());
				writeS(minionsCommonData.getName()); // object id
				break;
			case 4: // Lock
				if (minionsCommonData == null) {
					return;
				}
				writeD(minionsCommonData.getObjectId());
				writeC(minionsCommonData.isLocked() ? 1 : 0);
				break;
			case 5: // Spawn Minion
				if (minion == null) {
					return;
				}
				writeS(minion.getName());
				writeD(minion.getObjectId());
				writeD(minion.getMinionId());
				writeD(minion.getMaster().getObjectId());
				break;
			case 6: // Dismiss
				if (minion == null) {
					return;
				}
				writeD(minion.getObjectId());
				if (minion.getMaster().getLifeStats().isAlreadyDead()) {
					writeC(0); // Disappearing effect
				} else {
					writeC(21); // Disappearing effect
				}
				break;
			case 7: // Growth point renewal
				if (minionsCommonData == null) {
					return;
				}
				writeD(minionsCommonData.getObjectId()); // object id
				writeD(minionsCommonData.getGrowthPoint()); // object id
				break;
			case 8: //Feature settings
				writeC(functionId);
				writeC(subFunctionId);

				if (functionId == 1) {
					if (subFunctionId == 0) {
						writeC(isActing ? 1 : 0);
					} else if(subFunctionId ==1) {
						writeD(lootNpcId);
					}
				} else if (functionId == 0) {
					switch (subFunctionId) {
						case 0:
							writeD(minionsCommonData.getObjectId());
							writeD(dopeItemId);
							writeD(dopeSlot);
							break;
						case 1:
							writeD(minionsCommonData.getObjectId());
							writeD(dopeSlot);
							break;
						case 2:
							break;
						case 3:
							writeD(minionsCommonData.getObjectId());
							writeD(dopeItemId);
							break;
					}
				}
				break;
			case 9: // Enable Use Function
				if(player.getCommonData().getMinionFunctionTime() != null){
					writeD((int) (player.getCommonData().getMinionFunctionTime().getTime() / 1000)); //Time
				}else{
					writeD(0); //Time
				}

				writeD(0);
			case 10: // Disable Use Function
				break;
			case 11: // Skill Points
				writeD(player.getCommonData().getMinionPoint());
				writeC(player.getCommonData().isMinionAutoCharge() ? 1 : 0);
				break;
			case 12://Auto Fuction
				writeC(isAutoRenewFunc ? 0 : 1);
				break;
			case 13: // Auto Renew
			case 14: // Auto Renew
				writeD(minion.getObjectId());
				writeH(0x14);
				writeC(0);
				break;
		}
	}

}