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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssemblyItem")
public class AssemblyItem
{
	@XmlAttribute(required = true)
	protected int id;
	
	@XmlAttribute(name = "parts_num")
	protected int partsNum;

	@XmlAttribute(name = "proc_assembly")
	protected int procAssembly;

	@XmlAttribute(required = true)
	protected List<Integer> parts;
	
	public List<Integer> getParts() {
		if (parts == null) {
			parts = new ArrayList<Integer>();
		}
		return parts;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int value) {
		id = value;
	}
	
	public int getPartsNum() {
		return partsNum;
	}
	
	public void setPartsNum(int value) {
		partsNum = value;
	}

	public int getProcAssembly() {
		return procAssembly;
	}

	public void setProcAssembly(int value) {
		procAssembly = value;
	}
}