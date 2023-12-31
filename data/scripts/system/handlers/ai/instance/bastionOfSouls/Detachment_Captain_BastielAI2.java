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
package ai.instance.bastionOfSouls;

import ai.GeneralNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.NpcType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/****/
/** Author (Encom)
/****/

@AIName("Bastiel_Terrarium")
public class Detachment_Captain_BastielAI2 extends GeneralNpcAI2
{
	private boolean isInstanceDestroyed;
	
	@Override
	protected void handleDialogStart(Player player) {
		if (player.getLevel() >= 66) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
		}
	}
	
	@Override
    public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000) {
			startInvulnerable();
			startBastionOfSoulsWave();
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}
	
	private void startInvulnerable() {
		final Npc detachmentCaptainBastiel = getPosition().getWorldMapInstance().getNpc(806702);
		detachmentCaptainBastiel.setTarget(getOwner());
		detachmentCaptainBastiel.setNpcType(NpcType.INVULNERABLE);
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		for (Player player: instance.getPlayersInside()) {
			if (MathUtil.isIn3dRange(player, detachmentCaptainBastiel, 50)) {
				player.getEffectController().updatePlayerEffectIcons();
				player.clearKnownlist();
				player.updateKnownlist();
			}
		}
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		Npc owner = getOwner();
		owner.getLifeStats().setCurrentHpPercent(5);
	}
	
	private void startBastionOfSoulsWave() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				attackBastionOfSouls((Npc)spawn(246520, 260.62317f, 753.64874f, 421.74033f, (byte) 67), 238.8924f, 748.75555f, 421.254f, false);
				attackBastionOfSouls((Npc)spawn(246521, 229.63022f, 773.9174f, 421.83142f, (byte) 94), 238.8924f, 748.75555f, 421.254f, false);
				attackBastionOfSouls((Npc)spawn(246522, 220.49199f, 743.5425f, 421.625f, (byte) 14), 238.8924f, 748.75555f, 421.254f, false);
				attackBastionOfSouls((Npc)spawn(246523, 244.47363f, 733.99384f, 421.38748f, (byte) 33), 238.8924f, 748.75555f, 421.254f, false);
			}
		}, 1000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				attackBastionOfSouls((Npc)spawn(246520, 260.62317f, 753.64874f, 421.74033f, (byte) 67), 238.8924f, 748.75555f, 421.254f, false);
				attackBastionOfSouls((Npc)spawn(246521, 229.63022f, 773.9174f, 421.83142f, (byte) 94), 238.8924f, 748.75555f, 421.254f, false);
				attackBastionOfSouls((Npc)spawn(246522, 220.49199f, 743.5425f, 421.625f, (byte) 14), 238.8924f, 748.75555f, 421.254f, false);
				attackBastionOfSouls((Npc)spawn(246523, 244.47363f, 733.99384f, 421.38748f, (byte) 33), 238.8924f, 748.75555f, 421.254f, false);
			}
		}, 30000);
	}
	
	private void attackBastionOfSouls(final Npc npc, float x, float y, float z, boolean despawn) {
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}
	
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
	}
}