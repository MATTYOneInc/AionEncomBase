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
package com.aionemu.gameserver.model.templates.springzones;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/****/
/** Author Rinzler (Encom)
/****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpringTemplate")
public class SpringTemplate
{
	@XmlAttribute(name = "map_id")
	protected int mapId;
	
	@XmlAttribute(name = "x")
	protected float x;
	
	@XmlAttribute(name = "y")
	protected float y;
	
	@XmlAttribute(name = "z")
	protected float z;
	
	@XmlAttribute(name = "range")
	protected float range;
	
	public int getMapId() {
		return mapId;
	}
	
	public void setMapId(int value) {
		mapId = value;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float value) {
		x = value;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float value) {
		y = value;
	}
	
	public float getZ() {
		return z;
	}
	
	public void setZ(float value) {
		z = value;
	}
	
	public float getRange() {
		return range;
	}
}