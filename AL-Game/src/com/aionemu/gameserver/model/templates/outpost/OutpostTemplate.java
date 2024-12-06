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
package com.aionemu.gameserver.model.templates.outpost;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Wnkrz on 27/08/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Outpost")
public class OutpostTemplate {
	@XmlAttribute(name = "id")
	protected int id;

	@XmlAttribute(name = "world")
	protected int world;

	@XmlAttribute(name = "name")
	protected String nameId;

	@XmlAttribute(name = "artifact_id")
	protected int artifactId;

	public int getId() {
		return this.id;
	}

	public int getWorldId() {
		return this.world;
	}

	public String getName() {
		return nameId;
	}

	public int getArtifactId() {
		return artifactId;
	}
}