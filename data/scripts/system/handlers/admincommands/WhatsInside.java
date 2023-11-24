package admincommands;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.DisassembleItemGroups;
import com.aionemu.gameserver.model.templates.item.DisassembleItems;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author BeckUp.Media
 */
public class WhatsInside extends AdminCommand
{
	public WhatsInside()
	{
		super("whatsinside");
	}

	@Override
	public void execute(Player player, String... params)
	{
		if ((params.length < 0) || (params.length < 1)) {
			onFail(player, "Pls use an Ingame Item Link");
			return;
		}
		int itemId = 0;
		String item = params[0];
		if (item.equals("[item:")) {
			item = params[1];
			Pattern id = Pattern.compile("(\\d{9})");
			Matcher result = id.matcher(item);
			if (result.find()) {
				itemId = Integer.parseInt(result.group(1));
			}
		} else {
			Pattern id = Pattern.compile("\\[item:(\\d{9})");
			Matcher result = id.matcher(item);
			if (result.find()) {
				itemId = Integer.parseInt(result.group(1));
			}
		}
		List<DisassembleItemGroups> GroupList = null;
		ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(itemId);
		String MainItemIngameString = "[item: " + template.getTemplateId() + "]";
		try {
			GroupList = DataManager.DISASSEMBLY_ITEMS_DATA.getInfoByItemId(itemId);
		} catch (Exception e) {
			PacketSendUtility.sendMessage(player, "Pls use an Disassembly Item.");
		}
		if (GroupList != null) {
			PacketSendUtility.sendMessage(player, String.format("Disassembly List for Item %s", MainItemIngameString));
			int groupIndex = 0;
			for (DisassembleItemGroups group : GroupList) {
				PacketSendUtility.sendMessage(player, String.format("GroupIndex: %d - GroupProb: %d \n- LevelMin: %d - LevelMax: %d \n- Race: %s - OnlyClass: %s"
						, groupIndex, group.getGroupProb(), group.getMinLevel(), group.getMaxLevel(), group.getRace()
						, (group.getPlayerClassList() != null ? group.getPlayerClassList().toString() : "ALL")));
				groupIndex++;
				int MaxValue = 0;
				for (DisassembleItems i : group.getGroupItems()) {
					ItemTemplate ITemplate = DataManager.ITEM_DATA.getItemTemplate(i.getItem().getItemId());
					String IngameString = "[item: " + (ITemplate != null ? ITemplate.getTemplateId() : "missing in templates " + i.getItem().getItemId()) + "]";
					int ItemProb = i.getItemProb();
					int ItemCount = i.getItem().getCount();
					boolean disuse = i.getItem().isDisuse();
					MaxValue += ItemProb;
					PacketSendUtility.sendMessage(player, String.format("%s - Rate: %d - Count: %d", IngameString, ItemProb, ItemCount));
					if (disuse)
						PacketSendUtility.sendMessage(player, "This item is an Disuse Item, deactivated in 5.8");
				}
				if (MaxValue == 0) {
					PacketSendUtility.sendMessage(player, "All fine, its an Select Box.\n");
				} else if (MaxValue != 10000)
					PacketSendUtility.sendMessage(player, String.format("MaxValue is not 10000. Current: %d\n", MaxValue));
				else
					PacketSendUtility.sendMessage(player, "All fine, all items in total are 10000.\n");
			}
		}
	}
}
