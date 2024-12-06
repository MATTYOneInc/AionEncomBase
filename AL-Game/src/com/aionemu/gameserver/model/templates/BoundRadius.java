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
package com.aionemu.gameserver.model.templates;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BoundRadius")
public class BoundRadius {
	@XmlAttribute
	private float front;

	@XmlAttribute
	private float side;

	@XmlAttribute
	private float upper;

	private float collision;

	public static final BoundRadius DEFAULT = new BoundRadius(0f, 0f, 0f);

	public BoundRadius() {
	}

	public BoundRadius(float front, float side, float upper) {
		this.front = front;
		this.side = side;
		this.upper = upper;
		calculateCollision(front, side);
	}

	protected void afterUnmarshal(Unmarshaller u, Object parent) {
		calculateCollision(front, side);
	}

	protected void calculateCollision(float front, float side) {
		this.collision = (float) Math.sqrt(side * front);
	}

	public float getFront() {
		return front;
	}

	public float getSide() {
		return side;
	}

	public float getUpper() {
		return upper;
	}

	public float getCollision() {
		return collision;
	}
}