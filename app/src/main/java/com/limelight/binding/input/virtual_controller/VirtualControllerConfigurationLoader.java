/**
 * Created by Karim Mreisi.
 */

package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.util.DisplayMetrics;

import com.limelight.binding.input.virtual_controller.game_setting.MyCommonListItemView;
import com.limelight.binding.input.virtual_controller.game_setting.MySection;
import com.limelight.utils.DBG;
import com.limelight.utils.controller.LayoutEditHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VirtualControllerConfigurationLoader {


    private static GeneralButton createGeneralButton(
            final String elementId,
            final String[] keyInfo,
            final int layer,
            final int icon,
            final VirtualController controller,
            final Context context) {
        GeneralButton generalButton = new GeneralButton(controller,elementId,keyInfo,null,layer,context);
        generalButton.setText(keyInfo[3]);
        generalButton.setIcon(icon);
        return generalButton;
    }

    private static GeneralAnalogStick createGeneralAnalogStick(
            final String elementId,
            final String[] keyInfo,
            final VirtualController controller,
            final Context context) {
        GeneralAnalogStick generalAnalogStick = new GeneralAnalogStick(controller,elementId,context,keyInfo);
        return generalAnalogStick;
    }

    private static GeneralPad createGeneralPad(
            final String elementId,
            final String[] keyInfo,
            final VirtualController controller,
            final Context context) {
        GeneralPad generalPad = new GeneralPad(controller,elementId,context,keyInfo);
        return generalPad;
    }



    public static void loadConfiguration(Context context,QMUIGroupListView groupListView){
        Map<String, String> allProfile = LayoutEditHelper.loadAllConf(context);
        for (String keyName : allProfile.keySet()) {

            if (keyName.startsWith("S_")){
                String noPreKeyName = keyName.substring(2);
                String[] indexes = noPreKeyName.split("-");
                //获取item对象
                ArrayList<MyCommonListItemView> myCommonListItemViews = ((MySection) groupListView.getSection(Integer.parseInt(indexes[0]))).getMyCommonListItemViews();
                MyCommonListItemView myCommonListItemView = myCommonListItemViews.get(Integer.parseInt(indexes[1]));

                myCommonListItemView.setStatus(allProfile.get(keyName));
            }
        }

    }

    public static void loadButtons(final VirtualController controller, final Context context){
        controller.removeAllElements();
        DisplayMetrics screen = context.getResources().getDisplayMetrics();

        // Displace controls on the right by this amount of pixels to account for different aspect ratios
        int rightDisplacement = screen.widthPixels - screen.heightPixels * 16 / 9;

        // NOTE: Some of these getPercent() expressions seem like they can be combined
        // into a single call. Due to floating point rounding, this isn't actually possible.

        Map<String, String> allProfile = LayoutEditHelper.loadAllConf(context);
        for (String keyName : allProfile.keySet()) {
            if (!keyName.startsWith("S_")){
                String[] keyInfo = keyName.split("-");

                int leftMargin = 0;
                int topMargin = 0;
                int width = 0;
                int height = 0;
                try {
                    JSONObject buttonInfo = new JSONObject(allProfile.get(keyName));
                    leftMargin = buttonInfo.getInt("LEFT");
                    topMargin = buttonInfo.getInt("TOP");
                    width = buttonInfo.getInt("WIDTH");
                    height = buttonInfo.getInt("HEIGHT");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                switch (keyInfo[0]){
                    case "BUTTON" : {
                        controller.addElement(
                                createGeneralButton(keyName,keyInfo,1,-1,controller,context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "PAD" : {
                        controller.addElement(
                                createGeneralPad(keyName,keyInfo,controller,context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                    case "STICK" : {
                        controller.addElement(
                                createGeneralAnalogStick(keyName,keyInfo,controller,context),
                                leftMargin,
                                topMargin,
                                width,
                                height
                        );
                        break;
                    }
                }

            }
        }


    }

    public static void createButton(final VirtualController controller, final Context context,Map<String, String> keyInfoMap){
        DisplayMetrics screen = context.getResources().getDisplayMetrics();

        // Displace controls on the right by this amount of pixels to account for different aspect ratios
        int rightDisplacement = screen.widthPixels - screen.heightPixels * 16 / 9;

        // NOTE: Some of these getPercent() expressions seem like they can be combined
        // into a single call. Due to floating point rounding, this isn't actually possible.

        for (String keyName : keyInfoMap.keySet()){
            String[] keyInfo = keyName.split("-");

            int leftMargin = 0;
            int topMargin = 0;
            int width = 0;
            int height = 0;
            try {
                JSONObject buttonInfo = new JSONObject((String) keyInfoMap.get(keyName));
                leftMargin = buttonInfo.getInt("LEFT");
                topMargin = buttonInfo.getInt("TOP");
                width = buttonInfo.getInt("WIDTH");
                height = buttonInfo.getInt("HEIGHT");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (keyInfo[0]){
                case "BUTTON" : {
                    controller.addElement(
                            createGeneralButton(keyName,keyInfo,1,-1,controller,context),
                            leftMargin,
                            topMargin,
                            width,
                            height
                    );
                    break;
                }
                case "PAD" : {
                    controller.addElement(
                            createGeneralPad(keyName,keyInfo,controller,context),
                            leftMargin,
                            topMargin,
                            width,
                            height
                    );
                    break;
                }
                case "STICK" : {
                    controller.addElement(
                            createGeneralAnalogStick(keyName,keyInfo,controller,context),
                            leftMargin,
                            topMargin,
                            width,
                            height
                    );
                    break;
                }
            }

        }


    }


    public static void saveProfile(final VirtualController controller,
                                   final Context context, QMUIGroupListView groupListView) {

        Map<String, String> elementConfigurationsMap = new HashMap<>();
        for (VirtualControllerElement element : controller.getElements()) {
            String prefKey = ""+element.getElementId();
            try {
                elementConfigurationsMap.put(prefKey, element.getConfiguration().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        /*数据保存格式
           |设置数据                                                  |
           |key          |value                                     |
           |--------------------------------------------------------|
           |S_1-1        |50;true;                                  |
   （解释）  |菜单组-菜单项  |数据值;数据值(值对应哪个view由item自己确定)      |

           |按键数据                                                                               |
           |key                                |value                                            |
           |-------------------------------------------------------------------------------------|
           |BUTTON-COMMON-K-A-1                |{"LEFT":100,"TOP":100,"WIDTH":100,"HEIGHT":100}  |
   （解释）  |按键类型-操作方式-键盘-A键-索引         |位置大小信息                                        |
           |STICK-UP-DOWN-LEFT-RIGHT-CENTER-1  |{"LEFT":100,"TOP":100,"WIDTH":100,"HEIGHT":100}  |
   （解释）  |摇杆类型-上-下-左-右-中-索引           |位置大小信息                                        |
           |PAD-UP-DOWN-LEFT-RIGHT-1           |{"LEFT":100,"TOP":100,"WIDTH":100,"HEIGHT":100}  |
   （解释）  |十字键-上-下-左-右-索引                |位置大小信息                                       |
        */

        for (int i = 0;i < groupListView.getSectionCount(); i++){
            ArrayList<MyCommonListItemView> myCommonListItemViews = ((MySection) groupListView.getSection(i)).getMyCommonListItemViews();
            for (int j = 0; j < myCommonListItemViews.size(); j ++){
                String key = "S_" + i + "-" + j;
                String value = myCommonListItemViews.get(j).getStatus();
                if (value == null){
                    continue;
                }
                elementConfigurationsMap.put(key,value);
            }
        }

        System.out.println("wangguan" + elementConfigurationsMap);
        LayoutEditHelper.storeAllConf(context,elementConfigurationsMap);

    }




}
