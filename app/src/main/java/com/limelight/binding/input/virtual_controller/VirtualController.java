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
import com.limelight.preferences.PreferenceConfiguration;
import com.limelight.utils.LayoutSelectHelper;

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
        SelectLayout
    }

    private static final boolean _PRINT_DEBUG_INFORMATION = false;

    private final ControllerHandler controllerHandler;
    private final Game game;
    private final Context context;

    private FrameLayout frame_layout = null;

    private Timer retransmitTimer;

    ControllerMode currentMode = ControllerMode.Active;
    ControllerInputContext inputContext = new ControllerInputContext();

    private static Map<String,KeyEvent> keyEventMap = new HashMap<>();
    private Button buttonConfigure = null;
    private VirtualControllerLayoutSelector VCLSelector = null;


    private List<VirtualControllerElement> elements = new ArrayList<>();
    private VirtualController virtualController;
    private PreferenceConfiguration config;


    public VirtualController(final ControllerHandler controllerHandler, FrameLayout layout, final Context context, final Game game) {
        this.controllerHandler = controllerHandler;
        this.game = game;
        this.frame_layout = layout;
        this.context = context;
        this.virtualController = this;

        config = PreferenceConfiguration.readPreferences(context);

        VCLSelector = new VirtualControllerLayoutSelector(context,frame_layout);
        VCLSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LayoutSelectHelper.selectLayout(i);
                VCLSelector.setSelection(i);
                VirtualControllerConfigurationLoader.loadFromPreferences(virtualController, context, LayoutSelectHelper.getCurrentLayoutName());

                for (VirtualControllerElement element : elements) {
                    element.invalidate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                    message = "Entering configuration mode (Select layout)";
                } else if (currentMode == ControllerMode.SelectLayout) {
                    currentMode = ControllerMode.MoveButtons;
                    message = "Entering configuration mode (Move buttons)";
                } else if (currentMode == ControllerMode.MoveButtons) {
                    currentMode = ControllerMode.ResizeButtons;
                    message = "Entering configuration mode (Resize buttons)";
                } else {
                    currentMode = ControllerMode.Active;
                    VirtualControllerConfigurationLoader.saveProfile(VirtualController.this, context, LayoutSelectHelper.getCurrentLayoutName());
                    message = "Exiting configuration mode";
                }

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                buttonConfigure.invalidate();

                for (VirtualControllerElement element : elements) {
                    element.invalidate();
                }
                if (currentMode == ControllerMode.SelectLayout){
                    VCLSelector.setVisibility(View.VISIBLE);
                } else {
                    VCLSelector.setVisibility(View.INVISIBLE);
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

        frame_layout.removeView(buttonConfigure);
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

        DisplayMetrics screen = context.getResources().getDisplayMetrics();

        int buttonSize = (int)(screen.heightPixels*0.06f);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(buttonSize, buttonSize);
        params.leftMargin = 15;
        params.topMargin = 15;
        frame_layout.addView(buttonConfigure, params);

        // Start with the default layout
        if (config.onscreenController){
            VirtualControllerConfigurationLoader.createDefaultLayout(this, context);
        } else if (config.onscreenKeyboard) {
            VirtualControllerConfigurationLoader.createDefaultKeyboardButton(this,context);
        }


        // Apply user preferences onto the default layout
        VirtualControllerConfigurationLoader.loadFromPreferences(this, context, LayoutSelectHelper.getCurrentLayoutName());
        VCLSelector.refreshLayout();


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
