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
package com.aionemu.gameserver.model.templates.zone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author MrPoke
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cylinder")
public class Cylinder {

	@XmlAttribute
	protected Float top;
	@XmlAttribute
	protected Float bottom;
	@XmlAttribute
	protected Float x;
	@XmlAttribute
	protected Float y;
	@XmlAttribute
	protected Float r;

	public Cylinder() {
	}

	public Cylinder(float x, float y, float radius, float top, float bottom) {
		this.x = x;
		this.y = y;
		this.r = radius;
		this.top = top;
		this.bottom = bottom;
	}

	public Float getTop() {
		return top;
	}

	public Float getBottom() {
		return bottom;
	}

	public Float getX() {
		return x;
	}

	public Float getY() {
		return y;
	}

	public Float getR() {
		return r;
	}
}