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
package com.aionemu.gameserver.model.templates.shield;

import com.aionemu.gameserver.model.utils3d.Point3D;

import javax.xml.bind.annotation.*;

/**
 * @author M@xx, Wakizashi
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Shield")
public class ShieldTemplate {

	@XmlAttribute(name = "name")
	protected String name;

	@XmlAttribute(name = "map")
	protected int map;

	@XmlAttribute(name = "id")
	protected int id;

	@XmlAttribute(name = "radius")
	protected float radius;

	@XmlElement(name = "center")
	protected ShieldPoint center;

	public String getName() {
		return name;
	}

	public int getMap() {
		return map;
	}

	public float getRadius() {
		return radius;
	}

	public ShieldPoint getCenter() {
		return center;
	}

	public int getId() {
		return id;
	}

	public ShieldTemplate() {
	};

	public ShieldTemplate(String name, int mapId, Point3D center) {
		this.name = name;
		this.map = mapId;
		this.radius = 6;
		this.center = new ShieldPoint(center);
	}
}