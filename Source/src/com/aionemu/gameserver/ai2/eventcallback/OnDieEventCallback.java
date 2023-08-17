/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.ai2.eventcallback;

import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.event.AIEventType;

/**
 * Callback for {@link AIEventType#DIED} event
 *
 * @author SoulKeeper
 */
public abstract class OnDieEventCallback extends OnHandleAIGeneralEvent {

	@Override
	protected void onBeforeHandleGeneralEvent(AbstractAI obj, AIEventType eventType) {
		if (AIEventType.DIED == eventType) {
			onBeforeDie(obj);
		}
	}

	@Override
	protected void onAfterHandleGeneralEvent(AbstractAI obj, AIEventType eventType) {
		if (AIEventType.DIED == eventType) {
			onAfterDie(obj);
		}
	}

	public abstract void onBeforeDie(AbstractAI obj);

	public abstract void onAfterDie(AbstractAI obj);
}