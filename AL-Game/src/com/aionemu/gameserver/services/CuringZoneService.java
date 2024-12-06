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
package com.aionemu.gameserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.curingzone.CuringObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.curingzones.CuringTemplate;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

public class CuringZoneService {

	Logger log = LoggerFactory.getLogger(CuringZoneService.class);
	private FastList<CuringObject> curingObjects = new FastList<CuringObject>();

	private CuringZoneService() {
		for (CuringTemplate t : DataManager.CURING_OBJECTS_DATA.getCuringObject()) {
			CuringObject obj = new CuringObject(t, 0);
			obj.spawn();
			curingObjects.add(obj);
		}
		log.info("spawned Curing Zones");
		startTask();
	}

	private void startTask() {
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			public void run() {
				for (final CuringObject obj : curingObjects)
					obj.getKnownList().doOnAllPlayers(new Visitor<Player>() {
						public void visit(Player player) {
							if ((MathUtil.isIn3dRange(obj, player, obj.getRange()))
									&& (!player.getEffectController().hasAbnormalEffect(8751))) {
								SkillEngine.getInstance().getSkill(player, 8751, 1, player).useNoAnimationSkill();
							}
						}
					});
			}
		}, 1000, 1000);
	}

	public static final CuringZoneService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final CuringZoneService instance = new CuringZoneService();
	}
}