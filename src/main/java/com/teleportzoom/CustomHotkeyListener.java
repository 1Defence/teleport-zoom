package com.teleportzoom;


import java.awt.event.KeyEvent;
import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.client.config.Keybind;
import net.runelite.client.input.KeyListener;

@RequiredArgsConstructor
public abstract class CustomHotkeyListener implements KeyListener
{
    private final Supplier<Keybind> keybind;
    @Getter(AccessLevel.PACKAGE)
    private boolean isPressed = false;

    private boolean isConsumingTyped = false;

    @Setter
    private boolean enabledOnLoginScreen;

    @Override
    public boolean isEnabledOnLoginScreen()
    {
        return enabledOnLoginScreen;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        if (isConsumingTyped)
        {
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (keybind.get().matches(e))
        {
            boolean wasPressed = isPressed;
            isPressed = true;
            if (!wasPressed)
            {
                hotkeyPressed();
            }
            if (Keybind.getModifierForKeyCode(e.getKeyCode()) == null)
            {
                isConsumingTyped = true;
                // Only consume non modifier keys
                e.consume();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (keybind.get().matches(e))
        {
            ReleaseHotkey();
        }
    }

    public void ReleaseHotkey(){
        isPressed = false;
        isConsumingTyped = false;
        hotkeyReleased();
    }

    public void hotkeyPressed()
    {
    }

    public void hotkeyReleased()
    {
    }
}