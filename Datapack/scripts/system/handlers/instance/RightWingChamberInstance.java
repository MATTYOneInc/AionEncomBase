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

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
* Author (Encom)
* Walkthrough:
* After entering the instance, there is a blue Aether barrier.
* Once the Aetheric barrier is broken, there are 15 minutes until the treasure chests vanish.
* All mobs are elite (except the Drakies) in this instance, and give AP.
* There are many small chests that may contain AP relics, medals, kinah or quest items.
* Also there is a Golden chest that spawns at one of the four possible locations and may contain level 30 AP items, relics or kinah.
* Pesky Drakies will prevent you from opening chests, so either kill them or use Anti-Shock scrolls and damage absorbing skills to ignore the drakies.
* Pull mobs very carefully and watch out for patrols.
* If there is a level 44+ player in your group, who isn't mentoring, chests won't drop anything except medals and quest items.
* Note!!! that relics and abyss items aren't tradeable, not even temporary.
**/

@InstanceID(300090000)
public class RightWingChamberInstance extends GeneralInstanceHandler
{
	private Future<?> chestRWCTask;
	private Future<?> rightWingTask;
	private boolean isStartTimer = false;
	protected boolean isInstanceDestroyed = false;
	private List<Npc> AncienTreasureBox = new ArrayList<Npc>();
	
