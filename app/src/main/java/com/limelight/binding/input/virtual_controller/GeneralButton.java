package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.view.KeyEvent;

import com.limelight.binding.input.virtual_controller.sender.ControllerSender;
import com.limelight.binding.input.virtual_controller.sender.GamePadSender;
import com.limelight.binding.input.virtual_controller.sender.KeyboardSender;
import com.limelight.binding.input.virtual_controller.sender.MouseSender;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class GeneralButton extends DigitalButton {


    private final ControllerSender sender;
    private byte sendFlag = 0;
    private DigitalButtonListener digitalButtonListener;


    public GeneralButton(VirtualController controller, String elementId, String[] keyShort, String[] keyLong, int layer, Context context) {
        super(controller, elementId, layer, context);


        switch (keyShort[2]) {
            case "K" :
                sender = new KeyboardSender(keyShort[3],virtualController);
                break;
            case "M" :
                sender = new MouseSender(keyShort[3],virtualController);
                break;
            case "G" :
                sender = new GamePadSender(keyShort[3],keyShort[0],virtualController);
                break;
            default:
                sender = null;
                break;
        }


        addDigitalButtonListener(new DigitalButtonListener() {
            @Override
            public void onClick() {
               sendFlag |= sender.sendMessage(-1,true);

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
            public void onLongClick() {

            }

            @Override
            public void onRelease() {
                sendFlag |= sender.sendMessage(-1,false);
                if ((sendFlag & 0x1) == 1) {
                    virtualController.sendKeyboardInputPadKey();
                } else if (((sendFlag >> 1) & 0x1) == 1) {
                    virtualController.sendMouseKey();
                } else if (((sendFlag >> 2) & 0x1) == 1) {
                    virtualController.sendControllerInputContext();
                }
                sendFlag = 0;
            }
        });

    }


}
