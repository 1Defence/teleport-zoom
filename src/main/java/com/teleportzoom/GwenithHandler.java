/*
 * Copyright (c) 2025, 1Defence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.teleportzoom;

import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ScriptID;
import net.runelite.api.WorldEntity;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.WorldEntitySpawned;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Specific to Gwenith glide, you want the transition to occur as the teleport is occurring rather than after it's processed to not be jarring
 * It also wouldn't be super viable to go around every portal's edge and set a direction/zoom without getting sent through it not to mention the excessive number of them
 */
@Slf4j
@Singleton
public class GwenithHandler
{

    private final TeleportZoomPlugin plugin;
    private final TeleportZoomConfig config;
    private final ConfigManager configManager;
    private final Client client;
    private WorldEntity boatEntity = null;
    private WorldPoint lastBoatWP = null;
    private int lastFadeInTick = -1;

    public boolean configPortalLabels, configEnableHandles;

    /**
     * Pairing of WorldPoint(Portal center) to PortalData
     * PortalData is set to the default static values, effectively just the name,config name and color
     * PortalData's Handles are set later during config caching
     */
    public Map<WorldPoint, PortalData> portalMap = new HashMap<>(Map.ofEntries(
            Map.entry(new WorldPoint(2260, 3497, 0), new PortalData("White A","whiteA", Color.WHITE)),
            Map.entry(new WorldPoint(2158, 3293, 0), new PortalData("White B","whiteB", Color.WHITE)),
            Map.entry(new WorldPoint(2241, 3574, 0), new PortalData("Blue A", "blueA",Color.BLUE)),
            Map.entry(new WorldPoint(2155, 3247, 0), new PortalData("Blue B", "blueB",Color.BLUE)),
            Map.entry(new WorldPoint(2207, 3584, 0), new PortalData("Black A", "blackA",Color.BLACK)),
            Map.entry(new WorldPoint(2107, 3543, 0), new PortalData("Purple A", "purpleA",Color.PINK)),
            Map.entry(new WorldPoint(2197, 3512, 0), new PortalData("Green A", "greenA",Color.GREEN)),
            Map.entry(new WorldPoint(2127, 3171, 0), new PortalData("Green B", "greenB",Color.GREEN)),
            Map.entry(new WorldPoint(2157, 3464, 0), new PortalData("Yellow A", "yellowA",Color.YELLOW)),
            Map.entry(new WorldPoint(2105, 3423, 0), new PortalData("Black B", "blackB",Color.BLACK)),
            Map.entry(new WorldPoint(2142, 3582, 0), new PortalData("Cyan A", "cyanA",Color.CYAN)),
            Map.entry(new WorldPoint(2126, 3356, 0), new PortalData("Cyan B", "cyanB",Color.CYAN)),
            Map.entry(new WorldPoint(2172, 3523, 0), new PortalData("Purple B", "purpleB",Color.PINK)),
            Map.entry(new WorldPoint(2118, 3439, 0), new PortalData("Yellow B", "yellowB",Color.YELLOW)),
            Map.entry(new WorldPoint(2162, 3508, 0), new PortalData("Red A", "redA",Color.RED)),
            Map.entry(new WorldPoint(2106, 3574, 0), new PortalData("Red B", "redB",Color.RED)),
            Map.entry(new WorldPoint(2190, 3508, 0), new PortalData("Green C","greenC",Color.GREEN))
    ));

    /**Used for config updates without needing to reparse the entire collection*/
    public Map<String, PortalData> configPortalMapping = new HashMap<>();
    String HANDLE_POSTFIX = "Handle";
    String DIRECTION_POSTFIX = "Direction";
    String ZOOM_POSTFIX = "Zoom";

    /**What to handle when teleporting through a portal*/
    public enum HANDLE_TYPE {
        DIRECTION(1),
        ZOOM(2),
        BOTH(3),
        DISABLED(0);

        private final int value;

        HANDLE_TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @Inject
    public GwenithHandler(TeleportZoomPlugin plugin,TeleportZoomConfig config, ConfigManager configManager,Client client){
        this.plugin = plugin;
        this.config = config;
        this.configManager = configManager;
        this.client = client;
        cachePortalSettings();
    }

    /**Register boat on startup when no spawn events occur*/
    public void registerBoat(){
        WorldView wv = client.getTopLevelWorldView();
        if(wv == null)
            return;

        for (WorldEntity worldEntity : wv.worldEntities())
        {
            if (worldEntity.getOwnerType() == WorldEntity.OWNER_TYPE_SELF_PLAYER)
            {
                boatEntity = worldEntity;
                break;
            }
        }
    }

