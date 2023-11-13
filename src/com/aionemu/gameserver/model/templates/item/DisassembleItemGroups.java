package com.aionemu.gameserver.model.templates.item;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author BeckUp.Media
 */
@XmlRootElement(name = "itemGroup")
public class DisassembleItemGroups
{
	@XmlAttribute(name = "gProb")
	private int GroupProb;
	@XmlAttribute(name = "minLevel")
	private int MinLevel;
	@XmlAttribute(name = "maxLevel")
	private int MaxLevel;
	@XmlList
	@XmlAttribute(name = "onlyClass")
	private List<PlayerClass> OnlyClass;
	private List<PlayerClass> onlyClassList;
	@XmlAttribute(name = "race")
	private Race PlayerRace = Race.PC_ALL;
	@XmlElement(name = "item")
	private List<DisassembleItems> GroupItems;

	//todo need to parse correct classes
	/*void afterUnmarshal(Unmarshaller u, Object parent) {
		if (OnlyClass != null) {
			for (PlayerClass clazz : OnlyClass) {
				onlyClassList.add(clazz);
			}
			OnlyClass = null;
		}
	}*/
	public int getGroupProb()
	{
		return GroupProb;
	}

	public int getMinLevel()
	{
		return MinLevel;
	}

	public int getMaxLevel()
	{
		return MaxLevel;
	}
	public Race getRace()
	{
		return PlayerRace;
	}
	public List<PlayerClass> getPlayerClassList()
	{
		return OnlyClass;
	}

	public List<DisassembleItems> getGroupItems()
	{
		return GroupItems;
	}
}
