/*
 * This file is part of Encom.
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
package admincommands;

import com.aionemu.gameserver.configs.Config;
import com.aionemu.gameserver.dataholders.*;
import com.aionemu.gameserver.dataholders.loadingutils.XmlValidationHandler;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.services.EventService;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.utils.chathandlers.ChatProcessor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.io.filefilter.FileFilterUtils.*;

/**
 * @author MrPoke
 */
public class Reload extends AdminCommand {

	private static final Logger log = LoggerFactory.getLogger(Reload.class);

	public Reload() {
		super("reload");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length != 1) {
			PacketSendUtility.sendMessage(admin,
				"syntax //reload <quest | skill | portal | spawn | commands | drop | gameshop | events | config | mail>");
			return;
		}
		if (params[0].equals("quest")) {
			File xml = new File("./data/static_data/quest_data/quest_data.xml");
			File dir = new File("./data/static_data/quest_script_data");
			try {
				QuestEngine.getInstance().shutdown();
				JAXBContext jc = JAXBContext.newInstance(StaticData.class);
				Unmarshaller un = jc.createUnmarshaller();
				un.setSchema(getSchema("./data/static_data/static_data.xsd"));
				QuestsData newQuestData = (QuestsData) un.unmarshal(xml);
				QuestsData questsData = DataManager.QUEST_DATA;
				questsData.setQuestsData(newQuestData.getQuestsData());
				XMLQuests questScriptsData = DataManager.XML_QUESTS;
				questScriptsData.getQuest().clear();
				for (File file : listFiles(dir, true)) {
					XMLQuests data = ((XMLQuests) un.unmarshal(file));
					if (data != null)
						if (data.getQuest() != null)
							questScriptsData.getQuest().addAll(data.getQuest());
				}
				QuestEngine.getInstance().load(null);
			}
			catch (Exception e) {
				PacketSendUtility.sendMessage(admin, "Quest reload failed!");
				log.error("quest reload fail", e);
			}
			finally {
				PacketSendUtility.sendMessage(admin, "Quest reload Success!");
			}
		}

		else if (params[0].equals("skill")) {
			File dir = new File("./data/static_data/skills");
			try {
				JAXBContext jc = JAXBContext.newInstance(StaticData.class);
				Unmarshaller un = jc.createUnmarshaller();
				un.setSchema(getSchema("./data/static_data/static_data.xsd"));
				List<SkillTemplate> newTemplates = new ArrayList<SkillTemplate>();
				for (File file : listFiles(dir, true)) {
					SkillData data = (SkillData) un.unmarshal(file);
					if (data != null)
						newTemplates.addAll(data.getSkillTemplates());
				}
				DataManager.SKILL_DATA.setSkillTemplates(newTemplates);
				DataManager.SKILL_DATA.initializeCooldownGroups();
			}
			catch (Exception e) {
				PacketSendUtility.sendMessage(admin, "Skill reload failed!");
				log.error("Skill reload failed!", e);
			}
			finally {
				PacketSendUtility.sendMessage(admin, "Skill reload Success!");
			}
		}
		else if (params[0].equals("portal")) {

			try {
				JAXBContext jc = JAXBContext.newInstance(StaticData.class);
				Unmarshaller un = jc.createUnmarshaller();
				un.setSchema(getSchema("./data/static_data/static_data.xsd"));
			}
			catch (Exception e) {
				PacketSendUtility.sendMessage(admin, "Portal reload failed!");
				log.error("Portal reload failed!", e);
			}
			finally {
				PacketSendUtility.sendMessage(admin, "Portal reload Success!");
			}
		}
		else if (params[0].equals("commands")) {
			ChatProcessor.getInstance().reload();
			PacketSendUtility.sendMessage(admin, "Admin commands successfully reloaded!");
		}
		else if (params[0].equals("config")) {
			Config.reload();
			PacketSendUtility.sendMessage(admin, "Configs successfully reloaded!");
		}
		// Needs to be implented in NpcDropData.java
		/**else if (params[0].equals("drop")) {
			NpcDropData npcDropData = NpcDropData.load();
			DataManager.NPC_DROP_DATA = npcDropData;
			PacketSendUtility.sendMessage(admin, "NpcDrops successfully reloaded!");
		}*/
		else if (params[0].equals("gameshop")) {
			InGameShopEn.getInstance().reload();
			PacketSendUtility.sendMessage(admin, "Gameshop successfully reloaded!");
		}
		else if (params[0].equals("events")) {
			File eventXml = new File("./data/static_data/events_config/events_config.xml");
			EventData data = null;
			try {
				JAXBContext jc = JAXBContext.newInstance(EventData.class);
				Unmarshaller un = jc.createUnmarshaller();
				un.setEventHandler(new XmlValidationHandler());
				un.setSchema(getSchema("./data/static_data/static_data.xsd"));
				data = (EventData) un.unmarshal(eventXml);
			}
			catch (Exception e) {
				PacketSendUtility.sendMessage(admin, "Event reload failed! Keeping the last version ...");
				log.error("Event reload failed!", e);
				return;
			}
			if (data != null) {
				EventService.getInstance().stop();
				String text = data.getActiveText();
				if (text == null || text.trim().length() == 0)
					text = "NONE";
				DataManager.EVENT_DATA.setAllEvents(data.getAllEvents(), data.getActiveText());
				PacketSendUtility.sendMessage(admin, "Active events: " + text);
				EventService.getInstance().start();
			}
		}
		else
			PacketSendUtility.sendMessage(admin,
				"syntax //reload <quest | skill | portal | spawn | commands | drop | gameshop | events | config>");

	}

	private Schema getSchema(String xml_schema) {
		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try {
			schema = sf.newSchema(new File(xml_schema));
		}
		catch (SAXException saxe) {
			throw new Error("Error while getting schema", saxe);
		}

		return schema;
	}

	private Collection<File> listFiles(File root, boolean recursive) {
		IOFileFilter dirFilter = recursive ? makeSVNAware(HiddenFileFilter.VISIBLE) : null;

		return FileUtils.listFiles(root,
			and(and(notFileFilter(prefixFileFilter("new")), suffixFileFilter(".xml")), HiddenFileFilter.VISIBLE), dirFilter);
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player,
			"syntax //reload <quest | skill | portal | commands | drop | gameshop | events | config>");
	}
}