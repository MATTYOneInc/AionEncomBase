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
package ai.instance.esoterrace;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("wardensurama")
public class Warden_SuramaAI2 extends AggressiveNpcAI2
{
	private List<Integer> percents = new ArrayList<Integer>();
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void checkPercentage(int hpPercentage) {
		for (Integer percent: percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 90:
					    sendMsg();
					    spawnAirWave();
						startDranaReactor();
					break;
					case 70:
					    sendMsg();
					    spawnAirWave();
						startDranaReactor();
					break;
					case 50:
					    sendMsg();
					    spawnAirWave();
						startDranaReactor();
					break;
					case 30:
					    sendMsg();
					    spawnAirWave();
						startDranaReactor();
					break;
					case 10:
						sendMsg();
						spawnAirWave();
						startDranaReactor();
					break;
				}
				percents.remove(percent);
				break;
			}
		}
	}
	
	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[]{90, 70, 50, 30, 10});
	}
	
	private void startDranaReactor() {
		spawn(283173, 1333.9995f, 1176.8633f, 51.493996f, (byte) 37);
		spawn(283173, 1321.4896f, 1190.6715f, 51.493996f, (byte) 76);
		spawn(283173, 1298.6306f, 1179.3395f, 51.493996f, (byte) 92);
		spawn(283173, 1299.0713f, 1164.9993f, 51.493996f, (byte) 98);
		spawn(283173, 1312.0051f, 1151.1003f, 51.493996f, (byte) 113);
		spawn(283173, 1330.9470f, 1164.1063f, 51.493996f, (byte) 41);
		spawn(283174, 1316.3091f, 1170.6173f, 51.799908f, (byte) 98);
	}
	
	private void spawnAirWave() {
		SkillEngine.getInstance().getSkill(getOwner(), 19332, 60, getOwner()).useNoAnimationSkill();
		spawn(282171, 1316.7438f, 1145.0411f, 51.536953f, (byte) 0, 595);
		spawn(282172, 1342.0642f, 1170.9083f, 51.539276f, (byte) 0, 596);
		spawn(282173, 1316.7826f, 1196.8873f, 51.544514f, (byte) 0, 598);
		spawn(282174, 1290.6938f, 1170.3945f, 51.536175f, (byte) 0, 597);
		spawn(282425, 1304.8906f, 1159.2782f, 51.378143f, (byte) 0, 721);
		spawn(282426, 1328.4050f, 1159.1759f, 51.372219f, (byte) 0, 718);
		spawn(282427, 1328.2821f, 1182.4735f, 51.375172f, (byte) 0, 722);
		spawn(282428, 1304.9152f, 1182.2593f, 51.377087f, (byte) 0, 719);
	}
	
	private void sendMsg() {
		//Ha, fly little Daeva... if you can.
		sendMsg(1500201, getObjectId(), false, 0);
		//I'll... kill you all...
		sendMsg(1500197, getObjectId(), false, 5000);
		//It's a shame... I deceived even Tiamat... and now a mere Daeva...
		sendMsg(1500203, getObjectId(), false, 10000);
		//I'll show you what real power looks like!
		sendMsg(1500202, getObjectId(), false, 15000);
		//The Surkana Steam Jet has generated an updraft.
		PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDF4Re_Drana_09, 5000);
		//Management Director Surama uses Collapsing Earth.
		PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDF4Re_Drana_10, 15000);
	}
	
	@Override
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(282171));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282172));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282173));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282174));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282425));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282426));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282427));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282428));
			deleteNpcs(p.getWorldMapInstance().getNpcs(283173));
		}
		//Arrggh...
		sendMsg(1500196, getObjectId(), false, 0);
		super.handleDied();
	}
	
	@Override
	protected void handleBackHome() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(282171));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282172));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282173));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282174));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282425));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282426));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282427));
			deleteNpcs(p.getWorldMapInstance().getNpcs(282428));
			deleteNpcs(p.getWorldMapInstance().getNpcs(283173));
		}
		addPercent();
		super.handleBackHome();
	}
	
	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}