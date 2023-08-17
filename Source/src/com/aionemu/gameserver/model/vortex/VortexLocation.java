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
package com.aionemu.gameserver.model.vortex;

import com.aionemu.gameserver.controllers.RVController;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.vortex.HomePoint;
import com.aionemu.gameserver.model.templates.vortex.ResurrectionPoint;
import com.aionemu.gameserver.model.templates.vortex.StartPoint;
import com.aionemu.gameserver.model.templates.vortex.VortexTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.vortexservice.DimensionalVortex;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.zone.InvasionZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.handler.ZoneHandler;
import javolution.util.FastMap;

import java.util.ArrayList;
import java.util.List;

public class VortexLocation implements ZoneHandler
{
	protected boolean isActive;
	protected DimensionalVortex<VortexLocation> activeVortex;
	protected RVController vortexController;
	protected VortexTemplate template;
	protected int id;
	protected Race offenceRace;
	protected Race defendsRace;
	protected List<InvasionZoneInstance> zones;
	protected FastMap<Integer, Player> players = new FastMap<Integer, Player>();
	protected FastMap<Integer, Kisk> kisks = new FastMap<Integer, Kisk>();
	private final List<VisibleObject> spawned = new ArrayList<VisibleObject>();
	protected HomePoint home;
	protected ResurrectionPoint resurrection;
	protected StartPoint start;
	
	public VortexLocation() {
	}
	
	public VortexLocation(VortexTemplate template) {
		this.template = template;
		this.id = template.getId();
		this.offenceRace = template.getInvadersRace();
		this.defendsRace = template.getDefendersRace();
		this.zones = new ArrayList<InvasionZoneInstance>();
		this.home = template.getHomePoint();
		this.resurrection = template.getResurrectionPoint();
		this.start = template.getStartPoint();
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setActiveVortex(DimensionalVortex<VortexLocation> vortex) {
		isActive = vortex != null;
		this.activeVortex = vortex;
	}
	
	public DimensionalVortex<VortexLocation> getActiveVortex() {
		return activeVortex;
	}
	
	public void setVortexController(RVController controller) {
		this.vortexController = controller;
	}
	
	public RVController getVortexController() {
		return vortexController;
	}
	
	public final VortexTemplate getTemplate() {
		return template;
	}
	
	public WorldPosition getHomePoint() {
		return home.getHomePoint();
	}
	
	public WorldPosition getResurrectionPoint() {
		return resurrection.getResurrectionPoint();
	}
	
	public WorldPosition getStartPoint() {
		return start.getStartPoint();
	}
	
	public int getId() {
		return id;
	}
	
	public Race getDefendersRace() {
		return defendsRace;
	}
	
	public Race getInvadersRace() {
		return offenceRace;
	}
	
	public int getHomeWorldId() {
		return home.getWorldId();
	}
	
	public int getInvasionWorldId() {
		return start.getWorldId();
	}
	
	public List<VisibleObject> getSpawned() {
		return spawned;
	}
	
	public FastMap<Integer, Player> getPlayers() {
		return players;
	}
	
	public FastMap<Integer, Kisk> getInvadersKisks() {
		return kisks;
	}
	
	public boolean isInvaderInside(int objId) {
		return isActive() && getVortexController().getPassedPlayers().containsKey(objId);
	}
	
	public boolean isInsideActiveVortex(Player player) {
		return isActive() && isInsideLocation(player);
	}
	
	public void addZone(InvasionZoneInstance zone) {
		this.zones.add(zone);
		zone.addHandler(this);
	}
	
	public boolean isInsideLocation(Creature creature) {
		if (zones.isEmpty()) {
			return false;
		} for (int i = 0; i < zones.size(); i++) {
			if (zones.get(i).isInsideCreature(creature)) {
				return true;
			}
		}
		return false;
	}
	
	public List<InvasionZoneInstance> getZones() {
		return zones;
	}
	
	@Override
	public void onEnterZone(Creature creature, ZoneInstance zone) {
		if (creature instanceof Kisk) {
			if (creature.getRace().equals(getInvadersRace())) {
				kisks.putEntry(creature.getObjectId(), (Kisk) creature);
			}
		} else if (creature instanceof Player) {
			Player player = (Player) creature;
			if (!players.containsKey(player.getObjectId())) {
				 players.putEntry(player.getObjectId(), player);
				if (isActive()) {
					if (player.getRace().equals(getInvadersRace())) {
						if (getVortexController().getPassedPlayers().containsKey(player.getObjectId()) && !getActiveVortex().getInvaders().containsKey(player.getObjectId())) {
							getActiveVortex().addPlayer(player, true);
						}
					} else {
						getActiveVortex().updateDefenders(player);
					}
				}
			}
		}
	}
	
	@Override
	public void onLeaveZone(Creature creature, ZoneInstance zone) {
		if (!isInsideLocation(creature)) {
			if (creature instanceof Kisk) {
				kisks.remove(creature.getObjectId());
			} if (creature instanceof Player) {
				final Player player = (Player) creature;
				players.remove(player.getObjectId());
				if (isActive()) {
					if (player.getRace().equals(getInvadersRace())) {
						if (getVortexController().getPassedPlayers().containsKey(player.getObjectId())) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(904305));
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									if (player.isOnline() && !isInsideActiveVortex(player)) {
										getActiveVortex().kickPlayer(player, true);
									}
								}
							}, 10 * 1000);
						}
					} else {
						ThreadPoolManager.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								if (player.isOnline() && !isInsideActiveVortex(player)) {
									getActiveVortex().kickPlayer(player, false);
								}
							}
						}, 10 * 1000);
					}
				}
			}
		}
	}
}