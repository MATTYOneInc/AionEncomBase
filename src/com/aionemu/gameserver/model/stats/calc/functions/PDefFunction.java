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
package com.aionemu.gameserver.model.stats.calc.functions;

import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.container.StatEnum;

class PDefFunction extends StatFunction {

	PDefFunction() {
		stat = StatEnum.PHYSICAL_DEFENSE;
	}

	@Override
	public void apply(Stat2 stat) {
		if (stat.getOwner().isInFlyingState()) {
			stat.setBonus(stat.getBonus() - (stat.getBase() / 2));
		}
	}

	@Override
	public int getPriority() {
		return 60;
	}
}