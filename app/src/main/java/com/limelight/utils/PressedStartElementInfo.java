package com.limelight.utils;

public class PressedStartElementInfo {
    public int startElementPositionX = 0;
    public int startElementPositionY = 0;
    public int startElementWidth = 0;
    public int startElementHeight = 0;
    public int elementCenterPositionX = 0;
    public int elementCenterPositionY = 0;

    @Override
    public String toString() {
        return "wangguan startElementPositionX:" + startElementPositionX +
                "startElementPositionY:" + startElementPositionY +
                "startElementWidth:" + startElementWidth+
                "startElementHeight:" + startElementHeight +
                "elementCenterPositionX:" + elementCenterPositionX+
                "elementCenterPositionY" + elementCenterPositionY;
    }
}
