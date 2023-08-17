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
package admincommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.mail.SystemMailService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * Created by Wnkrz on 05/08/2017.
 */

public class MailReward extends AdminCommand
{
    public MailReward() {
        super("mailreward");
    }
	
    @Override
    public void execute(Player admin, String... params) {
        int param = 0;
        if (params == null || params.length != 1) {
            PacketSendUtility.sendMessage(admin, "syntax //mailreward <Id> ");
            return;
        } try {
            param = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            PacketSendUtility.sendMessage(admin, "Parameter must be an integer, or cancel.");
            return;
        }
        SystemMailService.getInstance().sendTemplateRewardMail(param, admin.getCommonData());
    }
	
    @Override
    public void onFail(Player player, String message) {
    }
}