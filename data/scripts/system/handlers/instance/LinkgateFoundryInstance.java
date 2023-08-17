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
package instance;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@InstanceID(301270000)
public class LinkgateFoundryInstance extends GeneralInstanceHandler
{
    private Future<?> linkgateTask;
	private boolean isStartTimer1 = false;
	private List<Npc> Drs = new ArrayList<Npc>();
	private List<Npc> Drs2 = new ArrayList<Npc>();
	private List<Npc> Drs3 = new ArrayList<Npc>();
	private List<Npc> TailedBuzzBug = new ArrayList<Npc>();
	private List<Npc> IrradiatedStog = new ArrayList<Npc>();
	private List<Npc> VashartiDracuni = new ArrayList<Npc>();
	private List<Npc> ThecynonBruiser = new ArrayList<Npc>();
	private List<Npc> IridescentLeowasp = new ArrayList<Npc>();
	private List<Npc> DementedAshulagen = new ArrayList<Npc>();
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
			case 233898: //Volatile Belsagos.
			case 234990: //Wounded Belsagos.
			case 234991: //Furious Belsagos.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); //Major Stigma Support Bundle.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000236, 3)); //Blood Mark.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053295, 1)); //Empyrean Plume Chest.
				switch (Rnd.get(1, 5)) {
					case 1:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080005, 2)); //Lesser Minion Contract.
					break;
					case 2:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080006, 2)); //Greater Minion Contract.
					break;
					case 3:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080007, 2)); //Major Minion Contract.
					break;
					case 4:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080008, 2)); //Cute Minion Contract.
					break;
					case 5:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190200000, 50)); //Minium.
					break;
				}
            break;
			case 234194: //Linkgate Foundry Supply Chest.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053238, 1)); //Scroll Bundle (Linkgate Foundry).
			break;
			case 234195: //Linkgate Foundry Supply Chest.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053239, 1)); //Scroll Bundle (Linkgate Foundry).
			break;
		}
	}
	
	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		if (!isStartTimer1) {
			isStartTimer1 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
				@Override
			    public void visit(Player player) {
					if (player.isOnline()) {
					    startLinkgateTimer();
					    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 1200)); //20 Minutes.
					}
				}
			});
			//\\//\\//\\//***Dimensional Research Security***\//\\//\\//\\//
			Drs.add((Npc) spawn(233888, 237.69263f, 205.63078f, 311.48178f, (byte) 106));
            Drs.add((Npc) spawn(233888, 200.84613f, 228.59465f, 311.36386f, (byte) 82));
            Drs.add((Npc) spawn(233888, 196.50275f, 205.12178f, 311.4676f, (byte) 23));
            Drs.add((Npc) spawn(233888, 251.4772f, 313.84366f, 311.54544f, (byte) 20));
            Drs.add((Npc) spawn(233888, 277.61133f, 217.1048f, 311.46527f, (byte) 3));
            Drs.add((Npc) spawn(233887, 187.72894f, 300.97208f, 311.49493f, (byte) 1));
            Drs.add((Npc) spawn(233888, 286.83957f, 290.11908f, 311.43384f, (byte) 32));
            Drs.add((Npc) spawn(233888, 213.80443f, 204.86569f, 311.42764f, (byte) 103));
            Drs.add((Npc) spawn(233888, 200.0f, 227.0f, 351.8521f, (byte) 103));
            Drs.add((Npc) spawn(233888, 195.87144f, 203.70865f, 351.8469f, (byte) 26));
            Drs.add((Npc) spawn(233888, 187.08223f, 301.8191f, 351.94647f, (byte) 98));
            Drs.add((Npc) spawn(233887, 277.22986f, 217.76042f, 351.96573f, (byte) 107));
            Drs.add((Npc) spawn(233888, 287.73267f, 290.20822f, 351.89896f, (byte) 30));
            Drs.add((Npc) spawn(233888, 236.16246f, 204.72261f, 351.90912f, (byte) 100));
            Drs.add((Npc) spawn(233888, 251.44023f, 312.4486f, 351.90738f, (byte) 45));
            Drs.add((Npc) spawn(233888, 199.99397f, 203.93137f, 392.36688f, (byte) 28));
            Drs.add((Npc) spawn(233888, 278.47595f, 216.65788f, 392.3399f, (byte) 118));
            Drs.add((Npc) spawn(233888, 236.96892f, 205.75906f, 392.38068f, (byte) 106));
            Drs.add((Npc) spawn(233887, 200.63882f, 228.64465f, 392.3062f, (byte) 94));
            Drs.add((Npc) spawn(233888, 296.8903f, 251.10568f, 392.40906f, (byte) 11));
            Drs.add((Npc) spawn(233888, 251.5145f, 313.33185f, 392.44107f, (byte) 52));
            Drs.add((Npc) spawn(233888, 286.32993f, 290.0961f, 392.30145f, (byte) 29));
            Drs.add((Npc) spawn(233888, 188.33983f, 302.67587f, 392.42343f, (byte) 111));
			//\\//\\//\\//***Dimensional Research Security***\//\\//\\//\\//
			Drs2.add((Npc) spawn(233889, 252.57417f, 188.46666f, 311.58368f, (byte) 13));
            Drs2.add((Npc) spawn(233889, 186.80626f, 215.55573f, 311.4676f, (byte) 23));
            Drs2.add((Npc) spawn(233889, 254.27109f, 188.97246f, 352.01318f, (byte) 58));
            Drs2.add((Npc) spawn(233889, 186.01834f, 215.38379f, 351.84723f, (byte) 30));
            Drs2.add((Npc) spawn(233889, 237.50761f, 186.87372f, 392.3759f, (byte) 46));
            Drs2.add((Npc) spawn(233889, 186.02515f, 218.48848f, 392.3669f, (byte) 14));
			//\\//\\//\\//***Dimensional Research Security***\//\\//\\//\\//
			Drs3.add((Npc) spawn(233890, 288.41348f, 315.20276f, 311.49292f, (byte) 74));
            Drs3.add((Npc) spawn(233890, 251.03166f, 206.34558f, 311.48178f, (byte) 99));
            Drs3.add((Npc) spawn(233890, 210.81549f, 301.42322f, 311.39096f, (byte) 61));
            Drs3.add((Npc) spawn(233890, 235.94667f, 189.2081f, 311.58368f, (byte) 38));
            Drs3.add((Npc) spawn(233890, 303.28467f, 217.2989f, 311.569f, (byte) 49));
            Drs3.add((Npc) spawn(233890, 207.35115f, 215.74403f, 311.3639f, (byte) 83));
            Drs3.add((Npc) spawn(233890, 234.48203f, 330.68378f, 311.54224f, (byte) 91));
            Drs3.add((Npc) spawn(233890, 234.16716f, 330.32004f, 352.01163f, (byte) 109));
            Drs3.add((Npc) spawn(233890, 210.74463f, 300.0151f, 351.85934f, (byte) 60));
            Drs3.add((Npc) spawn(233890, 207.51857f, 218.61983f, 351.85214f, (byte) 87));
            Drs3.add((Npc) spawn(233890, 237.7854f, 189.14494f, 352.0134f, (byte) 19));
            Drs3.add((Npc) spawn(233890, 251.18358f, 204.95128f, 351.90912f, (byte) 101));
            Drs3.add((Npc) spawn(233890, 302.25256f, 216.4175f, 351.96124f, (byte) 46));
            Drs3.add((Npc) spawn(233890, 291.08755f, 313.04135f, 351.896f, (byte) 66));
            Drs3.add((Npc) spawn(233890, 233.74527f, 330.0903f, 392.5317f, (byte) 87));
            Drs3.add((Npc) spawn(233890, 291.127f, 315.13788f, 392.40533f, (byte) 75));
            Drs3.add((Npc) spawn(233890, 210.87248f, 301.1562f, 392.31958f, (byte) 43));
            Drs3.add((Npc) spawn(233890, 249.24078f, 205.4877f, 392.38077f, (byte) 98));
            Drs3.add((Npc) spawn(233890, 209.99449f, 218.43645f, 392.30554f, (byte) 85));
            Drs3.add((Npc) spawn(233890, 313.33997f, 267.43057f, 392.40585f, (byte) 73));
            Drs3.add((Npc) spawn(233890, 302.81894f, 218.95981f, 392.40195f, (byte) 45));
            Drs3.add((Npc) spawn(233890, 230.5059f, 196.22823f, 392.38034f, (byte) 0));
			//\\//\\//\\//***Thecynon Bruiser***\//\\//\\//\\//
			ThecynonBruiser.add((Npc) spawn(233891, 198.24977f, 216.42741f, 311.36505f, (byte) 15));
            ThecynonBruiser.add((Npc) spawn(233891, 305.81207f, 252.76137f, 311.4109f, (byte) 18));
            ThecynonBruiser.add((Npc) spawn(233891, 198.1695f, 216.08076f, 351.8517f, (byte) 38));
            ThecynonBruiser.add((Npc) spawn(233891, 305.7409f, 246.09627f, 351.94623f, (byte) 27));
            ThecynonBruiser.add((Npc) spawn(233891, 199.60883f, 216.30334f, 392.29834f, (byte) 42));
            ThecynonBruiser.add((Npc) spawn(233891, 289.86395f, 205.12016f, 392.40198f, (byte) 56));
            ThecynonBruiser.add((Npc) spawn(233891, 244.0f, 322.0f, 270.94745f, (byte) 10));
			//\\//\\//\\//***Irradiated Stog***\//\\//\\//\\//
			IrradiatedStog.add((Npc) spawn(233892, 281.15204f, 303.85956f, 311.42645f, (byte) 27));
            IrradiatedStog.add((Npc) spawn(233892, 202.26726f, 292.80936f, 311.39096f, (byte) 61));
            IrradiatedStog.add((Npc) spawn(233892, 288.35504f, 226.83403f, 311.46527f, (byte) 116));
            IrradiatedStog.add((Npc) spawn(233892, 300.54388f, 302.1155f, 351.896f, (byte) 88));
            IrradiatedStog.add((Npc) spawn(233892, 288.24832f, 227.9325f, 351.96588f, (byte) 96));
            IrradiatedStog.add((Npc) spawn(233892, 202.0295f, 290.456f, 351.85934f, (byte) 47));
            IrradiatedStog.add((Npc) spawn(233892, 281.62222f, 302.4515f, 392.30148f, (byte) 30));
            IrradiatedStog.add((Npc) spawn(233892, 287.86456f, 226.26303f, 392.33844f, (byte) 119));
            IrradiatedStog.add((Npc) spawn(233892, 200.50499f, 292.2182f, 392.31958f, (byte) 60));
			//\\//\\//\\//***Iridescent Leowasp***\//\\//\\//\\//
			IridescentLeowasp.add((Npc) spawn(233893, 236.8951f, 314.6218f, 311.5455f, (byte) 40));
            IridescentLeowasp.add((Npc) spawn(233893, 239.98874f, 315.16837f, 351.9074f, (byte) 48));
            IridescentLeowasp.add((Npc) spawn(233893, 238.3112f, 315.24155f, 392.44107f, (byte) 47));
			//\\//\\//\\//***Demented Ashulagen***\//\\//\\//\\//
			DementedAshulagen.add((Npc) spawn(233895, 291.9794f, 205.02562f, 311.56903f, (byte) 54));
            DementedAshulagen.add((Npc) spawn(233895, 198.75613f, 311.9456f, 311.49512f, (byte) 118));
            DementedAshulagen.add((Npc) spawn(233895, 298.35406f, 301.52652f, 311.49295f, (byte) 93));
            DementedAshulagen.add((Npc) spawn(233895, 251.24818f, 330.70688f, 311.5422f, (byte) 105));
            DementedAshulagen.add((Npc) spawn(233895, 249.21078f, 330.48877f, 352.01157f, (byte) 106));
            DementedAshulagen.add((Npc) spawn(233895, 198.59459f, 311.66528f, 351.94662f, (byte) 119));
            DementedAshulagen.add((Npc) spawn(233895, 292.94528f, 208.55768f, 351.9629f, (byte) 59));
            DementedAshulagen.add((Npc) spawn(233895, 280.837f, 300.9692f, 351.8991f, (byte) 26));
            DementedAshulagen.add((Npc) spawn(233895, 298.09726f, 210.5587f, 392.40198f, (byte) 46));
            DementedAshulagen.add((Npc) spawn(233895, 298.99954f, 303.81363f, 392.40564f, (byte) 89));
            DementedAshulagen.add((Npc) spawn(233895, 198.71239f, 312.11276f, 392.42377f, (byte) 119));
            DementedAshulagen.add((Npc) spawn(233895, 243.90527f, 331.27017f, 392.5317f, (byte) 90));
			//\\//\\//\\//***Tailed Buzz Bug***\//\\//\\//\\//
			TailedBuzzBug.add((Npc) spawn(233896, 292.22864f, 302.4627f, 311.42404f, (byte) 100));
            TailedBuzzBug.add((Npc) spawn(233896, 289.6608f, 302.36197f, 351.8988f, (byte) 104));
            TailedBuzzBug.add((Npc) spawn(233896, 290.82986f, 302.63284f, 392.30264f, (byte) 103));
			//\\//\\//\\//***Vasharti Dracuni***\//\\//\\//\\//
			VashartiDracuni.add((Npc) spawn(233897, 198.85709f, 301.7359f, 311.39212f, (byte) 16));
            VashartiDracuni.add((Npc) spawn(233897, 199.55707f, 300.5287f, 351.85858f, (byte) 16));
            VashartiDracuni.add((Npc) spawn(233897, 197.58377f, 300.5562f, 392.32074f, (byte) 16));
		}
	}
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
		    case 233898: //Volatile Belsagos.
			case 234990: //Wounded Belsagos.
			case 234991: //Furious Belsagos.
				sendMsg("[SUCCES]: You have finished <Linkgate Foundry >");
				spawn(702338, 225.60777f, 259.7162f, 312.62796f, (byte) 119); //Linkgate Foundry Exit.
			break;
		}
	}
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(185000196, storage.getItemCountByItemId(185000196)); //Abyss Gap Sealing Key's.
	}
	
	private void startLinkgateTimer() {
		//You have entered the Linkgate Foundry.
		//Monsters in the lab, except Belsagos, will disappear in 20 minutes.
		sendMsg(1402453);
		//Monsters in the lab, except Belsagos, will disappear in 15 minutes.
		this.sendMessage(1402454, 5 * 60 * 1000);
		//Monsters in the lab, except Belsagos, will disappear in 10 minutes.
		this.sendMessage(1402455, 10 * 60 * 1000);
		//Monsters in the lab, except Belsagos, will disappear in 5 minutes.
		this.sendMessage(1402456, 15 * 60 * 1000);
		//Monsters in the lab, except Belsagos, will disappear in 3 minutes.
		this.sendMessage(1402457, 17 * 60 * 1000);
		//Monsters in the lab, except Belsagos, will disappear in 1 minute.
		this.sendMessage(1402458, 19 * 60 * 1000);
		//All monsters except Belsagos have disappeared from the Linkgate Foundry.
		this.sendMessage(1402461, 20 * 60 * 1000);
		linkgateTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//***Dimensional Research Security***//
				Drs.get(0).getController().onDelete();
				Drs.get(1).getController().onDelete();
				Drs.get(2).getController().onDelete();
				Drs.get(3).getController().onDelete();
				Drs.get(4).getController().onDelete();
				Drs.get(5).getController().onDelete();
				Drs.get(6).getController().onDelete();
				Drs.get(7).getController().onDelete();
				Drs.get(8).getController().onDelete();
				Drs.get(9).getController().onDelete();
				Drs.get(10).getController().onDelete();
				Drs.get(11).getController().onDelete();
				Drs.get(12).getController().onDelete();
				Drs.get(13).getController().onDelete();
				Drs.get(14).getController().onDelete();
				Drs.get(15).getController().onDelete();
				Drs.get(16).getController().onDelete();
				Drs.get(17).getController().onDelete();
				Drs.get(18).getController().onDelete();
				Drs.get(19).getController().onDelete();
				Drs.get(20).getController().onDelete();
				Drs.get(21).getController().onDelete();
				Drs.get(22).getController().onDelete();
				//***Dimensional Research Security***//
				Drs2.get(0).getController().onDelete();
				Drs2.get(1).getController().onDelete();
				Drs2.get(2).getController().onDelete();
				Drs2.get(3).getController().onDelete();
				Drs2.get(4).getController().onDelete();
				Drs2.get(5).getController().onDelete();
				//***Dimensional Research Security***//
				Drs3.get(0).getController().onDelete();
				Drs3.get(1).getController().onDelete();
				Drs3.get(2).getController().onDelete();
				Drs3.get(3).getController().onDelete();
				Drs3.get(4).getController().onDelete();
				Drs3.get(5).getController().onDelete();
				Drs3.get(6).getController().onDelete();
				Drs3.get(7).getController().onDelete();
				Drs3.get(8).getController().onDelete();
				Drs3.get(9).getController().onDelete();
				Drs3.get(10).getController().onDelete();
				Drs3.get(11).getController().onDelete();
				Drs3.get(12).getController().onDelete();
				Drs3.get(13).getController().onDelete();
				Drs3.get(14).getController().onDelete();
				Drs3.get(15).getController().onDelete();
				Drs3.get(16).getController().onDelete();
				Drs3.get(17).getController().onDelete();
				Drs3.get(18).getController().onDelete();
				Drs3.get(19).getController().onDelete();
				Drs3.get(20).getController().onDelete();
				Drs3.get(21).getController().onDelete();
				//***Thecynon Bruiser***//
				ThecynonBruiser.get(0).getController().onDelete();
				ThecynonBruiser.get(1).getController().onDelete();
				ThecynonBruiser.get(2).getController().onDelete();
				ThecynonBruiser.get(3).getController().onDelete();
				ThecynonBruiser.get(4).getController().onDelete();
				ThecynonBruiser.get(5).getController().onDelete();
				ThecynonBruiser.get(6).getController().onDelete();
				//***Irradiated Stog***//
				IrradiatedStog.get(0).getController().onDelete();
				IrradiatedStog.get(1).getController().onDelete();
				IrradiatedStog.get(2).getController().onDelete();
				IrradiatedStog.get(3).getController().onDelete();
				IrradiatedStog.get(4).getController().onDelete();
				IrradiatedStog.get(5).getController().onDelete();
				IrradiatedStog.get(6).getController().onDelete();
				IrradiatedStog.get(7).getController().onDelete();
				IrradiatedStog.get(8).getController().onDelete();
				//***Iridescent Leowasp***//
				IridescentLeowasp.get(0).getController().onDelete();
				IridescentLeowasp.get(1).getController().onDelete();
				IridescentLeowasp.get(2).getController().onDelete();
				//***Demented Ashulagen***//
				DementedAshulagen.get(0).getController().onDelete();
				DementedAshulagen.get(1).getController().onDelete();
				DementedAshulagen.get(2).getController().onDelete();
				DementedAshulagen.get(3).getController().onDelete();
				DementedAshulagen.get(4).getController().onDelete();
				DementedAshulagen.get(5).getController().onDelete();
				DementedAshulagen.get(6).getController().onDelete();
				DementedAshulagen.get(7).getController().onDelete();
				DementedAshulagen.get(8).getController().onDelete();
				DementedAshulagen.get(9).getController().onDelete();
				DementedAshulagen.get(10).getController().onDelete();
				DementedAshulagen.get(11).getController().onDelete();
				//***Tailed Buzz Bug***//
				TailedBuzzBug.get(0).getController().onDelete();
				TailedBuzzBug.get(1).getController().onDelete();
				TailedBuzzBug.get(2).getController().onDelete();
				//***Vasharti Dracuni***//
				VashartiDracuni.get(0).getController().onDelete();
				VashartiDracuni.get(1).getController().onDelete();
				VashartiDracuni.get(2).getController().onDelete();
			}
		}, 1200000); //20 Minutes.
    }
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
	}
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	
	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
	}
	
	private void sendMessage(final int msgId, long delay) {
        if (delay == 0) {
            this.sendMsg(msgId);
        } else {
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                public void run() {
                    sendMsg(msgId);
                }
            }, delay);
        }
    }
}