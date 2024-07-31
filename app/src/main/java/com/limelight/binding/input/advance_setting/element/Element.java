package com.limelight.binding.input.advance_setting.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.limelight.binding.input.advance_setting.superpage.SuperPageLayout;

public abstract class Element extends View {


    public static final String COLUMN_LONG_CONFIG_ID = "config_id";
    public static final String COLUMN_LONG_ELEMENT_ID = "element_id";
    public static final String COLUMN_INT_ELEMENT_TYPE = "element_type";
    public static final String COLUMN_STRING_ELEMENT_VALUE = "element_value";
    public static final String COLUMN_STRING_ELEMENT_TEXT = "element_text";
    public static final String COLUMN_INT_ELEMENT_WIDTH = "element_width";
    public static final String COLUMN_INT_ELEMENT_HEIGHT = "element_height";
    public static final String COLUMN_INT_ELEMENT_LAYER = "element_layer";
    public static final String COLUMN_INT_ELEMENT_MODE = "element_mode";
    public static final String COLUMN_INT_ELEMENT_SENSE = "element_sense";
    public static final String COLUMN_INT_ELEMENT_CENTRAL_X = "element_central_x";
    public static final String COLUMN_INT_ELEMENT_CENTRAL_Y = "element_central_y";
    public static final String COLUMN_INT_ELEMENT_RADIUS = "element_radius";
    public static final String COLUMN_INT_ELEMENT_OPACITY = "element_opacity";
    public static final String COLUMN_INT_ELEMENT_THICK = "element_thick";
    public static final String COLUMN_INT_ELEMENT_NORMAL_COLOR = "element_color";
    public static final String COLUMN_INT_ELEMENT_PRESSED_COLOR = "element_pressed_color";
    public static final String COLUMN_INT_ELEMENT_BACKGROUND_COLOR = "element_background_color";

    public static final int ELEMENT_TYPE_DIGITAL_BUTTON = 0;
    public static final int ELEMENT_TYPE_DIGITAL_DIGITAL_PAD = 1;
    public static final int ELEMENT_TYPE_ANALOG_STICK = 2;



    protected class HexInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i)) && (source.charAt(i) < 'A' || source.charAt(i) > 'F')) {
                    return "";
                }
            }
            return null;
        }
    }

    public enum ElementMode{
        Unselect,
        Select
    }

    protected final Long elementId;
    protected final Long configId;
    protected final int elementType;
    protected final ElementController elementController;
    private ElementMode elementMode = ElementMode.Unselect;
    private final Paint paint = new Paint();
    private final RectF rect = new RectF();
    protected int centralXMax;
    protected int centralXMin;
    protected int centralYMax;
    protected int centralYMin;
    protected int widthMax;
    protected int widthMin;
    protected int heightMax;
    protected int heightMin;
    private float lastX;
    private float lastY;
    private boolean isClick = true;


    public Element(Long elementId, Long configId, int elementType, ElementController elementController,Context context) {
        super(context);
        this.elementId = elementId;
        this.configId = configId;
        this.elementType = elementType;
        this.elementController = elementController;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));

    }

    public boolean inRange(float x, float y) {
        return (this.getX() < x && this.getX() + this.getWidth() > x) &&
                (this.getY() < y && this.getY() + this.getHeight() > y);
    }

    public int getCentralX(){
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        return layoutParams.leftMargin + layoutParams.width / 2;
    }

    public int getCentralY(){
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        return layoutParams.topMargin + layoutParams.height / 2;
    }


    public void setCentralX(int centralX){
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        if (centralX > centralXMax){
            layoutParams.leftMargin = centralXMax - layoutParams.width/2;
        } else if (centralX < centralXMin){
            layoutParams.leftMargin = centralXMin - layoutParams.width/2;
        } else {
            layoutParams.leftMargin = centralX - layoutParams.width/2;
        }
        //保存中心点坐标
        requestLayout();


    }

    public void setCentralY(int centralY){
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        if (centralY > centralYMax){
            layoutParams.topMargin = centralYMax - layoutParams.height/2;
        } else if (centralY < centralYMin){
            layoutParams.topMargin = centralYMin - layoutParams.height/2;
        } else {
            layoutParams.topMargin = centralY - layoutParams.height/2;
        }
        requestLayout();
    }

    public void setParamWidth(int width){
        int centralPosX = getCentralX();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        if (width > widthMax){
            layoutParams.width = widthMax;
        } else if (width < widthMin){
            layoutParams.width = widthMin;
        } else {
            layoutParams.width = width;
        }
        setCentralX(centralPosX);
    }

    public void setParamHeight(int height){
        int centralPosY = getCentralY();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        if (height > heightMax){
            layoutParams.height = heightMax;
        } else if (height < heightMin){
            layoutParams.height = heightMin;
        } else {
            layoutParams.height = height;
        }
        setLayoutParams(layoutParams);
        setCentralY(centralPosY);
    }

    public int getParamWidth(){
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        return layoutParams.width;
    }

    public int getParamHeight(){
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        return layoutParams.height;
    }

    public void setMode(ElementMode elementMode){
        this.elementMode = elementMode;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        onElementDraw(canvas);
        if (elementMode == ElementMode.Select){
            // 绘画范围
            rect.left = rect.top = 2;
            rect.right = getWidth() - rect.left;
            rect.bottom = getHeight() - rect.top;
            // 边框
            paint.setColor(0xf041954a);
            canvas.drawRoundRect(rect, 0, 0, paint);
        } else if (elementController.getMode() == ElementController.Mode.Edit){
            // 绘画范围
            rect.left = rect.top = 2;
            rect.right = getWidth() - rect.left;
            rect.bottom = getHeight() - rect.top;
            // 边框
            paint.setColor(0xf0dc143c);
            canvas.drawRoundRect(rect, 0, 0, paint);
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Ignore secondary touches on controls
        //
        // NB: We can get an additional pointer down if the user touches a non-StreamView area
        // while also touching an OSC control, even if that pointer down doesn't correspond to
        // an area of the OSC control.
        if (event.getActionIndex() != 0) {
            return true;
        }

        if (elementController.getMode() == ElementController.Mode.Normal){
            return onElementTouchEvent(event);
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                lastX = event.getX();
                lastY = event.getY();
                isClick = true;
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                float x = event.getX();
                float y = event.getY();
                float deltaX = (int)(x - lastX);
                System.out.println("deltaX = " + deltaX);
                float deltaY = (int)(y - lastY);
                System.out.println("deltaY = " + deltaY);
                if (deltaX + deltaY < 2){
                    return true;
                }
                isClick = false;
                setCentralX(getCentralX() + (int) deltaX);
                setCentralY(getCentralY() + (int) deltaY);
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (isClick){
                    elementController.toggleSettingPage(getSettingPage());
                }
                return true;
            }
            default: {
            }
        }
        return true;
    }
    abstract public SuperPageLayout getSettingPage();

    abstract public void updatePageInfo();

    abstract public void updateDataBase();
    abstract protected void onElementDraw(Canvas canvas);

    abstract public boolean onElementTouchEvent(MotionEvent event);


    protected final float getPercent(float value, float percent) {
        return value / 100 * percent;
    }

}
