/**
 * Created by Karim Mreisi.
 */

package com.limelight.binding.input.virtual_controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class VirtualControllerElement extends View {
    protected static boolean _PRINT_DEBUG_INFORMATION = true;


    protected VirtualController virtualController;

    public String getElementId() {
        return elementId;
    }

    private final String elementId;

    private final Paint paint = new Paint();

    private int normalColor = 0xF0888888;
    protected int pressedColor = 0xF00000FF;
    private int configEditNormalColor = 0xFF03A9F4;
    private int configEditSelectedColor = 0xF0FF0000;
    private int configEditColor = configEditNormalColor;
    private int configSelectedColor = 0xF000FF00;

    public boolean getSelectedStatus() {
        return selectedStatus;
    }

    protected void setSelectedStatus(boolean selectedStatus) {
        if (selectedStatus){
            configEditColor = configEditSelectedColor;
        } else {
            configEditColor = configEditNormalColor;
        }
        this.selectedStatus = selectedStatus;
    }

    private boolean selectedStatus = false;



    protected VirtualControllerElement(VirtualController controller, Context context, String elementId) {
        super(context);
        this.virtualController = controller;
        this.elementId = elementId;
    }

    public void moveElement(int moveLengthX, int moveLengthY, int startPositionX, int startPositionY, int maxMoveLengthLeft, int maxMoveLengthUp, int maxMoveLengthRight, int maxMoveLengthDown) {

        if (moveLengthX < -maxMoveLengthLeft){
            moveLengthX = -maxMoveLengthLeft;
        } else if (moveLengthX > maxMoveLengthRight){
            moveLengthX = maxMoveLengthRight;
        }

        if (moveLengthY < -maxMoveLengthUp){
            moveLengthY = -maxMoveLengthUp;
        } else if (moveLengthY > maxMoveLengthDown){
            moveLengthY = maxMoveLengthDown;
        }

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();


        //System.out.println("wangguan x:" + newPos_x + " y:" + newPos_y + " screenWidth:" + screenWidth + " screenHeight:" + screenHeight);

        layoutParams.leftMargin = startPositionX + moveLengthX;
        layoutParams.topMargin = startPositionY + moveLengthY;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;

        requestLayout();
    }

    public void resizeElement(int changedDistanceX, int changedDistanceY, int startElementWidth, int startElementHeight,int centerPositionX, int centerPositionY,int maxIncreaseSizeX,int maxIncreaseSizeY) {

        if (changedDistanceX > maxIncreaseSizeX*2){
            changedDistanceX = maxIncreaseSizeX*2;
        }

        if (changedDistanceY > maxIncreaseSizeY*2){
            changedDistanceY = maxIncreaseSizeY*2;
        }


        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();


        int newWidth = startElementWidth + changedDistanceX;
        int newHeight = startElementHeight + changedDistanceY;
        newWidth = Math.max(newWidth,20);
        newHeight = Math.max(newHeight,20);

        int newPos_x = centerPositionX - newWidth/2;
        int newPos_y = centerPositionY - newHeight/2;

        layoutParams.height = newHeight;
        layoutParams.width = newWidth;
        layoutParams.leftMargin = newPos_x;
        layoutParams.topMargin = newPos_y;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;
        requestLayout();
    }




    @Override
    protected void onDraw(Canvas canvas) {
        onElementDraw(canvas);
        super.onDraw(canvas);
    }



    protected int getDefaultColor() {

        if (virtualController.getCurrentMode() == VirtualController.ControllerMode.EditButtons){
            return configEditColor;
        } else {
            adjustOpacity();
            return normalColor;
        }

    }

    protected int getDefaultStrokeWidth() {
        DisplayMetrics screen = getResources().getDisplayMetrics();
        return (int)(screen.heightPixels*0.004f);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Ignore secondary touches on controls
        //
        // NB: We can get an additional pointer down if the user touches a non-StreamView area
        // while also touching an OSC control, even if that pointer down doesn't correspond to
        // an area of the OSC control.

        return onElementTouchEvent(event);
    }

    abstract protected void onElementDraw(Canvas canvas);

    abstract public boolean onElementTouchEvent(MotionEvent event);

    protected static final void _DBG(String text) {
        if (_PRINT_DEBUG_INFORMATION) {
            System.out.println(text);
        }
    }

    public void setColors(int normalColor, int pressedColor) {
        this.normalColor = normalColor;
        this.pressedColor = pressedColor;

        invalidate();
    }



    public void adjustOpacity() {
        int hexOpacity = virtualController.getElementsOpacity() * 255 / 100;
        this.normalColor = (hexOpacity << 24) | (normalColor & 0x00FFFFFF);
        this.pressedColor = (hexOpacity << 24) | (pressedColor & 0x00FFFFFF);

        invalidate();
    }

    protected final float getPercent(float value, float percent) {
        return value / 100 * percent;
    }

    protected final int getCorrectWidth() {
        return getWidth() > getHeight() ? getHeight() : getWidth();
    }


    public JSONObject getConfiguration() throws JSONException {
        JSONObject configuration = new JSONObject();

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();

        configuration.put("LEFT", layoutParams.leftMargin);
        configuration.put("TOP", layoutParams.topMargin);
        configuration.put("WIDTH", layoutParams.width);
        configuration.put("HEIGHT", layoutParams.height);

        return configuration;
    }

    public void loadConfiguration(JSONObject configuration) throws JSONException {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();

        layoutParams.leftMargin = configuration.getInt("LEFT");
        layoutParams.topMargin = configuration.getInt("TOP");
        layoutParams.width = configuration.getInt("WIDTH");
        layoutParams.height = configuration.getInt("HEIGHT");

        requestLayout();
    }
}
