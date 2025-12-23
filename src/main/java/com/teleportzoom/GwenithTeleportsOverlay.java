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

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.api.Point;

import javax.inject.Inject;
import java.awt.*;
import java.util.Map;

import com.teleportzoom.GwenithHandler.PortalData;


public class GwenithTeleportsOverlay extends Overlay
{

    private final Client client;
    private final GwenithHandler handler;

    @Inject
    public GwenithTeleportsOverlay(Client client, GwenithHandler handler)
    {
        this.client = client;
        this.handler = handler;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
        setPriority(OverlayPriority.MED);

    }

    /**Render label over portals, used to indicate to the user what portal is what in the configuration.*/
    @Override
    public Dimension render(Graphics2D graphics)
    {

        if(!handler.configPortalLabels)
        {
            return null;
        }

        for (Map.Entry<WorldPoint, PortalData> entry : handler.portalMap.entrySet()) {
            WorldPoint centerTile = entry.getKey();
            PortalData portal = entry.getValue();

            renderTile(graphics,centerTile,portal.drawColor);
            LocalPoint portalLP = LocalPoint.fromWorld(client,centerTile);
            if(portalLP != null)
            {
                FontMetrics fm2 = graphics.getFontMetrics();
                int halfWidth = fm2.stringWidth(portal.portalDisplayName)/2;

                final int height = client.getLocalPlayer().getLogicalHeight();
                Point textPointA = Perspective.localToCanvas(client, portalLP, 0, height);
                if(textPointA != null)
                {
                    Point textPointB = new Point(textPointA.getX() - halfWidth, textPointA.getY());
                    graphics.setFont(new Font(FontManager.getRunescapeFont().getName(), Font.BOLD, 20));
                    OverlayUtil.renderTextLocation(graphics, textPointB, portal.portalDisplayName, portal.drawColor);
                }
            }
        }


        return null;
    }

    /**Renders a singular tile on the scene*/
    private void renderTile(Graphics2D graphics, WorldPoint tile, Color tileBorderColor)
    {
        LocalPoint lp;
        if((lp = LocalPoint.fromWorld(client, tile)) == null){
            return;
        }

        Polygon poly = Perspective.getCanvasTilePoly(client, lp);
        if (poly != null)
        {
            OverlayUtil.renderPolygon(graphics, poly, tileBorderColor,new Color(tileBorderColor.getRed(),tileBorderColor.getGreen(),tileBorderColor.getBlue(),20), new BasicStroke(2f));
        }
    }

}

