package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.templates.item.DisassembleItemGroups;
import com.aionemu.gameserver.model.templates.item.DisassemblyItemSet;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author BeckUp.Media
 */
@XmlRootElement(name = "disassemblyitemsets")
@XmlAccessorType(XmlAccessType.FIELD)
public class DisassemblyItemSetsData
{
	@XmlElement(name = "disassemblyitemset")
	private List<DisassemblyItemSet> DisassemblyItemSet;

	private TIntObjectHashMap<List<DisassembleItemGroups>> disassemblyItemGroups = new TIntObjectHashMap<List<DisassembleItemGroups>>();
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		disassemblyItemGroups.clear();
		for (DisassemblyItemSet template : DisassemblyItemSet) {
			disassemblyItemGroups.put(template.getDisassemblyItemId(), template.getDisassembleSetList().getItemGroups());
		}
	}

	public List<DisassemblyItemSet> getDisassemblyItemSet()
	{
		return DisassemblyItemSet;
	}

	public int size()
	{
		return disassemblyItemGroups.size();
	}

	public List<DisassembleItemGroups> getInfoByItemId(int itemId)
	{
		return disassemblyItemGroups.get(itemId);
	}
}
