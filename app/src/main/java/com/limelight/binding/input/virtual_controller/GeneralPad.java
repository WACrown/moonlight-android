package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import com.limelight.binding.input.virtual_controller.sender.ControllerSender;
import com.limelight.binding.input.virtual_controller.sender.GamePadSender;
import com.limelight.binding.input.virtual_controller.sender.KeyboardSender;
import com.limelight.binding.input.virtual_controller.sender.MouseSender;

import java.util.Date;

public class GeneralPad extends DigitalPad{


    private final ControllerSender[] padSender = new ControllerSender[4];
    private final boolean[] padBool = new boolean[4];
    private int sendFlag = 0;

    public GeneralPad(VirtualController controller, String elementId, Context context, String[] keyInfo) {
        super(controller, elementId, context);

        for (int i = 1; i < 9; i +=2){
            switch (keyInfo[i]) {
                case "K" :
                    padSender[(i-1)/2] = new KeyboardSender(keyInfo[i+1],virtualController);
                    break;
                case "M" :
                    padSender[(i-1)/2] = new MouseSender(keyInfo[i+1],virtualController);
                    break;
                case "G" :
                    padSender[(i-1)/2] = new GamePadSender(keyInfo[i+1],keyInfo[0],virtualController);
                    break;
                default:
                    padSender[(i-1)/2] = null;
                    break;
            }
        }

        addDigitalPadListener(new DigitalPadListener() {
            @Override
            public void onDirectionChange(int direction) {
                if (direction == DigitalPad.DIGITAL_PAD_DIRECTION_NO_DIRECTION) {
                    sendFlag |= padSender[0].sendMessage(-1,false);
                    sendFlag |= padSender[1].sendMessage(-1,false);
                    sendFlag |= padSender[2].sendMessage(-1,false);
                    sendFlag |= padSender[3].sendMessage(-1,false);
                } else {
                    if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_LEFT) > 0) {
                        sendFlag |= padSender[2].sendMessage(-1,true);
                    }
                    if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_RIGHT) > 0) {
                        sendFlag |= padSender[3].sendMessage(-1,true);
                    }
                    if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_UP) > 0) {
                        sendFlag |= padSender[0].sendMessage(-1,true);
                    }
                    if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_DOWN) > 0) {
                        sendFlag |= padSender[1].sendMessage(-1,true);
                    }
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
        });

    }
}
