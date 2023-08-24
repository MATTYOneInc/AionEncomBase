package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gm.GmPanelCommands;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.gmhandler.CmdAddSkill;
import com.aionemu.gameserver.network.aion.gmhandler.CmdAttrBonus;
import com.aionemu.gameserver.network.aion.gmhandler.CmdChangeClass;
import com.aionemu.gameserver.network.aion.gmhandler.CmdDeleteQuest;
import com.aionemu.gameserver.network.aion.gmhandler.CmdEndQuest;
import com.aionemu.gameserver.network.aion.gmhandler.CmdGiveTitle;
import com.aionemu.gameserver.network.aion.gmhandler.CmdInvisible;
import com.aionemu.gameserver.network.aion.gmhandler.CmdItemCoolTime;
import com.aionemu.gameserver.network.aion.gmhandler.CmdLevelUpDown;
import com.aionemu.gameserver.network.aion.gmhandler.CmdResurrect;
import com.aionemu.gameserver.network.aion.gmhandler.CmdStartQuest;
import com.aionemu.gameserver.network.aion.gmhandler.CmdTeleportTo;
import com.aionemu.gameserver.network.aion.gmhandler.CmdTeleportToNamed;
import com.aionemu.gameserver.network.aion.gmhandler.CmdVisible;
import com.aionemu.gameserver.network.aion.gmhandler.CmdWish;
import com.aionemu.gameserver.network.aion.gmhandler.CmdWishId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CM_GM_COMMAND_SEND extends AionClientPacket {
  private String cmd = "";
  
  private String params = "";
  
  private Player admin;
  
  public CM_GM_COMMAND_SEND(int opcode, AionConnection.State state, AionConnection.State... restStates) {
    super(opcode, state, restStates);
  }
  
  protected void readImpl() {
    this.admin = ((AionConnection)getConnection()).getActivePlayer();
    String clientCmd = readS();
    int index = clientCmd.indexOf(" ");
    this.cmd = clientCmd;
    if (index >= 0) {
      this.cmd = clientCmd.substring(0, index).toUpperCase();
      this.params = clientCmd.substring(index + 1);
    } 
  }
  
  protected void runImpl() {
    QuestState elyos, asmo;
    if (this.admin == null)
      return; 
    if (this.admin.getAccessLevel() < AdminConfig.GM_PANEL)
      return; 
    switch (GmPanelCommands.getValue(this.cmd)) {
      case REMOVE_SKILL_DELAY_ALL:
        return;
      case ITEMCOOLTIME:
        new CmdItemCoolTime(this.admin);
      case ATTRBONUS:
        new CmdAttrBonus(this.admin, this.params);
      case TELEPORTTO:
        new CmdTeleportTo(this.admin, this.params);
      case TELEPORT_TO_NAMED:
        new CmdTeleportToNamed(this.admin, this.params);
      case RESURRECT:
        new CmdResurrect(this.admin, "");
      case INVISIBLE:
        new CmdInvisible(this.admin, "");
      case VISIBLE:
        new CmdVisible(this.admin, "");
      case LEVELDOWN:
        new CmdLevelUpDown(this.admin, this.params, CmdLevelUpDown.LevelUpDownState.DOWN);
      case LEVELUP:
        elyos = this.admin.getQuestStateList().getQuestState(10521);
        asmo = this.admin.getQuestStateList().getQuestState(20521);
        if (this.admin.getLevel() >= 65 && this.admin.getRace() == Race.ELYOS && elyos.getStatus() != QuestStatus.COMPLETE) {
          PacketSendUtility.sendPacket(this.admin, (AionServerPacket)new SM_SYSTEM_MESSAGE(1403187, new Object[] { Integer.valueOf(66) }));
        } else if (this.admin.getLevel() >= 65 && this.admin.getRace() == Race.ASMODIANS && asmo.getStatus() != QuestStatus.COMPLETE) {
          PacketSendUtility.sendPacket(this.admin, (AionServerPacket)new SM_SYSTEM_MESSAGE(1403187, new Object[] { Integer.valueOf(66) }));
        } else {
          new CmdLevelUpDown(this.admin, this.params, CmdLevelUpDown.LevelUpDownState.UP);
        } 
      case WISHID:
        new CmdWishId(this.admin, this.params);
      case DELETECQUEST:
        new CmdDeleteQuest(this.admin, this.params);
      case GIVETITLE:
        new CmdGiveTitle(this.admin, this.params);
      case DELETE_ITEMS:
        PacketSendUtility.sendMessage(this.admin, "Invalid command: " + this.cmd.toString());
      case CHANGECLASS:
        new CmdChangeClass(this.admin, this.params);
      case CLASSUP:
        new CmdChangeClass(this.admin, this.params);
      case WISH:
        new CmdWish(this.admin, this.params);
      case ADDQUEST:
        new CmdStartQuest(this.admin, this.params);
      case ENDQUEST:
        new CmdEndQuest(this.admin, this.params);
      case ADDSKILL:
        new CmdAddSkill(this.admin, this.params);
      case SETINVENTORYGROWTH:
      case SKILLPOINT:
      case COMBINESKILL:
      case DELETESKILL:
      case ENCHANT100:
      case SEARCH:
      case BOOKMARK_ADD:
        PacketSendUtility.sendMessage(this.admin, "Invalid command: " + this.cmd.toString());
      case FREEFLY:
        PacketSendUtility.sendMessage(this.admin, "Freefly On");
    } 
    PacketSendUtility.sendMessage(this.admin, "Invalid command: " + this.cmd.toString());
  }
}
