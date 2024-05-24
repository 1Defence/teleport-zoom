package com.teleportzoom;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@ConfigGroup(TeleportZoomConfig.GROUP)
public interface TeleportZoomConfig extends Config
{
	String GROUP = "teleportzoom";
	String PREFIX = "region_";
	@ConfigItem(
			keyName = "saveZoomKey",
			name = "Save Zoom",
			description = "When you press this key you'll save your zoom to the current region",
			position = 0
	)
	default Keybind saveKey()
	{
		return new Keybind(KeyEvent.VK_INSERT, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
	}

	@ConfigItem(
			keyName = "deleteZoomKey",
			name = "Delete Zoom",
			description = "When you press this key you'll delete your zoom of the current region",
			position = 1
	)
	default Keybind deleteKey()
	{
		return new Keybind(KeyEvent.VK_DELETE, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
	}
}
