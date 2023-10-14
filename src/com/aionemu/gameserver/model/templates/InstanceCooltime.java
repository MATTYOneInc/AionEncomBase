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
package com.aionemu.gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.instance.InstanceCoolTimeType;
import com.aionemu.gameserver.model.instance.InstanceType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstanceCooltime")
public class InstanceCooltime
{
	@XmlElement(name = "type")
    protected InstanceCoolTimeType coolTimeType;
	
    @XmlElement(name = "type_value")
    protected String typeValue;
	
    @XmlElement(name = "ent_cool_time")
    protected Integer entCoolTime;
	
    @XmlElement(name = "indun_type")
    protected InstanceType indunType;
	
    @XmlElement(name = "max_member_light")
    protected Integer maxMemberLight;
	
    @XmlElement(name = "max_member_dark")
    protected Integer maxMemberDark;
	
    @XmlElement(name = "enter_min_level_light")
    protected Integer enterMinLevelLight;
	
    @XmlElement(name = "enter_max_level_light")
    protected Integer enterMaxLevelLight;
	
    @XmlElement(name = "enter_min_level_dark")
    protected Integer enterMinLevelDark;
	
    @XmlElement(name = "enter_max_level_dark")
    protected Integer enterMaxLevelDark;
	
    @XmlElement(name = "alarm_unit_score")
    protected Integer alarmUnitScore;
	
    @XmlElement(name = "can_enter_mentor")
    protected boolean canEnterMentor;
	
    @XmlElement(name = "enter_guild")
    protected boolean enterGuild;
	
    @XmlElement(name = "max_count")
    protected Integer max_count;
	
	//4.9
	@XmlElement(name = "count_build_up")
    protected Integer countBuildUp;
	
	@XmlElement(name = "count_build_up_level")
    protected Integer countBuildUpLevel;
	
   /**
	*/
    @XmlAttribute(required = true)
    protected int id;
	
    @XmlAttribute(required = true)
    protected int worldId;
	
    @XmlAttribute(required = true)
    protected Race race;
	
	public int getId() {
		return id;
	}
	
	public int getWorldId() {
		return worldId;
	}
	
	public Race getRace() {
		return race;
	}
	
	public InstanceCoolTimeType getCoolTimeType() {
        return coolTimeType;
    }
	
    public String getTypeValue() {
        return typeValue;
    }
	
    public InstanceType getTypeInstance() {
        return indunType;
    }
	
	public Integer getEntCoolTime() {
		return entCoolTime;
	}
	
	public Integer getMaxMemberLight() {
		return maxMemberLight;
	}
	
	public Integer getMaxMemberDark() {
		return maxMemberDark;
	}
	
	public Integer getEnterMinLevelLight() {
		return enterMinLevelLight;
	}
	
	public Integer getEnterMaxLevelLight() {
		return enterMaxLevelLight;
	}
	
	public Integer getEnterMinLevelDark() {
		return enterMinLevelDark;
	}
	
	public Integer getEnterMaxLevelDark() {
		return enterMaxLevelDark;
	}
	
	public Integer getAlarmUnitScore() {
		return alarmUnitScore;
	}
	
	public boolean getCanEnterMentor() {
		return canEnterMentor;
	}
	
	public boolean getEnterGuild() {
		return enterGuild;
	}
	
	public Integer getMaxEntriesCount() {
		return max_count;
	}
	
	public Integer getCountBuildUp() {
		return countBuildUp;
	}
	
	public Integer getCountBuildUpLevel() {
		return countBuildUpLevel;
	}
}