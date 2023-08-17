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

import com.aionemu.gameserver.model.challenge.ChallengeQuest;
import com.aionemu.gameserver.model.challenge.ChallengeTask;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.challenge.ChallengeType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import java.util.List;

public class SM_CHALLENGE_LIST extends AionServerPacket
{
	int action;
	int ownerId;
	ChallengeType ownerType;
	List<ChallengeTask> tasks;
	ChallengeTask task;
	
	public SM_CHALLENGE_LIST(int action, int ownerId, ChallengeType ownerType, List<ChallengeTask> tasks) {
		this.action = action;
		this.ownerId = ownerId;
		this.ownerType = ownerType;
		this.tasks = tasks;
	}
	
	public SM_CHALLENGE_LIST(int action, int ownerId, ChallengeType ownerType, ChallengeTask task) {
		this.action = action;
		this.ownerId = ownerId;
		this.ownerType = ownerType;
		this.task = task;
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		writeC(action);
		writeD(ownerId);
		writeC(ownerType.getId());
		writeD(player.getObjectId());
		switch (action) {
			case 2:
				writeD((int) (System.currentTimeMillis() / 1000));
				writeH(tasks.size());
				for (ChallengeTask task : tasks) {
					writeD(32);
					writeD(task.getTaskId());
					writeC(1);
					writeC(21);
					writeC(0);
					writeD((int) (task.getCompleteTime().getTime() / 1000));
				}
			break;
			case 7:
				writeD(32);
				writeD(task.getTaskId());
				writeH(task.getQuestsCount());
				for (ChallengeQuest quest : task.getQuests().values()) {
					writeD(quest.getQuestId());
					writeH(quest.getMaxRepeats());
					writeD(quest.getScorePerQuest());
					writeH(quest.getCompleteCount());
				}
			break;
		}
	}
}