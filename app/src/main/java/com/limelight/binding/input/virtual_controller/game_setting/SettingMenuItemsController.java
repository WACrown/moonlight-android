package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.limelight.R;
import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;
import com.limelight.binding.input.virtual_controller.VirtualControllerElement;
import com.limelight.binding.input.virtual_controller.game_setting.item.MenuItem;
import com.limelight.binding.input.virtual_controller.game_setting.item.MenuItemCommonButton;
import com.limelight.binding.input.virtual_controller.game_setting.item.MenuItemFatherMenu;
import com.limelight.binding.input.virtual_controller.game_setting.item.MenuItemListSelect;
import com.limelight.utils.PressedStartElementInfo;
import com.limelight.utils.controller.LayoutAdminHelper;
import com.limelight.utils.controller.LayoutEditHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SettingMenuItemsController {

    private final static int TYPE_FATHER_MENU = 0;
    private final static int TYPE_COMMON_BUTTON = 1;
    private final static int TYPE_LIST_SELECT = 2;
    private final static String CONFIGURATION = "CONF";

    private final Context context;
    private final VirtualController virtualController;
    private final GameSetting gameSetting;


    private List<String> keyList;
    private List<String> funcList;
    private List<String> percentList;
    private List<String> layoutList;
    private List<String> selectedList;

    public Map<String, MenuItem> getItemViewMap() {
        return itemViewMap;
    }

    private final Map<String, MenuItem> itemViewMap = new HashMap<>();
    private final Map<VirtualControllerElement, PressedStartElementInfo> editElements = new HashMap<>();
    private final int screenWidth;
    private final int screenHeight;


    public SettingMenuItemsController(Context context, GameSetting gameSetting, VirtualController virtualController) {


        this.gameSetting = gameSetting;
        this.virtualController = virtualController;
        this.context = context;

        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        screenWidth = screen.widthPixels;
        screenHeight = screen.heightPixels;
        initList();
        initMenu(Arrays.asList(
                addMenuItem("back_to_stream",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_back_to_stream),null),
                addMenuItem("select_layout",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_layout),layoutList),
                addMenuItem("edit_controller",TYPE_FATHER_MENU,context.getResources().getString(R.string.game_setting_menu_item_edit),Arrays.asList(
                        addMenuItem("back",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_back),null),
                        addMenuItem("back_to_stream",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_back_to_stream),null),
                        addMenuItem("hide_menu",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_hide_menu),null),
                        addMenuItem("add_button",TYPE_FATHER_MENU,context.getResources().getString(R.string.game_setting_menu_item_edit_add_button),Arrays.asList(
                                addMenuItem("back",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_back),null),
                                addMenuItem("back_to_stream",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_back_to_stream),null),
                                addMenuItem("hide_menu",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_hide_menu),null),
                                addMenuItem("button_context",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_button),keyList),
                                addMenuItem("button_type",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_button_type),funcList),
                                addMenuItem("button_size",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_button_size), percentList),
                                addMenuItem("ensure_add_button",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_edit_add),null)
                        )),
                        addMenuItem("add_pad",TYPE_FATHER_MENU,context.getResources().getString(R.string.game_setting_menu_item_edit_add_pad),Arrays.asList(
                                addMenuItem("back",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_back),null),
                                addMenuItem("back_to_stream",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_back_to_stream),null),
                                addMenuItem("hide_menu",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_hide_menu),null),
                                addMenuItem("button_up",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_up),keyList),
                                addMenuItem("button_down",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_down),keyList),
                                addMenuItem("button_left",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_left),keyList),
                                addMenuItem("button_right",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_right),keyList),
                                addMenuItem("button_size",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_button_size), percentList),
                                addMenuItem("ensure_add_button",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_edit_add),null)
                        )),
                        addMenuItem("add_stick",TYPE_FATHER_MENU,context.getResources().getString(R.string.game_setting_menu_item_edit_add_stick),Arrays.asList(
                                addMenuItem("back",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_back),null),
                                addMenuItem("back_to_stream",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_back_to_stream),null),
                                addMenuItem("hide_menu",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_hide_menu),null),
                                addMenuItem("button_up",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_up),keyList),
                                addMenuItem("button_down",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_down),keyList),
                                addMenuItem("button_left",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_left),keyList),
                                addMenuItem("button_right",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_right),keyList),
                                addMenuItem("button_context",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_button),keyList),
                                addMenuItem("button_size",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_select_button_size), percentList),
                                addMenuItem("ensure_add_button",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_edit_add),null)
                        )),
                        addMenuItem("ensure_delete_button",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_edit_delete),null)
                )),
                addMenuItem("S_button_opacity",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_button_opacity),percentList),
