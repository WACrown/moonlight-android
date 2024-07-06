package com.limelight.binding.input.advance_setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CombineKeyPreference {

    private static final String GAME_MENU_PREFERENCE_PREFIX = "game_menu_";
    private static final String GAME_MENU_SPECIAL_KEY_NAME_PREFIX = "special_key_";


    private SharedPreferences gameMenuPreference;
    private SharedPreferences.Editor gameMenuPreferenceEditor;

    public CombineKeyPreference(Context context){
        gameMenuPreference =  PreferenceManager.getDefaultSharedPreferences(context);
        gameMenuPreferenceEditor = gameMenuPreference.edit();



    }

    public void addCombineKey(CombineKeyBean combineKey){
        gameMenuPreferenceEditor.putString(GAME_MENU_SPECIAL_KEY_NAME_PREFIX + combineKey.getId(),new Gson().toJson(combineKey));
        gameMenuPreferenceEditor.apply();
    }

    public void deleteCombineKey(CombineKeyBean combineKey){
        gameMenuPreferenceEditor.remove(GAME_MENU_SPECIAL_KEY_NAME_PREFIX + combineKey.getId());
        gameMenuPreferenceEditor.apply();
    }

    public List<CombineKeyBean> loadCombineKeys(){
        new CombineKeyBean(
                null,
                null,
                null,
                null,
                0L
        );
        List<CombineKeyBean> list = new ArrayList<>();

        // 从SharePreference中获取Map<String, String>格式的Element信息
        Map<String, String> combineKeyStrings = (Map<String, String>) gameMenuPreference.getAll();
        // 将Map<String, String>格式转换为Map<String, KeyboardBean>
        for (Map.Entry<String, String> entry: combineKeyStrings.entrySet()){
            if (entry.getKey().matches("^special_key_.*")){
                CombineKeyBean combineKey = new Gson().fromJson(entry.getValue(), CombineKeyBean.class);
                // 转换完成后放到List<ElementBean>中
                list.add(combineKey);
            }
        }

        //对element根据创建时间进行排序
        Collections.sort(list, new Comparator<CombineKeyBean>() {
            @Override
            public int compare(CombineKeyBean o1, CombineKeyBean o2) {
                return Long.compare(o1.getCreateTime(), o2.getCreateTime());
            }
        });
        return list;
    }

    private String getGameMenuPreferenceName(String configId){
        return GAME_MENU_PREFERENCE_PREFIX + configId;
    }


}
