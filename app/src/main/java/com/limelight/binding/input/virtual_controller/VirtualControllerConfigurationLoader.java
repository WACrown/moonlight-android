/**
 * Created by Karim Mreisi.
 */

package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

import com.limelight.nvstream.input.ControllerPacket;
import com.limelight.nvstream.input.MouseButtonPacket;
import com.limelight.utils.controller.LayoutEditHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VirtualControllerConfigurationLoader {

    private static int getPercent(
            int percent,
            int total) {
        return (int) (((float) total / (float) 100) * (float) percent);
    }

    // The default controls are specified using a grid of 128*72 cells at 16:9
    private static int screenScale(int units, int height) {
        return (int) (((float) height / (float) 72) * (float) units);
    }

    private static DigitalPad createDigitalPad(
            final VirtualController controller,
            final String elementId,
            final Context context) {

        DigitalPad digitalPad = new DigitalPad(controller, elementId, context);
        digitalPad.addDigitalPadListener(new DigitalPad.DigitalPadListener() {
            @Override
            public void onDirectionChange(int direction) {
                VirtualController.ControllerInputContext inputContext =
                        controller.getControllerInputContext();

                if (direction == DigitalPad.DIGITAL_PAD_DIRECTION_NO_DIRECTION) {
                    inputContext.inputMap &= ~ControllerPacket.LEFT_FLAG;
                    inputContext.inputMap &= ~ControllerPacket.RIGHT_FLAG;
                    inputContext.inputMap &= ~ControllerPacket.UP_FLAG;
                    inputContext.inputMap &= ~ControllerPacket.DOWN_FLAG;

                    controller.sendControllerInputContext();
                    return;
                }
                if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_LEFT) > 0) {
                    inputContext.inputMap |= ControllerPacket.LEFT_FLAG;
                }
                if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_RIGHT) > 0) {
                    inputContext.inputMap |= ControllerPacket.RIGHT_FLAG;
                }
                if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_UP) > 0) {
                    inputContext.inputMap |= ControllerPacket.UP_FLAG;
                }
                if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_DOWN) > 0) {
                    inputContext.inputMap |= ControllerPacket.DOWN_FLAG;
                }
                controller.sendControllerInputContext();
            }
        });

        return digitalPad;
    }



    private static DigitalButton createDigitalButton(
            final String elementId,
            final int keyShort,
            final int keyLong,
            final int layer,
            final String text,
            final int icon,
            final VirtualController controller,
            final Context context) {
        DigitalButton button = new DigitalButton(controller, elementId, layer, context);
        button.setText(text);
        button.setIcon(icon);

        button.addDigitalButtonListener(new DigitalButton.DigitalButtonListener() {
            @Override
            public void onClick() {
                VirtualController.ControllerInputContext inputContext =
                        controller.getControllerInputContext();
                inputContext.inputMap |= keyShort;

                controller.sendControllerInputContext();
            }

            @Override
            public void onLongClick() {
                VirtualController.ControllerInputContext inputContext =
                        controller.getControllerInputContext();
                inputContext.inputMap |= keyLong;

                controller.sendControllerInputContext();
            }

            @Override
            public void onRelease() {
                VirtualController.ControllerInputContext inputContext =
                        controller.getControllerInputContext();
                inputContext.inputMap &= ~keyShort;
                inputContext.inputMap &= ~keyLong;

                controller.sendControllerInputContext();
            }
        });

        return button;
    }

    private static DigitalButton createLeftTrigger(
            final String elementId,
            final int layer,
            final String text,
            final int icon,
            final VirtualController controller,
            final Context context) {
        LeftTrigger button = new LeftTrigger(controller, layer, context, elementId);
        button.setText(text);
        button.setIcon(icon);
        return button;
    }

    private static DigitalButton createRightTrigger(
            final String elementId,
            final int layer,
            final String text,
            final int icon,
            final VirtualController controller,
            final Context context) {
        RightTrigger button = new RightTrigger(controller, layer, context, elementId);
        button.setText(text);
        button.setIcon(icon);
        return button;
    }

    private static AnalogStick createLeftStick(
            final VirtualController controller,
            final String elementId,
            final Context context) {
        return new LeftAnalogStick(controller, elementId, context);
    }

    private static AnalogStick createRightStick(
            final VirtualController controller,
            final String elementId,
            final Context context) {
        return new RightAnalogStick(controller, elementId, context);
    }

    private static AnalogStick createKeyboardStick(
            final VirtualController controller,
            final String elementId,
            final Context context,
            final int keyUp, final int keyDown, final int keyLeft, final int keyRight, final int keyDouble) {
        return new KeyboardAnalogStick(controller, elementId, context, keyUp, keyDown, keyLeft, keyRight ,keyDouble);
    }

    private static DigitalButton createKeyboardButton(
            final String elementId,
            final int keyShort,
            final int keyLong,
            final int layer,
            final String text,
            final int icon,
            final VirtualController controller,
            final Context context) {
        DigitalButton button = new DigitalButton(controller, elementId, layer, context);
        button.setText(text);
        button.setIcon(icon);
        Map<String,KeyEvent> keyEventMap = controller.getKeyboardInputContext();

        button.addDigitalButtonListener(new DigitalButton.DigitalButtonListener() {
            @Override
            public void onClick() {
                keyEventMap.put(""+keyShort,new KeyEvent(KeyEvent.ACTION_DOWN,keyShort));
                controller.sendKeyboardInputPadKey();
            }

            @Override
            public void onLongClick() {

            }

            @Override
            public void onRelease() {
                keyEventMap.put(""+keyShort,new KeyEvent(KeyEvent.ACTION_UP,keyShort));
                controller.sendKeyboardInputPadKey();
            }
        });

        return button;
    }

    private static DigitalButton createMouseButton(
            final String elementId,
            final Byte keyShort,
            final int keyLong,
            final int layer,
            final String text,
            final int icon,
            final VirtualController controller,
            final Context context) {
        DigitalButton button = new DigitalButton(controller, elementId, layer, context);
        button.setText(text);
        button.setIcon(icon);

        Map<Byte,Boolean> mouseMap = controller.getMouseInputContext();
        button.addDigitalButtonListener(new DigitalButton.DigitalButtonListener() {
            @Override
            public void onClick() {
                mouseMap.put(keyShort,true);
                controller.sendMouseKey();
            }

            @Override
            public void onLongClick() {

            }

            @Override
            public void onRelease() {
                mouseMap.put(keyShort,false);
                controller.sendMouseKey();
            }
        });

        return button;
    }


    private static DigitalPad createDirectionPad(
            final VirtualController controller,
            final String elementId,
            final Context context ,
            final int keyUp, final int keyDown, final int keyLeft, final int keyRight) {

        DigitalPad digitalPad = new DigitalPad(controller, elementId, context);
        Map<String,KeyEvent> keyEventMap = controller.getKeyboardInputContext();
        digitalPad.addDigitalPadListener(new DigitalPad.DigitalPadListener() {
            @Override
            public void onDirectionChange(int direction) {

                keyEventMap.put(""+keyLeft,new KeyEvent(KeyEvent.ACTION_UP,keyLeft));
                keyEventMap.put(""+keyRight,new KeyEvent(KeyEvent.ACTION_UP,keyRight));
                keyEventMap.put(""+keyUp,new KeyEvent(KeyEvent.ACTION_UP,keyUp));
                keyEventMap.put(""+keyDown,new KeyEvent(KeyEvent.ACTION_UP,keyDown));



                if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_LEFT) > 0) {
                    keyEventMap.put(""+keyLeft,new KeyEvent(KeyEvent.ACTION_DOWN,keyLeft));
                }
                if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_RIGHT) > 0) {
                    keyEventMap.put(""+keyRight,new KeyEvent(KeyEvent.ACTION_DOWN,keyRight));
                }
                if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_UP) > 0) {
                    keyEventMap.put(""+keyUp,new KeyEvent(KeyEvent.ACTION_DOWN,keyUp));
                }
                if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_DOWN) > 0) {
                    keyEventMap.put(""+keyDown,new KeyEvent(KeyEvent.ACTION_DOWN,keyDown));
                }

                controller.sendKeyboardInputPadKey();
            }
        });

        return digitalPad;
    }



    public static void createButtons(final VirtualController controller, final Context context,Map<String, String> keyInfoMap){
        DisplayMetrics screen = context.getResources().getDisplayMetrics();

        // Displace controls on the right by this amount of pixels to account for different aspect ratios
        int rightDisplacement = screen.widthPixels - screen.heightPixels * 16 / 9;

        // NOTE: Some of these getPercent() expressions seem like they can be combined
        // into a single call. Due to floating point rounding, this isn't actually possible.

        for (String keyName : keyInfoMap.keySet()){
            String[] keyTypeAndCodeAndName = keyName.split("-");

            int leftMargin = 0;
            int topMargin = 0;
            int width = 0;
            int height = 0;
            try {
                JSONObject buttonInfo = new JSONObject((String) keyInfoMap.get(keyName));
                leftMargin = buttonInfo.getInt("LEFT");
                topMargin = buttonInfo.getInt("TOP");
                width = buttonInfo.getInt("WIDTH");
                height = buttonInfo.getInt("HEIGHT");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (keyTypeAndCodeAndName[0].equals("KEYBOARD")){

                controller.addElement(
                        createKeyboardButton(keyName,getKeycode(keyTypeAndCodeAndName[1]),getKeycode(keyTypeAndCodeAndName[1]),1,keyTypeAndCodeAndName[1],-1,controller,context),
                        leftMargin,
                        topMargin,
                        width,
                        height
                );

            }else if (keyTypeAndCodeAndName[0].equals("PAD")){
                VirtualControllerElement element =createDirectionPad(controller, keyName, context,getKeycode(keyTypeAndCodeAndName[1]),getKeycode(keyTypeAndCodeAndName[2]),getKeycode(keyTypeAndCodeAndName[3]),getKeycode(keyTypeAndCodeAndName[4]));

                controller.addElement(
                        element,
                        leftMargin,
                        topMargin,
                        width,
                        height
                );
            }else if (keyTypeAndCodeAndName[0].equals("STICK")){
                controller.addElement(
                        createKeyboardStick(controller, keyName, context,getKeycode(keyTypeAndCodeAndName[1]),getKeycode(keyTypeAndCodeAndName[2]),getKeycode(keyTypeAndCodeAndName[3]),getKeycode(keyTypeAndCodeAndName[4]),getKeycode(keyTypeAndCodeAndName[5])),
                        leftMargin,
                        topMargin,
                        width,
                        height
                );
            }else if (keyTypeAndCodeAndName[0].equals("GAMEPAD")){
                switch (keyTypeAndCodeAndName[1]){
                    case "GX" : {
                        controller.addElement(createDigitalButton(
                                        keyName,
                                        ControllerPacket.X_FLAG, 0, 1,
                                        "GX", -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "GY" : {
                        controller.addElement(createDigitalButton(
                                        keyName,
                                        ControllerPacket.Y_FLAG, 0, 1,
                                        "GY", -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "GA" : {
                        controller.addElement(createDigitalButton(
                                        keyName,
                                        ControllerPacket.A_FLAG, 0, 1,
                                        "GA", -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "GB" : {
                        controller.addElement(createDigitalButton(
                                        keyName,
                                        ControllerPacket.B_FLAG, 0, 1,
                                        "GB", -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "PAD" : {
                        controller.addElement(createDigitalPad(controller, keyName, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "LT" : {
                        controller.addElement(createLeftTrigger(
                                        keyName,
                                        1,"LT", -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "RT" : {
                        controller.addElement(createRightTrigger(
                                        keyName,
                                        1, "RT", -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "LB" : {
                        controller.addElement(createDigitalButton(
                                        keyName,
                                        ControllerPacket.LB_FLAG, 0, 1, "LB", -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "RB" : {
                        controller.addElement(createDigitalButton(
                                        keyName,
                                        ControllerPacket.RB_FLAG, 0, 1, "RB", -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "LS" : {
                        controller.addElement(createLeftStick(controller, keyName, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "RS" : {
                        controller.addElement(createRightStick(controller, keyName,context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "START" : {
                        controller.addElement(createDigitalButton(
                                        keyName,
                                        ControllerPacket.PLAY_FLAG, 0, 3, "START", -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "BACK" : {
                        controller.addElement(createDigitalButton(
                                        keyName,
                                        ControllerPacket.BACK_FLAG, 0, 2, "BACK", -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "LSB" : {
                        controller.addElement(createDigitalButton(
                                        keyName,
                                        ControllerPacket.LS_CLK_FLAG, 0, 1, "LSB", -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "RSB" : {
                        controller.addElement(createDigitalButton(
                                        keyName,
                                        ControllerPacket.RS_CLK_FLAG, 0, 1, "RSB", -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                    }

                }
            }else if (keyTypeAndCodeAndName[0].equals("MOUSE")){
                switch (keyTypeAndCodeAndName[1]) {
                    case "ML": {
                        controller.addElement(createMouseButton(
                                        keyName,
                                        MouseButtonPacket.BUTTON_LEFT, 0, 1,
                                        keyTypeAndCodeAndName[1], -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "MR": {
                        controller.addElement(createMouseButton(
                                        keyName,
                                        MouseButtonPacket.BUTTON_RIGHT, 0, 1,
                                        keyTypeAndCodeAndName[1], -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "MM": {
                        controller.addElement(createMouseButton(
                                        keyName,
                                        MouseButtonPacket.BUTTON_MIDDLE, 0, 1,
                                        keyTypeAndCodeAndName[1], -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "MB1": {
                        controller.addElement(createMouseButton(
                                        keyName,
                                        MouseButtonPacket.BUTTON_X1, 0, 1,
                                        keyTypeAndCodeAndName[1], -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "MB2": {
                        controller.addElement(createMouseButton(
                                        keyName,
                                        MouseButtonPacket.BUTTON_X2, 0, 1,
                                        keyTypeAndCodeAndName[1], -1, controller, context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                }
            }else {

            }
        }


    }


    public static void saveProfile(final VirtualController controller,
                                   final Context context) {

        Map<String, String> elementConfigurationsMap = new HashMap<>();
        for (VirtualControllerElement element : controller.getElements()) {
            String prefKey = ""+element.elementId;
            try {
                elementConfigurationsMap.put(prefKey, element.getConfiguration().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        LayoutEditHelper.storeAllButton(context,elementConfigurationsMap);

    }

    public static int getKeycode(String key){
        switch (key){
            case "A" :return KeyEvent.KEYCODE_A;
            case "B" :return KeyEvent.KEYCODE_B;
            case "C" :return KeyEvent.KEYCODE_C;
            case "D" :return KeyEvent.KEYCODE_D;
            case "E" :return KeyEvent.KEYCODE_E;
            case "F" :return KeyEvent.KEYCODE_F;
            case "G" :return KeyEvent.KEYCODE_G;
            case "H" :return KeyEvent.KEYCODE_H;
            case "L" :return KeyEvent.KEYCODE_L;
            case "M" :return KeyEvent.KEYCODE_M;
            case "N" :return KeyEvent.KEYCODE_N;
            case "O" :return KeyEvent.KEYCODE_O;
            case "P" :return KeyEvent.KEYCODE_P;
            case "Q" :return KeyEvent.KEYCODE_Q;
            case "R" :return KeyEvent.KEYCODE_R;
            case "S" :return KeyEvent.KEYCODE_S;
            case "T" :return KeyEvent.KEYCODE_T;
            case "U" :return KeyEvent.KEYCODE_U;
            case "V" :return KeyEvent.KEYCODE_V;
            case "W" :return KeyEvent.KEYCODE_W;
            case "X" :return KeyEvent.KEYCODE_X;
            case "Y" :return KeyEvent.KEYCODE_Y;
            case "Z" :return KeyEvent.KEYCODE_Z;
            //
            case "CTRLL"  :return KeyEvent.KEYCODE_CTRL_LEFT;
            case "SHIFTL" :return KeyEvent.KEYCODE_SHIFT_LEFT;
            case "CTRLR"  :return KeyEvent.KEYCODE_CTRL_RIGHT;
            case "SHIFTR" :return KeyEvent.KEYCODE_SHIFT_RIGHT;
            case "ALTL"   :return KeyEvent.KEYCODE_ALT_LEFT;
            case "ALTR"   :return KeyEvent.KEYCODE_ALT_RIGHT;
            case "ENTER"  :return KeyEvent.KEYCODE_ENTER;
            case "BACK"   :return KeyEvent.KEYCODE_DEL;
            case "SPACE"  :return KeyEvent.KEYCODE_SPACE;
            case "TAB"    :return KeyEvent.KEYCODE_TAB;
            case "CAPS"   :return KeyEvent.KEYCODE_CAPS_LOCK;
            //功能键
            case "WIN"    :return KeyEvent.KEYCODE_META_LEFT;
            case "DEL"    :return KeyEvent.KEYCODE_FORWARD_DEL;
            case "INS"    :return KeyEvent.KEYCODE_INSERT;
            case "HOME"   :return KeyEvent.KEYCODE_MOVE_HOME;
            case "END"    :return KeyEvent.KEYCODE_MOVE_END;
            case "PGUP"   :return KeyEvent.KEYCODE_PAGE_UP;
            case "PGDN"   :return KeyEvent.KEYCODE_PAGE_DOWN;
            case "BREAK"  :return KeyEvent.KEYCODE_BREAK;
            case "SLCK"   :return KeyEvent.KEYCODE_SCROLL_LOCK;
            case "PRINT"  :return KeyEvent.KEYCODE_SYSRQ;
            case "UP"     :return KeyEvent.KEYCODE_DPAD_UP;
            case "DOWN"   :return KeyEvent.KEYCODE_DPAD_DOWN;
            case "LEFT"   :return KeyEvent.KEYCODE_DPAD_LEFT;
            case "RIGHT"  :return KeyEvent.KEYCODE_DPAD_RIGHT;
            //
            case "1"   :return KeyEvent.KEYCODE_1;
            case "2"   :return KeyEvent.KEYCODE_2;
            case "3"   :return KeyEvent.KEYCODE_3;
            case "4"   :return KeyEvent.KEYCODE_4;
            case "5"   :return KeyEvent.KEYCODE_5;
            case "6"   :return KeyEvent.KEYCODE_6;
            case "7"   :return KeyEvent.KEYCODE_7;
            case "8"   :return KeyEvent.KEYCODE_8;
            case "9"   :return KeyEvent.KEYCODE_9;
            case "0"   :return KeyEvent.KEYCODE_0;
            //
            case "F1"  :return KeyEvent.KEYCODE_F1;
            case "F2"  :return KeyEvent.KEYCODE_F2;
            case "F3"  :return KeyEvent.KEYCODE_F3;
            case "F4"  :return KeyEvent.KEYCODE_F4;
            case "F5"  :return KeyEvent.KEYCODE_F5;
            case "F6"  :return KeyEvent.KEYCODE_F6;
            case "F7"  :return KeyEvent.KEYCODE_F7;
            case "F8"  :return KeyEvent.KEYCODE_F8;
            case "F9"  :return KeyEvent.KEYCODE_F9;
            case "F10" :return KeyEvent.KEYCODE_F10;
            case "F11" :return KeyEvent.KEYCODE_F11;
            case "F12" :return KeyEvent.KEYCODE_F12;
            //
            case "~"   :return KeyEvent.KEYCODE_GRAVE;
            case "_"   :return KeyEvent.KEYCODE_MINUS;
            case "="   :return KeyEvent.KEYCODE_EQUALS;
            case "["   :return KeyEvent.KEYCODE_LEFT_BRACKET;
            case "]"   :return KeyEvent.KEYCODE_RIGHT_BRACKET;
            case "\\"  :return KeyEvent.KEYCODE_BACKSLASH;
            case ";"   :return KeyEvent.KEYCODE_SEMICOLON;
            case "\""  :return KeyEvent.KEYCODE_APOSTROPHE;
            case "<"   :return KeyEvent.KEYCODE_COMMA;
            case ">"   :return KeyEvent.KEYCODE_PERIOD;
            case "/"   :return KeyEvent.KEYCODE_SLASH;
            //
            case "NUM1"   :return KeyEvent.KEYCODE_NUMPAD_1;
            case "NUM2"   :return KeyEvent.KEYCODE_NUMPAD_2;
            case "NUM3"   :return KeyEvent.KEYCODE_NUMPAD_3;
            case "NUM4"   :return KeyEvent.KEYCODE_NUMPAD_4;
            case "NUM5"   :return KeyEvent.KEYCODE_NUMPAD_5;
            case "NUM6"   :return KeyEvent.KEYCODE_NUMPAD_6;
            case "NUM7"   :return KeyEvent.KEYCODE_NUMPAD_7;
            case "NUM8"   :return KeyEvent.KEYCODE_NUMPAD_8;
            case "NUM9"   :return KeyEvent.KEYCODE_NUMPAD_9;
            case "NUM0"   :return KeyEvent.KEYCODE_NUMPAD_0;
            case "NUM."   :return KeyEvent.KEYCODE_NUMPAD_DOT;
            case "NUM+"   :return KeyEvent.KEYCODE_NUMPAD_ADD;
            case "NUM_"   :return KeyEvent.KEYCODE_NUMPAD_SUBTRACT;
            case "NUM*"   :return KeyEvent.KEYCODE_NUMPAD_MULTIPLY;
            case "NUM/"   :return KeyEvent.KEYCODE_NUMPAD_DIVIDE;
            case "NUMENT" :return KeyEvent.KEYCODE_NUMPAD_ENTER;
            case "NUMLCK" :return KeyEvent.KEYCODE_NUM_LOCK;

        }
        return -1;
    }
}
