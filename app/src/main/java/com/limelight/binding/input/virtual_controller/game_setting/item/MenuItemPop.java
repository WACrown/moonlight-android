package com.limelight.binding.input.virtual_controller.game_setting.item;

import android.view.View;

import com.limelight.R;
import com.limelight.binding.input.virtual_controller.game_setting.GameSetting;

public abstract class MenuItemPop extends MenuItem{


    private GameSetting gameSetting;
    private OnMenuItemClickListener onMenuItemClickListener;

    public MenuItemPop(String name, GameSetting gameSetting) {
        super(name, gameSetting.getContext());
        this.gameSetting = gameSetting;

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPreAction();
                v.setBackgroundColor(gameSetting.getContext().getResources().getColor(R.color.game_setting_item_background_color_pressed));
                if (onMenuItemClickListener != null){
                    onMenuItemClickListener.onClick(v);
                }

            }
        });

    }

    @Override
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
        onMenuItemClickListener.onCreate(this);
    }

    public abstract void onClickPreAction();

    public void runReturnAction(){
        this.setBackgroundColor(gameSetting.getContext().getResources().getColor(R.color.game_setting_item_background_color_primary));
        if (onMenuItemClickListener != null){
            onMenuItemClickListener.returnAction(this);
        }
    }
}
