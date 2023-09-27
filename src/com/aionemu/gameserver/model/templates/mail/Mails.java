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
package com.aionemu.gameserver.model.templates.mail;

import com.aionemu.gameserver.model.Race;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sysMailTemplates" })
@XmlRootElement(name = "mails")
public class Mails {

	@XmlElement(name = "mail")
	private List<SysMail> sysMailTemplates;

	@XmlTransient
	private Map<String, SysMail> sysMailByName = new HashMap<String, SysMail>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (SysMail template : sysMailTemplates) {
			String sysMailName = template.getName().toLowerCase();
			sysMailByName.put(sysMailName, template);
		}
		sysMailTemplates.clear();
		sysMailTemplates = null;
	}

	public MailTemplate getMailTemplate(String name, String eventName, Race playerRace) {
		SysMail template = (SysMail) sysMailByName.get(name.toLowerCase());
		if (template == null) {
			return null;
		}
		return template.getTemplate(eventName, playerRace);
	}

	public int size() {
		return sysMailByName.values().size();
	}
}