	private void startRightWingChamberTimer() {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
				    rightWingTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							instance.doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									onExitInstance(player);
								}
							});
							onInstanceDestroy();
						}
					}, 910000); //15 Minutes + 10s.
				}
			}
		});
    }
	
	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		if (!isStartTimer) {
			isStartTimer = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
						startRightWingChamberTimer();
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900));
					}
				}
			});
			//Ancien Treasure Box.
			AncienTreasureBox.add((Npc) spawn(700471, 231.73753f, 227.93089f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 236.99757f, 263.49643f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 317.27744f, 175.38466f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 252.82173f, 189.68864f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 227.95578f, 261.40512f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 296.99759f, 195.86714f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 272.4028f, 274.64877f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 206.99757f, 193.50139f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 286.99759f, 227.0141f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 249.06851f, 239.85635f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 247.50642f, 167.74895f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 278.04263f, 163.91827f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 291.91599f, 277.2468f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 322.4664f, 245.91565f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 262.92972f, 143.87341f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 265.76846f, 286.30405f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 324.21567f, 247.67709f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 208.17552f, 216.23126f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 288.5665f, 158.84013f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 260.87411f, 235.07755f, 104.7138f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 196.78035f, 205.36314f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 281.89609f, 188.39912f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 289.43814f, 167.92781f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 292.82162f, 264.27817f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 244.81511f, 282.16879f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 207.90291f, 214.01634f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 256.0322f, 199.54347f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 279.02621f, 223.20438f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 305.07681f, 183.13441f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 274.97446f, 152.21568f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 315.31979f, 206.69179f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 234.13031f, 208.9537f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 272.51724f, 248.91733f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 287.2659f, 181.1086f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 293.12595f, 239.04901f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 238.65863f, 213.50137f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 303.61697f, 259.99002f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 203.57285f, 240.41481f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 288.76791f, 245.76738f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 256.08817f, 245.48064f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 226.99582f, 168.10304f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 266.21896f, 173.60332f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 261.03098f, 162.87076f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 209.85797f, 231.00948f, 104.7138f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 216.99397f, 193.50139f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 247.0006f, 153.50571f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 275.16562f, 204.3246f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 207.84865f, 178.02254f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 294.49319f, 198.21326f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 280.90457f, 251.30917f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 286.99759f, 213.50139f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 247.77362f, 196.16948f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 276.3541f, 241.02629f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 284.69919f, 272.09042f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 315.8649f, 258.07578f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 279.43375f, 195.55321f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 334.3577f, 211.83925f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 249.21669f, 209.87109f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 216.99483f, 213.50139f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 297.05496f, 161.79532f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 327.4451f, 213.50139f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 307.00107f, 207.93672f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 284.59f, 229.68991f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 266.9006f, 212.89882f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 266.90134f, 260.07059f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 247.0006f, 253.50137f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 258.38638f, 233.32773f, 104.7138f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 316.99283f, 263.49911f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 271.86032f, 224.75293f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 194.75903f, 208.34169f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 247.46281f, 180.91096f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 246.07719f, 207.27487f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 255.17043f, 255.14981f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 275.16507f, 208.84822f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 255.21681f, 151.22867f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 273.87778f, 236.17476f, 104.7138f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 318.55533f, 179.28625f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 306.72372f, 250.17752f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 239.67795f, 234.51668f, 104.7138f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 261.99649f, 287.45389f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 332.71445f, 222.77109f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 193.05125f, 220.26607f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 307.00107f, 211.48532f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 267.03714f, 188.97569f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 206.99413f, 253.50137f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 306.83066f, 164.92036f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 276.99667f, 283.50436f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 316.87729f, 192.1488f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 230.38156f, 241.39223f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 256.31332f, 223.59155f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 287.98056f, 254.04358f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 216.82956f, 248.84976f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 293.57925f, 189.34985f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 259.54117f, 263.90353f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 298.13449f, 174.24188f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 236.99757f, 156.46873f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 216.99483f, 163.49989f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 210.29207f, 198.55447f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 264.25473f, 161.79749f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 328.17856f, 234.85568f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 307.00107f, 230.88341f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 263.06723f, 274.47568f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 258.57303f, 214.15033f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 234.76421f, 245.85658f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 304.09531f, 229.81836f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 229.09982f, 225.93761f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 299.85745f, 218.48045f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 221.16797f, 176.00925f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 325.76743f, 195.0495f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 298.18954f, 245.28854f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 235.29192f, 251.91757f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 307.59671f, 170.9375f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 235.06235f, 185.98874f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 219.20956f, 221.34203f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 264.38736f, 200.31575f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 240.47816f, 278.56345f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 226.21344f, 189.20557f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 260.00919f, 175.66891f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 235.93323f, 181.53033f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 298.14182f, 213.77261f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 219.61308f, 227.06232f, 104.7138f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 198.96434f, 236.58838f, 104.7138f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 219.81798f, 183.50502f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 262.44193f, 218.30005f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 244.15779f, 233.42017f, 104.7138f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 251.12874f, 273.34256f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 268.33786f, 250.07753f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 245.11807f, 222.28328f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 222.63193f, 202.37735f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 285.39462f, 202.28313f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 257.00235f, 183.50502f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 235.61336f, 206.10374f, 104.71777f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 223.27577f, 258.16458f, 104.71452f, (byte) 0));
	        AncienTreasureBox.add((Npc) spawn(700471, 212.30345f, 268.06534f, 104.71452f, (byte) 0));
			chestRWCTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					//One of the Ancient Treasure Boxes is missing.
					sendMsgByRace(1400245, Race.PC_ALL, 0);
					//All the Ancient Treasure Boxes are missing.
					sendMsgByRace(1400244, Race.PC_ALL, 4000);
					AncienTreasureBox.get(0).getController().onDelete();
					AncienTreasureBox.get(1).getController().onDelete();
					AncienTreasureBox.get(2).getController().onDelete();
					AncienTreasureBox.get(3).getController().onDelete();
					AncienTreasureBox.get(4).getController().onDelete();
					AncienTreasureBox.get(5).getController().onDelete();
					AncienTreasureBox.get(6).getController().onDelete();
					AncienTreasureBox.get(7).getController().onDelete();
					AncienTreasureBox.get(8).getController().onDelete();
					AncienTreasureBox.get(9).getController().onDelete();
					AncienTreasureBox.get(10).getController().onDelete();
					AncienTreasureBox.get(11).getController().onDelete();
					AncienTreasureBox.get(12).getController().onDelete();
					AncienTreasureBox.get(13).getController().onDelete();
					AncienTreasureBox.get(14).getController().onDelete();
					AncienTreasureBox.get(15).getController().onDelete();
					AncienTreasureBox.get(16).getController().onDelete();
					AncienTreasureBox.get(17).getController().onDelete();
					AncienTreasureBox.get(18).getController().onDelete();
					AncienTreasureBox.get(19).getController().onDelete();
					AncienTreasureBox.get(20).getController().onDelete();
					AncienTreasureBox.get(21).getController().onDelete();
					AncienTreasureBox.get(22).getController().onDelete();
					AncienTreasureBox.get(23).getController().onDelete();
					AncienTreasureBox.get(24).getController().onDelete();
					AncienTreasureBox.get(25).getController().onDelete();
					AncienTreasureBox.get(26).getController().onDelete();
					AncienTreasureBox.get(27).getController().onDelete();
					AncienTreasureBox.get(28).getController().onDelete();
					AncienTreasureBox.get(29).getController().onDelete();
					AncienTreasureBox.get(30).getController().onDelete();
					AncienTreasureBox.get(31).getController().onDelete();
					AncienTreasureBox.get(32).getController().onDelete();
					AncienTreasureBox.get(33).getController().onDelete();
					AncienTreasureBox.get(34).getController().onDelete();
					AncienTreasureBox.get(35).getController().onDelete();
					AncienTreasureBox.get(36).getController().onDelete();
					AncienTreasureBox.get(37).getController().onDelete();
					AncienTreasureBox.get(38).getController().onDelete();
					AncienTreasureBox.get(39).getController().onDelete();
					AncienTreasureBox.get(40).getController().onDelete();
					AncienTreasureBox.get(41).getController().onDelete();
					AncienTreasureBox.get(42).getController().onDelete();
					AncienTreasureBox.get(43).getController().onDelete();
					AncienTreasureBox.get(44).getController().onDelete();
					AncienTreasureBox.get(45).getController().onDelete();
					AncienTreasureBox.get(46).getController().onDelete();
					AncienTreasureBox.get(47).getController().onDelete();
					AncienTreasureBox.get(48).getController().onDelete();
					AncienTreasureBox.get(49).getController().onDelete();
					AncienTreasureBox.get(50).getController().onDelete();
					AncienTreasureBox.get(51).getController().onDelete();
					AncienTreasureBox.get(52).getController().onDelete();
					AncienTreasureBox.get(53).getController().onDelete();
					AncienTreasureBox.get(54).getController().onDelete();
					AncienTreasureBox.get(55).getController().onDelete();
					AncienTreasureBox.get(56).getController().onDelete();
					AncienTreasureBox.get(57).getController().onDelete();
					AncienTreasureBox.get(58).getController().onDelete();
					AncienTreasureBox.get(59).getController().onDelete();
					AncienTreasureBox.get(60).getController().onDelete();
					AncienTreasureBox.get(61).getController().onDelete();
					AncienTreasureBox.get(62).getController().onDelete();
					AncienTreasureBox.get(63).getController().onDelete();
					AncienTreasureBox.get(64).getController().onDelete();
					AncienTreasureBox.get(65).getController().onDelete();
					AncienTreasureBox.get(66).getController().onDelete();
					AncienTreasureBox.get(67).getController().onDelete();
					AncienTreasureBox.get(68).getController().onDelete();
					AncienTreasureBox.get(69).getController().onDelete();
					AncienTreasureBox.get(70).getController().onDelete();
					AncienTreasureBox.get(71).getController().onDelete();
					AncienTreasureBox.get(72).getController().onDelete();
					AncienTreasureBox.get(73).getController().onDelete();
					AncienTreasureBox.get(74).getController().onDelete();
					AncienTreasureBox.get(75).getController().onDelete();
					AncienTreasureBox.get(76).getController().onDelete();
					AncienTreasureBox.get(77).getController().onDelete();
					AncienTreasureBox.get(78).getController().onDelete();
					AncienTreasureBox.get(79).getController().onDelete();
					AncienTreasureBox.get(80).getController().onDelete();
					AncienTreasureBox.get(81).getController().onDelete();
					AncienTreasureBox.get(82).getController().onDelete();
					AncienTreasureBox.get(83).getController().onDelete();
					AncienTreasureBox.get(84).getController().onDelete();
					AncienTreasureBox.get(85).getController().onDelete();
					AncienTreasureBox.get(86).getController().onDelete();
					AncienTreasureBox.get(87).getController().onDelete();
					AncienTreasureBox.get(88).getController().onDelete();
					AncienTreasureBox.get(89).getController().onDelete();
					AncienTreasureBox.get(90).getController().onDelete();
					AncienTreasureBox.get(91).getController().onDelete();
					AncienTreasureBox.get(92).getController().onDelete();
					AncienTreasureBox.get(93).getController().onDelete();
					AncienTreasureBox.get(94).getController().onDelete();
					AncienTreasureBox.get(95).getController().onDelete();
					AncienTreasureBox.get(96).getController().onDelete();
					AncienTreasureBox.get(97).getController().onDelete();
					AncienTreasureBox.get(98).getController().onDelete();
					AncienTreasureBox.get(99).getController().onDelete();
					AncienTreasureBox.get(100).getController().onDelete();
					AncienTreasureBox.get(101).getController().onDelete();
					AncienTreasureBox.get(102).getController().onDelete();
					AncienTreasureBox.get(103).getController().onDelete();
					AncienTreasureBox.get(104).getController().onDelete();
					AncienTreasureBox.get(105).getController().onDelete();
					AncienTreasureBox.get(106).getController().onDelete();
					AncienTreasureBox.get(107).getController().onDelete();
					AncienTreasureBox.get(108).getController().onDelete();
					AncienTreasureBox.get(109).getController().onDelete();
					AncienTreasureBox.get(110).getController().onDelete();
					AncienTreasureBox.get(111).getController().onDelete();
					AncienTreasureBox.get(112).getController().onDelete();
					AncienTreasureBox.get(113).getController().onDelete();
					AncienTreasureBox.get(114).getController().onDelete();
					AncienTreasureBox.get(115).getController().onDelete();
					AncienTreasureBox.get(116).getController().onDelete();
					AncienTreasureBox.get(117).getController().onDelete();
					AncienTreasureBox.get(118).getController().onDelete();
					AncienTreasureBox.get(119).getController().onDelete();
					AncienTreasureBox.get(120).getController().onDelete();
					AncienTreasureBox.get(121).getController().onDelete();
					AncienTreasureBox.get(122).getController().onDelete();
					AncienTreasureBox.get(123).getController().onDelete();
					AncienTreasureBox.get(124).getController().onDelete();
					AncienTreasureBox.get(125).getController().onDelete();
					AncienTreasureBox.get(126).getController().onDelete();
					AncienTreasureBox.get(127).getController().onDelete();
					AncienTreasureBox.get(128).getController().onDelete();
					AncienTreasureBox.get(129).getController().onDelete();
					AncienTreasureBox.get(130).getController().onDelete();
					AncienTreasureBox.get(131).getController().onDelete();
					AncienTreasureBox.get(132).getController().onDelete();
					AncienTreasureBox.get(133).getController().onDelete();
				}
			}, 900000); //15 Minutes.
		}
	}
	
	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
	}
	
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	protected void sendMsgByRace(final int msg, final Race race, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
						}
					}
				});
			}
		}, time);
	}
}