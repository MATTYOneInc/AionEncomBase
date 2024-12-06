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
package admincommands;

import com.aionemu.gameserver.model.drop.DropLists;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Phantom, ATracer
 */
public class Drop extends AdminCommand {

	public Drop() {
		super("drop");
	}

	@Override
	public void execute(Player player, String... params) {
		int num = Integer.parseInt(params[0]);
		int min = 0;
		int max = 0;
		switch (num) {
			case 1:
				min = 200000;max = 212500;
				break;
			case 2:
				min = 212501;max = 215000;
				break;
			case 3:
				min = 215001;max = 217500;
				break;
			case 4:
				min = 217501;max = 260000;
				break;
			case 5:
				min = 260001;max = 840000;
				break;
		}
		DropLists.Xmlmian(min, max);
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}