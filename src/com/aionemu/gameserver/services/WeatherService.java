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
package com.aionemu.gameserver.services;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.world.WeatherEntry;
import com.aionemu.gameserver.model.templates.world.WeatherTable;
import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_WEATHER;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.gametime.DayTime;
import com.aionemu.gameserver.utils.gametime.GameTime;
import com.aionemu.gameserver.utils.gametime.GameTimeManager;
import com.aionemu.gameserver.world.World;

import java.util.*;

public class WeatherService
{
	private Map<WeatherKey, WeatherEntry[]> worldZoneWeathers;
	
    public static final WeatherService getInstance() {
        return SingletonHolder.instance;
    }
	
    private WeatherService() {
        worldZoneWeathers = new HashMap<WeatherKey, WeatherEntry[]>();
        GameTime gameTime = (GameTime) GameTimeManager.getGameTime().clone();
        for (Iterator<WorldMapTemplate> mapIterator = DataManager.WORLD_MAPS_DATA.iterator(); mapIterator.hasNext();) {
            int mapId = mapIterator.next().getMapId();
            WeatherTable table = DataManager.MAP_WEATHER_DATA.getWeather(mapId);
            if (table != null) {
                WeatherKey key = new WeatherKey(gameTime, mapId);
                worldZoneWeathers.put(key, new WeatherEntry[table.getZoneCount()]);
                setNextWeather(key);
            }
        }
    }
	
    private class WeatherKey {
        private GameTime created;
        private final int mapId;
		
        public WeatherKey(GameTime createdTime, int mapId) {
            this.created = createdTime;
            this.mapId = mapId;
        }
		
        public int getMapId() {
            return mapId;
        }
		
        public GameTime getCreatedTime() {
            return created;
        }
		
        @Override
        public boolean equals(Object o) {
            WeatherKey other = (WeatherKey) o;
            return this.mapId == other.mapId;
        }
		
        @Override
        public int hashCode() {
            return Integer.valueOf(mapId).hashCode();
        }
    }
	
