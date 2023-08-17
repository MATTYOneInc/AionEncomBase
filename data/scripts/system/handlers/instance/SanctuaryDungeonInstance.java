/*
 * This file is part of Encom.
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
package instance;

import java.util.*;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/****/
/** Author (Encom)
/** Source: https://www.youtube.com/watch?v=8Qt-ZODwhoA
/****/

@InstanceID(301580000)
public class SanctuaryDungeonInstance extends GeneralInstanceHandler
{
	private Race spawnRace;
	
	@Override
	public void onEnterInstance(Player player) {
		if (spawnRace == null) {
			spawnRace = player.getRace();
			spawnSanctuaryRace();
		}
	}
	
	private void spawnSanctuaryRace() {
		//Npc
		final int Feregran_Weatha = spawnRace == Race.ASMODIANS ? 806080 : 806076;
		spawn(Feregran_Weatha, 432.54724f, 479.6076f, 99.59915f, (byte) 31);
		//Tp
		final int Dungeon_Exit = spawnRace == Race.ASMODIANS ? 806190 : 806189;
		spawn(Dungeon_Exit, 432.7019f, 475.63489f, 99.471016f, (byte) 0, 20);
	}
}