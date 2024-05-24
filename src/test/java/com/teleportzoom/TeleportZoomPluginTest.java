package com.teleportzoom;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TeleportZoomPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TeleportZoomPlugin.class);
		RuneLite.main(args);
	}
}