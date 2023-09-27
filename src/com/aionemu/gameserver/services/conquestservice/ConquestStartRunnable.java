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
package com.aionemu.gameserver.services.conquestservice;

import com.aionemu.gameserver.model.conquest.ConquestLocation;
import com.aionemu.gameserver.services.ConquestService;

import java.util.Map;
/**
 * @author Rinzler (Encom)
 */

public class ConquestStartRunnable implements Runnable
{
	private final int id;
	
	public ConquestStartRunnable(int id) {
		this.id = id;
	}
	
	@Override
	public void run() {
		//Shugo Emperor's Vault 4.7.5 is now open !!!
		ConquestService.getInstance().emperorVaultMsg(id);
		//Emperor Trillirunerk's Safe 4.9.1 is now open !!!
		ConquestService.getInstance().trillirunerkSafeMsg(id);
		//Smoldering Fire Temple 5.1 is now open !!!
		ConquestService.getInstance().smolderingFireTempleMsg(id);
		//Kumuki Cave 5.3 is now open !!!
		ConquestService.getInstance().kumukiCaveMsg(id);
		//IDEventDefMsg 5.6 is now open !!!
		ConquestService.getInstance().IDEventDefMsg(id);
		//Tiamaranta's Eye is now open !!!
		ConquestService.getInstance().tiamarantaMsg(id);
		//Conquest/Offering a rare monster appeared !!!
		ConquestService.getInstance().conquestOfferingMsg(id);
		Map<Integer, ConquestLocation> locations = ConquestService.getInstance().getConquestLocations();
		for (final ConquestLocation loc : locations.values()) {
			if (loc.getId() == id) {
				ConquestService.getInstance().startConquest(loc.getId());
			}
		}
	}
}