package com.limelight.binding.input.advance_setting;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.limelight.Game;
import com.limelight.R;
import com.limelight.binding.video.PerformanceInfo;

public class SimplifyPerformanceController extends Controller{

    private FrameLayout simplifyPerformanceLayout;
    private TextView bandWidthInfo;
    private TextView delayInfo;
    private TextView frameInfo;
    private TextView lostInfo;
    private Context context;
    private int visibility = View.GONE;
    
    public SimplifyPerformanceController(FrameLayout simplifyPerformanceLayout, ControllerManager controllerManager, Context context){
        this.context = context;
        this.simplifyPerformanceLayout = simplifyPerformanceLayout;
        bandWidthInfo = simplifyPerformanceLayout.findViewById(R.id.simplify_performance_bandwidth);
        delayInfo = simplifyPerformanceLayout.findViewById(R.id.simplify_performance_delay);
        frameInfo = simplifyPerformanceLayout.findViewById(R.id.simplify_performance_frame);
        lostInfo = simplifyPerformanceLayout.findViewById(R.id.simplify_performance_lost);

    }

    public void refreshSimplifyPerformance(PerformanceInfo performanceInfo){
        bandWidthInfo.setText("带宽: " + performanceInfo.bandWidth);
        delayInfo.setText("主机/网络/解码: " + String.format("%.0f", performanceInfo.aveHostProcessingLatency)
                + "/" + String.format("%d",(int)(performanceInfo.rttInfo >> 32))
                + "/" + String.format("%.0f",performanceInfo.decodeTimeMs)
                + " ms");
        frameInfo.setText("帧率: " + String.format("%.0f",performanceInfo.totalFps));
        lostInfo.setText("丢包: " + String.format("%.1f",performanceInfo.lostFrameRate) + " %");
    }

    public void open() {
        visibility = View.VISIBLE;
        simplifyPerformanceLayout.setVisibility(visibility);
        ((Game)context).getPrefConfig().enableSimplifyPerfOverlay = true;
    }

    public void close() {
        visibility = View.GONE;
        simplifyPerformanceLayout.setVisibility(visibility);
        ((Game)context).getPrefConfig().enableSimplifyPerfOverlay = false;
    }

    public void setOpacity(float opacity){
        simplifyPerformanceLayout.setAlpha(opacity);
    }
}
