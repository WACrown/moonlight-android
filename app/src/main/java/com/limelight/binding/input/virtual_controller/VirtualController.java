/**
 * Created by Karim Mreisi.
 */

package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.limelight.Game;
import com.limelight.LimeLog;
import com.limelight.binding.input.ControllerHandler;
import com.limelight.binding.input.virtual_controller.game_setting.GameSetting;
import com.limelight.binding.input.virtual_controller.selector.VirtualControllerAddButton;
import com.limelight.binding.input.virtual_controller.selector.VirtualControllerFuncSelector;
import com.limelight.binding.input.virtual_controller.selector.VirtualControllerTypeSelector;
import com.limelight.nvstream.NvConnection;
import com.limelight.utils.PressedStartElementInfo;
import com.limelight.utils.controller.LayoutEditHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VirtualController {

    public enum ControllerMode {
        Active,
        EditButtons
    }

    public final Set<VirtualControllerElement> virtualControllerNeedDeleteElementSet = new HashSet<>();

    private static final boolean _PRINT_DEBUG_INFORMATION = false;

    private final ControllerHandler controllerHandler;
    private final Game game;
    private final NvConnection conn;
    private final Context context;
    private final Map<String,KeyEvent> keyEventMap = new HashMap<>();
    private final Map<Byte,Boolean> mouseMap = new HashMap<>();
    private final List<VirtualControllerElement> elements = new ArrayList<>();
    private final VirtualController virtualController;
    private short[] gamePadInputContext = {0,0,0,0,0,0,0};//inputMap,leftTrigger ,rightTrigger,leftStickX,leftStickY,rightStickX,rightStickY
    private final Handler handler;

    private final Runnable delayedRetransmitRunnable = new Runnable() {
        @Override
        public void run() {
            sendControllerInputContextInternal();
        }
    };

    private final FrameLayout frameFatherLayout;
    private final FrameLayout frameButtonLayout;
    private final FrameLayout frameSettingLayout;

    private ControllerMode currentMode = ControllerMode.Active;

    private final GameSetting gameSetting;




    public VirtualController(final ControllerHandler controllerHandler, FrameLayout layout, final Context context, final Game game, final NvConnection conn) {
        this.controllerHandler = controllerHandler;
        this.game = game;
        this.conn = conn;
        this.frameFatherLayout = layout;
        this.context = context;
        this.virtualController = this;
        this.handler = new Handler(Looper.getMainLooper());
        this.frameButtonLayout = new FrameLayout(context);
        this.frameSettingLayout = new FrameLayout(context);
        this.gameSetting = new GameSetting(context, frameSettingLayout,virtualController);

        refreshFrameButtonLayout();
        refreshFrameSettingLayout();
    }


    private void refreshFrameButtonLayout(){
        //System.out.println("wangguan allButton" + allButton + "layoutName" + LayoutAdminHelper.getCurrentLayoutName(context));
       // VirtualControllerConfigurationLoader.loadProfile(this,context,gameSetting.getSettingMenuItems().getItemViewMap());
    }

    private void refreshFrameSettingLayout(){

        gameSetting.refreshLayout();
    }


    Handler getHandler() {
        return handler;
    }

    public void setSelectedElement(VirtualControllerElement element, boolean select){
        element.setSelectedStatus(select);
        element.invalidate();
    }

    public ControllerMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(ControllerMode currentMode) {
        this.currentMode = currentMode;
        for (VirtualControllerElement element : elements){
            element.invalidate();
        }
    }

    public void hideButton(String display){
        if (display.equals("TRUE")){
            frameButtonLayout.setVisibility(View.VISIBLE);
        } else {
            frameButtonLayout.setVisibility(View.INVISIBLE);
        }

    }

    public void hide() {

        frameSettingLayout.setVisibility(View.INVISIBLE);
    }

    public void show() {
        frameButtonLayout.setVisibility(View.VISIBLE);
        frameSettingLayout.setVisibility(View.VISIBLE);
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

        frameButtonLayout.addView(element, layoutParams);
    }

    public void removeElements(Set<VirtualControllerElement> elements){
        for(VirtualControllerElement element : elements){
            this.elements.remove(element);
            frameButtonLayout.removeView(element);
        }

    }

    public void removeAllElements(){
        for(VirtualControllerElement element : elements){
            frameButtonLayout.removeView(element);
        }
        elements.clear();
    }

    public void moveElements(Map<VirtualControllerElement, PressedStartElementInfo> elementsMap,
                             int moveLengthX,
                             int moveLengthY,
                             int maxMoveLengthLeft,
                             int maxMoveLengthUp,
                             int maxMoveLengthRight,
                             int maxMoveLengthDown){
        for (VirtualControllerElement editElement : elementsMap.keySet()){
            PressedStartElementInfo pressedStartElementInfo = elementsMap.get(editElement);
            editElement.moveElement(moveLengthX,
                    moveLengthY,
                    pressedStartElementInfo.startElementPositionX,
                    pressedStartElementInfo.startElementPositionY,
                    maxMoveLengthLeft,
                    maxMoveLengthUp,
                    maxMoveLengthRight,
                    maxMoveLengthDown);
        }
    }

    public void resizeElements(Map<VirtualControllerElement,PressedStartElementInfo> elementsMap,
                               int changedDistanceX,
                               int changedDistanceY,
                               int maxIncreaseSizeX,
                               int maxIncreaseSizeY){
        for (VirtualControllerElement editElement : elementsMap.keySet()){
            PressedStartElementInfo pressedStartElementInfo = elementsMap.get(editElement);
            editElement.resizeElement(changedDistanceX,
                    changedDistanceY,
                    pressedStartElementInfo.startElementWidth,
                    pressedStartElementInfo.startElementHeight,
                    pressedStartElementInfo.elementCenterPositionX,
                    pressedStartElementInfo.elementCenterPositionY,
                    maxIncreaseSizeX,
                    maxIncreaseSizeY);
        }

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
        frameFatherLayout.removeView(frameButtonLayout);
        frameFatherLayout.removeView(frameSettingLayout);
        frameFatherLayout.addView(frameButtonLayout);
        frameFatherLayout.addView(frameSettingLayout);

    }


    public short[] getGamePadInputContext() {
        return gamePadInputContext;
    }


    public Map<String,KeyEvent> getKeyboardInputContext() {
        return keyEventMap;
    }

    public Map<Byte,Boolean> getMouseInputContext() {
        return mouseMap;
    }


    public void sendControllerInputContextInternal() {
        _DBG("INPUT_MAP + " + gamePadInputContext[0]);
        _DBG("LEFT_TRIGGER " + gamePadInputContext[1]);
        _DBG("RIGHT_TRIGGER " + gamePadInputContext[2]);
        _DBG("LEFT STICK X: " + gamePadInputContext[3] + " Y: " + gamePadInputContext[4]);
        _DBG("RIGHT STICK X: " + gamePadInputContext[5] + " Y: " + gamePadInputContext[6]);


        if (controllerHandler != null) {
            controllerHandler.reportOscState(
                    gamePadInputContext[0],
                    gamePadInputContext[3],
                    gamePadInputContext[4],
                    gamePadInputContext[5],
                    gamePadInputContext[6],
                    (byte) gamePadInputContext[1],
                    (byte) gamePadInputContext[2]
            );
        }
    }


    public void sendKeyboardInputPadKey() {
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
            keyEventMap.clear();
        }
    }


    public void sendMouseKey() {
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
            mouseMap.clear();
        }
    }


    void sendControllerInputContext() {
        // Cancel retransmissions of prior gamepad inputs
        handler.removeCallbacks(delayedRetransmitRunnable);

        sendControllerInputContextInternal();

        // HACK: GFE sometimes discards gamepad packets when they are received
        // very shortly after another. This can be critical if an axis zeroing packet
        // is lost and causes an analog stick to get stuck. To avoid this, we retransmit
        // the gamepad state a few times unless another input event happens before then.
        handler.postDelayed(delayedRetransmitRunnable, 25);
        handler.postDelayed(delayedRetransmitRunnable, 50);
        handler.postDelayed(delayedRetransmitRunnable, 75);
    }
}
