package ai.instance.contaminedUnderpath;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("defense_turret_platform")
public class Defense_Turret_PlatformAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player) {
		if (player.getLevel() >= 10) {
		    //This is a magical transformation cube created by the Empyrean Lord.
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
		} else {
            //You can use [Bright Aether] to set up any turret you like.
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
        }
	}
	
	@Override
    public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		//Rapid Fire Multiple Fire Cannon Installation (1 Bright Aether).
		if (dialogId == 10000 && player.getInventory().decreaseByItemId(182007405, 1)) { //Bright Aether.
			spawn(833808, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); //Single Fire Cannon.
		}
		//Ranged Cannon Installation (2 Bright Aether)
		else if (dialogId == 10001 && player.getInventory().decreaseByItemId(182007405, 2)) { //Bright Aether.
			spawn(833809, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); //Area Antiaircraft Gun.
		}
		//Powerful Magic Cannon Installation (2 Bright Aether).
		else if (dialogId == 10002 && player.getInventory().decreaseByItemId(182007405, 2)) { //Bright Aether.
			spawn(833810, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); //Wide Area Capture Device.
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		AI2Actions.deleteOwner(this);
		return true;
	}
}