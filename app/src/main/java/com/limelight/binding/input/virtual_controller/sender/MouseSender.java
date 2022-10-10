package com.limelight.binding.input.virtual_controller.sender;

import android.view.KeyEvent;

import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;
import com.limelight.nvstream.input.MouseButtonPacket;

import java.util.Map;

public class MouseSender extends ControllerSender{

    private final String key;
    private final byte keyCode;
    private boolean lastStatus = false;
    private final Map<Byte,Boolean> mouseMap;

    public MouseSender(String key,VirtualController virtualController) {
        this.key = key;
        this.keyCode = MouseButtonPacket.getMouseCode(key);
        this.mouseMap = virtualController.getMouseInputContext();
    }

    @Override
    public byte sendMessage(boolean isPress) {
        if (lastStatus == isPress){
            return 0x0;
        }
        lastStatus = isPress;
        mouseMap.put(keyCode,isPress);
        return 0x2;
    }

    @Override
    public byte sendMessage(int index, boolean isPress) {
        return 0x2;
    }

}
