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
package com.aionemu.gameserver.model.templates.item.upgrade;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Ranastic (Encom)
 */

@XmlRootElement(name = "UpgradeResultItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpgradeResultItem
{
	@XmlAttribute(name = "item_id")
	private int item_id;
	
	@XmlAttribute(name = "check_enchant_count")
	private int check_enchant_count;
	
	@XmlAttribute(name = "check_authorize_count")
	private int check_authorize_count;
	
	private UpgradeMaterials upgrade_materials;
	
	private NeedAbyssPoint need_abyss_point;
	
	private NeedKinah need_kinah;
	
	public int getCheck_enchant_count() {
		return check_enchant_count;
	}
	
	public int getCheck_authorize_count() {
		return check_authorize_count;
	}
	
	public int getItem_id() {
		return item_id;
	}
	
	public UpgradeMaterials getUpgrade_materials() {
		return upgrade_materials;
	}
	
	public NeedAbyssPoint getNeed_abyss_point() {
		return need_abyss_point;
	}
	
	public NeedKinah getNeed_kinah() {
		return need_kinah;
	}
}