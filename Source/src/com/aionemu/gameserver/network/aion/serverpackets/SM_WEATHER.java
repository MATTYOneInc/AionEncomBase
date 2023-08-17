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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.templates.world.WeatherEntry;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer
 * @author Kwazar
 * @author Nemesiss :D:D
 */
public class SM_WEATHER extends AionServerPacket {

	private WeatherEntry[] weatherEntries;

	public SM_WEATHER(WeatherEntry[] weatherEntries) {
		this.weatherEntries = weatherEntries;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(0x00);// unk
		writeC(weatherEntries.length);
		for (WeatherEntry entry : weatherEntries) {
			writeC(entry.getCode());
		}
	}
}