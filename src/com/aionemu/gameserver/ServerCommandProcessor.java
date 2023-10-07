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

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.World;

/**
 * @author PenguinJoe
 * @mod yayaya ServerCommandProcessor implements a background thread to process
 *      commands from the console - either OS shell or a java-based launcher
 */

public class ServerCommandProcessor extends Thread {
	private static final Logger log = LoggerFactory.getLogger(ServerCommandProcessor.class);

	
	@Override
	public void run() {
		

		
        Frame f = new Frame("Ya-admin panel");
        f.setBackground( Color.black);
        //final TextField tf = new TextField();
        //tf.setBounds(50, 50, 150, 20);
        Button shutdown = new Button("Shutdown");
        shutdown.setBounds(20, 40, 60, 30);
        
        Button online = new Button("Online");
        online.setBounds(80, 40, 60, 30);
        
        Button who = new Button("Who");
        who.setBounds(140, 40, 60, 30);

        f.add(shutdown);
        f.add(online);
        f.add(who);

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
				if(players.isEmpty()) {
					log.info("There is no players online!");
					return;
				}
				for (Player player : players) {
					log.info("Char: " + player.getName() + " - Race: " + player.getCommonData().getRace().name()
							+ " - Acc: " + player.getAcountName());
				}
            }
        };
        
        shutdown.addActionListener(al_shutdown);
        online.addActionListener(al_online);
        who.addActionListener(al_who);
        //f.add(tf);
        f.setSize(400, 200);
        f.setLayout(null);
        f.setVisible(true);
        // close frame
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	log.info("Ya-admin panel: you want to close me? push shutdown button");
            }
        });
    
        
        

		// commands are only accepted from stdin when <enter> is hit. Otherwise we will
		// waste no time reading char by char.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String command = null;
		log.info("Server command processor thread started");
		try {
			while ((command = br.readLine()) != null) {
				// 
				if (command.equalsIgnoreCase("online")) {
					int playerCount = DAOManager.getDAO(PlayerDAO.class).getOnlinePlayerCount();
					if (playerCount == 1) {
						log.info("There is " + (playerCount) + " player online !");
					} else {
						log.info("There is " + (playerCount) + " players online !");
					}
				}

				// 
				if (command.equalsIgnoreCase("who")) {

					Collection<Player> players = World.getInstance().getAllPlayers();
					for (Player player : players) {
						log.info("Char: " + player.getName() + " - Race: " + player.getCommonData().getRace().name()
								+ " - Acc: " + player.getAcountName());
					}
				}
				// 
				if (command.equalsIgnoreCase("shutdown") || command.equalsIgnoreCase("quit")
						|| command.equalsIgnoreCase("exit"))
					System.exit(0); // this will run finalizers and shutdown hooks for a clean shutdown.
			}
		} catch (IOException e) {
			// an IOException here indicates the console (or other launcher) is closing. The
			// server needs to shut down too.
			System.exit(0); // exit using shutdown handlers.
		}
	}
	
}