//                addMenuItem("edit_mouse",TYPE_FATHER_MENU,"edit_mouse",Arrays.asList(
//                        addMenuItem("back",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_back),null),
//                        addMenuItem("back_to_stream",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_back_to_stream),null),
//                        addMenuItem("hide_menu",TYPE_COMMON_BUTTON,context.getResources().getString(R.string.game_setting_menu_item_hide_menu),null)
//                )),
                addMenuItem("hide_button",TYPE_LIST_SELECT,context.getResources().getString(R.string.game_setting_menu_item_hide_button),selectedList)
        ));


        bindOnClickListener();
    }


    private void initList(){
        keyList = Arrays.asList("K-A", "K-B", "K-C", "K-D", "K-E", "K-F", "K-G", "K-H", "K-I", "K-J", "K-K", "K-L", "K-M", "K-N", "K-O", "K-P", "K-Q", "K-R", "K-S", "K-T", "K-U", "K-V", "K-W", "K-X", "K-Y", "K-Z",
                "K-ESC","K-CTRLL" , "K-SHIFTL", "K-CTRLR" , "K-SHIFTR", "K-ALTL"  , "K-ALTR"  , "K-ENTER" , "K-KBACK"  , "K-SPACE" , "K-TAB"   , "K-CAPS"  , "K-WIN", "K-DEL", "K-INS", "K-HOME", "K-END", "K-PGUP", "K-PGDN", "K-BREAK", "K-SLCK", "K-PRINT", "K-UP", "K-DOWN", "K-LEFT", "K-RIGHT",
                "K-1", "K-2", "K-3", "K-4", "K-5", "K-6", "K-7", "K-8", "K-9", "K-0", "K-F1", "K-F2", "K-F3", "K-F4", "K-F5", "K-F6", "K-F7", "K-F8", "K-F9", "K-F10", "K-F11", "K-F12",
                "K-~", "K-_", "K-=", "K-[", "K-]", "K-\\", "K-;", "\"", "K-<", "K->", "K-/",
                "K-NUM1", "K-NUM2", "K-NUM3", "K-NUM4", "K-NUM5", "K-NUM6", "K-NUM7", "K-NUM8", "K-NUM9", "K-NUM0", "K-NUM.", "K-NUM+", "K-NUM_", "K-NUM*", "K-NUM/", "K-NUMENT", "K-NUMLCK",
                "G-GA", "G-GB", "G-GX", "G-GY", "G-PU","G-PD","G-PL","G-PR","G-LT", "G-RT", "G-LB", "G-RB", "G-LSB", "G-RSB", "G-START","G-BACK","G-LSU","G-LSD","G-LSL","G-LSR","G-RSU","G-RSD","G-RSL","G-RSR",
                "M-ML", "M-MR", "M-MM", "M-M1", "M-M2");
        funcList = Arrays.asList("COMMON");
        selectedList = Arrays.asList("TRUE","FALSE");
        percentList = new ArrayList<>();
        for (int i = 100;i > 0; i--){
            percentList.add(i + "%");
        }
        layoutList = LayoutAdminHelper.getLayoutList(context);


    }

    private void initMenu(List<MenuItem> menuItems){
        MenuItemFatherMenu beginMenu = new MenuItemFatherMenu("beginMenu",gameSetting,"beginMenu",menuItems);
        gameSetting.goToNextMenu(beginMenu);
    }

    private MenuItem addMenuItem(String name, int type, String text, List<?> items){
        if (itemViewMap.containsKey(name)){
            return itemViewMap.get(name);
        }
        MenuItem menuItem = null;
        switch (type){
            case TYPE_COMMON_BUTTON:
                menuItem = new MenuItemCommonButton(name,gameSetting,text);
                break;
            case TYPE_FATHER_MENU:
                menuItem = new MenuItemFatherMenu(name,gameSetting,text,(List<MenuItem>) items);
                break;
            case TYPE_LIST_SELECT:
                menuItem = new MenuItemListSelect(name,gameSetting,text,(List<String>) items);
                break;
        }
        itemViewMap.put(name,menuItem);
        return menuItem;
    }

    public void bindOnClickListener(){

        itemViewMap.get("back_to_stream").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public void onCreate(View v) {

            }

            @Override
            public void onClick(View v) {
                gameSetting.setVisibility(View.INVISIBLE);
                for (int i = gameSetting.getMenuQueue().size() - 1; i > 0; i--) {
                    gameSetting.backToPreviousMenu();
                }
                VirtualControllerConfigurationLoader.saveProfile(virtualController,context,itemViewMap);
            }

            @Override
            public void returnAction(View v) {

            }
        });

        itemViewMap.get("back").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public void onCreate(View v) {

            }

            @Override
            public void onClick(View v) {
                gameSetting.backToPreviousMenu();
            }

            @Override
            public void returnAction(View v) {

            }
        });

        itemViewMap.get("hide_menu").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public void onCreate(View v) {

            }

            @Override
            public void onClick(View v) {
                gameSetting.setVisibility(View.INVISIBLE);
            }

            @Override
            public void returnAction(View v) {

            }
        });

        itemViewMap.get("edit_controller").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {


            @Override
            public void onCreate(View v) {

                gameSetting.getGlassPanelEditor().setOnTouchListener(new View.OnTouchListener() {
                    private boolean isMove = false;
                    private boolean isOneFinger = false;
                    private int startFingerPressedPositionX = 0;
                    private int startFingerPressedPositionY = 0;
                    private int startTwoFingerDistanceX = 0;
                    private int startTwoFingerDistanceY = 0;
                    private int maxMoveLengthLeft;
                    private int maxMoveLengthUp;
                    private int maxMoveLengthRight;
                    private int maxMoveLengthDown;
                    private int maxIncreaseSizeX;
                    private int maxIncreaseSizeY;

                    private void setOneFingerStartInfo(MotionEvent event){
                        maxMoveLengthLeft = screenHeight;
                        maxMoveLengthUp = screenWidth;
                        maxMoveLengthRight = screenHeight;
                        maxMoveLengthDown = screenWidth;
                        startFingerPressedPositionX = (int) event.getX();
                        startFingerPressedPositionY = (int) event.getY();
                        for (VirtualControllerElement editElement : editElements.keySet()){
                            PressedStartElementInfo startInfo = editElements.get(editElement);
                            startInfo.startElementPositionX = (int) editElement.getX();
                            startInfo.startElementPositionY = (int) editElement.getY();
                            startInfo.startElementWidth = (int) editElement.getWidth();
                            startInfo.startElementHeight = (int) editElement.getHeight();
                            maxMoveLengthLeft = Math.min(startInfo.startElementPositionX, maxMoveLengthLeft);
                            maxMoveLengthUp = Math.min(startInfo.startElementPositionY, maxMoveLengthUp);
                            maxMoveLengthRight = Math.min(screenHeight - (startInfo.startElementWidth + startInfo.startElementPositionX), maxMoveLengthRight);
                            maxMoveLengthDown = Math.min(screenWidth - (startInfo.startElementHeight + startInfo.startElementPositionY), maxMoveLengthDown);
                            }

                    }

                    private void setTwoFingerStartInfo(MotionEvent event){
                        startFingerPressedPositionX = (int) event.getX();
                        startFingerPressedPositionY = (int) event.getY();
                        startTwoFingerDistanceX = Math.abs((int) event.getX(1) - (int) event.getX(0));
                        startTwoFingerDistanceY = Math.abs((int) event.getY(1) - (int) event.getY(0));
                        maxIncreaseSizeX = screenHeight;
                        maxIncreaseSizeY = screenWidth;
                        for (VirtualControllerElement editElement : editElements.keySet()){
                            PressedStartElementInfo startInfo = editElements.get(editElement);
                            startInfo.startElementPositionX = (int) editElement.getX();
                            startInfo.startElementPositionY = (int) editElement.getY();
                            startInfo.startElementWidth = (int) editElement.getWidth();
                            startInfo.startElementHeight = (int) editElement.getHeight();
                            startInfo.elementCenterPositionX = startInfo.startElementPositionX + startInfo.startElementWidth/2;
                            startInfo.elementCenterPositionY = startInfo.startElementPositionY + startInfo.startElementHeight/2;
                            maxIncreaseSizeX = Math.min(startInfo.startElementPositionX,maxIncreaseSizeX);
                            maxIncreaseSizeX = Math.min(screenHeight - (startInfo.startElementPositionX + startInfo.startElementWidth),maxIncreaseSizeX);
                            maxIncreaseSizeY = Math.min(startInfo.startElementPositionY,maxIncreaseSizeY);
                            maxIncreaseSizeY = Math.min(screenWidth - (startInfo.startElementPositionY + startInfo.startElementHeight),maxIncreaseSizeY);
                        }

                    }

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        //System.out.println("wangguan panel touch:" + event.getAction());


                        switch (event.getAction() & MotionEvent.ACTION_MASK){
                            case MotionEvent.ACTION_DOWN:
                                isMove = false;
                                isOneFinger = true;
                                setOneFingerStartInfo(event);
                                return true;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                isOneFinger = false;
                                setTwoFingerStartInfo(event);
                                return true;
                            case MotionEvent.ACTION_MOVE:
                                isMove = true;
                                if (isOneFinger){
                                    virtualController.moveElements(editElements,
                                            (int) event.getX() - startFingerPressedPositionX,
                                            (int) event.getY() - startFingerPressedPositionY,
                                            maxMoveLengthLeft,
                                            maxMoveLengthUp,
                                            maxMoveLengthRight,
                                            maxMoveLengthDown);
                                } else {
                                    virtualController.resizeElements(editElements,
                                            Math.abs(((int) event.getX(1) - (int) event.getX(0))) - startTwoFingerDistanceX,
                                            Math.abs(((int) event.getY(1) - (int) event.getY(0))) - startTwoFingerDistanceY,
                                            maxIncreaseSizeX,
                                            maxIncreaseSizeY);
                                }
                                return true;
                            case MotionEvent.ACTION_POINTER_UP:
                                isOneFinger = true;
                                setOneFingerStartInfo(event);
                                return true;
                            case MotionEvent.ACTION_UP:
                                if (!isMove) {
                                    List<VirtualControllerElement> elements = virtualController.getElements();
                                    for (int i = elements.size() - 1;i > -1;i--){
                                        VirtualControllerElement element = elements.get(i);
                                        int pressedX = (int) event.getX();
                                        int pressedY = (int) event.getY();
                                        int elementStartX = (int) element.getX();
                                        int elementStartY = (int) element.getY();
                                        int elementEndX = elementStartX + element.getWidth();
                                        int elementEndY = elementStartY + element.getHeight();
                                        if (((pressedX > elementStartX) && (pressedX < elementEndX)) && ((pressedY > elementStartY) && (pressedY < elementEndY))){
                                            System.out.println("wangguan " + element.getElementId());
                                            if (true){
                                                singleEditMode(element);
                                            }else {
                                                multipleEditMode(element);
                                            }
                                            break;


                                        }
                                    }
                                }
                                return true;
                        }


                        return true;
                    }

                    private void singleEditMode(VirtualControllerElement element){
                        boolean previousStatus = element.getSelectedStatus();
                        for (VirtualControllerElement editElement : editElements.keySet()){
                            virtualController.setSelectedElement(editElement,false);
                        }
                        editElements.clear();
                        if (!previousStatus){
                            virtualController.setSelectedElement(element,true);
                            editElements.put(element,new PressedStartElementInfo());
                        }

                    }

                    private void multipleEditMode(VirtualControllerElement element){

                        if (element.getSelectedStatus()){
                            virtualController.setSelectedElement(element,false);
                            editElements.remove(element);
                        } else {
                            virtualController.setSelectedElement(element,true);
                            editElements.put(element,new PressedStartElementInfo());
                        }

                    }

                });
            }

            @Override
            public void onClick(View v) {
                gameSetting.getGlassPanelEditor().setVisibility(View.VISIBLE);
                virtualController.setCurrentMode(VirtualController.ControllerMode.EditButtons);
            }

            @Override
            public void returnAction(View v) {
                gameSetting.getGlassPanelEditor().setVisibility(View.INVISIBLE);
                virtualController.setCurrentMode(VirtualController.ControllerMode.Active);
            }
        });

        itemViewMap.get("ensure_delete_button").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public void onCreate(View v) {

            }

            @Override
            public void onClick(View v) {
                virtualController.removeElements(editElements.keySet());
                editElements.clear();
            }

            @Override
            public void returnAction(View v) {

            }
        });

        itemViewMap.get("ensure_add_button").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public void onCreate(View v) {

            }

            @Override
            public void onClick(View v) {
                String buttonNamePre = "";
                List<MenuItemFatherMenu> menuQueue = gameSetting.getMenuQueue();
                switch (menuQueue.get(menuQueue.size() - 1).getName()) {

                    case "add_button":
                        buttonNamePre = "BUTTON-" +
                                ((MenuItemListSelect) itemViewMap.get("button_type")).getDynamicText() + "-" +
                                ((MenuItemListSelect) itemViewMap.get("button_context")).getDynamicText() + "-";

                        break;
                    case "add_pad":
                        buttonNamePre = "PAD-" +
                                ((MenuItemListSelect) itemViewMap.get("button_up")).getDynamicText() + "-" +
                                ((MenuItemListSelect) itemViewMap.get("button_down")).getDynamicText() + "-" +
                                ((MenuItemListSelect) itemViewMap.get("button_left")).getDynamicText() + "-" +
                                ((MenuItemListSelect) itemViewMap.get("button_right")).getDynamicText() + "-";

                        break;
                    case "add_stick":
                        buttonNamePre = "STICK-" +
                                ((MenuItemListSelect) itemViewMap.get("button_up")).getDynamicText() + "-" +
                                ((MenuItemListSelect) itemViewMap.get("button_down")).getDynamicText() + "-" +
                                ((MenuItemListSelect) itemViewMap.get("button_left")).getDynamicText() + "-" +
                                ((MenuItemListSelect) itemViewMap.get("button_right")).getDynamicText() + "-" +
                                ((MenuItemListSelect) itemViewMap.get("button_context")).getDynamicText() + "-";
                        break;
                }

                Set<String> allButtonName = new HashSet<>();

                for (VirtualControllerElement element : virtualController.getElements()){
                    allButtonName.add(element.getElementId());
                }

                for (int i = 0;i < 100;i ++){
                    String buttonName = buttonNamePre + i;
                    if (allButtonName.contains(buttonName)) {
                        continue;
                    }
                    String buttonSizeSelected = ((MenuItemListSelect) itemViewMap.get("button_size")).getDynamicText();
                    int buttonSize = (int) ((screenWidth - 40) * Integer.parseInt(buttonSizeSelected.substring(0,buttonSizeSelected.length() - 1)) * 0.01);
                    int left = screenHeight/2 - buttonSize/2;
                    int top = screenWidth/2 - buttonSize/2;
                    String buttonSizeString = "{\"LEFT\":" + left + ",\"TOP\":" + top + ",\"WIDTH\":" + buttonSize + ",\"HEIGHT\":" + buttonSize + "}";
                    Map<String, String> newButton = new HashMap<>();
                    newButton.put(buttonName,buttonSizeString);
                    VirtualControllerConfigurationLoader.createButtons(virtualController,context,newButton);
                    System.out.println("wangguan newButton:" + newButton);
                    break;

                }
                Toast.makeText(context,"已添加",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void returnAction(View v) {

            }
        });

        itemViewMap.get("S_button_opacity").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public void onCreate(View v) {

            }

            @Override
            public void onClick(View v) {
            }

            @Override
            public void returnAction(View v) {
                String dynamicText = ((MenuItemListSelect) v).getDynamicText();
                int opacity = Integer.parseInt(dynamicText.substring(0, dynamicText.length() - 1));
                virtualController.setOpacity(opacity);
            }
        });


        itemViewMap.get("select_layout").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public void onCreate(View v) {
                ((MenuItemListSelect)v).setDynamicText(LayoutAdminHelper.getCurrentLayoutName(context));
            }

            @Override
            public void onClick(View v) {

            }

            @Override
            public void returnAction(View v) {
                virtualController.removeAllElements(); //移除旧布局
                LayoutAdminHelper.selectLayout(context,((MenuItemListSelect)v).getDynamicText()); //选定新布局
                VirtualControllerConfigurationLoader.loadProfile(virtualController,context,itemViewMap);  //创建新布局
            }
        });

        itemViewMap.get("hide_button").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public void onCreate(View v) {

            }

            @Override
            public void onClick(View v) {

            }

            @Override
            public void returnAction(View v) {
                virtualController.hideButton(((MenuItemListSelect)v).getDynamicText());
            }
        });

    }
}
