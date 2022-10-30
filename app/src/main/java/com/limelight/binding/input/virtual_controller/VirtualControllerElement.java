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
    protected static boolean _PRINT_DEBUG_INFORMATION = false;


    protected VirtualController virtualController;
    protected final String elementId;

    private final Paint paint = new Paint();
    private final int screenWidth;
    private final int screenHeight;

    private int normalColor = 0xF0888888;
    protected int pressedColor = 0xF00000FF;
    private int configMoveColor = 0xF0FF0000;
    private int configResizeColor = 0xF0FF00FF;
    private int configEditColor = 0xFF03A9F4;
    private int configSelectedColor = 0xF000FF00;


    float position_pressed_x = 0;
    float position_pressed_y = 0;

    private enum Mode {
        Normal,
        Resize,
        Move,
        Edit
    }

    private Mode currentMode = Mode.Normal;

    protected VirtualControllerElement(VirtualController controller, Context context, String elementId) {
        super(context);
        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        screenWidth = screen.widthPixels;
        screenHeight = screen.heightPixels;
        this.virtualController = controller;
        this.elementId = elementId;
    }

    public void moveElement(int maxPositionX,int maxPositionY,int startPositionX, int startPositionY,int moveLengthX, int moveLengthY) {
        int newPos_x = startPositionX + moveLengthX;
        int newPos_y = startPositionY + moveLengthY;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();

        if (newPos_x < 0){
            newPos_x = 0;
        } else if (newPos_x > maxPositionX){
            newPos_x = maxPositionX;
        }

        if (newPos_y < 0){
            newPos_y = 0;
        } else if (newPos_y > maxPositionY){
            newPos_y = maxPositionY;
        }

        //System.out.println("wangguan x:" + newPos_x + " y:" + newPos_y + " screenWidth:" + screenWidth + " screenHeight:" + screenHeight);

        layoutParams.leftMargin = newPos_x;
        layoutParams.topMargin = newPos_y;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;

        requestLayout();
    }

    public void resizeElement(int centerPositionX, int centerPositionY,int maxElementWidth,int maxElementHeight,int startElementWidth, int startElementHeight, int changedDistanceX, int changedDistanceY) {

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();


        int newWidth = startElementWidth + changedDistanceX;
        int newHeight = startElementHeight + changedDistanceY;

        if (newWidth < 20){
            newWidth = 20;
        } else if (newWidth > maxElementWidth){
            newWidth = maxElementWidth;
        }

        if (newHeight < 20){
            newHeight = 20;
        } else if (newHeight > maxElementHeight){
            newHeight = maxElementHeight;
        }

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


    public boolean switchSelectedStatus() {

        if (configEditColor == 0xFF03A9F4){
            setConfigEditColor(0xF0FF0000);
            return true;
        } else {
            setConfigEditColor(0xFF03A9F4);
            return false;
        }

        //System.out.println("wangguan deleteElement" + elementId);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        onElementDraw(canvas);
        if (currentMode != Mode.Normal) {
            paint.setColor(configSelectedColor);
            paint.setStrokeWidth(getDefaultStrokeWidth());
            paint.setStyle(Paint.Style.STROKE);

            canvas.drawRect(paint.getStrokeWidth(), paint.getStrokeWidth(),
                    getWidth()-paint.getStrokeWidth(), getHeight()-paint.getStrokeWidth(),
                    paint);
        }

        super.onDraw(canvas);
    }


    protected void actionEnableEdit() {
        currentMode = Mode.Edit;
    }

    protected void actionEnableMove() {
        currentMode = Mode.Move;
    }

    protected void actionEnableResize() {
        currentMode = Mode.Resize;
    }

    protected void actionCancel() {
        currentMode = Mode.Normal;
        invalidate();
    }

    protected int getDefaultColor() {

        if (virtualController.getControllerMode() == VirtualController.ControllerMode.EditLayout)
            return configEditColor;
        else if (virtualController.getControllerMode() == VirtualController.ControllerMode.MoveButtons)
            return configMoveColor;
        else if (virtualController.getControllerMode() == VirtualController.ControllerMode.ResizeButtons)
            return configResizeColor;
        else
            return normalColor;
    }

    protected int getDefaultStrokeWidth() {
        DisplayMetrics screen = getResources().getDisplayMetrics();
        return (int)(screen.heightPixels*0.004f);
    }

    protected void showConfigurationDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

        alertBuilder.setTitle("Configuration");

        CharSequence functions[] = new CharSequence[]{
                "Move",
                "Resize",
                "Select",
            /*election
            "Set n
            Disable color sormal color",
            "Set pressed color",
            */
                "Cancel"
        };

        alertBuilder.setItems(functions, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: { // move
                        actionEnableMove();
                        break;
                    }
                    case 1: { // resize
                        actionEnableResize();
                        break;
                    }
                    case 2: { // select
                        actionEnableEdit();
                        break;
                    }
                /*
                case 2: { // set default color
                    actionShowNormalColorChooser();
                    break;
                }
                case 3: { // set pressed color
                    actionShowPressedColorChooser();
                    break;
                }
                */
                    default: { // cancel
                        actionCancel();
                        break;
                    }
                }
            }
        });
        AlertDialog alert = alertBuilder.create();
        // show menu
        alert.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Ignore secondary touches on controls
        //
        // NB: We can get an additional pointer down if the user touches a non-StreamView area
        // while also touching an OSC control, even if that pointer down doesn't correspond to
        // an area of the OSC control.
        System.out.println("wangguan element touch:" + event.getAction());

        if (event.getActionIndex() != 0) {
            return true;
        }

        if (virtualController.getControllerMode() == VirtualController.ControllerMode.Active) {
            return onElementTouchEvent(event);
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                position_pressed_x = event.getX();
                position_pressed_y = event.getY();
                return true;
            }

            case MotionEvent.ACTION_MOVE: {

                return false;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                switchSelectedStatus();
                actionCancel();
                return true;
            }
            default: {
            }
        }
        return true;
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

    public void setConfigEditColor(int deleteColor){
        this.configEditColor = deleteColor;
        invalidate();
    }


    public void setOpacity(int opacity) {
        int hexOpacity = opacity * 255 / 100;
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
