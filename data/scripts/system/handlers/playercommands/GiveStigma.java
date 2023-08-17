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
package playercommands;

import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_COMPLETED_LIST;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.ClassChangeService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import javolution.util.FastList;

/**
 * Created by K1ll3r
 */
public class GiveStigma extends PlayerCommand {
    public GiveStigma(){
        super("givestigma");
    }

    private static int[] elyosStigmaQuests = {1929, 3930, 3931, 3932, 11049, 11276, 11550, 30217};
    private static int[] asmodianStigmaQuests = {2900, 4934, 4935, 4936, 21049, 21278, 21550, 30317};


    public void execute(final Player player, String...param){
        if(param.length < 1){
            PacketSendUtility.sendMessage(player, " You can always remove the remaining items:\n .givestigma clean\n .givestigma add\n .givestigma unlock\n .givestigma class");
            return;
        }

        if(param[0].equals("unlock")){
            if (!(player.getPlayerAccount().getMembership() >= MembershipConfig.STIGMA_SLOT_QUEST)){
                PacketSendUtility.sendMessage(player, "Sorry! Stigma slot unlock is disabled!");
                return;
            }
            int stig = player.getCommonData().getAdvancedStigmaSlotSize();
            if(player.getRace() == Race.ELYOS){
                for (int quest_id : elyosStigmaQuests) {
                    completeQuest(player, quest_id);
                }
                if(stig < 12){
                    player.getCommonData().setAdvancedStigmaSlotSize(12);
                }
                PacketSendUtility.sendMessage(player, "You have unlocked all the stigma Slots, Try Relog Now");

            }else if(player.getRace() == Race.ASMODIANS){
                for (int quest_id : asmodianStigmaQuests) {
                    completeQuest(player, quest_id);
                }
                if(stig < 12){
                    player.getCommonData().setAdvancedStigmaSlotSize(12);
                }
                PacketSendUtility.sendMessage(player, "You have unlocked all the stigma Slots, Try Relog Now");
            }
        }

        if(param[0].equals("class")){
            ClassChangeService.showClassChangeDialog(player);
            return;
        }

        if(param[0].equals("clean")){
            clean(player);
            return;
        }

        if(param[0].equals("add")){
            RequestResponseHandler RequestHim = new RequestResponseHandler(player) {
                @Override
                public void acceptRequest(Creature requester, Player responder) {
                    switch (player.getPlayerClass()){
                        case GLADIATOR:
                            glad(player, false);
                            PacketSendUtility.sendMessage(player, "You have successfully added 'GLADIATOR' stigma list!!");
                            break;
                        case TEMPLAR:
                            temp(player, false);
                            PacketSendUtility.sendMessage(player, "You have successfully added 'TEMPLAR' stigma list!!");
                            break;
                        case ASSASSIN:
                            sin(player, false);
                            PacketSendUtility.sendMessage(player, "You have successfully added 'ASSASSIN' stigma list!!");
                            break;
                        case RANGER:
                            ranger(player, false);
                            PacketSendUtility.sendMessage(player, "You have successfully added 'RANGER' stigma list!!");
                            break;
                        case SORCERER:
                            sorc(player, false);
                            PacketSendUtility.sendMessage(player, "You have successfully added 'SORCERER' stigma list!!");
                            break;
                        case SPIRIT_MASTER:
                            sm(player, false);
                            PacketSendUtility.sendMessage(player, "You have successfully added 'SPIRITMASTER' stigma list!!");
                            break;
                        case CLERIC:
                            cleric(player, false);
                            PacketSendUtility.sendMessage(player, "You have successfully added 'CLERIC' stigma list!!");
                            break;
                        case CHANTER:
                            chanter(player, false);
                            PacketSendUtility.sendMessage(player, "You have successfully added 'CHANTER' stigma list!!");
                            break;
                        case GUNSLINGER:
                            gunner(player, false);
                            PacketSendUtility.sendMessage(player, "You have successfully added 'GUNNER' stigma list!!");
                            break;
                        case AETHERTECH:
                            at(player, false);
                            PacketSendUtility.sendMessage(player, "You have successfully added 'AETHERTECH' stigma list!!");
                            break;
                        case SONGWEAVER:
                            bard(player, false);
                            PacketSendUtility.sendMessage(player, "You have successfully added 'BARD' stigma list!!");
                            break;
                        default:
                            PacketSendUtility.sendMessage(player, "Unknown Class ? ");
                            break;
                    }
                }
                @Override
                public void denyRequest(Creature requester, Player responder) {

                }
            };
            boolean areyousure = player.getResponseRequester().putRequest(1300564, RequestHim);
            if (areyousure){
                PacketSendUtility.sendPacket(player,new SM_QUESTION_WINDOW(1300564, 0, 0, "You are about to add 30 to 35 items to your inventory ( All the stigma skills to your class ) . Would you like to add them all?\n Be sure to have atleast free 1 cube : ) !"));
            }
        }
    }

