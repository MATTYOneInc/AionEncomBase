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
package com.aionemu.gameserver.model;

import org.apache.commons.lang.NotImplementedException;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum Race
{
	ELYOS(0, new DescriptionId(480480)),
	ASMODIANS(1, new DescriptionId(480481)),
	
	LYCAN(2),
	CONSTRUCT(3),
	CARRIER(4),
	DRAKAN(5),
	LIZARDMAN(6),
	TELEPORTER(7),
	NAGA(8),
	BROWNIE(9),
	KRALL(10),
	SHULACK(11),
	BARRIER(12),
	PC_LIGHT_CASTLE_DOOR(13),
	PC_DARK_CASTLE_DOOR(14),
	DRAGON_CASTLE_DOOR(15),
	GCHIEF_LIGHT(16),
	GCHIEF_DARK(17),
	DRAGON(18),
	OUTSIDER(19),
	RATMAN(20),
	DEMIHUMANOID(21),
	UNDEAD(22),
	BEAST(23),
	MAGICALMONSTER(24),
	ELEMENTAL(25),
	LIVINGWATER(28),
	NONE(26),
	PC_ALL(27),
	DEFORM(28),
	NEUT(29),
	GHENCHMAN_LIGHT(30),
	GHENCHMAN_DARK(31),
	EVENT_TOWER_DARK(32),
	EVENT_TOWER_LIGHT(33),
	GOBLIN(34),
	TRICODARK(35),
	NPC(36),
	LIGHT(37),
	DARK(38),
	WORLD_EVENT_DEFTOWER(39),
	ORC(40),
	DRAGONET(41),
	SIEGEDRAKAN(42),
	GCHIEF_DRAGON(43),
	WORLD_EVENT_BONFIRE(44),
	BATTLEGROUND_LI(45),
	BATTLEGROUND_DA(46),
	TYPE_A(47),
	TYPE_B(48),
	TYPE_C(49),
	TYPE_D(50),
	GINSENGS(51),
	EVENT_YEAR(52),
	F6_RAID_BOSS(53),
	HUMAN(54),
	TYPE_E(55),
	EVENT_FIFTH_01(56),
	EVENT_FIFTH_02(57),
	EVENT_HALLOWEEN(58);
	
	private int raceId;
	private DescriptionId descriptionId;
	
	private Race(int raceId) {
		this(raceId, null);
	}
	
	private Race(int raceId, DescriptionId descriptionId) {
		this.raceId = raceId;
		this.descriptionId = descriptionId;
	}
	
	public int getRaceId() {
		return raceId;
	}
	
	public boolean isPlayerRace() {
		return raceId < 2 || raceId == 27;
	}
	
	public DescriptionId getRaceDescriptionId(){
		if (descriptionId == null) {
			throw new NotImplementedException("Race name DescriptionId is unknown for race" + this);
		}
		return descriptionId;
	}
	
	public static Race getRaceByString(String fieldName) {
		for (Race r: values()) {
			if (r.toString().equals(fieldName)) {
				return r;
			}
		}
		return null;
	}
}