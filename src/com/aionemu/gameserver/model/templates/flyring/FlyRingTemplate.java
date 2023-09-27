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
package com.aionemu.gameserver.model.templates.flyring;

import com.aionemu.gameserver.model.utils3d.Point3D;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlyRing")
public class FlyRingTemplate
{
	@XmlAttribute(name = "name")
	protected String name;
	
	@XmlAttribute(name = "map")
	protected int map;
	
	@XmlAttribute(name = "radius")
	protected float radius;
	
	@XmlElement(name = "center")
	protected FlyRingPoint center;
	
	@XmlElement(name = "left")
	protected FlyRingPoint left;
	
	@XmlElement(name = "right")
	protected FlyRingPoint right;
	
	public String getName() {
		return name;
	}
	public int getMap() {
		return map;
	}
	public float getRadius() {
		return radius;
	}
	public FlyRingPoint getCenter() {
		return center;
	}
	public FlyRingPoint getLeft() {
		return left;
	}
	public FlyRingPoint getRight() {
		return right;
	}
	public FlyRingTemplate() {
	};
	
	public FlyRingTemplate(String name, int mapId, Point3D center, Point3D left, Point3D right, int radius) {
		this.name = name;
		this.map = mapId;
		this.radius = radius;
		this.center = new FlyRingPoint(center);
		this.left = new FlyRingPoint(left);
		this.right = new FlyRingPoint(right);
	}
}