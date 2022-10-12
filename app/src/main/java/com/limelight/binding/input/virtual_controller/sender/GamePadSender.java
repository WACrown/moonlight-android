package com.limelight.binding.input.virtual_controller.sender;

import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.nvstream.input.ControllerPacket;

public class GamePadSender extends ControllerSender{

    private final String key;
    private final int keyCodePress;
    private final int keyCodeRelease;
    private final int reversalFlag;
    private final boolean isAnalog;
    private boolean lastStatus = false;
    private final short[] gamePadInputContext;
    private final int gamePadInputType;
    private static final int INPUT_CONTEXT = 0;
    private static final int LEFT_TRIGGER = 1;
    private static final int RIGHT_TRIGGER = 2;
    private static final int LEFT_STICK_X = 3;
    private static final int LEFT_STICK_Y = 4;
    private static final int RIGHT_STICK_X = 5;
    private static final int RIGHT_STICK_Y = 6;

    public GamePadSender(String key,String type,VirtualController virtualController) {
        this.key = key;
        this.gamePadInputContext = virtualController.getGamePadInputContext();



        switch (key) {
            case "GA":{
                keyCodePress = ControllerPacket.A_FLAG;
                keyCodeRelease = ControllerPacket.A_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "GB":{
                keyCodePress = ControllerPacket.B_FLAG;
                keyCodeRelease = ControllerPacket.B_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "GX":{
                keyCodePress = ControllerPacket.X_FLAG;
                keyCodeRelease = ControllerPacket.X_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "GY":{
                keyCodePress = ControllerPacket.Y_FLAG;
                keyCodeRelease = ControllerPacket.Y_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "LB":{
                keyCodePress = ControllerPacket.LB_FLAG;
                keyCodeRelease = ControllerPacket.LB_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "RB":{
                keyCodePress = ControllerPacket.RB_FLAG;
                keyCodeRelease = ControllerPacket.RB_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "PU":{
                keyCodePress = ControllerPacket.UP_FLAG;
                keyCodeRelease = ControllerPacket.UP_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "PD":{
                keyCodePress = ControllerPacket.DOWN_FLAG;
                keyCodeRelease = ControllerPacket.DOWN_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "PL":{
                keyCodePress = ControllerPacket.LEFT_FLAG;
                keyCodeRelease = ControllerPacket.LEFT_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "PR":{
                keyCodePress = ControllerPacket.RIGHT_FLAG;
                keyCodeRelease = ControllerPacket.RIGHT_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "LT":{
                keyCodePress = 0xFF;
                keyCodeRelease = 0xFF;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = LEFT_TRIGGER;
                break;
            }
            case "RT":{
                keyCodePress = 0xFF;
                keyCodeRelease = 0xFF;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = RIGHT_TRIGGER;
                break;
            }
            case "LSB":{
                keyCodePress = ControllerPacket.LS_CLK_FLAG;
                keyCodeRelease = ControllerPacket.LS_CLK_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "RSB":{
                keyCodePress = ControllerPacket.RS_CLK_FLAG;
                keyCodeRelease = ControllerPacket.RS_CLK_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "LSU":{
                keyCodePress = 0x7FFE;
                keyCodeRelease = 0xFFFF;
                isAnalog = type.equals("STICK");
                reversalFlag = 1;
                gamePadInputType = LEFT_STICK_Y;
                break;
            }
            case "LSD":{
                keyCodePress = 0x7FFE;
                keyCodeRelease = 0xFFFF;
                isAnalog = type.equals("STICK");
                reversalFlag = -1;
                gamePadInputType = LEFT_STICK_Y;
                break;
            }
            case "LSL":{
                keyCodePress = 0x7FFE;
                keyCodeRelease = 0xFFFF;
                isAnalog = type.equals("STICK");
                reversalFlag = -1;
                gamePadInputType = LEFT_STICK_X;
                break;
            }
            case "LSR":{
                keyCodePress = 0x7FFE;
                keyCodeRelease = 0xFFFF;
                isAnalog = type.equals("STICK");
                reversalFlag = 1;
                gamePadInputType = LEFT_STICK_X;
                break;
            }
            case "RSU":{
                keyCodePress = 0x7FFE;
                keyCodeRelease = 0xFFFF;
                isAnalog = type.equals("STICK");
                reversalFlag = 1;
                gamePadInputType = RIGHT_STICK_Y;
                break;
            }
            case "RSD":{
                keyCodePress = 0x7FFE;
                keyCodeRelease = 0xFFFF;
                isAnalog = type.equals("STICK");
                reversalFlag = -1;
                gamePadInputType = RIGHT_STICK_Y;
                break;
            }
            case "RSL":{
                keyCodePress = 0x7FFE;
                keyCodeRelease = 0xFFFF;
                isAnalog = type.equals("STICK");
                reversalFlag = -1;
                gamePadInputType = RIGHT_STICK_X;
                break;
            }
            case "RSR":{
                keyCodePress = 0x7FFE;
                keyCodeRelease = 0xFFFF;
                isAnalog = type.equals("STICK");
                reversalFlag = 1;
                gamePadInputType = RIGHT_STICK_X;
                break;
            }
            case "START":{
                keyCodePress = ControllerPacket.PLAY_FLAG;
                keyCodeRelease = ControllerPacket.PLAY_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            case "BACK":{
                keyCodePress = ControllerPacket.BACK_FLAG;
                keyCodeRelease = ControllerPacket.BACK_FLAG;
                isAnalog = false;
                reversalFlag = 1;
                gamePadInputType = INPUT_CONTEXT;
                break;
            }
            default:{
                keyCodePress = -2;
                keyCodeRelease = -2;
                isAnalog = false;
                gamePadInputType = -2;
                reversalFlag = -2;
                break;
            }


        }

    }


    @Override
    public byte sendMessage(int index, boolean isPress) {

        if (isAnalog) {

            if (index == -1) {
                return 0x0;
            }
            gamePadInputContext[gamePadInputType] = (short) (reversalFlag * index);
        } else {
            if (lastStatus == isPress){
                return 0x0;
            }
            lastStatus = isPress;
            if (isPress) {
                gamePadInputContext[gamePadInputType] |= keyCodePress;
            }else {
                gamePadInputContext[gamePadInputType] &= ~keyCodeRelease;
            }
        }
        return 0x4;
    }

}
