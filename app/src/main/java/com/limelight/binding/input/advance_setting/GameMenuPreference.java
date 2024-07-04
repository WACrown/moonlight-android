package com.limelight.binding.input.advance_setting;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GameMenuPreference {

    private static final String GAME_MENU_PREFERENCE_PREFIX = "game_menu_";
    private static final String GAME_MENU_SPECIAL_KEY_NAME_PREFIX = "special_key_";


    private SharedPreferences gameMenuPreference;
    private SharedPreferences.Editor gameMenuPreferenceEditor;

    public GameMenuPreference(String configId,Context context){
        gameMenuPreference = context.getSharedPreferences(getGameMenuPreferenceName(configId),Context.MODE_PRIVATE);
        gameMenuPreferenceEditor = gameMenuPreference.edit();



    }

    public void addGameMenuSpecialKey(GameMenuSpecialKeyBean gameMenuSpecialKey){
        gameMenuPreferenceEditor.putString(GAME_MENU_SPECIAL_KEY_NAME_PREFIX + gameMenuSpecialKey.getId(),new Gson().toJson(gameMenuSpecialKey));
        gameMenuPreferenceEditor.apply();
    }

    public void deleteGameMenuSpecialKey(GameMenuSpecialKeyBean gameMenuSpecialKey){
        gameMenuPreferenceEditor.remove(GAME_MENU_SPECIAL_KEY_NAME_PREFIX + gameMenuSpecialKey.getId());
        gameMenuPreferenceEditor.apply();
    }

    public List<GameMenuSpecialKeyBean> loadGameMenuSpecialKeys(){
        new GameMenuSpecialKeyBean(
                null,
                null,
                null,
                null,
                0L
        );
        List<GameMenuSpecialKeyBean> list = new ArrayList<>();

        // 从SharePreference中获取Map<String, String>格式的Element信息
        Map<String, String> gameMenuSpecialKeyStrings = (Map<String, String>) gameMenuPreference.getAll();
        // 将Map<String, String>格式转换为Map<String, KeyboardBean>
        for (Map.Entry<String, String> entry: gameMenuSpecialKeyStrings.entrySet()){
            GameMenuSpecialKeyBean gameMenuSpecialKey = new Gson().fromJson(entry.getValue(),GameMenuSpecialKeyBean.class);
            // 转换完成后放到List<ElementBean>中
            list.add(gameMenuSpecialKey);
        }

        //对element根据创建时间进行排序
        Collections.sort(list, new Comparator<GameMenuSpecialKeyBean>() {
            @Override
            public int compare(GameMenuSpecialKeyBean o1, GameMenuSpecialKeyBean o2) {
                return Long.compare(o1.getCreateTime(), o2.getCreateTime());
            }
        });
        return list;
    }

    private String getGameMenuPreferenceName(String configId){
        return GAME_MENU_PREFERENCE_PREFIX + configId;
    }


}
