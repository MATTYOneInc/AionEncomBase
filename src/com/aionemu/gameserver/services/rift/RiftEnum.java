/**
 * This file is part of Encom.
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
package com.aionemu.gameserver.services.rift;

import com.aionemu.gameserver.model.Race;

/****/
/** Author Rinzler (Encom)
/****/

public enum RiftEnum
{
   /**
	* Elyos Rift
	*/
	//Dimensional Vortex 3.9
	KAISINEL_AM(1170, "KAISINEL_AM", "KAISINEL_AS", 24, 10000, 46, 65, Race.ASMODIANS, true),
	
	//Eltnen Rift 4.9
	ELTNEN_AM(2120, "ELTNEN_AM", "MORHEIM_AS", 20, 32, 20, 30, Race.ASMODIANS),
	ELTNEN_BM(2121, "ELTNEN_BM", "MORHEIM_BS", 30, 32, 20, 30, Race.ASMODIANS),
	ELTNEN_CM(2122, "ELTNEN_CM", "MORHEIM_CS", 40, 32, 20, 32, Race.ASMODIANS),
	ELTNEN_DM(2123, "ELTNEN_DM", "MORHEIM_DS", 40, 32, 20, 32, Race.ASMODIANS),
	ELTNEN_EM(2124, "ELTNEN_EM", "MORHEIM_ES", 45, 32, 20, 32, Race.ASMODIANS),
	ELTNEN_FM(2125, "ELTNEN_FM", "MORHEIM_FS", 50, 32, 20, 32, Race.ASMODIANS),
	ELTNEN_GM(2126, "ELTNEN_GM", "MORHEIM_GS", 50, 32, 20, 35, Race.ASMODIANS),
	
	//Heiron Rift 4.9
	HEIRON_AM(2140, "HEIRON_AM", "BELUSLAN_AS", 30, 32, 20, 40, Race.ASMODIANS),
	HEIRON_BM(2141, "HEIRON_BM", "BELUSLAN_BS", 40, 32, 20, 40, Race.ASMODIANS),
	HEIRON_CM(2142, "HEIRON_CM", "BELUSLAN_CS", 50, 32, 20, 42, Race.ASMODIANS),
	HEIRON_DM(2143, "HEIRON_DM", "BELUSLAN_DS", 50, 32, 20, 42, Race.ASMODIANS),
	HEIRON_EM(2144, "HEIRON_EM", "BELUSLAN_ES", 60, 32, 20, 42, Race.ASMODIANS),
	HEIRON_FM(2145, "HEIRON_FM", "BELUSLAN_FS", 60, 32, 20, 42, Race.ASMODIANS),
	HEIRON_GM(2146, "HEIRON_GM", "BELUSLAN_GS", 144, 32, 20, 45, Race.ASMODIANS),
	
	//Inggison Rift 4.8
	INGGISON_AM(2150, "INGGISON_AM", "GELKMAROS_AS", 150, 24, 50, 75, Race.ASMODIANS),
	INGGISON_BM(2151, "INGGISON_BM", "GELKMAROS_BS", 150, 24, 50, 75, Race.ASMODIANS),
	INGGISON_CM(2152, "INGGISON_CM", "GELKMAROS_CS", 150, 24, 50, 75, Race.ASMODIANS),
	INGGISON_DM(2153, "INGGISON_DM", "GELKMAROS_DS", 150, 24, 50, 75, Race.ASMODIANS),
	//Inggison Volatile Rift 4.8
	INGGISON_EM(2154, "INGGISON_EM", "GELKMAROS_ES", 6, 36, 50, 75, Race.ASMODIANS),
	INGGISON_FM(2155, "INGGISON_FM", "GELKMAROS_FS", 6, 36, 50, 75, Race.ASMODIANS),
	INGGISON_GM(2156, "INGGISON_GM", "GELKMAROS_GS", 6, 36, 50, 75, Race.ASMODIANS),
	INGGISON_HM(2157, "INGGISON_HM", "GELKMAROS_HS", 6, 36, 50, 75, Race.ASMODIANS),
	INGGISON_IM(2158, "INGGISON_IM", "GELKMAROS_IS", 6, 36, 50, 75, Race.ASMODIANS),
	INGGISON_JM(2159, "INGGISON_JM", "GELKMAROS_JS", 6, 36, 50, 75, Race.ASMODIANS),
	
