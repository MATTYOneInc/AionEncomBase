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
package com.aionemu.gameserver.model.templates.stats;

import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author xavier
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "modifiers")
public class ModifiersTemplate {

	@XmlElements({ 
		@XmlElement(name = "sub", type = com.aionemu.gameserver.model.stats.calc.functions.StatSubFunction.class),
		@XmlElement(name = "add", type = com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction.class),
		@XmlElement(name = "rate", type = com.aionemu.gameserver.model.stats.calc.functions.StatRateFunction.class),
		@XmlElement(name = "set", type = com.aionemu.gameserver.model.stats.calc.functions.StatSetFunction.class) })
	private List<StatFunction> modifiers;

	@XmlAttribute
	private float chance = 100;
	
	@XmlAttribute
	private int level;

	public List<StatFunction> getModifiers() {
		return modifiers;
	}

	public float getChance() {
		return chance;
	}
	
	public float getLevel() {
		return level;
	}
}