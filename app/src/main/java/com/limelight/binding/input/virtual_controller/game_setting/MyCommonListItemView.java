package com.limelight.binding.input.virtual_controller.game_setting;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.limelight.R;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

public class MyCommonListItemView extends QMUICommonListItemView {

    private View[] childViews;

    public void setStatusProcessor(StatusProcessor statusProcessor) {
        this.statusProcessor = statusProcessor;
    }

    private StatusProcessor statusProcessor;
    public interface StatusProcessor{
        void set(View[] childViews, String status);
        String get(View[] childViews);
    }

    public MyCommonListItemView(Context context) {
        super(context);
    }

    public void setStatus(String status){
        if (statusProcessor == null){
            return;
        }
        statusProcessor.set(childViews,status);
    }

    public String getStatus(){
        if (statusProcessor == null){
            return null;
        }
        return statusProcessor.get(childViews);
    }

    public View[] getChildViews() {
        return childViews;
    }

    public void setChildViews(View[] childViews) {
        this.childViews = childViews;
    }

    @Override
    public void setClickable(boolean clickable) {
        if (clickable){
            ((TextView)this.getChildAt(3)).setTextColor(getResources().getColor(com.qmuiteam.qmui.R.color.qmui_config_color_pure_black));
            ((TextView)this.getChildAt(4)).setTextColor(getResources().getColor(com.qmuiteam.qmui.R.color.qmui_config_color_50_pure_black));
        } else {
            ((TextView)this.getChildAt(3)).setTextColor(getResources().getColor(com.qmuiteam.qmui.R.color.qmui_config_color_25_pure_black));
            ((TextView)this.getChildAt(4)).setTextColor(getResources().getColor(com.qmuiteam.qmui.R.color.qmui_config_color_25_pure_black));
        }

        super.setClickable(clickable);
    }
}