	//Cygnea Rift 4.8
	CYGNEA_AM(2170, "CYGNEA_AM", "ENSHAR_AS", 12, 24, 50, 75, Race.ASMODIANS),
	CYGNEA_BM(2171, "CYGNEA_BM", "ENSHAR_BS", 36, 24, 50, 75, Race.ASMODIANS),
	CYGNEA_CM(2172, "CYGNEA_CM", "ENSHAR_CS", 48, 24, 55, 75, Race.ASMODIANS),
	CYGNEA_DM(2173, "CYGNEA_DM", "ENSHAR_DS", 48, 24, 55, 75, Race.ASMODIANS),
	CYGNEA_EM(2174, "CYGNEA_EM", "ENSHAR_ES", 48, 24, 55, 75, Race.ASMODIANS),
	CYGNEA_FM(2175, "CYGNEA_FM", "ENSHAR_FS", 48, 24, 55, 75, Race.ASMODIANS),
	//Cygnea Volatile Rift 4.8
	CYGNEA_GM(2176, "CYGNEA_GM", "ENSHAR_GS", 144, 36, 60, 75, Race.ASMODIANS),
	CYGNEA_HM(2177, "CYGNEA_HM", "ENSHAR_HS", 144, 36, 60, 75, Race.ASMODIANS),
	CYGNEA_IM(2178, "CYGNEA_IM", "ENSHAR_IS", 144, 36, 60, 75, Race.ASMODIANS),
	
	//Iluma Rift 5.0
	ILUMA_AM(2101, "ILUMA_AM", "NORSVOLD_AS", 24, 84, 66, 83, Race.ASMODIANS),
	ILUMA_BM(2102, "ILUMA_BM", "NORSVOLD_AS", 24, 84, 66, 83, Race.ASMODIANS),
	ILUMA_CM(2103, "ILUMA_CM", "NORSVOLD_AS", 24, 84, 66, 83, Race.ASMODIANS),
	ILUMA_DM(2104, "ILUMA_DM", "NORSVOLD_AS", 24, 84, 66, 83, Race.ASMODIANS),
	ILUMA_EM(2105, "ILUMA_EM", "NORSVOLD_AS", 24, 84, 66, 83, Race.ASMODIANS),
	ILUMA_FM(2106, "ILUMA_FM", "NORSVOLD_AS", 24, 84, 66, 83, Race.ASMODIANS),
	ILUMA_GM(2107, "ILUMA_GM", "NORSVOLD_AS", 24, 84, 66, 83, Race.ASMODIANS),
	
   /**
	* Asmodians Rift
	*/
	//Dimensional Vortex 3.9
	MARCHUTAN_AM(1280, "MARCHUTAN_AM", "MARCHUTAN_AS", 24, 10000, 46, 65, Race.ELYOS, true),
	
	//Morheim Rift 4.9
	MORHEIM_AM(2220, "MORHEIM_AM", "ELTNEN_AS", 20, 32, 20, 30, Race.ELYOS),
	MORHEIM_BM(2221, "MORHEIM_BM", "ELTNEN_BS", 30, 32, 20, 30, Race.ELYOS),
	MORHEIM_CM(2222, "MORHEIM_CM", "ELTNEN_CS", 40, 32, 20, 32, Race.ELYOS),
	MORHEIM_DM(2223, "MORHEIM_DM", "ELTNEN_DS", 40, 32, 20, 32, Race.ELYOS),
	MORHEIM_EM(2224, "MORHEIM_EM", "ELTNEN_ES", 45, 32, 20, 32, Race.ELYOS),
	MORHEIM_FM(2225, "MORHEIM_FM", "ELTNEN_FS", 50, 32, 20, 32, Race.ELYOS),
	MORHEIM_GM(2226, "MORHEIM_GM", "ELTNEN_GS", 50, 32, 20, 35, Race.ELYOS),
	
	//Beluslan Rift 4.9
	BELUSLAN_AM(2240, "BELUSLAN_AM", "HEIRON_AS", 30, 32, 20, 40, Race.ELYOS),
	BELUSLAN_BM(2241, "BELUSLAN_BM", "HEIRON_BS", 40, 32, 20, 40, Race.ELYOS),
	BELUSLAN_CM(2242, "BELUSLAN_CM", "HEIRON_CS", 50, 32, 20, 42, Race.ELYOS),
	BELUSLAN_DM(2243, "BELUSLAN_DM", "HEIRON_DS", 50, 32, 20, 42, Race.ELYOS),
	BELUSLAN_EM(2244, "BELUSLAN_EM", "HEIRON_ES", 60, 32, 20, 42, Race.ELYOS),
	BELUSLAN_FM(2245, "BELUSLAN_FM", "HEIRON_FS", 60, 32, 20, 42, Race.ELYOS),
	BELUSLAN_GM(2246, "BELUSLAN_GM", "HEIRON_GS", 144, 32, 20, 45, Race.ELYOS),
	
	//Gelkmaros Rift 4.8
	GELKMAROS_AM(2270, "GELKMAROS_AM", "INGGISON_AS", 150, 24, 50, 75, Race.ELYOS),
	GELKMAROS_BM(2271, "GELKMAROS_BM", "INGGISON_BS", 150, 24, 50, 75, Race.ELYOS),
	GELKMAROS_CM(2272, "GELKMAROS_CM", "INGGISON_CS", 150, 24, 50, 75, Race.ELYOS),
	GELKMAROS_DM(2273, "GELKMAROS_DM", "INGGISON_DS", 150, 24, 50, 75, Race.ELYOS),
	//Gelkmaros Volatile Rift 4.8
	GELKMAROS_EM(2274, "GELKMAROS_EM", "INGGISON_ES", 6, 36, 50, 75, Race.ELYOS),
	GELKMAROS_FM(2275, "GELKMAROS_FM", "INGGISON_FS", 6, 36, 50, 75, Race.ELYOS),
	GELKMAROS_GM(2276, "GELKMAROS_GM", "INGGISON_GS", 6, 36, 50, 75, Race.ELYOS),
	GELKMAROS_HM(2277, "GELKMAROS_HM", "INGGISON_HS", 6, 36, 50, 75, Race.ELYOS),
	GELKMAROS_IM(2278, "GELKMAROS_IM", "INGGISON_IS", 6, 36, 50, 75, Race.ELYOS),
	GELKMAROS_JM(2279, "GELKMAROS_JM", "INGGISON_JS", 6, 36, 50, 75, Race.ELYOS),
	
