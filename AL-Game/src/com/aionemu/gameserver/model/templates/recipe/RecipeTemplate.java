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
package com.aionemu.gameserver.model.templates.recipe;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecipeTemplate")
public class RecipeTemplate {
	protected List<ComboProduct> comboproduct;
	@XmlAttribute(name = "max_production_count")
	protected Integer maxProductionCount;
	@XmlAttribute(name = "craft_delay_time")
	protected Integer craftDelayTime;
	@XmlAttribute(name = "craft_delay_id")
	protected Integer craftDelayId;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "archdaeva")
	protected boolean archdaeva;
	@XmlAttribute
	protected int quantity;
	@XmlAttribute
	protected int productid;
	@XmlAttribute
	protected int autolearn;
	@XmlAttribute
	protected int dp;
	@XmlAttribute
	protected int skillpoint;
	@XmlAttribute
	protected Race race;
	@XmlAttribute
	protected int skillid;
	@XmlAttribute
	protected int itemid;
	@XmlAttribute
	protected int nameid;
	@XmlAttribute
	protected int id;
	@XmlElement(name = "component_panel")
	private List<Component> component_panel;

	public List<Component> getComponent() {
		return component_panel;
	}

	public Integer getComboProduct(int num) {
		if (comboproduct == null || comboproduct.get(num - 1) == null) {
			return null;
		}
		return comboproduct.get(num - 1).getItemid();
	}

	public Integer getComboProductSize() {
		if (comboproduct == null) {
			return 0;
		}
		return comboproduct.size();
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Integer getProductid() {
		return productid;
	}

	public int getAutoLearn() {
		return autolearn;
	}

	public Integer getDp() {
		return dp;
	}

	public Integer getSkillpoint() {
		return skillpoint;
	}

	public Race getRace() {
		return race;
	}

	public Integer getSkillid() {
		return skillid;
	}

	public Integer getItemid() {
		return itemid;
	}

	public String getName() {
		return name;
	}

	public int getNameid() {
		return nameid;
	}

	public Integer getId() {
		return id;
	}

	public Integer getMaxProductionCount() {
		return maxProductionCount;
	}

	public Integer getCraftDelayTime() {
		return craftDelayTime;
	}

	public Integer getCraftDelayId() {
		return craftDelayId;
	}

	public boolean isArchDaeva() {
		return archdaeva;
	}

	public void setArchDaeva(boolean value) {
		archdaeva = value;
	}
}