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
package com.aionemu.gameserver.model.idiandepths;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.idiandepths.IdianDepthsTemplate;
import com.aionemu.gameserver.services.idiandepthsservice.IdianDepths;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */

public class IdianDepthsLocation {
	protected int id;
	protected boolean isActive;
	protected IdianDepthsTemplate template;
	protected IdianDepths<IdianDepthsLocation> activeIdianDepths;
	protected FastMap<Integer, Player> players = new FastMap<Integer, Player>();
	private final List<VisibleObject> spawned = new ArrayList<VisibleObject>();

	public IdianDepthsLocation() {
	}

	public IdianDepthsLocation(IdianDepthsTemplate template) {
		this.template = template;
		this.id = template.getId();
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActiveIdianDepths(IdianDepths<IdianDepthsLocation> idianDepths) {
		isActive = idianDepths != null;
		this.activeIdianDepths = idianDepths;
	}

	public IdianDepths<IdianDepthsLocation> getActiveIdianDepths() {
		return activeIdianDepths;
	}

	public final IdianDepthsTemplate getTemplate() {
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