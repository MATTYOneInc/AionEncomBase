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
package com.aionemu.gameserver.model.rvr;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.rvr.RvrTemplate;
import com.aionemu.gameserver.services.rvrservice.Rvrlf3df3;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */

public class RvrLocation {
	protected int id;
	protected boolean isActive;
	protected RvrTemplate template;
	protected Rvrlf3df3<RvrLocation> activeRvr;
	protected FastMap<Integer, Player> players = new FastMap<Integer, Player>();
	private final List<VisibleObject> spawned = new ArrayList<VisibleObject>();

	public RvrLocation() {
	}

	public RvrLocation(RvrTemplate template) {
		this.template = template;
		this.id = template.getId();
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActiveRvr(Rvrlf3df3<RvrLocation> rvr) {
		isActive = rvr != null;
		this.activeRvr = rvr;
	}

	public Rvrlf3df3<RvrLocation> getActiveRvr() {
		return activeRvr;
	}

	public final RvrTemplate getTemplate() {
		return template;
	}

	public int getId() {
		return id;
	}

	public List<VisibleObject> getSpawned() {
		return spawned;
	}

	public FastMap<Integer, Player> getPlayers() {
		return players;
	}
}