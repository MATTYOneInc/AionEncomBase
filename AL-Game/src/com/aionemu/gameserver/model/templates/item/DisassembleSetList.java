package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author BeckUp.Media
 */
@XmlRootElement(name = "disassemble_set_list")
public class DisassembleSetList
{
	@XmlElement(name = "itemGroup")
	private List<DisassembleItemGroups> ItemGroups;

	public List<DisassembleItemGroups> getItemGroups()
	{
		return ItemGroups;
	}
}
