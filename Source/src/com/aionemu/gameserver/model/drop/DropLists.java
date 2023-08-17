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
package com.aionemu.gameserver.model.drop;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DropLists {

	private static List<Element> listgroups = new ArrayList<Element>();
	private static List<Element> drops = new ArrayList<Element>();
	private static List<Element> allnpcs = new ArrayList<Element>();
	private static List<Integer> npcids = new ArrayList<Integer>();
	private static final Logger log = LoggerFactory.getLogger(DropLists.class);

	public static void Xmlmian(int min, int max) {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document.setXmlVersion("1.0");
			document.setXmlStandalone(true);
			Element root = document.createElement("npc_drops");
			document.appendChild(root);
			Collection<NpcTemplate> tm = DataManager.NPC_DATA.getNpcData().valueCollection();
			for (NpcTemplate npc : tm) {
				if (npc == null) {
					continue;
				}
				if (npc.getTemplateId() < min || npc.getTemplateId() > max) {
					continue;
				}
				if (npc.getRace() == Race.ASMODIANS || npc.getRace() == Race.ELYOS || npc.getRace() == Race.PC_LIGHT_CASTLE_DOOR
					|| npc.getRace() == Race.PC_DARK_CASTLE_DOOR || npc.getRace() == Race.DRAGON_CASTLE_DOOR) {
					continue;
				}
				if (npc.getAi().equals("skillarea") || npc.getAi().equals("trap") || npc.getAi().equals("homing")
					|| npc.getAi().equals("servant") || npc.getAi().equals("dummy") || npc.getAi().equals("siege_weapon")
					|| npc.getAi().equals("general") || npc.getAi().equals("noaction") || npc.getAi().equals("siege_gaterepair")
					|| npc.getAi().equals("siege_mine") || npc.getAi().equals("fortressgate") || npc.getAi().equals("spring")
					|| npc.getAi().equals("artifact") || npc.getAi().equals("quest_use_item") || npc.getAi().equals("useitem")
					|| npc.getAi().equals("resurrect") || npc.getAi().equals("portal") || npc.getAi().equals("portal_dialog")
					|| npc.getAi().equals("kisk") || npc.getAi().equals("marbatacontroller") || npc.getAi().equals("surkana")
					|| npc.getAi().equals("onedmgperhit") || npc.getAi().equals("feedingmantutu") || npc.getAi().equals("artifact_protector")) {
					continue;
				}
				if (npc.getTribe() == TribeClass.FIELD_OBJECT_ALL && !npc.getAi().equals("chest")) {
					continue;
				}
				
				NpcDrop npcDrop = npc.getNpcDrop();
				if (npcDrop == null) {
					continue;
				}
				Element npcdrops = document.createElement("npc_drop");
				npcdrops.setAttribute("npc_id", String.valueOf(npc.getTemplateId()));
				List<DropGroup> listgroup = npcDrop.getDropGroup();
				for (int i = 0; i < listgroup.size(); i++) {
					Element dropGroups = document.createElement("drop_group");
					DropGroup gr = listgroup.get(i);
					dropGroups.setAttribute("name", gr.getGroupName());
					if (!gr.isUseCategory()) {
						dropGroups.setAttribute("use_category", "false");
					}
					if (gr.getRace() != Race.PC_ALL) {
						dropGroups.setAttribute("race", String.valueOf(gr.getRace()));
					}
					List<Drop> droplist = gr.getDrop();
					for (int d = 0; d < droplist.size(); d++) {
						Element drop = document.createElement("drop");
						Drop dr = droplist.get(d);
						if (dr.isNoReduction()) {
							drop.setAttribute("no_reduce", "true");
						}
						if (dr.isEachMember()) {
							drop.setAttribute("eachmember", "true");
						}
						drop.setAttribute("chance", String.valueOf(dr.getChance()));
						drop.setAttribute("min_amount", String.valueOf(dr.getMinAmount()));
						drop.setAttribute("max_amount", String.valueOf(dr.getMaxAmount()));
						drop.setAttribute("item_id", String.valueOf(dr.getItemId()));
						dropGroups.appendChild(drop);
						drops.add(drop);
					}
					npcdrops.appendChild(dropGroups);
					listgroups.add(dropGroups);
					npcids.add(npc.getTemplateId());
				}
				log.info("All drops for NpcId " + npc.getTemplateId() + " loaded completely!");
				root.appendChild(npcdrops);
				allnpcs.add(npcdrops);
			}
			drops.clear();
			listgroups.clear();
			allnpcs.clear();
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transFormer = transFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			String name = min + " - " + max;
			File file = new File("data/static_data/npc_drops/" + name + ".xml");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			StreamResult xmlResult = new StreamResult(out);
			transFormer.transform(domSource, xmlResult);
			System.out.println(file.getAbsolutePath());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		}
	}
}