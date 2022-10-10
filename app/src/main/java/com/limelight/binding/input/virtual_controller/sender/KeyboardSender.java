package com.limelight.binding.input.virtual_controller.sender;

import android.view.KeyEvent;

import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;

import java.util.Map;

public class KeyboardSender extends ControllerSender{

    private final String key;
    private final int keyCode;
    private boolean lastStatus = false;
    private final Map<String, KeyEvent> keyMap;

    public KeyboardSender(String key,VirtualController virtualController) {
        this.key = key;
        this.keyCode = VirtualControllerConfigurationLoader.getKeycode(key);
        this.keyMap = virtualController.getKeyboardInputContext();
    }

    @Override
    public byte sendMessage(boolean isPress) {
        if (lastStatus == isPress){
            return 0x0;
        }
        lastStatus = isPress;
        if (isPress) {
            keyMap.put(""+ keyCode,new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
        }else {
            keyMap.put(""+ keyCode,new KeyEvent(KeyEvent.ACTION_UP,keyCode));
        }
        return 0x1;
    }

    @Override
    public byte sendMessage(int index, boolean isPress) {
        return 0x1;
    }
}
