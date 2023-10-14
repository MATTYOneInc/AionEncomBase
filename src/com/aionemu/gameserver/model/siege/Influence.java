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
package com.aionemu.gameserver.model.siege;

import java.util.Iterator;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INFLUENCE_RATIO;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

public class Influence {
	private static final Influence instance = new Influence();

	// ======[ABYSS]=============
	private float abyss_e = 0;
	private float abyss_a = 0;
	private float abyss_b = 0;
	// ======[KALDOR]============
	private float kaldor_e = 0;
	private float kaldor_a = 0;
	private float kaldor_b = 0;
	// ======[BELUS]=============
	private float belus_e = 0;
	private float belus_a = 0;
	private float belus_b = 0;
	// ======[ASPIDA]============
	private float aspida_e = 0;
	private float aspida_a = 0;
	private float aspida_b = 0;
	// ======[ATANATOS]==========
	private float atanatos_e = 0;
	private float atanatos_a = 0;
	private float atanatos_b = 0;
	// ======[DISILLON]==========
	private float disillon_e = 0;
	private float disillon_a = 0;
	private float disillon_b = 0;
	// ======[GLOBAL]============
	private float global_e = 0;
	private float global_a = 0;
	private float global_b = 0;

	private Influence() {
		calculateInfluence();
	}

	public static Influence getInstance() {
		return instance;
	}

	public void recalculateInfluence() {
		calculateInfluence();
	}

	private void calculateInfluence() {
		float balaurea = 0.0019512194f;
		float abyss = 0.006097561f;
		// ======[ABYSS]==========
		float e_abyss = 0f;
		float a_abyss = 0f;
		float b_abyss = 0f;
		float t_abyss = 0f;
		// ======[KALDOR]======
		float e_kaldor = 0f;
		float a_kaldor = 0f;
		float b_kaldor = 0f;
		float t_kaldor = 0f;
		for (SiegeLocation sLoc : SiegeService.getInstance().getSiegeLocations().values()) {
			switch (sLoc.getWorldId()) {
			// ======[ABYSS]==========
			case 400010000:
				t_abyss += sLoc.getInfluenceValue();
				switch (sLoc.getRace()) {
				case ELYOS:
					e_abyss += sLoc.getInfluenceValue();
					break;
				case ASMODIANS:
					a_abyss += sLoc.getInfluenceValue();
					break;
				case BALAUR:
					b_abyss += sLoc.getInfluenceValue();
					break;
				}
				break;
			// ======[KALDOR]======
			case 600090000:
				if (sLoc instanceof FortressLocation) {
					t_kaldor += sLoc.getInfluenceValue();
					switch (sLoc.getRace()) {
					case ELYOS:
						e_kaldor += sLoc.getInfluenceValue();
						break;
					case ASMODIANS:
						a_kaldor += sLoc.getInfluenceValue();
						break;
					case BALAUR:
						b_kaldor += sLoc.getInfluenceValue();
						break;
					}
				}
				break;
			}
		}
		// ======[ABYSS]=========
		abyss_e = (e_abyss / t_abyss);
		abyss_a = (a_abyss / t_abyss);
		abyss_b = (b_abyss / t_abyss);
		// ======[KALDOR]=====
		kaldor_e = (e_kaldor / t_kaldor);
		kaldor_a = (a_kaldor / t_kaldor);
		kaldor_b = (b_kaldor / t_kaldor);
		// ======[GLOBAL]========
		global_e = (kaldor_e * balaurea + abyss_e * abyss) * 100f;
		global_a = (kaldor_a * balaurea + abyss_a * abyss) * 100f;
		global_b = (kaldor_b * balaurea + abyss_b * abyss) * 100f;
	}

	@SuppressWarnings("unused")
	private void broadcastInfluencePacket() {
		SM_INFLUENCE_RATIO pkt = new SM_INFLUENCE_RATIO();
		Iterator<Player> iter = World.getInstance().getPlayersIterator();
		while (iter.hasNext()) {
			Player player = iter.next();
			PacketSendUtility.sendPacket(player, pkt);
		}
	}

	// =======[GLOBAL]=========
	// ========================
	public float getGlobalElyosInfluence() {
		return global_e;
	}

	public float getGlobalAsmodiansInfluence() {
		return global_a;
	}

	public float getGlobalBalaursInfluence() {
		return global_b;
	}

	// ========[ABYSS]========
	// =======================
	public float getAbyssElyosInfluence() {
		return abyss_e;
	}

	public float getAbyssAsmodiansInfluence() {
		return abyss_a;
	}

	public float getAbyssBalaursInfluence() {
		return abyss_b;
	}

	// =======[KALDOR]========
	// =======================
	public float getKaldorElyosInfluence() {
		return kaldor_e;
	}

	public float getKaldorAsmodiansInfluence() {
		return kaldor_a;
	}

	public float getKaldorBalaursInfluence() {
		return kaldor_b;
	}

	// ======[PANESTERRA]=====
	// =======================
	public float getBelusElyosInfluence() {
		return belus_e;
	}

	public float getBelusAsmodiansInfluence() {
		return belus_a;
	}

	public float getBelusBalaursInfluence() {
		return belus_b;
	}

	public float getAspidaElyosInfluence() {
		return aspida_e;
	}

	public float getAspidaAsmodiansInfluence() {
		return aspida_a;
	}

	public float getAspidaBalaursInfluence() {
		return aspida_b;
	}

	public float getAtanatosElyosInfluence() {
		return atanatos_e;
	}

	public float getAtanatosAsmodiansInfluence() {
		return atanatos_a;
	}

	public float getAtanatosBalaursInfluence() {
		return atanatos_b;
	}

	public float getDisillonElyosInfluence() {
		return disillon_e;
	}

	public float getDisillonAsmodiansInfluence() {
		return disillon_a;
	}

	public float getDisillonBalaursInfluence() {
		return disillon_b;
	}

	public float getPvpRaceBonus(Race attRace) {
		float bonus = 1;
		float elyos = getGlobalElyosInfluence();
		float asmo = getGlobalAsmodiansInfluence();
		switch (attRace) {
		case ASMODIANS:
			if (elyos >= 0.81f && asmo <= 0.10f) {
				bonus = 1.2f;
			} else if (elyos >= 0.81f || (elyos >= 0.71f && asmo <= 0.10f)) {
				bonus = 1.15f;
			} else if (elyos >= 0.71f) {
				bonus = 1.1f;
			}
			break;
		case ELYOS:
			if (asmo >= 0.81f && elyos <= 0.10f) {
				bonus = 1.2f;
			} else if (asmo >= 0.81f || (asmo >= 0.71f && elyos <= 0.10f)) {
				bonus = 1.15f;
			} else if (asmo >= 0.71f) {
				bonus = 1.1f;
			}
			break;
		}
		return bonus;
	}
}