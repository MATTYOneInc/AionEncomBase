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
package com.aionemu.gameserver.restrictions;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Skill;

public interface Restrictions
{
	public boolean isRestricted(Player player, Class<? extends Restrictions> callingRestriction);
	public boolean canAttack(Player player, VisibleObject target);
	public boolean canAffectBySkill(Player player, VisibleObject target, Skill skill);
	public boolean canUseSkill(Player player, Skill skill);
	public boolean canChat(Player player);
	public boolean canInviteToGroup(Player player, Player target);
	public boolean canInviteToAlliance(Player player, Player target);
	public boolean canInviteToLeague(Player player, Player target);
	public boolean canChangeEquip(Player player);
	public boolean canUseWarehouse(Player player);
	public boolean canTrade(Player player);
	public boolean canUseItem(Player player, Item item);
}