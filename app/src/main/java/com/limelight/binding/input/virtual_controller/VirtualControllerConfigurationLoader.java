/**
 * Created by Karim Mreisi.
 */

package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.util.DisplayMetrics;

import com.limelight.binding.input.virtual_controller.game_setting.MyCommonListItemView;
import com.limelight.binding.input.virtual_controller.game_setting.MySection;
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


    public static void loadProfile(final VirtualController controller, final Context context, QMUIGroupListView groupListView){
        controller.removeAllElements();
        Map<String, String> allProfile = LayoutEditHelper.loadAllConf(context);
        System.out.println("wangguan allProfile:" + allProfile.toString());
        Map<String, String> allButton = new HashMap<>();
        Map<String, String> allConfiguration = new HashMap<>();
        for (String keyName : allProfile.keySet()) {

            if (keyName.startsWith("S_")){
                allConfiguration.put(keyName.substring(2),allProfile.get(keyName));
            } else {
                allButton.put(keyName,allProfile.get(keyName));
            }

        }
        createButtons(controller,context,allButton);
        loadConfiguration(allConfiguration,groupListView);
    }


    private static void loadConfiguration(Map<String, String> allConfiguration,QMUIGroupListView groupListView){
        for (String keyName : allConfiguration.keySet()){
            String[] indexes = keyName.split("-");
            //获取item对象
            ArrayList<MyCommonListItemView> myCommonListItemViews = ((MySection) groupListView.getSection(Integer.parseInt(indexes[0]))).getMyCommonListItemViews();
            MyCommonListItemView myCommonListItemView = myCommonListItemViews.get(Integer.parseInt(indexes[1]));

            myCommonListItemView.setStatus(allConfiguration.get(keyName));
        }

    }



    public static void createButtons(final VirtualController controller, final Context context,Map<String, String> keyInfoMap){

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
        //S_1-1  :  2-50;3-true;
        //section-item  :  childView-value;childView-value
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
