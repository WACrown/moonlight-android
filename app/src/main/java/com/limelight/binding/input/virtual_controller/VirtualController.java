/**
 * Created by Karim Mreisi.
 */

package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.limelight.Game;
import com.limelight.LimeLog;
import com.limelight.R;
import com.limelight.binding.input.ControllerHandler;
import com.limelight.binding.input.virtual_controller.selector.VirtualControllerAddButton;
import com.limelight.binding.input.virtual_controller.selector.VirtualControllerKeyTypeSelector;
import com.limelight.binding.input.virtual_controller.selector.VirtualControllerLayoutSelector;
import com.limelight.utils.controller.LayoutSelectHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class VirtualController {
    public static class ControllerInputContext {
        public short inputMap = 0x0000;
        public byte leftTrigger = 0x00;
        public byte rightTrigger = 0x00;
        public short rightStickX = 0x0000;
        public short rightStickY = 0x0000;
        public short leftStickX = 0x0000;
        public short leftStickY = 0x0000;
    }



    public enum ControllerMode {
        Active,
        MoveButtons,
        ResizeButtons,
        SelectLayout,
        AddButton,
        DeleteButton
    }

    private static final boolean _PRINT_DEBUG_INFORMATION = false;

    private final ControllerHandler controllerHandler;
    private final Game game;
    private final Context context;
    private final Map<String,KeyEvent> keyEventMap = new HashMap<>();
    private final List<VirtualControllerElement> elements = new ArrayList<>();
    private final VirtualController virtualController;



    private FrameLayout frame_layout = null;

    private Timer retransmitTimer;

    ControllerMode currentMode = ControllerMode.Active;
    ControllerInputContext inputContext = new ControllerInputContext();


    private Button buttonConfigure = null;
    private VirtualControllerLayoutSelector VCLSelector = null;
    private VirtualControllerKeyTypeSelector typeSelector = null;
    private VirtualControllerAddButton buttonUpSelector = null;
    private VirtualControllerAddButton buttonDownSelector = null;
    private VirtualControllerAddButton buttonLeftSelector = null;
    private VirtualControllerAddButton buttonRightSelector = null;
    private VirtualControllerAddButton buttonSelector = null;
    private Toast toast;




    public VirtualController(final ControllerHandler controllerHandler, FrameLayout layout, final Context mContext, final Game game) {
        this.controllerHandler = controllerHandler;
        this.game = game;
        this.frame_layout = layout;
        this.context = mContext;
        this.virtualController = this;
        VCLSelector = new VirtualControllerLayoutSelector(context,frame_layout,this);
        typeSelector = new VirtualControllerKeyTypeSelector(context,frame_layout);
        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        buttonUpSelector = new VirtualControllerAddButton(context,frame_layout,(int)(screen.heightPixels*0.025f));
        buttonDownSelector = new VirtualControllerAddButton(context,frame_layout,(int)(screen.heightPixels*0.275f));
        buttonLeftSelector = new VirtualControllerAddButton(context,frame_layout,(int)(screen.heightPixels*0.525f));
        buttonRightSelector = new VirtualControllerAddButton(context,frame_layout,(int)(screen.heightPixels*0.775f));
        buttonSelector = new VirtualControllerAddButton(context,frame_layout,(int)(screen.heightPixels*0.1f));


        buttonConfigure = new Button(context);
        buttonConfigure.setAlpha(0.25f);
        buttonConfigure.setFocusable(false);
        buttonConfigure.setBackgroundResource(R.drawable.ic_settings);
        buttonConfigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;

                if (currentMode == ControllerMode.Active){
                    currentMode = ControllerMode.SelectLayout;
                    message = "Setting (Select layout)";
                } else if (currentMode == ControllerMode.SelectLayout) {
                    currentMode = ControllerMode.AddButton;
                    message = "Setting (Add buttons)";
                } else if (currentMode == ControllerMode.AddButton) {
                    currentMode = ControllerMode.MoveButtons;
                    message = "Setting (Move buttons)";
                } else if (currentMode == ControllerMode.MoveButtons) {
                    currentMode = ControllerMode.ResizeButtons;
                    message = "Setting (Resize buttons)";
                } else if (currentMode == ControllerMode.ResizeButtons) {
                    currentMode = ControllerMode.DeleteButton;
                    message = "Setting (Delete buttons)";
                } else {
                    currentMode = ControllerMode.Active;
                    VirtualControllerConfigurationLoader.saveProfile(VirtualController.this, context, LayoutSelectHelper.getCurrentLayoutName(context));
                    message = "Exiting Setting";
                }


                if (toast != null){
                    toast.cancel();
                }
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                toast.show();


                buttonConfigure.invalidate();
                for (VirtualControllerElement element : elements) {
                    element.invalidate();
                }

                if (currentMode == ControllerMode.SelectLayout){
                    VCLSelector.setVisibility(View.VISIBLE);
                } else {
                    VCLSelector.setVisibility(View.INVISIBLE);
                }
                if (currentMode == ControllerMode.AddButton){
                    typeSelector.setVisibility(View.VISIBLE);
                    buttonSelector.setVisibility(View.VISIBLE);
                    buttonRightSelector.setVisibility(View.VISIBLE);
                    buttonLeftSelector.setVisibility(View.VISIBLE);
                    buttonDownSelector.setVisibility(View.VISIBLE);
                    buttonUpSelector.setVisibility(View.VISIBLE);
                } else {
                    typeSelector.setVisibility(View.INVISIBLE);
                    buttonSelector.setVisibility(View.INVISIBLE);
                    buttonRightSelector.setVisibility(View.INVISIBLE);
                    buttonLeftSelector.setVisibility(View.INVISIBLE);
                    buttonDownSelector.setVisibility(View.INVISIBLE);
                    buttonUpSelector.setVisibility(View.INVISIBLE);
                }


            }
        });

    }


    public VirtualController getVirtualController(){
        return this;
    }

    public void hide() {
        retransmitTimer.cancel();

        for (VirtualControllerElement element : elements) {
            element.setVisibility(View.INVISIBLE);
        }

        buttonConfigure.setVisibility(View.INVISIBLE);
    }

    public void show() {
        for (VirtualControllerElement element : elements) {
            element.setVisibility(View.VISIBLE);
        }

        buttonConfigure.setVisibility(View.VISIBLE);

        // HACK: GFE sometimes discards gamepad packets when they are received
        // very shortly after another. This can be critical if an axis zeroing packet
        // is lost and causes an analog stick to get stuck. To avoid this, we send
        // a gamepad input packet every 100 ms to ensure any loss can be recovered.
        retransmitTimer = new Timer("OSC timer", true);
        retransmitTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendControllerInputContext();
            }
        }, 100, 100);
    }

    public void removeElements() {
        for (VirtualControllerElement element : elements) {
            frame_layout.removeView(element);
        }
        elements.clear();
    }

    public void setOpacity(int opacity) {
        for (VirtualControllerElement element : elements) {
            element.setOpacity(opacity);
        }
    }


    public void addElement(VirtualControllerElement element, int x, int y, int width, int height) {
        elements.add(element);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.setMargins(x, y, 0, 0);

        frame_layout.addView(element, layoutParams);
    }

    public List<VirtualControllerElement> getElements() {
        return elements;
    }

    private static final void _DBG(String text) {
        if (_PRINT_DEBUG_INFORMATION) {
            LimeLog.info("VirtualController: " + text);
        }
    }

    public void refreshLayout() {
        removeElements();
        frame_layout.removeView(buttonConfigure);

        DisplayMetrics screen = context.getResources().getDisplayMetrics();

        int buttonSize = (int)(screen.heightPixels*0.06f);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(buttonSize, buttonSize);
        params.leftMargin = 15;
        params.topMargin = 15;
        frame_layout.addView(buttonConfigure, params);
        VirtualControllerConfigurationLoader.createButtonLayout(this,context);
        VCLSelector.refreshLayout();
        typeSelector.refreshLayout();
        buttonSelector.refreshLayout();
        buttonRightSelector.refreshLayout();
        buttonLeftSelector.refreshLayout();
        buttonDownSelector.refreshLayout();
        buttonUpSelector.refreshLayout();


    }

    public ControllerMode getControllerMode() {
        return currentMode;
    }

    public ControllerInputContext getControllerInputContext() {
        return inputContext;
    }

    public Map<String,KeyEvent> getKeyboardInputContext() {
        return keyEventMap;
    }

    void sendControllerInputContext() {
        _DBG("INPUT_MAP + " + inputContext.inputMap);
        _DBG("LEFT_TRIGGER " + inputContext.leftTrigger);
        _DBG("RIGHT_TRIGGER " + inputContext.rightTrigger);
        _DBG("LEFT STICK X: " + inputContext.leftStickX + " Y: " + inputContext.leftStickY);
        _DBG("RIGHT STICK X: " + inputContext.rightStickX + " Y: " + inputContext.rightStickY);

        if (controllerHandler != null) {
            controllerHandler.reportOscState(
                    inputContext.inputMap,
                    inputContext.leftStickX,
                    inputContext.leftStickY,
                    inputContext.rightStickX,
                    inputContext.rightStickY,
                    inputContext.leftTrigger,
                    inputContext.rightTrigger
            );
        }
    }

    void sendKeyboardInputPadKey() {

        _DBG("KEY_EVENT_MAP + " + keyEventMap);
        if (game != null) {
            for (String key : keyEventMap.keySet()){
                KeyEvent keyEvent = keyEventMap.get(key);
                if (keyEvent != null){
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                        game.handleKeyDown(keyEvent);
                    } else if (keyEvent.getAction() == KeyEvent.ACTION_UP){
                        game.handleKeyUp(keyEvent);
                    }
                }
            }
        }
    }

}
