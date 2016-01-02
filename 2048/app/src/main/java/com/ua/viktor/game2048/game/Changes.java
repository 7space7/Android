package com.ua.viktor.game2048.game;



public class Changes {
    public int oldPositionX;
    public int oldPositionY;

    public int newPositionX;
    public int newPositionY;

    public int newValue;

    public Changes(int oldPosX, int oldPosY, int newPosX, int newPosY, int newVal) {
        oldPositionX = oldPosX;
        oldPositionY = oldPosY;
        newPositionX = newPosX;
        newPositionY = newPosY;
        newValue = newVal;
    }

}