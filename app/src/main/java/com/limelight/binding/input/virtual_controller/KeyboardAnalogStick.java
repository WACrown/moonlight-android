package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.view.KeyEvent;

import java.util.Map;

public class KeyboardAnalogStick extends AnalogStick{

    private final int MIN_CIRCLE_R = 10000;  //当摇杆移动的非常小时，不产生操作，摇杆范围-32765<x,y<32765
    private final float EIGHTH_PI = 0.4142f;  // y=tan(π/8)x 分界线
    private final float EIGHTH_THREE_PI = 2.4142f;  //  y=tan(3π/8)x 分界线
    private final float NEGATIVE_EIGHTH_PI = -0.4142f; // y=tan(-π/8)x 分界线
    private final float NEGATIVE_EIGHTH_THREE_PI = -2.4142f; // y=tan(-3π/8)x 分界线
    private Map<String, KeyEvent> keyEventMap;
    public KeyboardAnalogStick(VirtualController controller, String elementId, Context context,
                               int keyUp,
                               int keyDown,
                               int keyLeft,
                               int keyRight,
                               int keyDouble) {
        super(controller, context, elementId);
        keyEventMap = controller.getKeyboardInputContext();

        addAnalogStickListener(new AnalogStickListener() {
            @Override
            public void onMovement(float xf, float yf) {
                short x = (short) (xf * 0x7FFE);
                short y = (short) (yf * 0x7FFE);

                keyEventMap.put(""+keyLeft,new KeyEvent(KeyEvent.ACTION_UP,keyLeft));
                keyEventMap.put(""+keyRight,new KeyEvent(KeyEvent.ACTION_UP,keyRight));
                keyEventMap.put(""+keyUp,new KeyEvent(KeyEvent.ACTION_UP,keyUp));
                keyEventMap.put(""+keyDown,new KeyEvent(KeyEvent.ACTION_UP,keyDown));
                keyEventMap.put(""+keyDouble,new KeyEvent(KeyEvent.ACTION_UP,keyDouble));

                boolean b = x * x + y * y > MIN_CIRCLE_R * MIN_CIRCLE_R;
                if (y >= EIGHTH_THREE_PI * x && y >= NEGATIVE_EIGHTH_THREE_PI * x && b){
                    //UP
                    keyEventMap.put(""+keyUp,new KeyEvent(KeyEvent.ACTION_DOWN,keyUp));
                } else if (y < EIGHTH_THREE_PI * x && y < NEGATIVE_EIGHTH_THREE_PI * x && b){
                    //DOWN
                    keyEventMap.put(""+keyDown,new KeyEvent(KeyEvent.ACTION_DOWN,keyDown));
                } else if (y >= EIGHTH_PI * x && y < NEGATIVE_EIGHTH_PI * x && b){
                    //LEFT
                    keyEventMap.put(""+keyLeft,new KeyEvent(KeyEvent.ACTION_DOWN,keyLeft));
                } else if (y < EIGHTH_PI * x && y >= NEGATIVE_EIGHTH_PI * x && b){
                    //RIGHT
                    keyEventMap.put(""+keyRight,new KeyEvent(KeyEvent.ACTION_DOWN,keyRight));
                } else if (y < NEGATIVE_EIGHTH_THREE_PI * x && y >= NEGATIVE_EIGHTH_PI * x && b){
                    //UP & LEFT
                    keyEventMap.put(""+keyUp,new KeyEvent(KeyEvent.ACTION_DOWN,keyUp));
                    keyEventMap.put(""+keyLeft,new KeyEvent(KeyEvent.ACTION_DOWN,keyLeft));
                } else if (y >= NEGATIVE_EIGHTH_THREE_PI * x && y < NEGATIVE_EIGHTH_PI * x && b){
                    //DOWN & RIGHT
                    keyEventMap.put(""+keyDown,new KeyEvent(KeyEvent.ACTION_DOWN,keyDown));
                    keyEventMap.put(""+keyRight,new KeyEvent(KeyEvent.ACTION_DOWN,keyRight));
                } else if (y >= EIGHTH_PI * x && y < EIGHTH_THREE_PI * x && b){
                    //UP & RIGHT
                    keyEventMap.put(""+keyUp,new KeyEvent(KeyEvent.ACTION_DOWN,keyUp));
                    keyEventMap.put(""+keyRight,new KeyEvent(KeyEvent.ACTION_DOWN,keyRight));
                } else if (y < EIGHTH_PI * x && y >= EIGHTH_THREE_PI * x && b){
                    //DOWN & LEFT
                    keyEventMap.put(""+keyDown,new KeyEvent(KeyEvent.ACTION_DOWN,keyDown));
                    keyEventMap.put(""+keyLeft,new KeyEvent(KeyEvent.ACTION_DOWN,keyLeft));
                } else {
                    keyEventMap.put(""+keyLeft,new KeyEvent(KeyEvent.ACTION_UP,keyLeft));
                    keyEventMap.put(""+keyRight,new KeyEvent(KeyEvent.ACTION_UP,keyRight));
                    keyEventMap.put(""+keyUp,new KeyEvent(KeyEvent.ACTION_UP,keyUp));
                    keyEventMap.put(""+keyDown,new KeyEvent(KeyEvent.ACTION_UP,keyDown));
                }
                controller.sendKeyboardInputPadKey();

            }

            @Override
            public void onClick() {

            }

            @Override
            public void onDoubleClick() {
                keyEventMap.put(""+keyDouble,new KeyEvent(KeyEvent.ACTION_DOWN,keyDouble));
                controller.sendKeyboardInputPadKey();
            }

            @Override
            public void onRevoke() {
                keyEventMap.put(""+keyLeft,new KeyEvent(KeyEvent.ACTION_UP,keyLeft));
                keyEventMap.put(""+keyRight,new KeyEvent(KeyEvent.ACTION_UP,keyRight));
                keyEventMap.put(""+keyUp,new KeyEvent(KeyEvent.ACTION_UP,keyUp));
                keyEventMap.put(""+keyDown,new KeyEvent(KeyEvent.ACTION_UP,keyDown));
                keyEventMap.put(""+keyDouble,new KeyEvent(KeyEvent.ACTION_UP,keyDouble));
                controller.sendKeyboardInputPadKey();
            }
        });
    }
}
