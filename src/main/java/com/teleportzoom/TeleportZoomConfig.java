package com.teleportzoom;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import com.teleportzoom.GwenithHandler.HANDLE_TYPE;

@ConfigGroup(TeleportZoomConfig.GROUP)
public interface TeleportZoomConfig extends Config
{
	String GROUP = "teleportzoom";
	String PREFIX = "region_";
	String PREFIX_DIRECTION = "regionDirection_";

    @ConfigSection(name="Gwenith General", description="Gwenith general portal settings", position=4)
    String gwenithGeneralPanel = "gwenithGeneralPanel";

	@ConfigSection(name="White Portals", description="Gwenith white portal settings", position=5, closedByDefault = true)
	String gwenithWhitePanel = "gwenithWhitePanel";

	@ConfigSection(name="Blue Portals", description="Gwenith blue portal settings", position=6, closedByDefault = true)
	String gwenithBluePanel = "gwenithBluePanel";

	@ConfigSection(name="Black Portals", description="Gwenith black portal settings", position=7, closedByDefault = true)
	String gwenithBlackPanel = "gwenithBlackPanel";

	@ConfigSection(name="Purple Portals", description="Gwenith purple portal settings", position=8, closedByDefault = true)
	String gwenithPurplePanel = "gwenithPurplePanel";

	@ConfigSection(name="Green Portals", description="Gwenith green portal settings", position=9, closedByDefault = true)
	String gwenithGreenPanel = "gwenithGreenPanel";

	@ConfigSection(name="Yellow Portals", description="Gwenith yellow portal settings", position=10, closedByDefault = true)
	String gwenithYellowPanel = "gwenithYellowPanel";

	@ConfigSection(name="Cyan Portals", description="Gwenith cyan portal settings", position=11, closedByDefault = true)
	String gwenithCyanPanel = "gwenithCyanPanel";

	@ConfigSection(name="Red Portals", description="Gwenith red portal settings", position=12, closedByDefault = true)
	String gwenithRedPanel = "gwenithRedPanel";


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

	@ConfigItem(
			keyName = "directionPrompt",
			name = "Direction Prompt",
			description = "Additionally prompt for a compass direction on save (Cardinals only)",
			position = 3
	)
	default boolean directionPrompt()
	{
		return false;
	}

	@ConfigItem(
			keyName = "portalLabels",
			name = "Portal Labels",
			description = "Draws labels over portals, used to determine what portals you want to modify the zoom/direction of",
			position = 14,
			section = gwenithGeneralPanel
	)
	default boolean portalLabels()
	{
		return false;
	}


	@ConfigItem(
			keyName = "enableHandles",
			name = "Enable Handles",
			description = "Enables Direction/zoom modification on portal teleports with below settings.",
			position = 15,
			section = gwenithGeneralPanel
	)
	default boolean enableHandles()
	{
		return false;
	}

