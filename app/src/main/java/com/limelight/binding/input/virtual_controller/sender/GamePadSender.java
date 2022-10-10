package com.limelight.binding.input.virtual_controller.sender;

import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.nvstream.input.ControllerPacket;

public class GamePadSender extends ControllerSender{

    private final String key;
    private final int keyCodePress;
    private final int keyCodeRelease;
    private final int reversalFlag;
    private boolean lastStatus = false;
    private final short[] gamePadInputContext;
    private final int gamePadInputType;

    public GamePadSender(String key,VirtualController virtualController) {
        this.key = key;
        this.gamePadInputContext = virtualController.getGamePadInputContext();

        switch (key) {
            case "GA":{
                keyCodePress = ControllerPacket.A_FLAG;
                keyCodeRelease = ControllerPacket.A_FLAG;
                reversalFlag = 0;
                gamePadInputType = 0;
                break;
            }
            default:{
                keyCodePress = ControllerPacket.A_FLAG;
                keyCodeRelease = ControllerPacket.A_FLAG;
                gamePadInputType = 0;
                reversalFlag = 0;
                break;
            }

        }
    }

    @Override
    public byte sendMessage(boolean isPress) {
        if (isPress) {
            gamePadInputContext[gamePadInputType] |= keyCodePress;
        }else {
            gamePadInputContext[gamePadInputType] &= ~keyCodeRelease;
        }
        return 0x4;
    }

    @Override
    public byte sendMessage(int index, boolean isPress) {

        gamePadInputContext[gamePadInputType] = (short) (reversalFlag * index);

        return 0x4;
    }

}
