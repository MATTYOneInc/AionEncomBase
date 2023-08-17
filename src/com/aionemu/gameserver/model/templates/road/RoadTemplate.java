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
package com.aionemu.gameserver.model.templates.road;

import com.aionemu.gameserver.model.utils3d.Point3D;

import javax.xml.bind.annotation.*;

/**
 * @author SheppeR
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Road")
public class RoadTemplate {

	@XmlAttribute(name = "name")
	protected String name;

	@XmlAttribute(name = "map")
	protected int map;

	@XmlAttribute(name = "radius")
	protected float radius;

	@XmlElement(name = "center")
	protected RoadPoint center;

	@XmlElement(name = "p1")
	protected RoadPoint p1;

	@XmlElement(name = "p2")
	protected RoadPoint p2;

	@XmlElement(name = "roadexit")
	protected RoadExit roadExit;

	public String getName() {
		return name;
	}

	public int getMap() {
		return map;
	}

	public float getRadius() {
		return radius;
	}

	public RoadPoint getCenter() {
		return center;
	}

	public RoadPoint getP1() {
		return p1;
	}

	public RoadPoint getP2() {
		return p2;
	}

	public RoadExit getRoadExit() {
		return roadExit;
	}

	public RoadTemplate() {

	};

	public RoadTemplate(String name, int mapId, Point3D center, Point3D p1, Point3D p2) {
		this.name = name;
		this.map = mapId;
		this.radius = 6;
		this.center = new RoadPoint(center);
		this.p1 = new RoadPoint(p1);
		this.p2 = new RoadPoint(p2);
	}
}