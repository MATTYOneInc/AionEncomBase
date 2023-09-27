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

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Points")
public class Points {

	@XmlElement(required = true)
	protected List<Point2D> point;
	@XmlAttribute(name = "top")
	protected float top;
	@XmlAttribute(name = "bottom")
	protected float bottom;

	public Points() {
	}

	public Points(float bottom, float top) {
		this.bottom = bottom;
		this.top = top;
	}

	public List<Point2D> getPoint() {
		if (point == null) {
			point = new ArrayList<Point2D>();
		}
		return this.point;
	}

	/**
	 * @return the top
	 */
	public float getTop() {
		return top;
	}

	/**
	 * @return the bottom
	 */
	public float getBottom() {
		return bottom;
	}
}