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
package com.aionemu.gameserver.utils.chathandlers;

import com.aionemu.commons.scripting.classlistener.ClassListener;
import com.aionemu.commons.utils.ClassUtils;

import java.lang.reflect.Modifier;

/**
 * Created on: 12.09.2009 14:13:24
 * 
 * @author Aquanox
 */
public class ChatCommandsLoader implements ClassListener {

	private ChatProcessor processor;

	public ChatCommandsLoader(ChatProcessor processor) {
		this.processor = processor;
	}

	@Override
	public void postLoad(Class<?>[] classes) {
		for (Class<?> c : classes) {
			if (!isValidClass(c)) {
				continue;
			}
			Class<?> tmp = (Class<?>) c;
			if (tmp != null) {
				try {
					processor.registerCommand((ChatCommand) tmp.newInstance());
				}
				catch (InstantiationException e) {
					e.printStackTrace();
				}
				catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		processor.onCompileDone();
	}

	@Override
	public void preUnload(Class<?>[] classes) {

	}

	public boolean isValidClass(Class<?> clazz) {
		final int modifiers = clazz.getModifiers();

		if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
			return false;
        }
		if (!Modifier.isPublic(modifiers)) {
			return false;
        }
		if (!ClassUtils.isSubclass(clazz, AdminCommand.class) && !ClassUtils.isSubclass(clazz, PlayerCommand.class)
			&& !ClassUtils.isSubclass(clazz, WeddingCommand.class)) {
			return false;
		}
		return true;
	}
}