	/**White**/
	@ConfigItem(
			keyName = "whiteAHandle",
			name = "White A Handle",
			description = "What zoom actions are handled when entering white portal A",
			position = 16,
			section = gwenithWhitePanel
	)
	default HANDLE_TYPE whiteAZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "whiteADirection",
			name = "White A Direction",
			description = "Direction to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 17,
			section = gwenithWhitePanel
	)
	default Direction whiteADirection()
	{
		return Direction.EAST;
	}

	@ConfigItem(
			keyName = "whiteAZoom",
			name = "White A Zoom",
			description = "Zoom to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 18,
			section = gwenithWhitePanel
	)
	default int whiteAZoom()
	{
		return 0;
	}

	@ConfigItem(
			name = "——————————————————————————————",
			keyName = "whiteDividerA",
			description = "",
			position = 18,
			section = gwenithWhitePanel
	)
	void whiteDividerA();

	@ConfigItem(
			keyName = "whiteBHandle",
			name = "White B Handle",
			description = "What zoom actions are handled when entering the white portal B",
			position = 19,
			section = gwenithWhitePanel
	)
	default HANDLE_TYPE whiteBZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "whiteBDirection",
			name = "White B Direction",
			description = "Direction to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 20,
			section = gwenithWhitePanel
	)
	default Direction whiteBDirection()
	{
		return Direction.NORTH;
	}

	@ConfigItem(
			keyName = "whiteBZoom",
			name = "White B Zoom",
			description = "Zoom to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 21,
			section = gwenithWhitePanel
	)
	default int whiteBZoom()
	{
		return 0;
	}



	/**Blue**/
	@ConfigItem(
			keyName = "blueAHandle",
			name = "Blue A Handle",
			description = "What zoom actions are handled when entering the blue portal A",
			position = 22,
			section = gwenithBluePanel
	)
	default HANDLE_TYPE blueAZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "blueADirection",
			name = "Blue A Direction",
			description = "Direction to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 23,
			section = gwenithBluePanel
	)
	default Direction blueADirection()
	{
		return Direction.EAST;
	}

	@ConfigItem(
			keyName = "blueAZoom",
			name = "Blue A Zoom",
			description = "Zoom to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 24,
			section = gwenithBluePanel
	)
	default int blueAZoom()
	{
		return 0;
	}

	@ConfigItem(
			name = "——————————————————————————————",
			keyName = "blueDividerA",
			description = "",
			position = 24,
			section = gwenithBluePanel
	)
	void blueDividerA();

	@ConfigItem(
			keyName = "blueBHandle",
			name = "Blue B Handle",
			description = "What zoom actions are handled when entering the blue portal B",
			position = 25,
			section = gwenithBluePanel
	)
	default HANDLE_TYPE blueBZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "blueBDirection",
			name = "Blue B Direction",
			description = "Direction to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 26,
			section = gwenithBluePanel
	)
	default Direction blueBDirection()
	{
		return Direction.SOUTH;
	}

	@ConfigItem(
			keyName = "blueBZoom",
			name = "Blue B Zoom",
			description = "Zoom to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 27,
			section = gwenithBluePanel
	)
	default int blueBZoom()
	{
		return 0;
	}


	/**Black**/
	@ConfigItem(
			keyName = "blackAHandle",
			name = "Black A Handle",
			description = "What zoom actions are handled when entering the black portal A",
			position = 28,
			section = gwenithBlackPanel
	)
	default HANDLE_TYPE blackAZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "blackADirection",
			name = "Black A Direction",
			description = "Direction to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 29,
			section = gwenithBlackPanel
	)
	default Direction blackADirection()
	{
		return Direction.WEST;
	}

	@ConfigItem(
			keyName = "blackAZoom",
			name = "Black A Zoom",
			description = "Zoom to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 30,
			section = gwenithBlackPanel
	)
	default int blackAZoom()
	{
		return 0;
	}

	@ConfigItem(
			name = "——————————————————————————————",
			keyName = "blackDividerA",
			description = "",
			position = 30,
			section = gwenithBlackPanel
	)
	void blackDividerA();

	@ConfigItem(
			keyName = "blackBHandle",
			name = "Black B Handle",
			description = "What zoom actions are handled when entering the black portal B",
			position = 31,
			section = gwenithBlackPanel
	)
	default HANDLE_TYPE blackBZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "blackBDirection",
			name = "Black B Direction",
			description = "Direction to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 32,
			section = gwenithBlackPanel
	)
	default Direction blackBDirection()
	{
		return Direction.WEST;
	}

	@ConfigItem(
			keyName = "blackBZoom",
			name = "Black B Zoom",
			description = "Zoom to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 33,
			section = gwenithBlackPanel
	)
	default int blackBZoom()
	{
		return 0;
	}

	/**Purple**/
	@ConfigItem(
			keyName = "purpleAHandle",
			name = "Purple A Handle",
			description = "What zoom actions are handled when entering the purple portal A",
			position = 34,
			section = gwenithPurplePanel
	)
	default HANDLE_TYPE purpleAZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "purpleADirection",
			name = "Purple A Direction",
			description = "Direction to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 35,
			section = gwenithPurplePanel
	)
	default Direction purpleADirection()
	{
		return Direction.WEST;
	}

	@ConfigItem(
			keyName = "purpleAZoom",
			name = "Purple A Zoom",
			description = "Zoom to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 36,
			section = gwenithPurplePanel
	)
	default int purpleAZoom()
	{
		return 0;
	}

	@ConfigItem(
			name = "——————————————————————————————",
			keyName = "purpleDividerA",
			description = "",
			position = 36,
			section = gwenithPurplePanel
	)
	void purpleDividerA();

	@ConfigItem(
			keyName = "purpleBHandle",
			name = "Purple B Handle",
			description = "What zoom actions are handled when entering the purple portal B",
			position = 37,
			section = gwenithPurplePanel
	)
	default HANDLE_TYPE purpleBZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "purpleBDirection",
			name = "Purple B Direction",
			description = "Direction to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 38,
			section = gwenithPurplePanel
	)
	default Direction purpleBDirection()
	{
		return Direction.NORTH;
	}

	@ConfigItem(
			keyName = "purpleBZoom",
			name = "Purple B Zoom",
			description = "Zoom to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 39,
			section = gwenithPurplePanel
	)
	default int purpleBZoom()
	{
		return 0;
	}

	/**Green**/
	@ConfigItem(
			keyName = "greenAHandle",
			name = "Green A Handle",
			description = "What zoom actions are handled when entering the green portal A",
			position = 40,
			section = gwenithGreenPanel
	)
	default HANDLE_TYPE greenAZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "greenADirection",
			name = "Green A Direction",
			description = "Direction to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 41,
			section = gwenithGreenPanel
	)
	default Direction greenADirection()
	{
		return Direction.NORTH;
	}

	@ConfigItem(
			keyName = "greenAZoom",
			name = "Green A Zoom",
			description = "Zoom to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 42,
			section = gwenithGreenPanel
	)
	default int greenAZoom()
	{
		return 0;
	}

	@ConfigItem(
			name = "——————————————————————————————",
			keyName = "greenDividerA",
			description = "",
			position = 42,
			section = gwenithGreenPanel
	)
	void greenDividerA();

	@ConfigItem(
			keyName = "greenBHandle",
			name = "Green B Handle",
			description = "What zoom actions are handled when entering the green portal B",
			position = 43,
			section = gwenithGreenPanel
	)
	default HANDLE_TYPE greenBZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "greenBDirection",
			name = "Green B Direction",
			description = "Direction to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 44,
			section = gwenithGreenPanel
	)
	default Direction greenBDirection()
	{
		return Direction.SOUTH;
	}

	@ConfigItem(
			keyName = "greenBZoom",
			name = "Green B Zoom",
			description = "Zoom to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 45,
			section = gwenithGreenPanel
	)
	default int greenBZoom()
	{
		return 0;
	}

	@ConfigItem(
			name = "——————————————————————————————",
			keyName = "greenDividerB",
			description = "",
			position = 45,
			section = gwenithGreenPanel
	)
	void greenDividerB();

	@ConfigItem(
			keyName = "greenCHandle",
			name = "Green C Handle",
			description = "What zoom actions are handled when entering the green portal C",
			position = 46,
			section = gwenithGreenPanel
	)
	default HANDLE_TYPE greenCZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "greenCDirection",
			name = "Green C Direction",
			description = "Direction to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 47,
			section = gwenithGreenPanel
	)
	default Direction greenCDirection()
	{
		return Direction.NORTH;
	}

	@ConfigItem(
			keyName = "greenCZoom",
			name = "Green C Zoom",
			description = "Zoom to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 48,
			section = gwenithGreenPanel
	)
	default int greenCZoom()
	{
		return 0;
	}

	/**Yellow**/
	@ConfigItem(
			keyName = "yellowAHandle",
			name = "Yellow A Handle",
			description = "What zoom actions are handled when entering the yellow portal A",
			position = 49,
			section = gwenithYellowPanel
	)
	default HANDLE_TYPE yellowAZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "yellowADirection",
			name = "Yellow A Direction",
			description = "Direction to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 50,
			section = gwenithYellowPanel
	)
	default Direction yellowADirection()
	{
		return Direction.WEST;
	}

	@ConfigItem(
			keyName = "yellowAZoom",
			name = "Yellow A Zoom",
			description = "Zoom to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 51,
			section = gwenithYellowPanel
	)
	default int yellowAZoom()
	{
		return 0;
	}

	@ConfigItem(
			name = "——————————————————————————————",
			keyName = "yellowDividerA",
			description = "",
			position = 51,
			section = gwenithYellowPanel
	)
	void yellowDividerA();

	@ConfigItem(
			keyName = "yellowBHandle",
			name = "Yellow B Handle",
			description = "What zoom actions are handled when entering the yellow portal B",
			position = 52,
			section = gwenithYellowPanel
	)
	default HANDLE_TYPE yellowBZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "yellowBDirection",
			name = "Yellow B Direction",
			description = "Direction to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 53,
			section = gwenithYellowPanel
	)
	default Direction yellowBDirection()
	{
		return Direction.NORTH;
	}

	@ConfigItem(
			keyName = "yellowBZoom",
			name = "Yellow B Zoom",
			description = "Zoom to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 54,
			section = gwenithYellowPanel
	)
	default int yellowBZoom()
	{
		return 0;
	}

	/**Cyan**/
	@ConfigItem(
			keyName = "cyanAHandle",
			name = "Cyan A Handle",
			description = "What zoom actions are handled when entering the cyan portal A",
			position = 55,
			section = gwenithCyanPanel
	)
	default HANDLE_TYPE cyanAZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "cyanADirection",
			name = "Cyan A Direction",
			description = "Direction to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 56,
			section = gwenithCyanPanel
	)
	default Direction cyanADirection()
	{
		return Direction.NORTH;
	}

	@ConfigItem(
			keyName = "cyanAZoom",
			name = "Cyan A Zoom",
			description = "Zoom to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 57,
			section = gwenithCyanPanel
	)
	default int cyanAZoom()
	{
		return 0;
	}

	@ConfigItem(
			name = "——————————————————————————————",
			keyName = "cyanDividerA",
			description = "",
			position = 57,
			section = gwenithCyanPanel
	)
	void cyanDividerA();

	@ConfigItem(
			keyName = "cyanBHandle",
			name = "Cyan B Handle",
			description = "What zoom actions are handled when entering the cyan portal B",
			position = 58,
			section = gwenithCyanPanel
	)
	default HANDLE_TYPE cyanBZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "cyanBDirection",
			name = "Cyan B Direction",
			description = "Direction to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 59,
			section = gwenithCyanPanel
	)
	default Direction cyanBDirection()
	{
		return Direction.SOUTH;
	}

	@ConfigItem(
			keyName = "cyanBZoom",
			name = "Cyan B Zoom",
			description = "Zoom to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 60,
			section = gwenithCyanPanel
	)
	default int cyanBZoom()
	{
		return 0;
	}

	/**Red**/
	@ConfigItem(
			keyName = "redAHandle",
			name = "Red A Handle",
			description = "What zoom actions are handled when entering the red portal A",
			position = 61,
			section = gwenithRedPanel
	)
	default HANDLE_TYPE redAZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "redADirection",
			name = "Red A Direction",
			description = "Direction to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 62,
			section = gwenithRedPanel
	)
	default Direction redADirection()
	{
		return Direction.SOUTH;
	}

	@ConfigItem(
			keyName = "redAZoom",
			name = "Red A Zoom",
			description = "Zoom to set when entering portal specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 63,
			section = gwenithRedPanel
	)
	default int redAZoom()
	{
		return 0;
	}

	@ConfigItem(
			name = "——————————————————————————————",
			keyName = "redDividerA",
			description = "",
			position = 63,
			section = gwenithRedPanel
	)
	void redDividerA();

	@ConfigItem(
			keyName = "redBHandle",
			name = "Red B Handle",
			description = "What zoom actions are handled when entering the red portal B",
			position = 64,
			section = gwenithRedPanel
	)
	default HANDLE_TYPE redBZoomType()
	{
		return HANDLE_TYPE.DIRECTION;
	}

	@ConfigItem(
			keyName = "redBDirection",
			name = "Red B Direction",
			description = "Direction to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 65,
			section = gwenithRedPanel
	)
	default Direction redBDirection()
	{
		return Direction.EAST;
	}

	@ConfigItem(
			keyName = "redBZoom",
			name = "Red B Zoom",
			description = "Zoom to set when entering specified portal (Refer to -Portal Labels- to know which portal is associated with what label)",
			position = 66,
			section = gwenithRedPanel
	)
	default int redBZoom()
	{
		return 0;
	}
}
