package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author BeckUp.Media
 */
@XmlRootElement(name = "create")
public class DisassembleItem
{
	@XmlAttribute(name = "itemId")
	private int ItemId;
	@XmlAttribute(name = "count")
	private int Count;
	@XmlAttribute(name = "disuse")
	private boolean disuse;

	public int getItemId()
	{
		return ItemId;
	}

	public int getCount()
	{
		return Count;
	}

	public boolean isDisuse()
	{
		return disuse;
	}
}
