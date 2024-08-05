package com.limelight.binding.input.advance_setting.element;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.limelight.Game;
import com.limelight.R;
import com.limelight.binding.input.ControllerHandler;
import com.limelight.binding.input.advance_setting.ControllerManager;
import com.limelight.binding.input.advance_setting.sqlite.SuperConfigDatabaseHelper;
import com.limelight.binding.input.advance_setting.superpage.SuperPageLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementController {




    public interface SendEventHandler {
        void sendEvent(boolean down);
        void sendEvent(int analog1, int analog2);
    }


    public enum Mode{
        Normal,
        Edit
    }

    public static class GamepadInputContext {
        public short inputMap = 0x0000;
        public byte leftTrigger = 0x00;
        public byte rightTrigger = 0x00;
        public short rightStickX = 0x0000;
        public short rightStickY = 0x0000;
        public short leftStickX = 0x0000;
        public short leftStickY = 0x0000;
    }


    private final Context context;
    private final Game game;
    private final Handler handler;

    private final ControllerManager controllerManager;
    private final ControllerHandler controllerHandler;

    private GamepadInputContext gamepadInputContext = new GamepadInputContext();


    private final List<Element> elements = new ArrayList<>();
    private List<Long> elementIds;
    private Map<Short, Runnable> keyEventRunnableMap = new HashMap<>();
    private Map<Integer, Runnable> mouseEventRunnableMap = new HashMap<>();
    private FrameLayout elementsLayout;
    private Mode mode = Mode.Normal;
    private SuperPageLayout pageEdit;
    private SuperPageLayout lastElementSettingPage;



    public ElementController(ControllerManager controllerManager, FrameLayout layout, final Context context) {
        this.elementsLayout = layout;
        this.context = context;
        this.game = (Game) context;
        this.controllerManager = controllerManager;
        this.controllerHandler = game.getControllerHandler();
        this.handler = new Handler(Looper.getMainLooper());
        this.pageEdit = (SuperPageLayout) LayoutInflater.from(context).inflate(R.layout.page_edit,null);

        initEditPage();
    }

    private void initEditPage(){
        pageEdit.findViewById(R.id.page_edit_exit_edit_mode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerManager.getPageSuperMenuController().exitElementEditMode();
                controllerManager.getTouchController().enableTouch(true);
                mode = Mode.Normal;
                for (Element element : elements){
                    element.invalidate();
                }
            }
        });
        pageEdit.findViewById(R.id.page_edit_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerManager.getElementController().addElement(Element.ELEMENT_TYPE_DIGITAL_BUTTON);
            }
        });
    }


    protected Handler getHandler() {
        return handler;
    }

    protected SuperConfigDatabaseHelper getSuperConfigDatabaseHelper() {
        return controllerManager.getSuperConfigDatabaseHelper();
    }

    public void loadAllElement(Long configId){
        removeAllElementsFromScreen();
        elementIds = controllerManager.getSuperConfigDatabaseHelper().queryAllElementIds(configId);
        for (Long elementId : elementIds){
            Map<String, Object> attributesMap =  controllerManager.getSuperConfigDatabaseHelper().queryAllElementAttributes(elementId);
            int type = ((Long) attributesMap.get(Element.COLUMN_INT_ELEMENT_TYPE)).intValue();
            Element element = null;
            switch (type){
                case Element.ELEMENT_TYPE_DIGITAL_BUTTON:
                    element = new DigitalButton(attributesMap,
                            this,
                            controllerManager.getTouchController(),
                            controllerManager.getPageDeviceController(),
                            context);
                    break;
                case Element.ELEMENT_TYPE_DIGITAL_DIGITAL_PAD:

                    break;
                case Element.ELEMENT_TYPE_ANALOG_STICK:

                    break;
            }
            elements.add(element);
            int elementWidth = ((Long) attributesMap.get(Element.COLUMN_INT_ELEMENT_WIDTH)).intValue();
            int elementHeight = ((Long) attributesMap.get(Element.COLUMN_INT_ELEMENT_HEIGHT)).intValue();
            int elementCentralX = ((Long) attributesMap.get( Element.COLUMN_INT_ELEMENT_CENTRAL_X)).intValue();
            int elementCentralY = ((Long) attributesMap.get( Element.COLUMN_INT_ELEMENT_CENTRAL_Y)).intValue();
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(elementWidth, elementHeight);
            layoutParams.leftMargin = elementCentralX - elementWidth / 2;
            layoutParams.topMargin = elementCentralY - elementHeight / 2;
            elementsLayout.addView(element,elementsLayout.getChildCount() - 1,layoutParams);
        }
    }

    private void addElement(int elementType){
        switch (elementType){
            case Element.ELEMENT_TYPE_DIGITAL_BUTTON:
                Long configId = controllerManager.getPageConfigController().getCurrentConfigId();
                // save a new element to sqlite
                ContentValues contentValues = DigitalButton.getInitialInfo();
                //ContentValues contentValues = new ContentValues();
                contentValues.put(Element.COLUMN_LONG_CONFIG_ID,configId);
                controllerManager.getSuperConfigDatabaseHelper().insertElement(contentValues);
                // add the element to screen
                loadAllElement(configId);
                break;
            case Element.ELEMENT_TYPE_DIGITAL_DIGITAL_PAD:

                break;
            case Element.ELEMENT_TYPE_ANALOG_STICK:

                break;
        }
    }

    protected void deleteElement(Element element){
        controllerManager.getSuperConfigDatabaseHelper().deleteElement(element.elementId);
        Long configId = controllerManager.getPageConfigController().getCurrentConfigId();
        loadAllElement(configId);
    }

    protected void copyElement(ContentValues contentValues){
        Long configId = controllerManager.getPageConfigController().getCurrentConfigId();
        contentValues.put(Element.COLUMN_LONG_CONFIG_ID,configId);
        controllerManager.getSuperConfigDatabaseHelper().insertElement(contentValues);
        // add the element to screen
        loadAllElement(configId);
    }


    public void toggleInfoPage(SuperPageLayout elementSettingPage){
        if (controllerManager.getSuperPagesController().getLastPage() == lastElementSettingPage && lastElementSettingPage != null){
            controllerManager.getSuperPagesController().close();
            if (elementSettingPage != lastElementSettingPage){
                controllerManager.getSuperPagesController().open(elementSettingPage);
                lastElementSettingPage = elementSettingPage;
            }
        } else {
            controllerManager.getSuperPagesController().open(elementSettingPage);
            lastElementSettingPage = elementSettingPage;
        }
    }

    public SuperPageLayout getPageEdit() {
        return pageEdit;
    }

    public void entryEditMode(){

        controllerManager.getTouchController().enableTouch(false);
        mode = Mode.Edit;
        for (Element element : elements){
            element.invalidate();
        }

    }

    public Mode getMode() {
        return mode;
    }

    //其他辅助方法----------------------------------
    public List<Element> getElements() {
        return elements;
    }
    public void removeAllElementsFromScreen() {
        for (Element element : elements) {
            elementsLayout.removeView(element);
        }
        elements.clear();
    }

    public int getElementsParentWidth(){
        return elementsLayout.getWidth();
    }

    public int getElementsParentHeight(){
        return elementsLayout.getHeight();
    }

    public SendEventHandler getSendEventHandler(String key){
        if (key.matches("k\\d+")){

            int keyCode = Integer.parseInt(key.substring(1));
            return new SendEventHandler() {
                @Override
                public void sendEvent(boolean down) {
                    sendKeyEvent(down,(short) keyCode);
                }

                @Override
                public void sendEvent(int analog1, int analog2) {

                }
            };

        } else if (key.matches("m\\d+")){
            int mouseCode = Integer.parseInt(key.substring(1));
            return new SendEventHandler() {
                @Override
                public void sendEvent(boolean down) {
                    sendMouseEvent(mouseCode,down);
                }

                @Override
                public void sendEvent(int analog1, int analog2) {

                }
            };

        } else if (key.matches("g\\d+")){
            int padCode = Integer.parseInt(key.substring(1));
            return new SendEventHandler() {
                @Override
                public void sendEvent(boolean down) {
                    if (down) {
                        gamepadInputContext.inputMap |= padCode;
                    } else {
                        gamepadInputContext.inputMap &= ~padCode;
                    }
                    sendGamepadEvent();
                }

                @Override
                public void sendEvent(int analog1, int analog2) {

                }
            };

        } else if (key.equals("LS")){
            return new SendEventHandler() {
                @Override
                public void sendEvent(boolean down) {

                }

                @Override
                public void sendEvent(int analog1, int analog2) {
                    gamepadInputContext.leftStickX = (short) analog1;
                    gamepadInputContext.leftStickY = (short) analog2;
                    sendGamepadEvent();
                }
            };
        } else if (key.equals("RS")){
            return new SendEventHandler() {
                @Override
                public void sendEvent(boolean down) {

                }

                @Override
                public void sendEvent(int analog1, int analog2) {
                    gamepadInputContext.rightStickX = (short) analog1;
                    gamepadInputContext.rightStickY = (short) analog2;
                    sendGamepadEvent();
                }
            };
        } else if (key.equals("lt")){
            return new SendEventHandler() {
                @Override
                public void sendEvent(boolean down) {
                    if (down) {
                        gamepadInputContext.leftTrigger = (byte) 0xFF;
                    } else {
                        gamepadInputContext.leftTrigger = (byte) 0;
                    }
                    sendGamepadEvent();
                }

                @Override
                public void sendEvent(int analog1, int analog2) {

                }
            };
        } else if (key.equals("rt")){
            return new SendEventHandler() {
                @Override
                public void sendEvent(boolean down) {
                    if (down) {
                        gamepadInputContext.rightTrigger = (byte) 0xFF;
                    } else {
                        gamepadInputContext.rightTrigger = (byte) 0;
                    }
                    sendGamepadEvent();
                }

                @Override
                public void sendEvent(int analog1, int analog2) {

                }
            };
        }
        return null;
    }



    public void sendKeyEvent(boolean buttonDown, short keyCode) {
        game.keyboardEvent(buttonDown,keyCode);
        //如果map中有对应按键的runnable，则删除该按键的runnable。
        if (keyEventRunnableMap.containsKey(keyCode)){
            handler.removeCallbacks(keyEventRunnableMap.get(keyCode));
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                game.keyboardEvent(buttonDown,keyCode);
            }
        };
        //把这个按键的runnable放到map中，以便这个按键重新发送的时候，重置runnable。
        keyEventRunnableMap.put(keyCode,runnable);


        handler.postDelayed(runnable, 50);
        handler.postDelayed(runnable, 75);
    }
    public void sendMouseEvent(int mouseId, boolean down){
        game.mouseButtonEvent(mouseId, down);
        if (mouseEventRunnableMap.containsKey(mouseId)){
            handler.removeCallbacks(mouseEventRunnableMap.get(mouseId));
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                game.mouseButtonEvent(mouseId, down);
            }
        };
        //把这个按键的runnable放到map中，以便这个按键重新发送的时候，重置runnable。
        mouseEventRunnableMap.put(mouseId,runnable);

        handler.postDelayed(runnable, 50);
        handler.postDelayed(runnable, 75);
    }

    public void sendGamepadEvent(){
        controllerHandler.reportOscState(
                gamepadInputContext.inputMap,
                gamepadInputContext.leftStickX,
                gamepadInputContext.leftStickY,
                gamepadInputContext.rightStickX,
                gamepadInputContext.rightStickY,
                gamepadInputContext.leftTrigger,
                gamepadInputContext.rightTrigger
        );

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                controllerHandler.reportOscState(
                        gamepadInputContext.inputMap,
                        gamepadInputContext.leftStickX,
                        gamepadInputContext.leftStickY,
                        gamepadInputContext.rightStickX,
                        gamepadInputContext.rightStickY,
                        gamepadInputContext.leftTrigger,
                        gamepadInputContext.rightTrigger
                );
            }
        };
        handler.postDelayed(runnable, 50);
        handler.postDelayed(runnable, 75);


    }
}