    public void checkWeathersTime() {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                for (WeatherKey key : worldZoneWeathers.keySet()) {
                    setNextWeather(key);
                    onWeatherChange(key.getMapId(), null);
                }
            }
        }, 0);
    }
	
    private synchronized void setNextWeather(WeatherKey key) {
        WeatherEntry[] weatherEntries = getWeatherEntries(key.getMapId());
        WeatherTable table = DataManager.MAP_WEATHER_DATA.getWeather(key.getMapId());
        key.created = (GameTime) GameTimeManager.getGameTime().clone();
        for (int zoneIndex = 0; zoneIndex < weatherEntries.length; zoneIndex++) {
            WeatherEntry oldEntry = weatherEntries[zoneIndex];
            WeatherEntry newEntry = null;
            if (oldEntry == null) {
                newEntry = getRandomWeather(key.getCreatedTime(), table, zoneIndex + 1);
			} else {
                newEntry = table.getWeatherAfter(oldEntry);
                if (newEntry == null) {
                    newEntry = getRandomWeather(key.getCreatedTime(), table, zoneIndex + 1);
				}
            }
            weatherEntries[zoneIndex] = newEntry;
        }
    }
	
    private WeatherEntry getRandomWeather(GameTime createdTime, WeatherTable table, int zoneId) {
        List<WeatherEntry> weathers = table.getWeathersForZone(zoneId);
		int attRanking = 2;
		int chance = Rnd.get(1, 100);
		if (chance > 33) {
            attRanking = 0;
		} else if (chance > 50) {
            attRanking = 1;
		}
        List<WeatherEntry> chosenWeather = new ArrayList<WeatherEntry>();
        while (attRanking >= 0) {
            for (WeatherEntry entry : weathers) {
                if (entry.getAttRanking() == -1) {
                    return entry;
				} if (entry.getAttRanking() == attRanking) {
                    chosenWeather.add(entry);
				}
            } if (chosenWeather.size() > 0) {
                attRanking = -1;
                break;
            }
            attRanking--;
        }
        WeatherEntry newWeather = null;
        if (chosenWeather.size() == 0) {
            newWeather = new WeatherEntry();
        } else {
            newWeather = chosenWeather.get(Rnd.get(chosenWeather.size()));
            //Weather Before.
			if (!newWeather.isBefore()) {
                for (WeatherEntry entry: weathers) {
                    if (newWeather.getWeatherName().equals(entry.getWeatherName()) && entry.isBefore()) {
                        newWeather = entry;
                        break;
                    }
                }
            }
			//Weather After.
			if (!newWeather.isAfter()) {
                for (WeatherEntry entry: weathers) {
                    if (newWeather.getWeatherName().equals(entry.getWeatherName()) && entry.isAfter()) {
                        newWeather = entry;
                        break;
                    }
                }
            }
            int dayTimeCorrection = 1;
            if (createdTime.getDayTime() == DayTime.AFTERNOON) {
                dayTimeCorrection *= 2;
				chance = Rnd.get(1, 100);
			} if ((newWeather.getAttRanking() == 0 && chance > 33 / dayTimeCorrection) ||
			    (newWeather.getAttRanking() == 1 && chance > 50 / dayTimeCorrection) ||
				(newWeather.getAttRanking() == 2 && chance > 66 / dayTimeCorrection)) {
                newWeather = new WeatherEntry();
			}
        }
        return newWeather;
    }
	
    public void loadWeather(Player player) {
        onWeatherChange(player.getWorldId(), player);
    }
	
    private WeatherKey getWeatherKeyByMapId(int mapId) {
        for (WeatherKey key : worldZoneWeathers.keySet()) {
            if (key.getMapId() == mapId) {
                return key;
            }
        }
        return null;
    }
	
    private WeatherEntry[] getWeatherEntries(int mapId) {
        WeatherKey key = getWeatherKeyByMapId(mapId);
        if (key == null) {
            return null;
		}
        return worldZoneWeathers.get(key);
    }
	
    public synchronized void changeRegionWeather(int mapId, int weatherCode) {
        WeatherKey key = new WeatherKey(null, mapId);
        WeatherEntry[] weatherEntries = worldZoneWeathers.get(key);
        if (weatherEntries == null) {
            return;
		}
        for (int i = 0; i < weatherEntries.length; i++) {
            WeatherEntry oldEntry = weatherEntries[i];
            if (oldEntry == null) {
                weatherEntries[i] = new WeatherEntry(0, weatherCode);
			} else {
                weatherEntries[i] = new WeatherEntry(oldEntry.getZoneId(), weatherCode);
			}
        }
        onWeatherChange(mapId, null);
    }
	
    public synchronized void resetWeather() {
        Set<WeatherKey> loadedWeathers = new HashSet<WeatherKey>(worldZoneWeathers.keySet());
        for (WeatherKey key: loadedWeathers) {
            WeatherEntry[] oldEntries = worldZoneWeathers.get(key);
            for (int i = 0; i < oldEntries.length; i++) {
                oldEntries[i] = new WeatherEntry(oldEntries[i].getZoneId(), 0);
            }
            onWeatherChange(key.getMapId(), null);
        }
    }
	
    public int getWeatherCode(int mapId, int weatherZoneId) {
        WeatherEntry[] weatherEntries = getWeatherEntries(mapId);
        for (WeatherEntry entry : weatherEntries) {
            if (entry != null && entry.getZoneId() == weatherZoneId) {
                return entry.getCode();
			}
        }
        return 0;
    }
	
    private void onWeatherChange(int mapId, Player player) {
        WeatherEntry[] weatherEntries = getWeatherEntries(mapId);
        if (weatherEntries == null) {
            return;
		}
        if (player == null) {
            for (Iterator<Player> playerIterator = World.getInstance().getPlayersIterator(); playerIterator.hasNext(); ) {
                Player currentPlayer = playerIterator.next();
                if (!currentPlayer.isSpawned()) {
                    continue;
				}
                if (currentPlayer.getWorldId() == mapId) {
                    PacketSendUtility.sendPacket(currentPlayer, new SM_WEATHER(weatherEntries));
                }
            }
        } else {
            PacketSendUtility.sendPacket(player, new SM_WEATHER(weatherEntries));
        }
        for (WeatherEntry entry : weatherEntries) {
            SiegeService.getInstance().onWeatherChanged(entry);
		}
    }
	
    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final WeatherService instance = new WeatherService();
    }
}