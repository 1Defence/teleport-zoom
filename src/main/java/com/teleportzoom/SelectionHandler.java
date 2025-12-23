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

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.chatbox.ChatboxPanelManager;

import javax.inject.Inject;

@Slf4j
public class SelectionHandler
{
    TeleportZoomPlugin plugin;
    ChatboxPanelManager chatboxPanelManager;
    ConfigManager configManager;

    @Inject
    public SelectionHandler(TeleportZoomPlugin plugin, ChatboxPanelManager chatboxPanelManager, ConfigManager configManager){
        this.plugin = plugin;
        this.chatboxPanelManager = chatboxPanelManager;
        this.configManager = configManager;
    }

    public void promptForDirection(int promptedRegion){
        chatboxPanelManager.openTextMenuInput("[Teleport Zoom] Save a direction?")
                .option("North", () -> SaveDirection(Direction.NORTH,promptedRegion))
                .option("East", () -> SaveDirection(Direction.EAST,promptedRegion))
                .option("South", () -> SaveDirection(Direction.SOUTH,promptedRegion))
                .option("West", () -> SaveDirection(Direction.WEST,promptedRegion))
                .option("None, Zoom only", () -> SaveDirection(Direction.UNSET,promptedRegion))
                .build();
    }

    void SaveDirection(Direction direction, int promptedRegion){
        if (direction == Direction.UNSET)
        {
            //while yes, the user may not have a value set in this selection.. unsetconfig already null checks so this option is both a clear or a no if not already present.
            configManager.unsetConfiguration(TeleportZoomConfig.GROUP, TeleportZoomConfig.PREFIX_DIRECTION + promptedRegion);
        }else{
            configManager.setConfiguration(TeleportZoomConfig.GROUP, TeleportZoomConfig.PREFIX_DIRECTION + promptedRegion, direction);
        }
    }

}