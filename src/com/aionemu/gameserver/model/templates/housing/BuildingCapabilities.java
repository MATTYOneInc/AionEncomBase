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

import javax.xml.bind.annotation.*;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "caps")
public class BuildingCapabilities {

	@XmlAttribute(required = true)
	protected boolean addon;

	@XmlAttribute(required = true)
	protected int emblemId;

	@XmlAttribute(required = true)
	protected boolean floor;

	@XmlAttribute(required = true)
	protected boolean room;

	@XmlAttribute(required = true)
	protected int interior;

	@XmlAttribute(required = true)
	protected int exterior;

	public boolean canHaveAddon() {
		return addon;
	}

	public int getEmblemId() {
		return emblemId;
	}

	public boolean canChangeFloor() {
		return floor;
	}

	public boolean canChangeRoom() {
		return room;
	}

	public int canChangeInterior() {
		return interior;
	}

	public int canChangeExterior() {
		return exterior;
	}
}