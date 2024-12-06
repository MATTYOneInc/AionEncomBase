package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author BeckUp.Media
 */
@XmlRootElement(name = "item")
public class DisassembleItems
{
	@XmlAttribute(name = "iProb")
	private int ItemProb;
	@XmlAttribute(name = "custom")
	private boolean custom;
	@XmlElement(name = "create")
	private DisassembleItem ItemToCreate;

	public int getItemProb()
	{
		return ItemProb;
	}

	public DisassembleItem getItem()
	{
		return ItemToCreate;
	}
}
