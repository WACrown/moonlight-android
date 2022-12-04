package com.limelight.binding.input.virtual_controller.selector;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.limelight.binding.input.virtual_controller.VirtualController;
import com.limelight.binding.input.virtual_controller.VirtualControllerConfigurationLoader;
import com.limelight.ui.AdapterSelector;
import com.limelight.utils.controller.LayoutAdminHelper;
import com.limelight.utils.controller.LayoutEditHelper;

public class VirtualControllerLayoutSelector extends Spinner{

    private Context context;
    private FrameLayout frame_layout;


    public VirtualControllerLayoutSelector(Context mContext, FrameLayout layout, VirtualController virtualController) {
        super(mContext);
        this.frame_layout = layout;
        this.context = mContext;
        VirtualControllerLayoutSelector virtualControllerLayoutSelector = this;
        this.setVisibility(View.INVISIBLE);
        this.setAdapter(new AdapterSelector(context,LayoutAdminHelper.getLayoutList(context)));
        this.setSelection(LayoutAdminHelper.getCurrentLayoutNum(getContext()));
        this.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //virtualController.removeElements(); //移除旧布局
                LayoutAdminHelper.selectLayout(context,i); //选定新布局
                virtualControllerLayoutSelector.setSelection(i);
                VirtualControllerConfigurationLoader.createButtons(virtualController,context, LayoutEditHelper.loadAllConf(context));  //创建新布局
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void refreshLayout(){
        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        int spinnerHigh = (int)(screen.heightPixels*0.1f);
        int spinnerWidth = (int)(screen.widthPixels*0.2f);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(spinnerWidth, spinnerHigh);
        params.leftMargin = (int)(screen.widthPixels*0.025f);
        params.topMargin = (int)(screen.heightPixels*0.1f);
        frame_layout.addView(this, params);
    }

}