	//Enshar Rift 4.8
	ENSHAR_AM(2280, "ENSHAR_AM", "CYGNEA_AS", 12, 24, 50, 75, Race.ELYOS),
	ENSHAR_BM(2281, "ENSHAR_BM", "CYGNEA_BS", 36, 24, 50, 75, Race.ELYOS),
	ENSHAR_CM(2282, "ENSHAR_CM", "CYGNEA_CS", 48, 24, 55, 75, Race.ELYOS),
	ENSHAR_DM(2283, "ENSHAR_DM", "CYGNEA_DS", 48, 24, 55, 75, Race.ELYOS),
	ENSHAR_EM(2284, "ENSHAR_EM", "CYGNEA_ES", 48, 24, 55, 75, Race.ELYOS),
	ENSHAR_FM(2285, "ENSHAR_FM", "CYGNEA_FS", 48, 24, 55, 75, Race.ELYOS),
	//Enshar Volatile Rift 4.8
	ENSHAR_GM(2286, "ENSHAR_GM", "CYGNEA_GS", 144, 36, 60, 75, Race.ELYOS),
	ENSHAR_HM(2287, "ENSHAR_HM", "CYGNEA_HS", 144, 36, 60, 75, Race.ELYOS),
	ENSHAR_IM(2288, "ENSHAR_IM", "CYGNEA_IS", 144, 36, 60, 75, Race.ELYOS),
	
	//Norsvold Rift 5.0
	NORSVOLD_AM(2201, "NORSVOLD_AM", "ILUMA_AS", 24, 84, 66, 83, Race.ELYOS),
	NORSVOLD_BM(2202, "NORSVOLD_BM", "ILUMA_AS", 24, 84, 66, 83, Race.ELYOS),
	NORSVOLD_CM(2203, "NORSVOLD_CM", "ILUMA_AS", 24, 84, 66, 83, Race.ELYOS),
	NORSVOLD_DM(2204, "NORSVOLD_DM", "ILUMA_AS", 24, 84, 66, 83, Race.ELYOS),
	NORSVOLD_EM(2205, "NORSVOLD_EM", "ILUMA_AS", 24, 84, 66, 83, Race.ELYOS),
	NORSVOLD_FM(2206, "NORSVOLD_FM", "ILUMA_AS", 24, 84, 66, 83, Race.ELYOS),
	NORSVOLD_GM(2207, "NORSVOLD_GM", "ILUMA_AS", 24, 84, 66, 83, Race.ELYOS);
	
	private int id;
	private String master;
	private String slave;
	private int entries;
	private int abyssPoint;
	private int minLevel;
	private int maxLevel;
	private Race destination;
	private boolean vortex;
	
	private RiftEnum(int id, String master, String slave, int entries, int abyssPoint, int minLevel, int maxLevel, Race destination) {
		this(id, master, slave, entries, abyssPoint, minLevel, maxLevel, destination, false);
	}
	
	private RiftEnum(int id, String master, String slave, int entries, int abyssPoint, int minLevel, int maxLevel, Race destination, boolean vortex) {
		this.id = id;
		this.master = master;
		this.slave = slave;
		this.entries = entries;
		this.abyssPoint = abyssPoint;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.destination = destination;
		this.vortex = vortex;
	}
	
	public static RiftEnum getRift(int id) throws IllegalArgumentException {
		for (RiftEnum rift : RiftEnum.values()) {
			if (rift.getId() == id) {
				return rift;
			}
		}
		throw new IllegalArgumentException("Unsupported rift id: " + id);
	}
	
	public static RiftEnum getVortex(Race race) throws IllegalArgumentException {
		for (RiftEnum rift : RiftEnum.values()) {
			if (rift.isVortex() && rift.getDestination().equals(race)) {
				return rift;
			}
		}
		throw new IllegalArgumentException("Unsupported vortex race: " + race);
	}
	
	public int getId() {
		return id;
	}
	
	public String getMaster() {
		return master;
	}
	
	public String getSlave() {
		return slave;
	}
	
	public int getEntries() {
		return entries;
	}
	
	public int getAbyssPoint() {
		return abyssPoint;
	}
	
	public int getMinLevel() {
		return minLevel;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public Race getDestination() {
		return destination;
	}
	
	public boolean isVortex() {
		return vortex;
	}
}