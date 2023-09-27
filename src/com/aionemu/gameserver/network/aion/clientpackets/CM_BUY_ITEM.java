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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate;
import com.aionemu.gameserver.model.templates.tradelist.TradeNpcType;
import com.aionemu.gameserver.model.trade.RepurchaseList;
import com.aionemu.gameserver.model.trade.TradeList;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.PrivateStoreService;
import com.aionemu.gameserver.services.RepurchaseService;
import com.aionemu.gameserver.services.TradeService;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CM_BUY_ITEM extends AionClientPacket
{
    private static final Logger log = LoggerFactory.getLogger(CM_BUY_ITEM.class);
    private int sellerObjId;
    private int tradeActionId;
    private int amount;
    private int itemId;
    private long count;
    private boolean isAudit;
    private TradeList tradeList;
    private RepurchaseList repurchaseList;
   
    public CM_BUY_ITEM(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }
   
    @Override
    protected void readImpl() {
        Player player = getConnection().getActivePlayer();
        sellerObjId = readD();
        tradeActionId = readH();
        amount = readH();
        if (amount < 0 || amount > 36) {
            isAudit = true;
            AuditLogger.info(player, "Player might be abusing CM_BUY_ITEM amount: " + amount);
            return;
        } if (tradeActionId == 2) {
            repurchaseList = new RepurchaseList(sellerObjId);
        } else {
            tradeList = new TradeList(sellerObjId);
        }
        for (int i = 0; i < amount; i++) {
            itemId = readD();
            count = readQ();
            if (count < 0 || (itemId <= 0 && tradeActionId != 0) || itemId == 190000073 || itemId == 190000074 || count > 20000) {
                isAudit = true;
                AuditLogger.info(player, "Player might be abusing CM_BUY_ITEM item: " + itemId + " count: " + count);
                break;
            } switch (tradeActionId) {
                case 0: //[Private Store]
                case 1: //[Sell To Shop]
                case 17: //[Pet Seller]
                case 18: //Inventory Shop
                case 19: //Inventory to Shop x64
                     tradeList.addSellItem(itemId, count);
                break;
                case 2: //[Repurchase]
                    repurchaseList.addRepurchaseItem(player, itemId, count);
                break;
                case 13: //[Buy From Shop]
                case 14: //[Buy From Abyss Shop]
                case 15: //[Buy From Reward Shop]
                    tradeList.addBuyItem(itemId, count);
                break;
            }
        }
    }
   
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (isAudit || player == null) {
            return;
        }
        VisibleObject target = player.getKnownList().getKnownObjects().get(sellerObjId);
        //added x64 enum
        if (tradeActionId != 18 && target == null && tradeActionId != 19) {
            return;
        } if (target instanceof Player && tradeActionId == 0) {
            Player targetPlayer = (Player) target;
            PrivateStoreService.sellStoreItem(targetPlayer, player, tradeList);
        } else if (target instanceof Npc) {
            Npc npc = (Npc) target;
            TradeListTemplate tlist = DataManager.TRADE_LIST_DATA.getTradeListTemplate(npc.getNpcId());
            TradeListTemplate purchaseTemplate = DataManager.TRADE_LIST_DATA.getPurchaseListTemplate(npc.getNpcId());
            switch (tradeActionId) {
                case 1: //Sell To Shop [Panesterra 4.7]
                    if (npc.getObjectTemplate().getTitleId() == 357001 || //Belus Relic Supervisor.
                        npc.getObjectTemplate().getTitleId() == 357002 || //Belus Abyss Equipment Merchand.
                        npc.getObjectTemplate().getTitleId() == 357013 || //Aspida Relic Supervisor.
                        npc.getObjectTemplate().getTitleId() == 357014 || //Aspida Abyss Equipment Merchand.
                        npc.getObjectTemplate().getTitleId() == 357025 || //Atanatos Relic Supervisor.
                        npc.getObjectTemplate().getTitleId() == 357026 || //Atanatos Abyss Equipment Merchand.
                        npc.getObjectTemplate().getTitleId() == 357037 || //Disilon Relic Supervisor.
                        npc.getObjectTemplate().getTitleId() == 357038 || //Disilon Abyss Equipment Merchand.
                        //Sell To Shop [Purchase List AP 4.3]
                        npc.getObjectTemplate().getTitleId() == 463209 ||
                        npc.getObjectTemplate().getTitleId() == 463222 ||
                        npc.getObjectTemplate().getTitleId() == 463224 ||
                        npc.getObjectTemplate().getTitleId() == 463230 ||
                        npc.getObjectTemplate().getTitleId() == 463491 ||
                        npc.getObjectTemplate().getTitleId() == 463492 ||
                        npc.getObjectTemplate().getTitleId() == 463493 ||
                        npc.getObjectTemplate().getTitleId() == 463495 ||
                        npc.getObjectTemplate().getTitleId() == 463628 ||
                        npc.getObjectTemplate().getTitleId() == 463648 ||
                        npc.getObjectTemplate().getTitleId() == 464194 ||
                        npc.getObjectTemplate().getTitleId() == 464201 ||
                        npc.getObjectTemplate().getTitleId() == 466388 ||
                        //Sell To Shop [Purchase List AP 4.8]
                        npc.getObjectTemplate().getTitleId() == 314357 || //Ancient Icon Administration Officer.
                        npc.getObjectTemplate().getTitleId() == 314358 || //Ancient Seal Administration Officer.
                        npc.getObjectTemplate().getTitleId() == 314359 || //Ancient Goblet Administration Officer.
                        npc.getObjectTemplate().getTitleId() == 314360 || //Ancient Crown Administration Officer.
                        npc.getObjectTemplate().getTitleId() == 357852 ||
                        npc.getObjectTemplate().getTitleId() == 358081 ||
                        npc.getObjectTemplate().getTitleId() == 358082 ||
                        npc.getObjectTemplate().getTitleId() == 358083 ||
                        npc.getObjectTemplate().getTitleId() == 358086 ||
                        npc.getObjectTemplate().getTitleId() == 358096 ||
                        npc.getObjectTemplate().getTitleId() == 358100 ||
                        npc.getObjectTemplate().getTitleId() == 358113 ||
                        npc.getObjectTemplate().getTitleId() == 358114 ||
                        npc.getObjectTemplate().getTitleId() == 358510 || //Ancien Relic Supervisor.
                        npc.getObjectTemplate().getTitleId() == 358540 || //Ancien Relic Supervisor.
                        npc.getObjectTemplate().getTitleId() == 370408 || //Ancien Icon Custodian.
                        npc.getObjectTemplate().getTitleId() == 370409 || //Ancien Seal Custodian.
                        npc.getObjectTemplate().getTitleId() == 370410 || //Ancien Goblet Custodian.
                        npc.getObjectTemplate().getTitleId() == 370411 || //Ancien Crown Custodian.
                        //Sell To Shop [Purchase List AP 5.3/5.5]
                        npc.getObjectTemplate().getTitleId() == 468783) {
                        TradeService.performSellForAPToShop(player, tradeList, purchaseTemplate);
                    }
                    //Sell To Shop [Purchase List Kinah 4.3]
                    else if (npc.getObjectTemplate().getTitleId() == 463203 ||
                        npc.getObjectTemplate().getTitleId() == 463206 ||
                        npc.getObjectTemplate().getTitleId() == 463490) {
                        TradeService.performSellForKinahToShop(player, tradeList, purchaseTemplate);
                    } else {
                        TradeService.performSellToShop(player, tradeList);
                    }
                break;
                case 2: //[Repurchase]
                    RepurchaseService.getInstance().repurchaseFromShop(player, repurchaseList);
                break;
                case 13: //[Buy From Shop]
                    if (tlist != null && tlist.getTradeNpcType() == TradeNpcType.NORMAL) {
                        TradeService.performBuyFromShop(npc, player, tradeList);
                    }
                break;
                case 14: //[Buy From Abyss Shop]
                    if (tlist != null && tlist.getTradeNpcType() == TradeNpcType.ABYSS) {
                        TradeService.performBuyFromAbyssShop(npc, player, tradeList);
                    }
                break;
                case 15: //[Buy From Reward Shop]
                    if (tlist != null && tlist.getTradeNpcType() == TradeNpcType.REWARD) {
                        TradeService.performBuyFromRewardShop(npc, player, tradeList);
                    }
                break;
                case 17: //[Pet Seller]
                    TradeService.performSellForKinahToShop(player, tradeList, purchaseTemplate);
                break;
                default:
                    log.info(String.format("Unhandle shop action unk1: %d", tradeActionId));
                break;
            }
        } if (tradeActionId == 18 || tradeActionId == 19 ) { //Inventory Shop
            TradeService.performSellToShop(player, tradeList);
        }
    }
}