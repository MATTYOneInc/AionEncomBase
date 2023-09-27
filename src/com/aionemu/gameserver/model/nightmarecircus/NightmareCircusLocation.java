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
package com.aionemu.gameserver.model.nightmarecircus;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.nightmarecircus.NightmareCircusTemplate;
import com.aionemu.gameserver.services.nightmarecircusservice.CircusInstance;
import javolution.util.FastMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rinzler (Encom)
 */

public class NightmareCircusLocation
{
	protected int id;
	protected boolean isActive;
	protected NightmareCircusTemplate template;
	protected CircusInstance<NightmareCircusLocation> activeNightmareCircus;
	protected FastMap<Integer, Player> players = new FastMap<Integer, Player>();
	private final List<VisibleObject> spawned = new ArrayList<VisibleObject>();
	
	public NightmareCircusLocation() {
	}
	
	public NightmareCircusLocation(NightmareCircusTemplate template) {
		this.template = template;
		this.id = template.getId();
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setActiveNightmareCircus(CircusInstance<NightmareCircusLocation> nightmareCircus) {
		isActive = nightmareCircus != null;
		this.activeNightmareCircus = nightmareCircus;
	}
	
	public CircusInstance<NightmareCircusLocation> getActiveNightmareCircus() {
		return activeNightmareCircus;
	}
	
	public final NightmareCircusTemplate getTemplate() {
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