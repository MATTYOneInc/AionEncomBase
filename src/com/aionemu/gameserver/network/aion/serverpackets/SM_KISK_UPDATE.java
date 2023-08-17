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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_KISK_UPDATE extends AionServerPacket
{
	private int objId;
	private int creatorid;
	private int useMask;
	private int currentMembers;
	private int maxMembers;
	private int remainingRessurects;
	private int maxRessurects;
	private int remainingLifetime;
	
	public SM_KISK_UPDATE(Kisk kisk) {
		this.objId = kisk.getObjectId();
		this.creatorid = kisk.getCreatorId();
		this.useMask = kisk.getUseMask();
		this.currentMembers = kisk.getCurrentMemberCount();
		this.maxMembers = kisk.getMaxMembers();
		this.remainingRessurects = kisk.getRemainingResurrects();
		this.maxRessurects = kisk.getMaxRessurects();
		this.remainingLifetime = kisk.getRemainingLifetime();
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(objId);
		writeD(creatorid);
		writeD(useMask);
		writeD(currentMembers);
		writeD(maxMembers);
		writeD(remainingRessurects);
		writeD(maxRessurects);
		writeD(remainingLifetime);
	}
}