/*
 * This file is part of the Aion-Emu project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package system.handlers.admincommands;

import static org.apache.commons.io.filefilter.FileFilterUtils.and;
import static org.apache.commons.io.filefilter.FileFilterUtils.makeSVNAware;
import static org.apache.commons.io.filefilter.FileFilterUtils.notFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.prefixFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.suffixFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.aionemu.gameserver.configs.Config;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.EventData;
import com.aionemu.gameserver.dataholders.QuestsData;
import com.aionemu.gameserver.dataholders.SkillData;
import com.aionemu.gameserver.dataholders.StaticData;
import com.aionemu.gameserver.dataholders.XMLQuests;
import com.aionemu.gameserver.dataholders.loadingutils.XmlValidationHandler;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.services.EventService;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.utils.chathandlers.ChatProcessor;

/**
 * @author MrPoke
 */
public class Reload extends AdminCommand
{
	
	private static final Logger log = LoggerFactory.getLogger(Reload.class);
	
	public Reload()
	{
		super("reload");
	}
	
	@Override
	public void execute(Player admin, String... params)
	{
		if ((params == null) || (params.length != 1))
		{
			PacketSendUtility.sendMessage(admin, "syntax //reload <quest | skill | portal | spawn | commands | drop | gameshop | events | config>");
			return;
		}
		if (params[0].equals("quest"))
		{
			final File xml = new File("./data/static_data/quest_data/quest_data.xml");
			final File dir = new File("./data/static_data/quest_script_data");
			try
			{
				QuestEngine.getInstance().shutdown();
				final JAXBContext jc = JAXBContext.newInstance(StaticData.class);
				final Unmarshaller un = jc.createUnmarshaller();
				un.setSchema(getSchema("./data/static_data/static_data.xsd"));
				final QuestsData newQuestData = (QuestsData) un.unmarshal(xml);
				final QuestsData questsData = DataManager.QUEST_DATA;
				questsData.setQuestsData(newQuestData.getQuestsData());
				final XMLQuests questScriptsData = DataManager.XML_QUESTS;
				questScriptsData.getQuest().clear();
				for (final File file : listFiles(dir, true))
				{
					final XMLQuests data = ((XMLQuests) un.unmarshal(file));
					if (data != null)
					{
						if (data.getQuest() != null)
						{
							questScriptsData.getQuest().addAll(data.getQuest());
						}
					}
				}
				QuestEngine.getInstance().load(null);
			}
			catch (final Exception e)
			{
				PacketSendUtility.sendMessage(admin, "Quest reload failed!");
				log.error("quest reload fail", e);
			}
			finally
			{
				PacketSendUtility.sendMessage(admin, "Quest reload Success!");
			}
		}
		else if (params[0].equals("skill"))
		{
			final File dir = new File("./data/static_data/skills");
			try
			{
				final JAXBContext jc = JAXBContext.newInstance(StaticData.class);
				final Unmarshaller un = jc.createUnmarshaller();
				un.setSchema(getSchema("./data/static_data/static_data.xsd"));
				final List<SkillTemplate> newTemplates = new ArrayList<>();
				for (final File file : listFiles(dir, true))
				{
					final SkillData data = (SkillData) un.unmarshal(file);
					if (data != null)
					{
						newTemplates.addAll(data.getSkillTemplates());
					}
				}
				DataManager.SKILL_DATA.setSkillTemplates(newTemplates);
				DataManager.SKILL_DATA.initializeCooldownGroups();
			}
			catch (final Exception e)
			{
				PacketSendUtility.sendMessage(admin, "Skill reload failed!");
				log.error("Skill reload failed!", e);
			}
			finally
			{
				PacketSendUtility.sendMessage(admin, "Skill reload Success!");
			}
		}
		else if (params[0].equals("portal"))
		{
			// File dir = new File("./data/static_data/portals");
			try
			{
				final JAXBContext jc = JAXBContext.newInstance(StaticData.class);
				final Unmarshaller un = jc.createUnmarshaller();
				un.setSchema(getSchema("./data/static_data/static_data.xsd"));
				// List<PortalTemplate> newTemplates = new ArrayList<PortalTemplate>();
				// for (File file : listFiles(dir, true)) {
				// PortalData data = (PortalData) un.unmarshal(file);
				// if (data != null && data.getPortals() != null)
				// newTemplates.addAll(data.getPortals());
				// }
				// DataManager.PORTAL_DATA.setPortals(newTemplates);
			}
			catch (final Exception e)
			{
				PacketSendUtility.sendMessage(admin, "Portal reload failed!");
				log.error("Portal reload failed!", e);
			}
			finally
			{
				PacketSendUtility.sendMessage(admin, "Portal reload Success!");
			}
		}
		else if (params[0].equals("commands"))
		{
			ChatProcessor.getInstance().reload();
			PacketSendUtility.sendMessage(admin, "Admin commands successfully reloaded!");
		}
		else if (params[0].equals("config"))
		{
			Config.reload();
			PacketSendUtility.sendMessage(admin, "Configs successfully reloaded!");
		}
		// Needs to be implented in NpcDropData.java
		/**
		 * else if (params[0].equals("drop")) { NpcDropData npcDropData = NpcDropData.load(); DataManager.NPC_DROP_DATA = npcDropData; PacketSendUtility.sendMessage(admin, "NpcDrops successfully reloaded!"); }
		 */
		else if (params[0].equals("gameshop"))
		{
			InGameShopEn.getInstance().reload();
			PacketSendUtility.sendMessage(admin, "Gameshop successfully reloaded!");
		}
		else if (params[0].equals("events"))
		{
			final File eventXml = new File("./data/static_data/events_config/events_config.xml");
			EventData data = null;
			try
			{
				final JAXBContext jc = JAXBContext.newInstance(EventData.class);
				final Unmarshaller un = jc.createUnmarshaller();
				un.setEventHandler(new XmlValidationHandler());
				un.setSchema(getSchema("./data/static_data/static_data.xsd"));
				data = (EventData) un.unmarshal(eventXml);
			}
			catch (final Exception e)
			{
				PacketSendUtility.sendMessage(admin, "Event reload failed! Keeping the last version ...");
				log.error("Event reload failed!", e);
				return;
			}
			if (data != null)
			{
				EventService.getInstance().stop();
				String text = data.getActiveText();
				if ((text == null) || (text.trim().length() == 0))
				{
					text = "NONE";
				}
				DataManager.EVENT_DATA.setAllEvents(data.getAllEvents(), data.getActiveText());
				PacketSendUtility.sendMessage(admin, "Active events: " + text);
				EventService.getInstance().start();
			}
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "syntax //reload <quest | skill | portal | spawn | commands | drop | gameshop | events | config>");
		}
		
	}
	
	private Schema getSchema(String xml_schema)
	{
		Schema schema = null;
		final SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		try
		{
			schema = sf.newSchema(new File(xml_schema));
		}
		catch (final SAXException saxe)
		{
			throw new Error("Error while getting schema", saxe);
		}
		
		return schema;
	}
	
	private Collection<File> listFiles(File root, boolean recursive)
	{
		final IOFileFilter dirFilter = recursive ? makeSVNAware(HiddenFileFilter.VISIBLE) : null;
		
		return FileUtils.listFiles(root, and(and(notFileFilter(prefixFileFilter("new")), suffixFileFilter(".xml")), HiddenFileFilter.VISIBLE), dirFilter);
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "syntax //reload <quest | skill | portal | commands | drop | gameshop | events | config>");
	}
}
