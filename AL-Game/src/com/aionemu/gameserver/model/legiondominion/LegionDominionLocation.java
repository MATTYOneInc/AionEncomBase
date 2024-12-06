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
package com.aionemu.gameserver.model.legiondominion;

import com.aionemu.gameserver.model.templates.legiondominion.LegionDominionTemplate;

public class LegionDominionLocation {
	protected LegionDominionTemplate template;
	protected LegionDominionRace legionDominionRace = LegionDominionRace.BALAUR;

	public LegionDominionLocation() {
	}

	public LegionDominionLocation(LegionDominionTemplate template) {
		this.template = template;
	}

	public LegionDominionTemplate getTemplate() {
		return template;
	}

	public int getLegionDominionId() {
		return template.getLegionDominionId();
	}

	public String getName() {
		return template.getName();
	}

	public int getWorldId() {
		return template.getWorldId();
	}

	public LegionDominionRace getRace() {
		return this.legionDominionRace;
	}

	public void setRace(LegionDominionRace legionDominionRace) {
		this.legionDominionRace = legionDominionRace;
	}
}