    /**
     * Set Portal handles (zoom/direction values & whether or not they're enabled) based on config values
     * Store in a separate mapping, the portal in question against it's 3 possible config values for fast modification on config edit.
     */
    public void cachePortalSettings(){
        configPortalLabels = config.portalLabels();
        configEnableHandles = config.enableHandles();
        configPortalMapping = new HashMap<>();
        for (Map.Entry<WorldPoint, PortalData> entry : portalMap.entrySet()) {
            PortalData portal = entry.getValue();

            String configPrefix = portal.portalConfigPrefix;
            configPortalMapping.put(configPrefix+HANDLE_POSTFIX,portal);
            configPortalMapping.put(configPrefix+DIRECTION_POSTFIX,portal);
            configPortalMapping.put(configPrefix+ZOOM_POSTFIX,portal);

            String handlesStr = configManager.getConfiguration(TeleportZoomConfig.GROUP,configPrefix+HANDLE_POSTFIX);
            if(!Strings.isNullOrEmpty(handlesStr))
            {
                try
                {
                    HANDLE_TYPE handles = HANDLE_TYPE.valueOf(handlesStr);
                    portal.SetHandles(handles);
                } catch (IllegalArgumentException ignored){}
            }

            String directionStr = configManager.getConfiguration(TeleportZoomConfig.GROUP,configPrefix+DIRECTION_POSTFIX);
            if(!Strings.isNullOrEmpty(directionStr))
            {
                try
                {
                    Direction direction = Direction.valueOf(directionStr);
                    portal.SetDirection(direction);
                } catch (IllegalArgumentException ignored){}
            }

            String zoomStr = configManager.getConfiguration(TeleportZoomConfig.GROUP,configPrefix+ZOOM_POSTFIX);
            if(!Strings.isNullOrEmpty(zoomStr))
            {
                try
                {
                    int zoom = Integer.parseInt(zoomStr);
                    portal.SetZoom(zoom);
                } catch (NumberFormatException ignored) {}
            }

        }
    }

    /**
     * There is a substantial collection of config values, modify the specific portal of the given config updated rather than refreshing the entire map
     */
    @Subscribe
    public void onConfigChanged(ConfigChanged configChanged){

        if(!configChanged.getGroup().equals(TeleportZoomConfig.GROUP))
        {
            return;
        }

        String configKey = configChanged.getKey();
        String configVal = configChanged.getNewValue();

        if(configKey.equals("portalLabels"))
        {
            configPortalLabels = config.portalLabels();
            return;
        }else if(configKey.equals("enableHandles"))
        {
            configEnableHandles = config.enableHandles();
            return;
        }

        PortalData portal = configPortalMapping.get(configKey);

        if(portal == null)
        {
            return;
        }

        if(configVal == null)
        {
            return;
        }

        if(configKey.endsWith(HANDLE_POSTFIX)){
            try
            {
                HANDLE_TYPE handles = HANDLE_TYPE.valueOf(configVal);
                portal.SetHandles(handles);
            } catch (IllegalArgumentException ignored) {}

        }else if(configKey.endsWith(DIRECTION_POSTFIX)){
            try
            {
                Direction direction = Direction.valueOf(configVal);
                portal.SetDirection(direction);
            } catch (IllegalArgumentException ignored){}

        }else if(configKey.endsWith(ZOOM_POSTFIX)){
            try
            {
                int zoom = Integer.parseInt(configVal);
                portal.SetZoom(zoom);
            } catch (NumberFormatException ignored) {}

        }

    }


    /**
     * Save current boat location on the tick
     * in edge case scenarios your boat is moved before the teleport or the targetlocation of said boat is incorrectly reported.
     * as such checking location on the fade tick can rarely, have drastically incorrect values.(this incorrect location is NOT post-teleport destination)
     * this is never the case for the previous tick's location as such we use this old value.
     */
    @Subscribe
    public void onGameTick(GameTick tick){
        lastBoatWP = GetBoatWorldLocation();
    }


    /**
     * Register the local players boat.
     */
    @Subscribe
    public void onWorldEntitySpawned(WorldEntitySpawned e)
    {
        WorldEntity worldEntity = e.getWorldEntity();
        if (worldEntity.getOwnerType() == WorldEntity.OWNER_TYPE_SELF_PLAYER)
        {
            boatEntity = worldEntity;
        }
    }

