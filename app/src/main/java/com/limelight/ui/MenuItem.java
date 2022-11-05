package com.limelight.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.limelight.R;

public abstract class MenuItem extends TextView {

    public static final int TYPE_COMMON_BUTTON = 0;
    public static final int TYPE_FATHER_MENU = 1;
    public static final int TYPE_LIST_SELECT = 2;


    public String getName() {
        return name;
    }

    private final String name;
    public interface OnClickAndBackListener{

        void onCreate();
        void onClick(View v);
        void callback();
    }

    private OnClickAndBackListener onClickAndBackListener;

    public MenuItem(String name,Context context) {
        super(context);
        this.name = name;
        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100));
        this.setTextColor(context.getResources().getColor(R.color.game_setting_item_text_color));
        this.setTextSize(15);
        this.setPadding(4,0,0,0);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setBackgroundColor(context.getResources().getColor(R.color.game_setting_item_background_color_primary));
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPreAction();
                if (onClickAndBackListener != null){
                    onClickAndBackListener.onClick(v);
                }

            }
        });
    }

    public abstract void onClickPreAction();

    public void setOnClickAndBackListener(OnClickAndBackListener listener){
        this.onClickAndBackListener = listener;
        listener.onCreate();
    }

    public void runCallback(){
        if (onClickAndBackListener != null){
            onClickAndBackListener.callback();
        }

    }

}
