/**
 * Created by Karim Mreisi.
 */

package com.limelight.binding.input.virtual_controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.limelight.LimeLog;
import com.limelight.R;
import com.limelight.binding.input.ControllerHandler;
import com.limelight.preferences.PreferenceConfiguration;
import com.limelight.utils.SelectControllerLayoutHelp;
import com.limelight.utils.SelectKeyboardLayoutHelp;

import java.util.ArrayList;
import java.util.List;
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
    private final Context context;

    private FrameLayout frame_layout = null;

    private Timer retransmitTimer;

    ControllerMode currentMode = ControllerMode.Active;
    ControllerInputContext inputContext = new ControllerInputContext();

    private Button buttonConfigure = null;
    private VirtualControllerLayoutSelector VCLSelector = null;

    private List<VirtualControllerElement> elements = new ArrayList<>();
    private VirtualController virtualController;
    private PreferenceConfiguration config;


    public VirtualController(final ControllerHandler controllerHandler, FrameLayout layout, final Context context) {
        this.controllerHandler = controllerHandler;
        this.frame_layout = layout;
        this.context = context;
        this.virtualController = this;

        config = PreferenceConfiguration.readPreferences(context);
        SelectControllerLayoutHelp.initSharedPreferences(context);
        SelectKeyboardLayoutHelp.initSharedPreferences(context);

        VCLSelector = new VirtualControllerLayoutSelector(context,frame_layout);
        VCLSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectControllerLayoutHelp.setCurrentNum(context,i);
                VCLSelector.setSelection(i);
                if (config.onscreenController){
                    VirtualControllerConfigurationLoader.loadFromPreferences(virtualController, context, SelectControllerLayoutHelp.loadSingleLayoutName(context, SelectControllerLayoutHelp.getCurrentNum(context)));
                } else if (true) {
                    VirtualControllerConfigurationLoader.loadFromPreferences(virtualController, context, SelectKeyboardLayoutHelp.loadSingleLayoutName(context, SelectKeyboardLayoutHelp.getCurrentNum(context)));
                }
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
                    message = "Entering configuration mode (Move buttons)";
                } else if (currentMode == ControllerMode.SelectLayout) {
                    currentMode = ControllerMode.MoveButtons;
                    message = "Entering configuration mode (Resize buttons)";
                } else if (currentMode == ControllerMode.MoveButtons) {
                    currentMode = ControllerMode.ResizeButtons;
                    message = "Entering configuration mode (Select layout)";
                } else {
                    currentMode = ControllerMode.Active;
                    if (config.onscreenController){
                        VirtualControllerConfigurationLoader.saveProfile(VirtualController.this, context, SelectControllerLayoutHelp.loadSingleLayoutName(context, SelectControllerLayoutHelp.getCurrentNum(context)));
                    } else if (true) {
                        VirtualControllerConfigurationLoader.saveProfile(VirtualController.this, context, SelectKeyboardLayoutHelp.loadSingleLayoutName(context, SelectKeyboardLayoutHelp.getCurrentNum(context)));
                        System.out.println("wangguan save");
                    }
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
        } else if (true) {
            VirtualControllerConfigurationLoader.createDefaultKeyboardButton(this,context);
        }


        // Apply user preferences onto the default layout
        if (config.onscreenController){
            VirtualControllerConfigurationLoader.loadFromPreferences(this, context, SelectControllerLayoutHelp.loadSingleLayoutName(context, SelectControllerLayoutHelp.getCurrentNum(context)));
        } else if (true) {
            VirtualControllerConfigurationLoader.loadFromPreferences(this, context, SelectKeyboardLayoutHelp.loadSingleLayoutName(context, SelectKeyboardLayoutHelp.getCurrentNum(context)));
        }
        VCLSelector.refreshLayout();


    }

    public ControllerMode getControllerMode() {
        return currentMode;
    }

    public ControllerInputContext getControllerInputContext() {
        return inputContext;
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
}
