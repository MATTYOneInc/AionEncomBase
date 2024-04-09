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
package com.aionemu.gameserver.model.gameobjects.player;

import java.sql.Timestamp;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerMinionsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.minion.MinionDopingBag;
import com.aionemu.gameserver.model.templates.minion.MinionTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.toypet.PetAdoptionService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.idfactory.IDFactory;

public class MinionCommonData extends VisibleObjectTemplate implements IExpirable {

	private final int petObjectId;
	private final int masterObjectId;
	MinionDopingBag dopingBag = null;
	private String name;
	private int minionId;
	private Timestamp birthday;
	private int lastSentPoints;
	private int expireTime;
	private int growthPoint;
	private int isErect;
	private Timestamp despawnTime;
	private int isLocked;
	private boolean isBuffing = false;
	private boolean isLooting = false;

	public MinionCommonData(int minionId, int masterObjectId, int expireTime) {
		this.petObjectId = IDFactory.getInstance().nextId();
		this.minionId = minionId;
		this.masterObjectId = masterObjectId;
		this.expireTime = expireTime;
		MinionTemplate template = DataManager.MINION_DATA.getMinionTemplate(minionId);
		if (template.isUseFuncOption()) {
			dopingBag = new MinionDopingBag();
		}
	}

	public final String getName() {
		if(name.contains("NEW_")){
			return "";
		}
		return name;
	}

	public final String getRealName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final int getMinionId() {
		return minionId;
	}

	public final void setMinionId(int minionId) {
		this.minionId = minionId;
	}

	public int getGrowthPoint() {
		return growthPoint;
	}

	public void setGrowthPoint(int growthPoint) {
		this.growthPoint = growthPoint;
	}

	public int getIsErect() {
		return isErect;
	}

	public void setIsErect(int isErect) {
		this.isErect = isErect;
	}

	public int getBirthday() {
		if (birthday == null) {
			return 0;
		}

		return (int) (birthday.getTime() / 1000);
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	public Timestamp getBirthdayTimestamp() {
		return birthday;
	}

	public int getObjectId() {
		return petObjectId;
	}

	public int getMasterObjectId() {
		return masterObjectId;
	}

	@Override
	public int getTemplateId() {
		return minionId;
	}

	@Override
	public int getNameId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public final int getLastSentPoints() {
		return lastSentPoints;
	}

	public final void setLastSentPoints(int points) {
		lastSentPoints = points;
	}

	/**
	 * @return the despawnTime
	 */
	public Timestamp getDespawnTime() {
		return despawnTime;
	}

	/**
	 * @param despawnTime the despawnTime to set
	 */
	public void setDespawnTime(Timestamp despawnTime) {
		this.despawnTime = despawnTime;
	}

	public boolean isLocked() {
		return isLocked == 0 ? false : true;
	}

	public void setLocked(int locked) {
		this.isLocked = locked;
	}

	public MinionDopingBag getDopingBag() {
		return dopingBag;
	}

	public void setIsBuffing(boolean isBuffing) {
		this.isBuffing = isBuffing;
	}

	public boolean isBuffing() {
		return this.isBuffing;
	}

	@Override
	public int getExpireTime() {
		return expireTime;
	}

	@Override
	public void expireEnd(Player player) {
		if (player == null) {
			return;
		}
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_PET_ABANDON_EXPIRE_TIME_COMPLETE(name));
		PetAdoptionService.surrenderPet(player, minionId);
	}

	@Override
	public boolean canExpireNow() {
		return true;
	}

	@Override
	public void expireMessage(Player player, int time) {
	}

	public void setIsLooting(boolean isLooting) {
		this.isLooting = isLooting;
	}

	public boolean isLooting() {
		return this.isLooting;
	}
}