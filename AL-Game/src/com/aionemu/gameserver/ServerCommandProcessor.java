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

package com.aionemu.gameserver;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import com.aionemu.gameserver.services.PunishmentService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author PenguinJoe
 * @mod yayaya ServerCommandProcessor implements a background thread to process
 *      commands from the console - either OS shell or a java-based launcher
 */

public class ServerCommandProcessor {
	private static final Logger log = LoggerFactory.getLogger(ServerCommandProcessor.class);

	Frame f = new Frame("Ya-admin panel 5.8");

	final TextField playerNameFieled = new TextField();
	final TextField itemID = new TextField();
	final TextField messageAnnounce = new TextField();

	public void startAdminPanel() {
		f.setBackground(Color.black);
		// posHoriz/pos Vert/size/sizeUpDown
		Button shutdown = new Button("Shutdown");
		shutdown.setBounds(20, 40, 60, 20);

		Button online = new Button("Online");
		online.setBounds(80, 40, 60, 20);

		Button who = new Button("Who");
		who.setBounds(140, 40, 60, 20);

		Button add = new Button("AddItem");
		add.setBounds(20, 80, 60, 20);

		Button kick = new Button("Kick");
		kick.setBounds(80, 80, 60, 20);

		Button sPrison = new Button("SPrison");
		sPrison.setBounds(140, 80, 60, 20);

		Button rPrison = new Button("RPrison");
		rPrison.setBounds(200, 80, 60, 20);

		playerNameFieled.setBounds(270, 80, 100, 20);
		playerNameFieled.setText("Player Name");

		itemID.setBounds(380, 80, 100, 20);
		itemID.setText("Iteam ID");

		Button announce = new Button("Announce");
		announce.setBounds(20, 120, 60, 20);

		messageAnnounce.setBounds(90, 120, 230, 20);
		messageAnnounce.setText("Announce message");

		f.add(playerNameFieled);
		f.add(itemID);
		f.add(messageAnnounce);

		f.add(shutdown);
		f.add(online);
		f.add(who);
		f.add(add);
		f.add(kick);
		f.add(announce);
		f.add(sPrison);
		f.add(rPrison);

		shutdown.addActionListener(al_shutdown);
		online.addActionListener(al_online);
		who.addActionListener(al_who);
		add.addActionListener(al_add);
		kick.addActionListener(al_kick);
		announce.addActionListener(al_announce);
		sPrison.addActionListener(al_sendPrison);
		rPrison.addActionListener(al_rescuePrison);

		f.setSize(600, 400);
		f.setLayout(null);
		f.setVisible(true);
		// close frame

		f.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				log.info("Ya-admin panel: you want to close me? push shutdown button");
			}
		});
	}

	ActionListener al_shutdown = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);

		}
	};

	ActionListener al_online = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			int playerCount = DAOManager.getDAO(PlayerDAO.class).getOnlinePlayerCount();
			if (playerCount == 1) {
				log.info("There is " + (playerCount) + " player online!");
			} else {
				log.info("There is " + (playerCount) + " players online!");
			}

		}
	};

	ActionListener al_who = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			Collection<Player> players = World.getInstance().getAllPlayers();
			if (players.isEmpty()) {
				log.info("There is no players online!");
				return;
			}
			for (Player player : players) {
				log.info("Char: " + player.getName() + " - Race: " + player.getCommonData().getRace().name()
						+ " - Acc: " + player.getAcountName());
			}
		}
	};

	ActionListener al_add = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int i = Integer.parseInt(itemID.getText());
			int itemId = i;
			long itemCount = 1;

			Player receiver;
			String playerName = playerNameFieled.getText();

			receiver = World.getInstance().findPlayer(playerName);

			if (itemID.getText() != null) {
				if (i != 0) {
					if (DataManager.ITEM_DATA.getItemTemplate(itemId) == null) {
						log.info("ADDcommad: Item id is incorrect: " + itemId);
						return;
					}
					ItemService.addItem(receiver, itemId, itemCount);
					log.info("ADDcommad: Success give[item:" + itemId + "] to player " + playerName + " in " + itemCount
							+ " pcs");
				} else {
					log.info("ADDcommad: IteamID = null");
				}
			} else {
				log.info("ADDcommad: Player: " + playerName + " is not online");
			}

		}
	};

	ActionListener al_kick = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (playerNameFieled.getText() != null && "All".equalsIgnoreCase(playerNameFieled.getText())) {
				for (final Player player : World.getInstance().getAllPlayers()) {
					if (!player.isGM()) {
						player.getClientConnection().close(new SM_QUIT_RESPONSE(), false);
						log.info("KICKcommad: Kicked player: " + player.getName());
					}
				}
			} else {
				Player player = World.getInstance().findPlayer(playerNameFieled.getText());
				if (player == null) {
					log.info("KICKcommad: The specified player is not online.");
					return;
				}
				player.getClientConnection().close(new SM_QUIT_RESPONSE(), false);
				log.info("KICKcommad: Kicked player : " + player.getName());
			}
		}
	};

	ActionListener al_announce = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (playerNameFieled.getText() != null || messageAnnounce.getText() != "Announce message") {
				Iterator<Player> iter = World.getInstance().getPlayersIterator();

				while (iter.hasNext()) {
					PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), messageAnnounce.getText());
				}
			}
		}
	};

	ActionListener al_sendPrison = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (playerNameFieled.getText() != null) {
				try {
					Player playerToPrison = World.getInstance().findPlayer(playerNameFieled.getText());
					int delay = 30;
					String reason = "Ban from Admin";

					if (playerToPrison != null) {
						PunishmentService.setIsInPrison(playerToPrison, true, delay, reason);
						log.info("SPRISONcommad: Admin " + playerToPrison.getName() + " send to prison till " + delay
								+ " min reason - " + reason + ".");
					}
				} catch (Exception eo) {

				}
			}
		}
	};

	ActionListener al_rescuePrison = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (playerNameFieled.getText() != null) {
				try {
					Player playerFromPrison = World.getInstance().findPlayer(playerNameFieled.getText());

					if (playerFromPrison != null) {
						PunishmentService.setIsInPrison(playerFromPrison, false, 0, "");
						log.info("SPRISONcommad: Admin " + playerFromPrison.getName() + " rescue you from prison.");
					}
				} catch (NoSuchElementException nsee) {
				} catch (Exception ee) {
				}
			}
		}
	};

}
