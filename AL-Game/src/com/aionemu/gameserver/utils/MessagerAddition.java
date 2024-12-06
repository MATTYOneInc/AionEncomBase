/*

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
package com.aionemu.gameserver.utils;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class MessagerAddition {
	protected void DEEPINSIDE() {
	}

	public static void announce(Player player, String msg) {
		PacketSendUtility.sendBrightYellowMessageOnCenter(player, msg);
	}

	public static void message(Player player, String msg) {
		PacketSendUtility.sendMessage(player, msg);
	}

	public static void whiteMsg(Player player, String msg) {
		PacketSendUtility.sendWhiteMessage(player, msg);
	}

	public static void whiteMsgOnCtr(Player player, String msg) {
		PacketSendUtility.sendWhiteMessageOnCenter(player, msg);
	}

	public static void yellowMsg(Player player, String msg) {
		PacketSendUtility.sendYellowMessage(player, msg);
	}

	public static void yellowMsgOnCtr(Player player, String msg) {
		PacketSendUtility.sendYellowMessageOnCenter(player, msg);
	}

	public static void announceAll(final String msg, int delay) {
		if (delay > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player sender) {
							PacketSendUtility.sendBrightYellowMessageOnCenter(sender, msg);
							return;
						}
					});
				}
			}, delay);
		} else {
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player sender) {
					PacketSendUtility.sendBrightYellowMessageOnCenter(sender, msg);
					return;
				}
			});
		}
	}

	public static void messageToAll(final String msg, int delay) {
		if (delay > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player sender) {
							PacketSendUtility.sendMessage(sender, msg);
							return;
						}
					});
				}
			}, delay);
		} else {
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player sender) {
					PacketSendUtility.sendMessage(sender, msg);
					return;
				}
			});
		}
	}

	public static void whiteMsgToAll(final String msg, int delay) {
		if (delay > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player sender) {
							PacketSendUtility.sendWhiteMessage(sender, msg);
							return;
						}
					});
				}
			}, delay);
		} else {
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player sender) {
					PacketSendUtility.sendWhiteMessage(sender, msg);
					return;
				}
			});
		}
	}

	public static void whiteAnnounceToAll(final String msg, int delay) {
		if (delay > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player sender) {
							PacketSendUtility.sendWhiteMessageOnCenter(sender, msg);
							return;
						}
					});
				}
			}, delay);
		} else {
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player sender) {
					PacketSendUtility.sendWhiteMessageOnCenter(sender, msg);
					return;
				}
			});
		}
	}

	public static void yellowMsgToAll(final String msg, int delay) {
		if (delay > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player sender) {
							PacketSendUtility.sendYellowMessage(sender, msg);
							return;
						}
					});
				}
			}, delay);
		} else {
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player sender) {
					PacketSendUtility.sendYellowMessage(sender, msg);
					return;
				}
			});
		}
	}

	public static void yellowAnnounceToAll(final String msg, int delay) {
		if (delay > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player sender) {
							PacketSendUtility.sendYellowMessageOnCenter(sender, msg);
							return;
						}
					});
				}
			}, delay);
		} else {
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player sender) {
					PacketSendUtility.sendYellowMessageOnCenter(sender, msg);
					return;
				}
			});
		}
	}

	public static void global(final String msg) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player sender) {
				PacketSendUtility.sendBrightYellowMessageOnCenter(sender, "[Global]:" + msg);
				return;
			}
		});
	}

	public static void attention(final String msg) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player sender) {
				PacketSendUtility.sendBrightYellowMessageOnCenter(sender, "[Attention]:" + msg);
				return;
			}
		});
	}
}