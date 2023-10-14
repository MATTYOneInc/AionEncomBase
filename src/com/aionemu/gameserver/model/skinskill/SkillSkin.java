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
package com.aionemu.gameserver.model.skinskill;

import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.SkillSkinTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rinzler (Encom)
 */
public class SkillSkin implements IExpirable {

	private SkillSkinTemplate template;
	private int id;
	private int dispearTime = 0;
	private int isActive;

	public SkillSkin(SkillSkinTemplate template, int id, int dispearTime, int isActive) {
		this.template = template;
		this.id = id;
		this.dispearTime = dispearTime;
		this.isActive = isActive;
	}

	public SkillSkinTemplate getTemplate() {
		return template;
	}

	public int getId() {
		return id;
	}

	public int getRemainingTime() {
		if (dispearTime == 0) {
			return 0;
		}
		return dispearTime - (int) (System.currentTimeMillis() / 1000L);
	}

	public int getIsActive() {
		return isActive;
	}

	@Override
	public int getExpireTime() {
		return dispearTime;
	}

	@Override
	public void expireEnd(Player player) {
		player.getSkillSkinList().removeSkillSkin(id);
	}

	@Override
	public void expireMessage(Player player, int time) {
		PacketSendUtility.sendBrightYellowMessageOnCenter(player, "Skill Animation Expired"); // For testing should be
																								// removed later if all
																								// works 100%
	}

	@Override
	public boolean canExpireNow() {
		return true;
	}
}