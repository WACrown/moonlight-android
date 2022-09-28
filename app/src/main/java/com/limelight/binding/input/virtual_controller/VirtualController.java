/**
 * Created by Karim Mreisi.
 */

package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.limelight.Game;
import com.limelight.LimeLog;
import com.limelight.R;
import com.limelight.binding.input.ControllerHandler;
import com.limelight.binding.input.virtual_controller.selector.VirtualControllerAddButton;
import com.limelight.binding.input.virtual_controller.selector.VirtualControllerTypeSelector;
import com.limelight.binding.input.virtual_controller.selector.VirtualControllerLayoutSelector;
import com.limelight.nvstream.NvConnection;
import com.limelight.utils.controller.LayoutAdminHelper;
import com.limelight.utils.controller.LayoutEditHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        EditLayout
    }

    public final Set<VirtualControllerElement> virtualControllerElementSet = new HashSet<>();

    private static final boolean _PRINT_DEBUG_INFORMATION = false;

    private final ControllerHandler controllerHandler;
    private final Game game;
    private final NvConnection conn;
    private final Context context;
    private final Map<String,KeyEvent> keyEventMap = new HashMap<>();
    private final Map<Byte,Boolean> mouseMap = new HashMap<>();
    private final List<VirtualControllerElement> elements = new ArrayList<>();
    private final VirtualController virtualController;



    private FrameLayout frame_layout = null;

    private Timer retransmitTimer;

    ControllerMode currentMode = ControllerMode.Active;
    ControllerInputContext inputContext = new ControllerInputContext();


    private Button buttonConfigure = null;
    private Button buttonAdd = null;
    private Button buttonDelete = null;
    private VirtualControllerLayoutSelector VCLSelector = null;
    private VirtualControllerTypeSelector typeSelector = null;
    private VirtualControllerAddButton buttonUpSelector = null;
    private VirtualControllerAddButton buttonDownSelector = null;
    private VirtualControllerAddButton buttonLeftSelector = null;
    private VirtualControllerAddButton buttonRightSelector = null;
    private VirtualControllerAddButton buttonSelector = null;
    private Toast toast;




    public VirtualController(final ControllerHandler controllerHandler, FrameLayout layout, final Context mContext, final Game game, final NvConnection conn) {
        this.controllerHandler = controllerHandler;
        this.game = game;
        this.conn = conn;
        this.frame_layout = layout;
        this.context = mContext;
        this.virtualController = this;
        VCLSelector = new VirtualControllerLayoutSelector(context,frame_layout,this);


        buttonAdd = new Button(context);
        buttonAdd.setBackgroundColor(0xFF3EFF13);
        buttonAdd.setText("ADD");
        buttonAdd.setTextColor(0xFF505050);
        buttonAdd.setVisibility(View.INVISIBLE);


        buttonDelete = new Button(context);
        buttonDelete.setBackgroundColor(0xFFF81010);
        buttonDelete.setText("DELETE");
        buttonDelete.setTextColor(0xFF505050);
        buttonDelete.setVisibility(View.INVISIBLE);


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (VirtualControllerElement virtualControllerElement : virtualControllerElementSet){
                    frame_layout.removeView(virtualControllerElement);
                    elements.remove(virtualControllerElement);
                }
                virtualControllerElementSet.clear();
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
                    currentMode = ControllerMode.EditLayout;
                    message = "Setting (Edit layout)";
                } else if (currentMode == ControllerMode.EditLayout) {
                    currentMode = ControllerMode.MoveButtons;
                    message = "Setting (Move buttons)";
                } else if (currentMode == ControllerMode.MoveButtons) {
                    currentMode = ControllerMode.ResizeButtons;
                    message = "Setting (Resize buttons)";
                } else {
                    currentMode = ControllerMode.Active;
                    VirtualControllerConfigurationLoader.saveProfile(VirtualController.this, context);
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

                if (currentMode == ControllerMode.EditLayout){
                    VCLSelector.setVisibility(View.VISIBLE);
                    typeSelector.setVisibility(View.VISIBLE);
                    buttonAdd.setVisibility(View.VISIBLE);
                    buttonDelete.setVisibility(View.VISIBLE);
                } else {
                    VCLSelector.setVisibility(View.INVISIBLE);
                    typeSelector.setVisibility(View.INVISIBLE);
                    buttonAdd.setVisibility(View.INVISIBLE);
                    buttonDelete.setVisibility(View.INVISIBLE);
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

        int ButtonHeight = (int)(screen.heightPixels*0.1f);
        int ButtonWidth = (int)(screen.widthPixels*0.2f);

        params = new FrameLayout.LayoutParams(ButtonWidth, ButtonHeight);
        params.leftMargin = (int)(screen.widthPixels*0.275f);
        params.topMargin = (int)(screen.heightPixels*0.4f);
        frame_layout.addView(buttonAdd, params);

        params = new FrameLayout.LayoutParams(ButtonWidth, ButtonHeight);
        params.leftMargin = (int)(screen.widthPixels*0.525f);
        params.topMargin = (int)(screen.heightPixels*0.4f);
        frame_layout.addView(buttonDelete, params);

        Map<String, String> allButton = LayoutEditHelper.loadAllButton(context);
        //System.out.println("wangguan allButton" + allButton + "layoutName" + LayoutAdminHelper.getCurrentLayoutName(context));
        VirtualControllerConfigurationLoader.createButtons(this,context,allButton);
        VCLSelector.refreshLayout();
        buttonEditSpinner();


    }



    private void buttonEditSpinner(){
        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        buttonUpSelector = new VirtualControllerAddButton(context,frame_layout,(int)(screen.widthPixels*0.025f),(int)(screen.heightPixels*0.25f));
        buttonDownSelector = new VirtualControllerAddButton(context,frame_layout,(int)(screen.widthPixels*0.275f),(int)(screen.heightPixels*0.25f));
        buttonLeftSelector = new VirtualControllerAddButton(context,frame_layout,(int)(screen.widthPixels*0.525f),(int)(screen.heightPixels*0.25f));
        buttonRightSelector = new VirtualControllerAddButton(context,frame_layout,(int)(screen.widthPixels*0.775f),(int)(screen.heightPixels*0.25f));
        buttonSelector = new VirtualControllerAddButton(context,frame_layout,(int)(screen.widthPixels*0.65f),(int)(screen.heightPixels*0.1f));
        typeSelector = new VirtualControllerTypeSelector(context,frame_layout,buttonSelector,buttonUpSelector,buttonDownSelector,buttonLeftSelector,buttonRightSelector);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Set<String> allButtonName = LayoutEditHelper.loadAllButton(context).keySet();
                String buttonNamePre = "";
                switch ((String) typeSelector.getSelectedItem()) {
                    case "KEYBOARD" : {
                        buttonNamePre = "KEYBOARD-" + (String) buttonSelector.getSelectedItem() + "-";
                        break;
                    }
                    case "PAD" : {
                        buttonNamePre = "PAD-" + (String) buttonUpSelector.getSelectedItem() + "-" + (String) buttonDownSelector.getSelectedItem() + "-" + (String) buttonLeftSelector.getSelectedItem() + "-" + (String) buttonRightSelector.getSelectedItem() + "-";
                        break;
                    }
                    case "STICK" : {
                        buttonNamePre = "STICK-" + (String) buttonUpSelector.getSelectedItem() + "-" + (String) buttonDownSelector.getSelectedItem() + "-" + (String) buttonLeftSelector.getSelectedItem() + "-" + (String) buttonRightSelector.getSelectedItem() + "-" + (String) buttonSelector.getSelectedItem() + "-";
                        break;
                    }
                    case "GAMEPAD" : {
                        buttonNamePre = "GAMEPAD-" + (String) buttonSelector.getSelectedItem() + "-";
                        break;
                    }
                    case "MOUSE" : {
                        buttonNamePre = "MOUSE-" + (String) buttonSelector.getSelectedItem() + "-";
                        break;
                    }

                }
                for (int i = 0;i < 100;i ++){
                    String buttonName = buttonNamePre + i;
                    if (!allButtonName.contains(buttonName)){
                        Map<String, String> newButton = new HashMap<>();
                        newButton.put(buttonName,"{\"LEFT\":57,\"TOP\":589,\"WIDTH\":431,\"HEIGHT\":431}");
                        VirtualControllerConfigurationLoader.createButtons(virtualController,context,newButton);
                        //System.out.println("wangguan " + newButton);
                        break;
                    }
                }

            }
        });

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

    public Map<Byte,Boolean> getMouseInputContext() {
        return mouseMap;
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


    void sendMouseKey() {
        _DBG("MOUSE_EVENT_MAP + " + mouseMap);
        if (game != null) {
            for (Byte mouseKey : mouseMap.keySet()){
                Boolean isDown = mouseMap.get(mouseKey);
                if (isDown != null){
                    if (isDown){
                        conn.sendMouseButtonDown(mouseKey);
                    } else {
                        conn.sendMouseButtonUp(mouseKey);
                    }
                }
            }
        }
    }

}
