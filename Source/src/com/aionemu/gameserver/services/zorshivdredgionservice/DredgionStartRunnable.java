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
package com.aionemu.gameserver.services.zorshivdredgionservice;

import com.aionemu.gameserver.model.zorshivdredgion.ZorshivDredgionLocation;
import com.aionemu.gameserver.services.ZorshivDredgionService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import java.util.Map;

/**
 * @author Rinzler (Encom)
 */

public class DredgionStartRunnable implements Runnable
{
	private final int id;
	
	public DredgionStartRunnable(int id) {
		this.id = id;
	}
	
	@Override
	public void run() {
		//Invasion Portal.
		ZorshivDredgionService.getInstance().adventPortalSP(id);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
			    //Invasion Lazer.
				ZorshivDredgionService.getInstance().adventDirectingSP(id);
			}
		}, 180000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Invasion Black Sky.
				ZorshivDredgionService.getInstance().adventControlSP(id);
			}
		}, 300000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
			    Map<Integer, ZorshivDredgionLocation> locations = ZorshivDredgionService.getInstance().getZorshivDredgionLocations();
				for (ZorshivDredgionLocation loc : locations.values()) {
					if (loc.getId() == id) {
						//Invasion Light Blue.
						ZorshivDredgionService.getInstance().adventEffectSP(id);
						//The Balaur Dredgion has appeared at levinshor.
						ZorshivDredgionService.getInstance().levinshorMsg(id);
						//The Balaur Dredgion has appeared at inggison.
						ZorshivDredgionService.getInstance().inggisonMsg(id);
						ZorshivDredgionService.getInstance().startZorshivDredgion(loc.getId());
					}
				}
			}
		}, 600000);
	}
}