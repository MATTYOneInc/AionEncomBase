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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.springzone.SpringObject;
import com.aionemu.gameserver.model.templates.springzones.SpringTemplate;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/****/
/** Author Rinzler (Encom)
/****/

public class SpringZoneService
{
	Logger log = LoggerFactory.getLogger(SpringZoneService.class);
	private FastList<SpringObject> springObjects = new FastList<SpringObject>();
	
	private SpringZoneService() {
		for (SpringTemplate t : DataManager.SPRING_OBJECTS_DATA.getSpringObject()) {
			SpringObject obj = new SpringObject(t, 0);
			obj.spawn();
			springObjects.add(obj);
		}
		startSpring();
	}
	
	private void startSpring() {
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			public void run() {
				for (final SpringObject obj : springObjects)
				obj.getKnownList().doOnAllPlayers(new Visitor<Player>() {
					public void visit(Player player) {
						if ((MathUtil.isIn3dRange(obj, player, obj.getRange())) &&
						   (!player.getEffectController().hasAbnormalEffect(17560))) { //Bless Of Guardian Spring.
							SkillEngine.getInstance().getSkill(player, 17560, 1, player).useNoAnimationSkill();
						}
					}
				});
			}
		}, 1000, 1000);
	}
	
	public static final SpringZoneService getInstance() {
		return SingletonHolder.instance;
	}
	
	private static class SingletonHolder {
		protected static final SpringZoneService instance = new SpringZoneService();
	}
}