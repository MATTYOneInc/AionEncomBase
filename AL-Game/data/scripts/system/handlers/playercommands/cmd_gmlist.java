/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package playercommands;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.FriendList;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eloann
 */
public class cmd_gmlist extends PlayerCommand {

    public cmd_gmlist() {
        super("gmlist");
    }

    @Override
    public void execute(Player player, String... params) {
        final List<Player> admins = new ArrayList<Player>();
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player object) {
                if (object.getAccessLevel() > 0 && object.getFriendList().getStatus() != FriendList.Status.OFFLINE) {
                    admins.add(object);
                }
            }
        });

        if (admins.size() > 0) {
            PacketSendUtility.sendMessage(player, "====================");
            if (admins.size() == 1) {
                PacketSendUtility.sendMessage(player, "There's one team member online:");
            } else {
                PacketSendUtility.sendMessage(player, "There are team member online:");
            }

            for (Player admin : admins) {

                if (AdminConfig.ADMIN_TAG_ENABLE) {
                    String adminTag = "%s";
                    StringBuilder sb = new StringBuilder(adminTag);
                    if (player.getAccessLevel() == 1) {
                        adminTag = sb.insert(0, AdminConfig.ADMIN_TAG_1.substring(0, AdminConfig.ADMIN_TAG_1.length() - 3)).toString();
                    } else if (player.getAccessLevel() == 2) {
                        adminTag = sb.insert(0, AdminConfig.ADMIN_TAG_2.substring(0, AdminConfig.ADMIN_TAG_2.length() - 3)).toString();
                    } else if (player.getAccessLevel() == 3) {
                        adminTag = sb.insert(0, AdminConfig.ADMIN_TAG_3.substring(0, AdminConfig.ADMIN_TAG_3.length() - 3)).toString();
                    } else if (player.getAccessLevel() == 4) {
                        adminTag = sb.insert(0, AdminConfig.ADMIN_TAG_4.substring(0, AdminConfig.ADMIN_TAG_4.length() - 3)).toString();
                    } else if (player.getAccessLevel() == 5) {
                        adminTag = sb.insert(0, AdminConfig.ADMIN_TAG_5.substring(0, AdminConfig.ADMIN_TAG_5.length() - 3)).toString();
					}
                    PacketSendUtility.sendMessage(player, String.format(adminTag, admin.getName()));
                }
            }
            PacketSendUtility.sendMessage(player, "====================");

        } else {
            PacketSendUtility.sendMessage(player, "There's no team member online!");
        }
    }
}