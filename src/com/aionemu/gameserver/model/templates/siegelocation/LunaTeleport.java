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
package com.aionemu.gameserver.model.templates.siegelocation;

import com.aionemu.gameserver.model.Race;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/****/
/** Author Rinzler (Encom)
/****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LunaTeleport")
public class LunaTeleport
{
	@XmlAttribute(name = "world_id")
	protected int worldId;
	
	@XmlAttribute(name = "race")
	protected Race race = Race.PC_ALL;
	
	@XmlAttribute(name = "x")
	protected float x;
	
	@XmlAttribute(name = "y")
	protected float y;
	
	@XmlAttribute(name = "z")
	protected float z;
	
	@XmlAttribute(name = "h")
	protected byte h;
	
	public int getWorldId() {
		return worldId;
	}
	
	public void setWorldId(int value) {
		worldId = value;
	}
	
	public Race getRace() {
		return race;
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
	
	public byte getH() {
		return h;
	}
	
	public void setH(byte value) {
		h = value;
	}
}