    private static void completeQuest(Player player, int questId) {
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null) {
            QuestState qState = player.getQuestStateList().getQuestState(questId);
            qState.setStatus(QuestStatus.COMPLETE);
            qState.setQuestVar(1);
            qState.setCompleteCount(qState.getCompleteCount() + 1);
            PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, qState.getStatus(), qState.getQuestVars().getQuestVars()));
        } else {
            player.getQuestStateList().addQuest(questId, new QuestState(questId, QuestStatus.START, 0, 0, null, 0, null));
            PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, 3, 0));
            player.getController().updateNearbyQuests();
            completeQuest(player, questId);
        }
        PacketSendUtility.sendPacket(player, new SM_QUEST_COMPLETED_LIST(player.getQuestStateList().getAllFinishedQuests()));
    }

    private void getStigmaStones(Player player, FastList<Integer> stoneList){
        for (FastList.Node<Integer> n = stoneList.head(), end = stoneList.tail(); (n = n.getNext()) != end; ) {
            ItemService.addItem(player, n.getValue(), 1);
        }
    }

    private void getRidOfStigmaStones(Player player, FastList<Integer> stoneList){
        for (FastList.Node<Integer> n = stoneList.head(), end = stoneList.tail(); (n = n.getNext()) != end; ){
            player.getInventory().decreaseByItemId(n.getValue(), 1);
        }
    }

    private FastList<Integer> glad(Player player, boolean getStoneIds){
        FastList<Integer> stones = new FastList<Integer>();
        stones.add(140001119);
        stones.add(140001118);
        stones.add(140001117);
        stones.add(140001116);
        stones.add(140001115);
        stones.add(140001114);
        stones.add(140001113);
        stones.add(140001112);
        stones.add(140001111);
        stones.add(140001110);
        stones.add(140001109);
        stones.add(140001108);
        stones.add(140001107);
        stones.add(140001106);
        stones.add(140001105);
        stones.add(140001104);
        stones.add(140001103);

        if (getStoneIds){
            return stones;
        }

        getStigmaStones(player, stones);
        return null;
    }
    private FastList<Integer> temp(Player player, boolean getStoneIds){
        FastList<Integer> stones = new FastList<Integer>();

        stones.add(140001135);
        stones.add(140001134);
        stones.add(140001133);
        stones.add(140001132);
        stones.add(140001131);
        stones.add(140001130);
        stones.add(140001129);
        stones.add(140001128);
        stones.add(140001127);
        stones.add(140001126);
        stones.add(140001125);
        stones.add(140001124);
        stones.add(140001123);
        stones.add(140001122);
        stones.add(140001121);
        stones.add(140001120);
        stones.add(140001114);

        if (getStoneIds){
            return stones;
        }

        getStigmaStones(player, stones);
        return null;
    }
    private FastList<Integer> sin(Player player, boolean getStoneIds){
        FastList<Integer> stones = new FastList<Integer>();

        stones.add(140001152);
        stones.add(140001151);
        stones.add(140001150);
        stones.add(140001149);
        stones.add(140001148);
        stones.add(140001147);
        stones.add(140001146);
        stones.add(140001145);
        stones.add(140001144);
        stones.add(140001143);
        stones.add(140001142);
        stones.add(140001141);
        stones.add(140001140);
        stones.add(140001139);
        stones.add(140001138);
        stones.add(140001137);
        stones.add(140001136);

        if (getStoneIds){
            return stones;
        }

        getStigmaStones(player, stones);
        return null;
    }
    private FastList<Integer> ranger(Player player, boolean getStoneIds){
        FastList<Integer> stones = new FastList<Integer>();

        if(player.getRace() == Race.ELYOS){
            stones.add(140001169);
            stones.add(140001165);
            stones.add(140001163);
            stones.add(140001160);
        }else{
            stones.add(140001170);
            stones.add(140001166);
            stones.add(140001164);
            stones.add(140001161);
        }

        stones.add(140001173);
        stones.add(140001172);
        stones.add(140001171);
        stones.add(140001168);
        stones.add(140001167);
        stones.add(140001162);
        stones.add(140001159);
        stones.add(140001158);
        stones.add(140001157);
        stones.add(140001156);
        stones.add(140001155);
        stones.add(140001154);
        stones.add(140001153);

        if (getStoneIds){
            return stones;
        }

        getStigmaStones(player, stones);
        return null;
    }
    private FastList<Integer> sorc(Player player, boolean getStoneIds){
        FastList<Integer> stones = new FastList<Integer>();

        if(player.getRace() == Race.ELYOS){
            stones.add(140001184);
            stones.add(140001182);
        }else{
            stones.add(140001185);
            stones.add(140001183);

        }

        stones.add(140001192);
        stones.add(140001191);
        stones.add(140001190);
        stones.add(140001189);
        stones.add(140001188);
        stones.add(140001187);
        stones.add(140001186);
        stones.add(140001181);
        stones.add(140001180);
        stones.add(140001179);
        stones.add(140001178);
        stones.add(140001177);
        stones.add(140001176);
        stones.add(140001175);
        stones.add(140001174);

        if (getStoneIds){
            return stones;
        }

        getStigmaStones(player, stones);
        return null;
    }
    private FastList<Integer> sm(Player player, boolean getStoneIds){
        FastList<Integer> stones = new FastList<Integer>();

        if(player.getRace() == Race.ELYOS){
            stones.add(140001197);
            stones.add(140001183);
        }else{
            stones.add(140001198);
            stones.add(140001182);
        }

        stones.add(140001210);
        stones.add(140001209);
        stones.add(140001208);
        stones.add(140001207);
        stones.add(140001206);
        stones.add(140001205);
        stones.add(140001204);
        stones.add(140001203);
        stones.add(140001202);
        stones.add(140001201);
        stones.add(140001200);
        stones.add(140001199);
        stones.add(140001196);
        stones.add(140001195);
        stones.add(140001194);
        stones.add(140001193);

        if (getStoneIds){
            return stones;
        }

        getStigmaStones(player, stones);
        return null;
    }
    private FastList<Integer> cleric(Player player, boolean getStoneIds){
        FastList<Integer> stones = new FastList<Integer>();

        if(player.getRace() == Race.ELYOS){
            stones.add(140001234);
            stones.add(140001230);
        }else{
            stones.add(140001235);
            stones.add(140001231);
        }

        stones.add(140001246);
        stones.add(140001245);
        stones.add(140001244);
        stones.add(140001243);
        stones.add(140001242);
        stones.add(140001241);
        stones.add(140001240);
        stones.add(140001239);
        stones.add(140001238);
        stones.add(140001237);
        stones.add(140001236);
        stones.add(140001233);
        stones.add(140001232);
        stones.add(140001229);
        stones.add(140001228);

        if (getStoneIds){
            return stones;
        }

        getStigmaStones(player, stones);
        return null;
    }
    private FastList<Integer> chanter(Player player, boolean getStoneIds){
        FastList<Integer> stones = new FastList<Integer>();

        stones.add(140001227);
        stones.add(140001226);
        stones.add(140001225);
        stones.add(140001224);
        stones.add(140001223);
        stones.add(140001222);
        stones.add(140001221);
        stones.add(140001220);
        stones.add(140001219);
        stones.add(140001218);
        stones.add(140001217);
        stones.add(140001216);
        stones.add(140001215);
        stones.add(140001214);
        stones.add(140001213);
        stones.add(140001212);
        stones.add(140001211);

        if (getStoneIds){
            return stones;
        }

        getStigmaStones(player, stones);
        return null;
    }
    private FastList<Integer> gunner(Player player, boolean getStoneIds){
        FastList<Integer> stones = new FastList<Integer>();

        stones.add(140001263);
        stones.add(140001262);
        stones.add(140001261);
        stones.add(140001260);
        stones.add(140001259);
        stones.add(140001258);
        stones.add(140001257);
        stones.add(140001256);
        stones.add(140001255);
        stones.add(140001254);
        stones.add(140001253);
        stones.add(140001252);
        stones.add(140001251);
        stones.add(140001250);
        stones.add(140001249);
        stones.add(140001248);
        stones.add(140001247);

        if (getStoneIds){
            return stones;
        }

        getStigmaStones(player, stones);
        return null;
    }
    private FastList<Integer> at(Player player, boolean getStoneIds){
        FastList<Integer> stones = new FastList<Integer>();

        stones.add(140001280);
        stones.add(140001279);
        stones.add(140001278);
        stones.add(140001277);
        stones.add(140001276);
        stones.add(140001275);
        stones.add(140001274);
        stones.add(140001273);
        stones.add(140001272);
        stones.add(140001271);
        stones.add(140001270);
        stones.add(140001269);
        stones.add(140001268);
        stones.add(140001267);
        stones.add(140001266);
        stones.add(140001265);
        stones.add(140001264);

        if (getStoneIds){
            return stones;
        }

        getStigmaStones(player, stones);
        return null;
    }
    private FastList<Integer> bard(Player player, boolean getStoneIds){
        FastList<Integer> stones = new FastList<Integer>();

        stones.add(140001297);
        stones.add(140001296);
        stones.add(140001295);
        stones.add(140001294);
        stones.add(140001293);
        stones.add(140001292);
        stones.add(140001291);
        stones.add(140001290);
        stones.add(140001289);
        stones.add(140001288);
        stones.add(140001287);
        stones.add(140001286);
        stones.add(140001285);
        stones.add(140001284);
        stones.add(140001283);
        stones.add(140001282);
        stones.add(140001281);

        if (getStoneIds){
            return stones;
        }

        getStigmaStones(player, stones);
        return null;
    }

    private void clean(Player player){
        switch (player.getPlayerClass()){
            case GLADIATOR:
                getRidOfStigmaStones(player, glad(player, true));
                break;
            case TEMPLAR:
                getRidOfStigmaStones(player, temp(player, true));
                break;
            case ASSASSIN:
                getRidOfStigmaStones(player, sin(player, true));
                break;
            case RANGER:
                getRidOfStigmaStones(player, ranger(player, true));
                break;
            case SORCERER:
                getRidOfStigmaStones(player, sorc(player, true));
                break;
            case SPIRIT_MASTER:
                getRidOfStigmaStones(player, sm(player, true));
                break;
            case CLERIC:
                getRidOfStigmaStones(player, cleric(player, true));
                break;
            case CHANTER:
                getRidOfStigmaStones(player, chanter(player, true));
                break;
            case GUNSLINGER:
                getRidOfStigmaStones(player, gunner(player, true));
                break;
            case AETHERTECH:
                getRidOfStigmaStones(player, at(player, true));
                break;
            case SONGWEAVER:
                getRidOfStigmaStones(player, bard(player, true));
                break;
        }
        PacketSendUtility.sendMessage(player, "All the junk stigma stones has been removed!");
    }

    public void onFail(Player player, String msg){
        PacketSendUtility.sendMessage(player, " " +
            "synax : .givestigma add  -- Adds Set of Stigma for you're Class\n    " +
            ".givestigma clean  -- Cleans the added Stigma's from .givestigma add\n  " +
            ".givestigma unlock  --  Unlocks the stigma slots (Might Require Relog)\n" +
            ".givestigma class  -- Pops up the Class Choose Dialog, if you didn't got the Class Choose Dialog");
    }
}