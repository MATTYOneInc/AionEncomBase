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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.InstanceCooltime;
import com.aionemu.gameserver.services.instance.InstanceService;
import javolution.util.FastMap;
import org.joda.time.DateTime;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "instance_cooltimes")
public class InstanceCooltimeData
{
	@XmlElement(name = "instance_cooltime", required = true)
	protected List<InstanceCooltime> instanceCooltime;
	private FastMap<Integer, InstanceCooltime> instanceCooltimes = new FastMap<Integer, InstanceCooltime>();
	private HashMap<Integer, Integer> syncIdToMapId = new HashMap<Integer, Integer>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (InstanceCooltime tmp : instanceCooltime) {
			instanceCooltimes.put(tmp.getWorldId(), tmp);
			syncIdToMapId.put(tmp.getId(), tmp.getWorldId());
		}
		instanceCooltime.clear();
	}
	
	public FastMap<Integer, InstanceCooltime> getAllInstances() {
		return instanceCooltimes;
	}
	
	public InstanceCooltime getInstanceCooltimeByWorldId(int worldId) {
		return instanceCooltimes.get(worldId);
	}
	
	public int getWorldId(int syncId) {
		if (!syncIdToMapId.containsKey(syncId)) {
			return 0;
		}
		return syncIdToMapId.get(syncId);
	}
	
	public long getInstanceEntranceCooltimeById(Player player, int syncId) {
		if (!syncIdToMapId.containsKey(syncId)) {
			return 0;
		}
		return getInstanceEntranceCooltime(player, syncIdToMapId.get(syncId));
	}
	
	public int getInstanceEntranceCountByWorldId(int worldId) {
		InstanceCooltime clt = getInstanceCooltimeByWorldId(worldId);
		if (clt != null) {
			return clt.getMaxEntriesCount();
		} else {
			return 0;
		}
	}
	
	public long getInstanceEntranceCooltime(Player player, int worldId) {
		int instanceCooldownRate = InstanceService.getInstanceRate(player, worldId);
		long instanceCoolTime = 0;
		InstanceCooltime clt = getInstanceCooltimeByWorldId(worldId);
		if (clt != null) {
			instanceCoolTime = clt.getEntCoolTime();
			if (clt.getCoolTimeType().isDaily()) {
				DateTime now = DateTime.now();
				DateTime repeatDate = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), clt.getEntCoolTime() / 100, 0, 0);
				if (now.isAfter(repeatDate)) {
					repeatDate = repeatDate.plusHours(24);
				}
				instanceCoolTime = repeatDate.getMillis();
			} else if (clt.getCoolTimeType().isWeekly()) {
				String[] days = clt.getTypeValue().split(",");
				instanceCoolTime = getUpdateHours(days, clt.getEntCoolTime() / 100);
			} else if (clt.getCoolTimeType().isRelative()) {
				switch(worldId) {
                    case 300480000: //Sealed Danuar Mysticarium.
                    case 300560000: //Shugo Imperial Tomb.
					case 301160000: //Nightmare Circus.
					case 301200000: //The Nightmare Circus.
					case 301320000: //Lucky Ophidan Bridge.
					case 301330000: //Lucky Danuar Reliquary.
					case 301400000: //The Shugo Emperor's Vault.
					case 301590000: //Emperor Trillirunerk's Safe.
					case 302350000: //Windy Gorge 5.5
					case 302370000: //5.6
					case 302420000: //5.6
					    DateTime now = DateTime.now();
					    DateTime repeatDate = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 9, 0, 0);
					    if (now.isAfter(repeatDate)) {
						    repeatDate = repeatDate.plusHours(24);
					    }
					    instanceCoolTime = repeatDate.getMillis();
					    instanceCoolTime = System.currentTimeMillis() + (clt.getEntCoolTime() * 60 * 1000);
					break;
				}
			}
		} if (instanceCooldownRate != 1) {
			instanceCoolTime = System.currentTimeMillis() + ((instanceCoolTime - System.currentTimeMillis()) / instanceCooldownRate);
		}
		return instanceCoolTime;
	}
	
	private long getUpdateHours(String[] days, int hour) {
        DateTime now = DateTime.now();
        DateTime repeatDate = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), hour, 0, 0);
        int curentDay = now.getDayOfWeek();
        for (String name : days) {
            int day = getDay(name);
            if (day < curentDay) {
                continue;
            } if (day == curentDay) {
                if (now.isBefore(repeatDate)) {
                    return repeatDate.getMillis();
                }
            } else {
                repeatDate = repeatDate.plusDays(day - curentDay);
                return repeatDate.getMillis();
            }
        }
        return repeatDate.plusDays((7 - curentDay) + getDay(days[0])).getMillis();
    }
	
	private int getDay(String day) {
        if (day.equals("Mon")) {
            return 1;
        } else if (day.equals("Tue")) {
            return 2;
        } else if (day.equals("Wed")) {
            return 3;
        } else if (day.equals("Thu")) {
            return 4;
        } else if (day.equals("Fri")) {
            return 5;
        } else if (day.equals("Sat")) {
            return 6;
        } else if (day.equals("Sun")) {
            return 7;
        }
        throw new IllegalArgumentException("Invalid Day: " + day);
    }
	
	public Integer size() {
		return instanceCooltimes.size();
	}
}