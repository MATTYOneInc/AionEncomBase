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
package com.aionemu.gameserver.model.gameobjects.player;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.stats.container.StatEnum;

public enum RewardType {
	AP_PLAYER {
		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.AP_BOOST, 100).getCurrent() / 100f;
			if (CustomConfig.ENABLE_EXP_PROGRESSIVE_AP_PLAYER) {
				if (player.getLevel() >= 25 && player.getLevel() <= 40) {
					return (long) (reward * 2 * player.getRates().getApPlayerGainRate() * statRate);
				} else if (player.getLevel() >= 41 && player.getLevel() <= 55) {
					return (long) (reward * 3 * player.getRates().getApPlayerGainRate() * statRate);
				} else if (player.getLevel() >= 56 && player.getLevel() <= 65) {
					return (long) (reward * 4 * player.getRates().getApPlayerGainRate() * statRate);
				} else if (player.getLevel() >= 66 && player.getLevel() <= 75) {
					return (long) (reward * 5 * player.getRates().getApPlayerGainRate() * statRate);
				} else if (player.getLevel() >= 76 && player.getLevel() <= 83) {
					return (long) (reward * 6 * player.getRates().getApPlayerGainRate() * statRate);
				}
			}
			return (long) (reward * player.getRates().getApPlayerGainRate() * statRate);
		}
	},
	AP_NPC {
		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.AP_BOOST, 100).getCurrent() / 100f;
			if (CustomConfig.ENABLE_EXP_PROGRESSIVE_AP_NPC) {
				if (player.getLevel() >= 25 && player.getLevel() <= 40) {
					return (long) (reward * 2 * player.getRates().getApNpcRate() * statRate);
				} else if (player.getLevel() >= 41 && player.getLevel() <= 55) {
					return (long) (reward * 3 * player.getRates().getApNpcRate() * statRate);
				} else if (player.getLevel() >= 56 && player.getLevel() <= 65) {
					return (long) (reward * 4 * player.getRates().getApNpcRate() * statRate);
				} else if (player.getLevel() >= 66 && player.getLevel() <= 75) {
					return (long) (reward * 5 * player.getRates().getApNpcRate() * statRate);
				} else if (player.getLevel() >= 76 && player.getLevel() <= 83) {
					return (long) (reward * 6 * player.getRates().getApNpcRate() * statRate);
				}
			}
			return (long) (reward * player.getRates().getApNpcRate() * statRate);
		}
	},
	GP_PLAYER {

		@Override
		public long calcReward(Player player, long reward) {
			return (long) (reward * player.getRates().getGpPlayerGainRate());
		}
	},
	HUNTING {
		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.BOOST_HUNTING_XP_RATE, 100).getCurrent() / 100f;
			if (CustomConfig.ENABLE_EXP_PROGRESSIVE_HUNTING) {
				if (player.getLevel() >= 1 && player.getLevel() <= 65) {
					return (long) (reward * 5 * player.getRates().getXpRate() * statRate);
				} else if (player.getLevel() >= 66 && player.getLevel() <= 75) {
					return (long) (reward * 6 * player.getRates().getXpRate() * statRate);
				} else if (player.getLevel() >= 76 && player.getLevel() <= 83) {
					return (long) (reward * 7 * player.getRates().getXpRate() * statRate);
				}
			}
			return (long) (reward * player.getRates().getXpRate() * statRate);
		}
	},
	GROUP_HUNTING {
		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.BOOST_GROUP_HUNTING_XP_RATE, 100).getCurrent()
					/ 100f;
			if (CustomConfig.ENABLE_EXP_PROGRESSIVE_GROUP_HUNTING) {
				if (player.getLevel() >= 1 && player.getLevel() <= 65) {
					return (long) (reward * 5 * player.getRates().getGroupXpRate() * statRate);
				} else if (player.getLevel() >= 66 && player.getLevel() <= 75) {
					return (long) (reward * 6 * player.getRates().getGroupXpRate() * statRate);
				} else if (player.getLevel() >= 76 && player.getLevel() <= 83) {
					return (long) (reward * 7 * player.getRates().getGroupXpRate() * statRate);
				}
			}
			return (long) (reward * player.getRates().getGroupXpRate() * statRate);
		}
	},
	MONSTER_BOOK {
		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.BOOST_BOOK_XP_RATE, 100).getCurrent() / 100f;
			if (CustomConfig.ENABLE_EXP_PROGRESSIVE_BOOK) {
				if (player.getLevel() >= 66 && player.getLevel() <= 75) {
					return (long) (reward * 6 * player.getRates().getBookXpRate() * statRate);
				} else if (player.getLevel() >= 76 && player.getLevel() <= 83) {
					return (long) (reward * 7 * player.getRates().getQuestXpRate() * statRate);
				}
			}
			return (long) (reward * player.getRates().getBookXpRate() * statRate);
		}
	},
	PVP_KILL {
		@Override
		public long calcReward(Player player, long reward) {
			return (reward);
		}
	},
	QUEST {
		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.BOOST_QUEST_XP_RATE, 100).getCurrent() / 100f;
			if (CustomConfig.ENABLE_EXP_PROGRESSIVE_QUEST) {
				if (player.getLevel() >= 1 && player.getLevel() <= 65) {
					return (long) (reward * 5 * player.getRates().getQuestXpRate() * statRate);
				} else if (player.getLevel() >= 66 && player.getLevel() <= 75) {
					return (long) (reward * 6 * player.getRates().getQuestXpRate() * statRate);
				} else if (player.getLevel() >= 76 && player.getLevel() <= 83) {
					return (long) (reward * 7 * player.getRates().getQuestXpRate() * statRate);
				}
			}
			return (long) (reward * player.getRates().getQuestXpRate() * statRate);
		}
	},
	CRAFTING {
		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.BOOST_CRAFTING_XP_RATE, 100).getCurrent() / 100f;
			return (long) (reward * player.getRates().getCraftingXPRate() * statRate);
		}
	},
	GATHERING {
		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.BOOST_GATHERING_XP_RATE, 100).getCurrent() / 100f;
			return (long) (reward * player.getRates().getGatheringXPRate() * statRate);
		}
	};

	public abstract long calcReward(Player player, long reward);
}