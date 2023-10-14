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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class HousingConfig {
	@Property(key = "gameserver.housing.visibility.distance", defaultValue = "200")
	public static float VISIBILITY_DISTANCE = 200f;
	@Property(key = "gameserver.housedoor.showid", defaultValue = "true")
	public static boolean ENABLE_SHOW_HOUSE_DOORID;
	@Property(key = "gameserver.housedoor.accesslevel", defaultValue = "3")
	public static int ENTER_HOUSE_ACCESSLEVEL;
	@Property(key = "gameserver.housing.auction.enable", defaultValue = "false")
	public static boolean ENABLE_HOUSE_AUCTIONS;
	@Property(key = "gameserver.housing.pay.enable", defaultValue = "false")
	public static boolean ENABLE_HOUSE_PAY;
	@Property(key = "gameserver.housing.auction.time", defaultValue = "0 5 12 ? * SUN")
	public static String HOUSE_AUCTION_TIME;
	@Property(key = "gameserver.housing.auction.registerend", defaultValue = "0 0 0 ? * SAT")
	public static String HOUSE_REGISTER_END;
	@Property(key = "gameserver.housing.maintain.time", defaultValue = "0 0 0 ? * MON")
	public static String HOUSE_MAINTENANCE_TIME;
	@Property(key = "gameserver.housing.auction.default_bid.house", defaultValue = "12000000")
	public static int HOUSE_MIN_BID;
	@Property(key = "gameserver.housing.auction.default_bid.mansion", defaultValue = "112000000")
	public static int MANSION_MIN_BID;
	@Property(key = "gameserver.housing.auction.default_bid.estate", defaultValue = "335000000")
	public static int ESTATE_MIN_BID;
	@Property(key = "gameserver.housing.auction.default_bid.palace", defaultValue = "1000000000")
	public static int PALACE_MIN_BID;
	@Property(key = "gameserver.housing.auction.bidding.min_level.house", defaultValue = "21")
	public static int HOUSE_MIN_BID_LEVEL;
	@Property(key = "gameserver.housing.auction.bidding.min_level.mansion", defaultValue = "30")
	public static int MANSION_MIN_BID_LEVEL;
	@Property(key = "gameserver.housing.auction.bidding.min_level.estate", defaultValue = "40")
	public static int ESTATE_MIN_BID_LEVEL;
	@Property(key = "gameserver.housing.auction.bidding.min_level.palace", defaultValue = "50")
	public static int PALACE_MIN_BID_LEVEL;
	@Property(key = "gameserver.housing.auction.default_refund", defaultValue = "0.3f")
	public static float BID_REFUND_PERCENT;
	@Property(key = "gameserver.housing.auction.steplimit", defaultValue = "100")
	public static float HOUSE_AUCTION_BID_LIMIT;
	@Property(key = "gameserver.housing.scripts.debug", defaultValue = "false")
	public static boolean HOUSE_SCRIPT_DEBUG;
	@Property(key = "gameserver.housing.auction.fill.auto", defaultValue = "false")
	public static boolean FILL_HOUSE_BIDS_AUTO;
	@Property(key = "gameserver.housing.auction.fill.auto.houses", defaultValue = "20")
	public static int FILL_AUTO_HOUSES_COUNT;
	@Property(key = "gameserver.housing.auction.fill.auto.mansion", defaultValue = "10")
	public static int FILL_AUTO_MANSION_COUNT;
	@Property(key = "gameserver.housing.auction.fill.auto.estate", defaultValue = "5")
	public static int FILL_AUTO_ESTATE_COUNT;
	@Property(key = "gameserver.housing.auction.fill.auto.palace", defaultValue = "1")
	public static int FILL_AUTO_PALACE_COUNT;
}