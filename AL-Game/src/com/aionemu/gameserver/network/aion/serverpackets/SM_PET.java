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

import java.util.Collection;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Pet;
import com.aionemu.gameserver.model.gameobjects.PetAction;
import com.aionemu.gameserver.model.gameobjects.player.PetCommonData;
import com.aionemu.gameserver.model.templates.pet.PetDopingEntry;
import com.aionemu.gameserver.model.templates.pet.PetFunctionType;
import com.aionemu.gameserver.model.templates.pet.PetTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_PET extends AionServerPacket {
	private int actionId;
	private Pet pet;
	private PetCommonData commonData;
	private int itemObjectId;
	private Collection<PetCommonData> pets;
	private int count;
	private int subType;
	private int shuggleEmotion;
	private boolean isActing;
	private int lootNpcId;
	private int dopeAction;
	private int dopeSlot;

	public SM_PET(int subType, int actionId, int objectId, int count, Pet pet) {
		this.subType = subType;
		this.actionId = actionId;
		this.count = count;
		this.itemObjectId = objectId;
		this.pet = pet;
		this.commonData = pet.getCommonData();
	}

	public SM_PET(int actionId) {
		this.actionId = actionId;
	}

	public SM_PET(int actionId, Pet pet) {
		this(0, actionId, 0, 0, pet);
	}

	public SM_PET(boolean isLooting) {
		this.actionId = 13;
		this.isActing = isLooting;
		this.subType = 3;
	}

	public SM_PET(boolean isLooting, int npcId) {
		this(isLooting);
		this.lootNpcId = npcId;
	}

	public SM_PET(int dopeAction, boolean isBuffing) {
		this.actionId = 13;
		this.dopeAction = dopeAction;
		this.isActing = isBuffing;
		this.subType = 2;
	}

	public SM_PET(boolean isCheering, int what, int wahtwaht) {
		this.actionId = 13;
		this.isActing = isCheering;
		this.subType = 5;
	}

	public SM_PET(int dopeAction, int itemId, int slot) {
		this(dopeAction, true);
		itemObjectId = itemId;
		dopeSlot = slot;
	}

	public SM_PET(Pet pet, int subType, int shuggleEmotion) {
		this(0, PetAction.MOOD.getActionId(), 0, 0, pet);
		this.shuggleEmotion = shuggleEmotion;
		this.subType = subType;
	}

	public SM_PET(int actionId, PetCommonData commonData) {
		this.actionId = actionId;
		this.commonData = commonData;
	}

	public SM_PET(int actionId, Collection<PetCommonData> pets) {
		this.actionId = actionId;
		this.pets = pets;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		PetTemplate petTemplate = null;
		writeH(actionId);
		switch (actionId) {
		case 0:
			writeC(0);
			writeH(pets.size());
			for (PetCommonData petCommonData : pets) {
				petTemplate = DataManager.PET_DATA.getPetTemplate(petCommonData.getPetId());
				int expireTime = petCommonData.getExpireTime();
				writeS(petCommonData.getName());
				writeD(petCommonData.getPetId());
				writeD(petCommonData.getObjectId());
				writeD(petCommonData.getMasterObjectId());
				writeD(0);
				writeD(0);
				writeD((int) petCommonData.getBirthday());
				writeD(expireTime != 0 ? expireTime - (int) (System.currentTimeMillis() / 1000) : 0);
				int specialtyCount = 0;
				if (petTemplate.ContainsFunction(PetFunctionType.WAREHOUSE)) {
					writeH(PetFunctionType.WAREHOUSE.getId());
					specialtyCount++;
				}
				if (petTemplate.ContainsFunction(PetFunctionType.LOOT)) {
					writeH(PetFunctionType.LOOT.getId());
					writeC(0);
					specialtyCount++;
				}
				if (petTemplate.ContainsFunction(PetFunctionType.CHEER)) {
					writeH(PetFunctionType.CHEER.getId());
					short cheer = (short) petTemplate.getPetFunction(PetFunctionType.CHEER).getId();
					writeH(cheer);
					specialtyCount++;
				}
				if (petTemplate.ContainsFunction(PetFunctionType.MERCHAND)) {
					writeH(PetFunctionType.MERCHAND.getId());
					short merchant = (short) petTemplate.getPetFunction(PetFunctionType.MERCHAND).getId();
					writeH(merchant);
					writeC(0x00);
					specialtyCount++;
				}
				if (petTemplate.ContainsFunction(PetFunctionType.DOPING)) {
					writeH(PetFunctionType.DOPING.getId());
					short dopeId = (short) petTemplate.getPetFunction(PetFunctionType.DOPING).getId();
					PetDopingEntry dope = DataManager.PET_DOPING_DATA.getDopingTemplate(dopeId);
					writeD(dope.isUseFood() ? petCommonData.getDopingBag().getFoodItem() : 0);
					writeD(dope.isUseDrink() ? petCommonData.getDopingBag().getDrinkItem() : 0);
					int[] scrollBag = petCommonData.getDopingBag().getScrollsUsed();
					if (scrollBag.length == 0) {
						writeQ(0);
						writeQ(0);
						writeQ(0);
					} else {
						writeD(scrollBag[0]);
						writeD(scrollBag.length > 1 ? scrollBag[1] : 0);
						writeD(scrollBag.length > 2 ? scrollBag[2] : 0);
						writeD(scrollBag.length > 3 ? scrollBag[3] : 0);
						writeD(scrollBag.length > 4 ? scrollBag[4] : 0);
						writeD(scrollBag.length > 5 ? scrollBag[5] : 0);
					}
					specialtyCount++;
				}
				if (petTemplate.ContainsFunction(PetFunctionType.FOOD)) {
					writeH(PetFunctionType.FOOD.getId());
					writeD(petCommonData.getFeedProgress().getDataForPacket());
					writeD((int) petCommonData.getTime() / 1000);
					specialtyCount++;
				}
				if (specialtyCount == 0) {
					writeH(PetFunctionType.NONE.getId());
					writeH(PetFunctionType.NONE.getId());
				} else if (specialtyCount == 1) {
					writeH(PetFunctionType.NONE.getId());
				}
				writeH(PetFunctionType.APPEARANCE.getId());
				writeC(0);
				writeC(0);
				writeC(0);
				writeD(petCommonData.getDecoration());
				writeD(0);
				writeD(0);
			}
			break;
		case 1:
			writeS(commonData.getName());
			writeD(commonData.getPetId());
			writeD(commonData.getObjectId());
			writeD(commonData.getMasterObjectId());
			writeD(0);
			writeD(0);
			writeD(commonData.getBirthday());
			writeD(commonData.getExpireTime() != 0
					? commonData.getExpireTime() - (int) (System.currentTimeMillis() / 1000)
					: 0);
			petTemplate = DataManager.PET_DATA.getPetTemplate(commonData.getPetId());
			int specialtyCount = 0;
			if (petTemplate.ContainsFunction(PetFunctionType.WAREHOUSE)) {
				writeH(PetFunctionType.WAREHOUSE.getId());
				specialtyCount++;
			}
			if (petTemplate.ContainsFunction(PetFunctionType.LOOT)) {
				writeH(PetFunctionType.LOOT.getId());
				writeC(0);
				specialtyCount++;
			}
			if (petTemplate.ContainsFunction(PetFunctionType.DOPING)) {
				writeH(PetFunctionType.DOPING.getId());
				writeQ(0);
				writeQ(0);
				writeQ(0);
				writeQ(0);
				specialtyCount++;
			}
			if (petTemplate.ContainsFunction(PetFunctionType.FOOD)) {
				writeH(PetFunctionType.FOOD.getId());
				writeQ(0);
				specialtyCount++;
			}
			if (specialtyCount == 0) {
				writeH(PetFunctionType.NONE.getId());
				writeH(PetFunctionType.NONE.getId());
			} else if (specialtyCount == 1) {
				writeH(PetFunctionType.NONE.getId());
			}
			writeH(PetFunctionType.APPEARANCE.getId());
			writeC(0);
			writeC(0);
			writeC(0);
			writeD(commonData.getDecoration());
			writeD(0);
			writeD(0);
			break;
		case 2:
			writeD(commonData.getPetId());
			writeD(commonData.getObjectId());
			writeD(0);
			writeD(0);
			break;
		case 3:
			writeS(pet.getName());
			writeD(pet.getPetId());
			writeD(pet.getObjectId());
			if (pet.getPosition().getX() == 0 && pet.getPosition().getY() == 0 && pet.getPosition().getZ() == 0) {
				writeF(pet.getMaster().getX());
				writeF(pet.getMaster().getY());
				writeF(pet.getMaster().getZ());
				writeF(pet.getMaster().getX());
				writeF(pet.getMaster().getY());
				writeF(pet.getMaster().getZ());
				writeC(pet.getMaster().getHeading());
			} else {
				writeF(pet.getPosition().getX());
				writeF(pet.getPosition().getY());
				writeF(pet.getPosition().getZ());
				writeF(pet.getMoveController().getTargetX2());
				writeF(pet.getMoveController().getTargetY2());
				writeF(pet.getMoveController().getTargetZ2());
				writeC(pet.getHeading());
			}
			writeD(pet.getMaster().getObjectId());
			writeC(1);
			writeD(0);
			writeD(pet.getCommonData().getDecoration());
			writeD(0);
			writeD(0);
			break;
		case 4:
			writeD(pet.getObjectId());
			writeC(0x01);
			break;
		case 9:
			writeH(1);
			writeC(1);
			writeC(subType);
			switch (subType) {
			case 1:
				writeD(commonData.getFeedProgress().getDataForPacket());
				writeD(0);
				writeD(itemObjectId);
				writeD(count);
				break;
			case 2:
				writeD(commonData.getFeedProgress().getDataForPacket());
				writeD(0);
				writeD(itemObjectId);
				writeD(count);
				writeC(0);
				break;
			case 3:
			case 4:
			case 5:
				writeD(commonData.getFeedProgress().getDataForPacket());
				writeD((int) commonData.getTime() / 1000);
				break;
			case 6:
				writeD(commonData.getFeedProgress().getDataForPacket());
				writeD(0);
				writeD(itemObjectId);
				writeC(0);
				break;
			case 7:
				writeD(commonData.getFeedProgress().getDataForPacket());
				writeD((int) commonData.getTime() / 1000);
				writeD(itemObjectId);
				writeD(0);
				break;
			case 8:
				writeD(commonData.getFeedProgress().getDataForPacket());
				writeD((int) commonData.getTime() / 1000);
				writeD(itemObjectId);
				writeD(count);
				break;
			}
			break;
		case 10:
			writeD(pet.getObjectId());
			writeS(pet.getName());
			break;
		case 12:
			switch (subType) {
			case 0:
				writeC(subType);
				if (commonData.getLastSentPoints() < commonData.getMoodPoints(true)) {
					writeD(commonData.getMoodPoints(true) - commonData.getLastSentPoints());
				} else {
					writeD(0);
					commonData.setLastSentPoints(commonData.getMoodPoints(true));
				}
				break;
			case 2:
				writeC(subType);
				writeD(0);
				writeD(pet.getCommonData().getMoodPoints(true));
				writeD(shuggleEmotion);
				commonData.setLastSentPoints(pet.getCommonData().getMoodPoints(true));
				commonData.setMoodCdStarted(System.currentTimeMillis());
				break;
			case 3:
				writeC(subType);
				writeD(pet.getPetTemplate().getConditionReward());
				commonData.setGiftCdStarted(System.currentTimeMillis());
				break;
			case 4:
				writeC(subType);
				writeD(commonData.getMoodPoints(true));
				writeD(commonData.getMoodRemainingTime());
				writeD(commonData.getGiftRemainingTime());
				commonData.setLastSentPoints(pet.getCommonData().getMoodPoints(true));
				break;
			}
			break;
		case 13:
			writeC(subType);
			if (subType == 2) {
				writeC(dopeAction);
				switch (dopeAction) {
				case 0:
					writeD(itemObjectId);
					writeD(dopeSlot);
					break;
				case 1:
					writeD(0);
					break;
				case 2:
					break;
				case 3:
					writeD(itemObjectId);
					break;
				}
			} else if (subType == 3) {
				if (lootNpcId > 0) {
					writeC(isActing ? 1 : 2);
					writeD(lootNpcId);
				} else {
					writeC(0);
					writeC(isActing ? 1 : 0);
				}
			} else if (subType == 4) {
				writeC(0);
				writeC(isActing ? 1 : 0);
			} else if (subType == 5) {
				writeC(isActing ? 0 : 1);
			}
			break;
		default:
			break;
		}
	}
}