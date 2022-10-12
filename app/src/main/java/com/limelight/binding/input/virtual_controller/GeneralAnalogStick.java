package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.view.KeyEvent;

import com.limelight.binding.input.virtual_controller.sender.ControllerSender;
import com.limelight.binding.input.virtual_controller.sender.GamePadSender;
import com.limelight.binding.input.virtual_controller.sender.KeyboardSender;
import com.limelight.binding.input.virtual_controller.sender.MouseSender;

import java.util.Date;
import java.util.Map;

public class GeneralAnalogStick extends AnalogStick{

    private final int MIN_CIRCLE_R = 10000;  //当摇杆移动的非常小时，不产生操作，摇杆范围-32765<x,y<32765
    private final float EIGHTH_PI = 0.4142f;  // y=tan(π/8)x 分界线
    private final float EIGHTH_THREE_PI = 2.4142f;  //  y=tan(3π/8)x 分界线
    private final float NEGATIVE_EIGHTH_PI = -0.4142f; // y=tan(-π/8)x 分界线
    private final float NEGATIVE_EIGHTH_THREE_PI = -2.4142f; // y=tan(-3π/8)x 分界线
    private byte sendFlag = 0;
    private final int[] stickIndex = new int[4];
    private final boolean[] stickBool = new boolean[4];
    private final ControllerSender[] stickSender = new ControllerSender[5];

    public GeneralAnalogStick(VirtualController controller, String elementId, Context context,
                               String[] keyInfo) {
        super(controller, context, elementId);

        for (int i = 1; i < 11; i +=2){

            switch (keyInfo[i]) {
                case "K" :
                    stickSender[(i-1)/2] = new KeyboardSender(keyInfo[i+1],virtualController);
                    break;
                case "M" :
                    stickSender[(i-1)/2] = new MouseSender(keyInfo[i+1],virtualController);
                    break;
                case "G" :
                    stickSender[(i-1)/2] = new GamePadSender(keyInfo[i+1],keyInfo[0],virtualController);
                    break;
                default:
                    stickSender[(i-1)/2] = null;
                    break;
            }
        }

        addAnalogStickListener(new AnalogStickListener() {
            @Override
            public void onMovement(float xf, float yf) {

                short x = (short) (xf * 0x7FFE);
                short y = (short) (yf * 0x7FFE);

                if (y > 0) {
                    stickIndex[0] = y;
                    stickIndex[1] = -1;
                } else if (y < 0) {
                    stickIndex[0] = -1;
                    stickIndex[1] = -y;
                } else {
                    stickIndex[0] = 0;
                    stickIndex[1] = -1;
                }

                if (x > 0) {
                    stickIndex[2] = -1;
                    stickIndex[3] = x;
                } else if (x < 0) {
                    stickIndex[2] = -x;
                    stickIndex[3] = -1;
                } else {
                    stickIndex[2] = 0;
                    stickIndex[3] = -1;
                }


                boolean b = x * x + y * y > MIN_CIRCLE_R * MIN_CIRCLE_R;
                if (y >= EIGHTH_THREE_PI * x && y >= NEGATIVE_EIGHTH_THREE_PI * x && b){
                    //UP
                    stickBool[0] = true;
                    stickBool[1] = false;
                    stickBool[2] = false;
                    stickBool[3] = false;
                } else if (y < EIGHTH_THREE_PI * x && y < NEGATIVE_EIGHTH_THREE_PI * x && b){
                    //DOWN
                    stickBool[0] = false;
                    stickBool[1] = true;
                    stickBool[2] = false;
                    stickBool[3] = false;
                } else if (y >= EIGHTH_PI * x && y < NEGATIVE_EIGHTH_PI * x && b){
                    //LEFT
                    stickBool[0] = false;
                    stickBool[1] = false;
                    stickBool[2] = true;
                    stickBool[3] = false;
                } else if (y < EIGHTH_PI * x && y >= NEGATIVE_EIGHTH_PI * x && b){
                    //RIGHT
                    stickBool[0] = false;
                    stickBool[1] = false;
                    stickBool[2] = false;
                    stickBool[3] = true;
                } else if (y < NEGATIVE_EIGHTH_THREE_PI * x && y >= NEGATIVE_EIGHTH_PI * x && b){
                    //UP & LEFT
                    stickBool[0] = true;
                    stickBool[1] = false;
                    stickBool[2] = true;
                    stickBool[3] = false;
                } else if (y >= NEGATIVE_EIGHTH_THREE_PI * x && y < NEGATIVE_EIGHTH_PI * x && b){
                    //DOWN & RIGHT
                    stickBool[0] = false;
                    stickBool[1] = true;
                    stickBool[2] = false;
                    stickBool[3] = true;
                } else if (y >= EIGHTH_PI * x && y < EIGHTH_THREE_PI * x && b){
                    //UP & RIGHT
                    stickBool[0] = true;
                    stickBool[1] = false;
                    stickBool[2] = false;
                    stickBool[3] = true;
                } else if (y < EIGHTH_PI * x && y >= EIGHTH_THREE_PI * x && b){
                    //DOWN & LEFT
                    stickBool[0] = false;
                    stickBool[1] = true;
                    stickBool[2] = true;
                    stickBool[3] = false;
                } else {
                    stickBool[0] = false;
                    stickBool[1] = false;
                    stickBool[2] = false;
                    stickBool[3] = false;
                }

                for (int i = 0; i < 4; i++) {
                    sendFlag |= stickSender[i].sendMessage(stickIndex[i],stickBool[i]);
                }
                if ((sendFlag & 0x1) == 1) {
                    virtualController.sendKeyboardInputPadKey();
                }
                if (((sendFlag >> 1) & 0x1) == 1) {
                    virtualController.sendMouseKey();
                }
                if (((sendFlag >> 2) & 0x1) == 1) {
                    virtualController.sendControllerInputContext();
                }

                sendFlag = 0;

            }

            @Override
            public void onClick() {

            }

            @Override
            public void onDoubleClick() {
                sendFlag |= stickSender[4].sendMessage(0,true);

                if ((sendFlag & 0x1) == 1) {
                    virtualController.sendKeyboardInputPadKey();
                } else if (((sendFlag >> 1) & 0x1) == 1) {
                    virtualController.sendMouseKey();
                } else if (((sendFlag >> 2) & 0x1) == 1) {
                    virtualController.sendControllerInputContext();
                }

                sendFlag = 0;

            }

            @Override
            public void onRevoke() {
                stickIndex[0] = 0;
                stickIndex[1] = -1;
                stickIndex[2] = 0;
                stickIndex[3] = -1;
                stickBool[0] = false;
                stickBool[1] = false;
                stickBool[2] = false;
                stickBool[3] = false;
                for (int i = 0; i < 4; i++) {
                    sendFlag |= stickSender[i].sendMessage(stickIndex[i],stickBool[i]);
                }

                sendFlag |= stickSender[4].sendMessage(0,false);


                if ((sendFlag & 0x1) == 1) {
                    virtualController.sendKeyboardInputPadKey();
                }
                if (((sendFlag >> 1) & 0x1) == 1) {
                    virtualController.sendMouseKey();
                }
                if (((sendFlag >> 2) & 0x1) == 1) {
                    virtualController.sendControllerInputContext();
                }

                sendFlag = 0;

            }
        });
    }

}
