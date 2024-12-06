/*
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
package mysql5;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ReadStH;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.BrokerDAO;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.dao.ItemStoneListDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.broker.BrokerRace;
import com.aionemu.gameserver.model.gameobjects.BrokerItem;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MySQL5BrokerDAO extends BrokerDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5BrokerDAO.class);

	@Override
	public List<BrokerItem> loadBroker() {
	
		final List<BrokerItem> brokerItems = new ArrayList<BrokerItem>();
        final List<Item> items = getBrokerItems();
        
        if (items != null && items.size() > 0) {
            DAOManager.getDAO(ItemStoneListDAO.class).load(items);
        }

        DB.select("SELECT * FROM broker", new ReadStH() {

			@Override
			public void handleRead(ResultSet rset) throws SQLException {
				while (rset.next()) {
					int itemPointer = rset.getInt("item_pointer");
					int itemId = rset.getInt("item_id");
					long itemCount = rset.getLong("item_count");
					String itemCreator = rset.getString("item_creator");
					String seller = rset.getString("seller");
					int sellerId = rset.getInt("seller_id");
					long price = rset.getLong("price");
					BrokerRace itemBrokerRace = BrokerRace.valueOf(rset.getString("broker_race"));
					Timestamp expireTime = rset.getTimestamp("expire_time");
					Timestamp settleTime = rset.getTimestamp("settle_time");
                    int sold = rset.getInt("is_sold");
                    int settled = rset.getInt("is_settled");
					int SplitSell = rset.getInt("is_splitsell");

                    boolean isSold = sold == 1;
                    boolean isSettled = settled == 1;
					boolean isSplitSell = SplitSell == 1;

                    Item item = null;
					if (!isSold)
						for (Item brItem : items) {
						if (itemPointer == brItem.getObjectId()) {
							item = brItem;
							break;
						}
                    }

                    brokerItems.add(new BrokerItem(item, itemId, itemPointer, itemCount, itemCreator, price, seller, sellerId, itemBrokerRace, isSold, isSettled, expireTime, settleTime, isSplitSell));
                }
            }
        });
		return brokerItems;
	}

	private List<Item> getBrokerItems() {
		final List<Item> brokerItems = new ArrayList<Item>();

		DB.select("SELECT * FROM inventory WHERE `item_location` = 126", new ReadStH() {

			@Override
			public void handleRead(ResultSet rset) throws SQLException {
				while (rset.next()) {
					int itemUniqueId = rset.getInt("item_unique_id");
					int itemId = rset.getInt("item_id");
					long itemCount = rset.getLong("item_count");
					int itemColor = rset.getInt("item_color");
					int colorExpireTime = rset.getInt("color_expires");
					String itemCreator = rset.getString("item_creator");
					int expireTime = rset.getInt("expire_time");
					int activationCount = rset.getInt("activation_count");
					long slot = rset.getLong("slot");
					int location = rset.getInt("item_location");
					int enchant = rset.getInt("enchant");
					int enchantBonus = rset.getInt("enchant_bonus");
					int itemSkin = rset.getInt("item_skin");
					int fusionedItem = rset.getInt("fusioned_item");
					int optionalSocket = rset.getInt("optional_socket");
					int optionalFusionSocket = rset.getInt("optional_fusion_socket");
					int charge = rset.getInt("charge");
					Integer randomNumber = rset.getInt("rnd_bonus");
					int rndCount = rset.getInt("rnd_count");
					int wrappingCount = rset.getInt("wrappable_count");
					int temperingLevel = rset.getInt("tempering_level");
					int reductionLevel = rset.getInt("reduction_level");
					int unSeal = rset.getInt("is_seal");
					boolean isEnhance = rset.getBoolean("isEnhance");
					int enhanceSkillId = rset.getInt("enhanceSkillId");
					int enhanceSkillEnchant = rset.getInt("enhanceSkillEnchant");
					brokerItems.add(new Item(itemUniqueId, itemId, itemCount, itemColor, colorExpireTime, itemCreator, expireTime,
					activationCount, false, false, slot, location, enchant, enchantBonus, itemSkin, fusionedItem, optionalSocket,
					optionalFusionSocket, charge, randomNumber, rndCount, wrappingCount, false, temperingLevel, false, 0, 0, false, reductionLevel, unSeal, isEnhance, enhanceSkillId, enhanceSkillEnchant));
				}
			}
		});
		return brokerItems;
	}

	@Override
	public boolean store(BrokerItem item) {
		boolean result = false;

		if (item == null) {
			log.warn("Null broker item on save");
			return result;
		}

		switch (item.getPersistentState()) {
			case NEW:
				result = insertBrokerItem(item);
				if (item.getItem() != null) {
					DAOManager.getDAO(InventoryDAO.class).store(item.getItem(), item.getSellerId());
				}
				break;

            case DELETED:
                result = deleteBrokerItem(item);
                break;
				
            case UPDATE_ITEM_BROKER:
                result = updateItem(item);
                break;
				
            case UPDATE_REQUIRED:
                result = updateBrokerItem(item);
                break;
		}

		if (result) {
			item.setPersistentState(PersistentState.UPDATED);
        }
		return result;
	}

	private boolean insertBrokerItem(final BrokerItem item) {
		boolean result = DB.insertUpdate("INSERT INTO `broker` (`item_pointer`, `item_id`, `item_count`, `item_creator`, `seller`, `price`, `broker_race`, `expire_time`, `settle_time`, `seller_id`, `is_sold`, `is_settled`, `is_splitsell`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, item.getItemUniqueId());
				stmt.setInt(2, item.getItemId());
				stmt.setLong(3, item.getItemCount());
				stmt.setString(4, item.getItemCreator());
				stmt.setString(5, item.getSeller());
                stmt.setLong(6, item.getPrice());
                stmt.setString(7, String.valueOf(item.getItemBrokerRace()));
                stmt.setTimestamp(8, item.getExpireTime());
			    stmt.setTimestamp(9, item.getSettleTime());
                stmt.setInt(10, item.getSellerId());
                stmt.setBoolean(11, item.isSold());
                stmt.setBoolean(12, item.isSettled());
				stmt.setBoolean(13, item.isSplitSell());
                stmt.execute();
            }
        });
		return result;
	}

	private boolean deleteBrokerItem(final BrokerItem item) {
		boolean result = DB.insertUpdate(
			"DELETE FROM `broker` WHERE `item_pointer` = ? AND `seller_id` = ? AND `expire_time` = ?", new IUStH() {

				@Override
				public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
					stmt.setInt(1, item.getItemUniqueId());
					stmt.setInt(2, item.getSellerId());
					stmt.setTimestamp(3, item.getExpireTime());

					stmt.execute();
				}
			});
		return result;
	}

	@Override
	public boolean preBuyCheck(int itemForCheck) {
		PreparedStatement st = DB.prepareStatement("SELECT * FROM broker WHERE `item_pointer` = ? and `is_sold` = 0");
		log.info("Checking broker item: " + itemForCheck);
		try {
			st.setInt(1, itemForCheck);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e) {
			log.error("Can't to prebuy broker check: ", e);
		}
		finally {
			DB.close(st);
		}
		return false;
	}

	private boolean updateBrokerItem(final BrokerItem item) {
		boolean result = DB.insertUpdate("UPDATE broker SET `is_sold` = ?, `is_settled` = 1, `settle_time` = ? WHERE `item_pointer` = ? AND `expire_time` = ? AND `seller_id` = ? AND `is_settled` = 0", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				stmt.setBoolean(1, item.isSold());
				stmt.setTimestamp(2, item.getSettleTime());
				stmt.setInt(3, item.getItemUniqueId());
				stmt.setTimestamp(4, item.getExpireTime());
				stmt.setInt(5, item.getSellerId());
				stmt.execute();
			}
		});
        return result;
    }
	
    private boolean updateItem(final BrokerItem item) {
        boolean result = DB.insertUpdate("UPDATE broker SET `item_count` = ?, `price` = ?, `is_sold` = ?, `is_settled` = ?, `settle_time` = ?, `is_splitsell` = ? WHERE `item_pointer` = ? AND `expire_time` = ? AND `seller_id` = ? AND `is_settled` = 0", new IUStH() {
                @Override
                public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                    stmt.setLong(1, item.getItemCount());
				    stmt.setLong(2, item.getPrice());
					stmt.setBoolean(3, item.isSold());
					stmt.setBoolean(4, item.isSettled());
                    stmt.setTimestamp(5, item.getSettleTime());
					stmt.setBoolean(6, item.isSplitSell());
                    stmt.setInt(7, item.getItemUniqueId());
                    stmt.setTimestamp(8, item.getExpireTime());
                    stmt.setInt(9, item.getSellerId());
                    stmt.execute();
                }
            });
        return result;
    }	

    @Override
    public int[] getUsedIDs() {
        PreparedStatement statement = DB.prepareStatement("SELECT id FROM players", ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

		try {
			ResultSet rs = statement.executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			int[] ids = new int[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				ids[i] = rs.getInt("id");
			}
			return ids;
		}
		catch (SQLException e) {
			log.error("Can't get list of id's from players table", e);
		}
		finally {
			DB.close(statement);
		}
		return new int[0];
	}

	@Override
	public boolean supports(String s, int i, int i1) {
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}