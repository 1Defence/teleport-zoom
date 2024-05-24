package com.teleportzoom;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientUI;

@Slf4j
@PluginDescriptor(
	name = "Teleport Zoom",
		description = "Set custom zoom value when arriving at a teleport"
)
public class TeleportZoomPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private TeleportZoomConfig config;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private ConfigManager configManager;

	@Inject
	private KeyManager keyManager;

	@Inject
	private ClientUI clientUI;

	private boolean lastFocusStatus = false;

	GameState lastState = GameState.LOGGED_IN;

	WorldPoint lastWorldPoint = null;

	private final CustomHotkeyListener saveKeyListener = new CustomHotkeyListener(() -> config.saveKey())
	{
		@Override
		public void hotkeyPressed()
		{
			clientThread.invoke(() -> SaveZoom());
		}
	};

	private final CustomHotkeyListener deleteKeyListener = new CustomHotkeyListener(() -> config.deleteKey())
	{
		@Override
		public void hotkeyPressed()
		{
			clientThread.invoke(() -> DeleteZoom());
		}
	};

	@Provides
	TeleportZoomConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TeleportZoomConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		keyManager.registerKeyListener(saveKeyListener);
		keyManager.registerKeyListener(deleteKeyListener);
	}

	@Override
	protected void shutDown() throws Exception
	{
		keyManager.unregisterKeyListener(saveKeyListener);
		keyManager.unregisterKeyListener(deleteKeyListener);
	}

	@Subscribe
	public void onClientTick(ClientTick event)
	{
		//Fix chat being locked & hotkeys from being unusable if user loses focus
		if(lastFocusStatus != clientUI.isFocused()){
			if(!clientUI.isFocused()){
				if(saveKeyListener.isPressed()){
					saveKeyListener.ReleaseHotkey();
				}
				if(deleteKeyListener.isPressed()){
					deleteKeyListener.ReleaseHotkey();
				}
			}
		}
		lastFocusStatus = clientUI.isFocused();
	}

	/**
	 * Save last known location to verify a teleport has occcured rather than running accross a load line.
	 */
	@Subscribe
	public void onGameTick(GameTick tick)
	{
		lastWorldPoint = GetCurrentWorldPoint();
	}

	/**
	 * Detect when a teleport load has occured and set the zoom accordingly if there is one saved for the given region.
	 */
	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (lastState == GameState.LOADING && gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			String savedZoomRaw = configManager.getConfiguration(TeleportZoomConfig.GROUP, TeleportZoomConfig.PREFIX + GetCurrentRegion());
			if(savedZoomRaw != null)
			{
				//ensures the zoom only triggers if the player traverses more than the max natural tiles possible in one tick(1=walk,2=run,3=run+light log)
				boolean distanceValid = lastWorldPoint == null || GetCurrentWorldPoint().distanceTo(lastWorldPoint) > 3;
				if(distanceValid)
				{
					try
					{
						int savedZoom = Integer.parseInt(savedZoomRaw);
						client.runScript(ScriptID.CAMERA_DO_ZOOM, savedZoom, savedZoom);
					} catch (NumberFormatException ignored)
					{
					}
				}
			}

		}
		lastState = gameStateChanged.getGameState();
	}

	/**
	 * Convenience function to send a game message.
	 */
	void SendGameMessage(String contents){
			String message = new ChatMessageBuilder()
					.append(ChatColorType.HIGHLIGHT)
					.append(contents)
					.build();
			chatMessageManager.queue(QueuedMessage.builder()
					.type(ChatMessageType.CONSOLE)
					.runeLiteFormattedMessage(message)
					.build());
	}


	/**
	 * If valid gets the current world point of the local player.
	 */
	WorldPoint GetCurrentWorldPoint(){
		Player localPlayer;
		if((localPlayer = client.getLocalPlayer()) == null)
			return null;

		WorldPoint worldPoint;
		if((worldPoint = localPlayer.getWorldLocation()) == null)
			return null;

		return worldPoint;
	}

	/**
	 * Determine and return the current region accounting for instances such as the POH
	 */
	int GetCurrentRegion(){
		WorldPoint worldPoint = GetCurrentWorldPoint();
		if(worldPoint == null)
			return -1;

		LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);

		if(localPoint == null)
			return -1;

		return WorldPoint.fromLocalInstance(client, localPoint).getRegionID();
	}

	/**
	 * Return the current zoom
	 */
	int GetCurrentZoom(){
		return client.getVarcIntValue(VarClientInt.CAMERA_ZOOM_FIXED_VIEWPORT);
	}

	/**
	 * Save the current zoom to the active loaded region
	 */
	void SaveZoom(){
		int currentRegionId = GetCurrentRegion();
		if(currentRegionId != -1)
		{
			int currentZoom = GetCurrentZoom();
			configManager.setConfiguration(TeleportZoomConfig.GROUP, TeleportZoomConfig.PREFIX + currentRegionId, currentZoom);
			SendGameMessage("Saved zoom...(" + currentZoom + ")");
		}
	}

	/**
	 * Delete the current zoom from the active loaded region
	 */
	void DeleteZoom(){
		int currentRegionId = GetCurrentRegion();
		if(currentRegionId != -1)
		{
			int currentZoom = GetCurrentZoom();
			configManager.unsetConfiguration(TeleportZoomConfig.GROUP, TeleportZoomConfig.PREFIX + currentRegionId);
			SendGameMessage("Deleted zoom...("+currentZoom+")");
		}
	}

}
