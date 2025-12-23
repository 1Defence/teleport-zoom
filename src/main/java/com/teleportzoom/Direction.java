package com.teleportzoom;

public enum Direction {
    UNSET(0),
    NORTH(1),
    EAST(2),
    SOUTH(3),
    WEST(4);

    private final int scriptValue;
    Direction(int scriptValue){
        this.scriptValue = scriptValue;
    }

    public int getScriptValue(){
        return scriptValue;
    }

}