    /**
     * Convert Local location of the boat into a worldPoint, additionally shifting it down 1 plane to render on the correct view
     */
    WorldPoint GetBoatWorldLocation(){
        if(boatEntity == null)
            return null;
        LocalPoint lp = boatEntity.getTargetLocation();
        //setting plane to 0 shifts the tile underneath the boats' worldview. possibly grab current player plane and dz -1 instead
        return WorldPoint.fromScene(client.getTopLevelWorldView().getScene(),lp.getSceneX(),lp.getSceneY(),0);
    }

    /**
     * Compare distance of player boat against portal WPs to determine the correct portal the player is going through, as both use centers this shouldn't have mismatches.
     */
    WorldPoint GetClosestPortal()
    {
        if(boatEntity == null)
            return null;
        WorldPoint boat = lastBoatWP;
        WorldPoint closestPoint = null;
        int lowestDist = Integer.MAX_VALUE;
        for (WorldPoint portalPoint : portalMap.keySet())
        {
            int compareDist = boat.distanceTo2D(portalPoint);
            if(compareDist < lowestDist)
            {
                lowestDist = compareDist;
                closestPoint = portalPoint;
            }
        }

        return closestPoint;
    }

    /**
     * 948 is the script that requests a fade, setting the initial values of starting and ending opacity
     * 'pre-fade' event [Starting opacity : 255, Ending opacity : 0]
     * 'post-fade' event [Starting opacity : 0, Ending opacity : 255]
     * For the purposes of teleport handling only the 'pre-fade' event has relevance.
     */
    @Subscribe
    public void onScriptPreFired(ScriptPreFired e)
    {
        if(e.getScriptId() == 948){
            //'pre-fade' : 948,0,255,0,0,15
            //'post-fade' : 948,0,0,0,255,15
            Object[] args = e.getScriptEvent().getArguments();
            int opacityStart = (int) args[2];
            int opacityEnd = (int) args[4];

            boolean startedTeleport = opacityStart == 255 && opacityEnd == 0;
            if(startedTeleport)
            {
                int currentFadeInTick = client.getTickCount();
                boolean concurrentFade = currentFadeInTick == (lastFadeInTick+1);
                lastFadeInTick = client.getTickCount();
                //a concurrent fade has occurred, this should not be possible skip it.
                if(concurrentFade)
                    return;

                //the final fade of lap completion isn't triggered via a portal, the only differentiator we have to invalidate the teleport is the busy value not being 0. no other varbit is set during this tick.
                if(client.getVarbitValue(VarbitID.SAILING_BT_GWENITH_GLIDE_MASTER_STATE) == 2 && client.getVarbitValue(VarbitID.BUSY) == 0)
                {
                    HandleTeleport();
                }
            }
        }
    }

    /**
     * Called when a teleport is about to occur
     * Handles either Direction change, Zoom change, both or none depending on the selected handles for the given portal.
     */
    void HandleTeleport(){

        if(!configEnableHandles)
        {
            return;
        }

        WorldPoint closestPortal = GetClosestPortal();
        if(closestPortal == null)
        {
            return;
        }

        PortalData portal = portalMap.get(closestPortal);
        if(portal.handlesDirection && portal.direction != null){
            plugin.SetDirection(portal.direction);
        }

        if(portal.handlesZoom){
            client.runScript(ScriptID.CAMERA_DO_ZOOM, portal.zoomValue, portal.zoomValue);
        }

    }

    /**
     * Stored data for every given relevant portal (portals the average player will never go through or do nothing are currently omitted)
     * Additionally when set, stores the zoom/direction data of the given portal from configs.
     */
    public static class PortalData{
        public String portalDisplayName;
        public String portalConfigPrefix;

        Color drawColor;
        PortalData(String portalDisplayName, String portalConfigPrefix, Color drawColor){
            this.portalDisplayName = portalDisplayName;
            this.portalConfigPrefix = portalConfigPrefix;
            this.drawColor = drawColor;
            this.direction = Direction.UNSET;
            this.zoomValue = 0;
            this.handlesZoom = false;
            this.handlesDirection = false;
        }

        void SetHandles(HANDLE_TYPE handles){
            int handleFlags = handles.getValue();
            handlesDirection = (handleFlags & HANDLE_TYPE.DIRECTION.getValue()) != 0;
            handlesZoom = (handleFlags & HANDLE_TYPE.ZOOM.getValue()) != 0;
        }

        void SetDirection(Direction direction){
            this.direction = direction;
        }

        void SetZoom(int zoomValue){
            this.zoomValue = zoomValue;
        }

        Direction direction;
        int zoomValue;
        boolean handlesZoom;
        boolean handlesDirection;
    }

}
