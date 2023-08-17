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
package com.aionemu.gameserver.model.templates.housing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LBox implements Cloneable {

	@XmlElement(required = true)
	protected int id;
	
	@XmlElement(required = true)
	protected String name;

	@XmlElement(required = true)
	protected String desc;

	@XmlElement(required = true)
	protected String script;

	@XmlElement(required = true)
	protected int icon;

	public int getId() {
		return id;
	}
	
	public void setId(int position) {
		id = 100 + position;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public String getScript() {
		return script;
	}

	public int getIcon() {
		return icon;
	}
	
	public void setIcon(int id) {
		icon = id;
	}
	
	@Override
	public Object clone() {
		LBox result = new LBox();
		result.id = this.id;
		result.name = this.name;
		result.desc = this.desc;
		result.script = this.script;
		result.icon = this.icon;
		return result;
	}
}