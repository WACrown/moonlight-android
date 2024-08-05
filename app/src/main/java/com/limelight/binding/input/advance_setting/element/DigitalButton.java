package com.limelight.binding.input.advance_setting.element;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.limelight.R;
import com.limelight.binding.input.advance_setting.superpage.ElementEditText;
import com.limelight.binding.input.advance_setting.superpage.NumberSeekbar;
import com.limelight.binding.input.advance_setting.PageDeviceController;
import com.limelight.binding.input.advance_setting.TouchController;
import com.limelight.binding.input.advance_setting.sqlite.SuperConfigDatabaseHelper;
import com.limelight.binding.input.advance_setting.superpage.SuperPageLayout;

import java.util.Map;

/**
 * This is a digital button on screen element. It is used to get click and double click user input.
 */
public class DigitalButton extends Element {

    /**
     * Listener interface to update registered observers.
     */
    public interface DigitalButtonListener {

        /**
         * onClick event will be fired on button click.
         */
        void onClick();

        /**
         * onLongClick event will be fired on button long click.
         */
        void onLongClick();

        /**
         * onRelease event will be fired on button unpress.
         */
        void onRelease();
    }

    public static final int DIGITAL_BUTTON_MODE_BUTTON = 0;
    public static final int DIGITAL_BUTTON_MODE_SWITCH = 1;
    public static final int DIGITAL_BUTTON_MODE_MOUSE = 2;

    private TouchController touchController;
    private SuperConfigDatabaseHelper superConfigDatabaseHelper;
    private PageDeviceController pageDeviceController;
    private DigitalButton digitalButton;

    private DigitalButtonListener buttonListener;
    private String elementText;
    private String elementValue;
    private int radius;
    private int mode;
    private int sense;
    private int layer;
    private int thick;
    private int normalColor;
    private int pressedColor;
    private int backgroundColor;

    private SuperPageLayout digitalButtonPage;
    private NumberSeekbar centralXNumberSeekbar;
    private NumberSeekbar centralYNumberSeekbar;
    private NumberSeekbar widthNumberSeekbar;
    private NumberSeekbar heightNumberSeekbar;
    private NumberSeekbar buttonRadiusNumberSeekbar;

    private float lastX;
    private float lastY;
    private long timerLongClickTimeout = 3000;
    private final Runnable longClickRunnable = new Runnable() {
        @Override
        public void run() {
            onLongClickCallback();
        }
    };
    private final Paint paintBorder = new Paint();
    private final Paint paintBackground = new Paint();
    private final Paint paintText = new Paint();
    private final RectF rect = new RectF();



    public DigitalButton(Map<String,Object> attributesMap,
                         ElementController controller,
                         TouchController touchController,
                         PageDeviceController pageDeviceController, Context context) {
        super((Long) attributesMap.get(Element.COLUMN_LONG_ELEMENT_ID),(Long)attributesMap.get(Element.COLUMN_LONG_CONFIG_ID),((Long) attributesMap.get(Element.COLUMN_INT_ELEMENT_TYPE)).intValue(),controller,context);
        System.out.println("System.currentTimeMillis()2 = " + System.currentTimeMillis());
        this.touchController = touchController;
        this.superConfigDatabaseHelper = controller.getSuperConfigDatabaseHelper();
        this.pageDeviceController = pageDeviceController;
        this.digitalButton = this;
        super.centralXMax  = controller.getElementsParentWidth();
        super.centralXMin  = 0;
        super.centralYMax  = controller.getElementsParentHeight();
        super.centralYMin  = 0;
        super.widthMax  = controller.getElementsParentWidth() / 2;
        super.widthMin  = 50;
        super.heightMax  = controller.getElementsParentHeight() / 2;
        super.heightMin  = 50;

        paintText.setTextAlign(Paint.Align.CENTER);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBackground.setStyle(Paint.Style.FILL);


        elementText = (String) attributesMap.get(COLUMN_STRING_ELEMENT_TEXT);
        radius = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_RADIUS)).intValue();
        mode = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_MODE)).intValue();
        sense = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_SENSE)).intValue();
        layer = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_LAYER)).intValue();
        thick = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_THICK)).intValue();
        normalColor = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_NORMAL_COLOR)).intValue();
        pressedColor = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_PRESSED_COLOR)).intValue();
        backgroundColor = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_BACKGROUND_COLOR)).intValue();
        elementValue = (String) attributesMap.get(COLUMN_STRING_ELEMENT_VALUE);
        ElementController.SendEventHandler sendHandler = controller.getSendEventHandler(elementValue);
        buttonListener = new DigitalButton.DigitalButtonListener() {
            @Override
            public void onClick() {
                sendHandler.sendEvent(true);
            }

            @Override
            public void onLongClick() {

            }

            @Override
            public void onRelease() {
                sendHandler.sendEvent(false);
            }
        };
        System.out.println("System.currentTimeMillis()6 = " + System.currentTimeMillis());
    }

    @Override
    protected void onElementDraw(Canvas canvas) {
        // 文字
        paintText.setTextSize(getPercent(getWidth(), 25));
        paintText.setColor(isPressed() ? pressedColor : normalColor);
        // 边框
        paintBorder.setStrokeWidth(thick);
        paintBorder.setColor(isPressed() ? pressedColor : normalColor);
        // 背景颜色
        paintBackground.setColor(backgroundColor);
        // 绘画范围
        rect.left = rect.top = (float) thick / 2;
        rect.right = getWidth() - rect.left;
        rect.bottom = getHeight() - rect.top;
        // 绘制背景
        canvas.drawRoundRect(rect, radius, radius, paintBackground);
        // 绘制边框
        canvas.drawRoundRect(rect, radius, radius, paintBorder);
        // 绘制文字
        canvas.drawText(elementText, getPercent(getWidth(), 50), getPercent(getHeight(), 63), paintText);

    }

    private void onClickCallback() {
        // notify listenersbuttonListener.onClick();
        System.out.println("onClickCallback");
        buttonListener.onClick();
        elementController.getHandler().removeCallbacks(longClickRunnable);
        elementController.getHandler().postDelayed(longClickRunnable, timerLongClickTimeout);

    }

    private void onLongClickCallback() {
        // notify listeners
        buttonListener.onLongClick();
    }

    private void onReleaseCallback() {
        // notify listeners
        System.out.println("onReleaseCallback");
        buttonListener.onRelease();

        // We may be called for a release without a prior click
        elementController.getHandler().removeCallbacks(longClickRunnable);
    }

    @Override
    public boolean onElementTouchEvent(MotionEvent event) {
        // get masked (not specific to a pointer) action
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                lastX = event.getX();
                lastY = event.getY();
                if (mode == DIGITAL_BUTTON_MODE_SWITCH){
                    if (isPressed()){
                        setPressed(false);
                        onReleaseCallback();
                    } else {
                        setPressed(true);
                        onClickCallback();
                    }
                } else {
                    setPressed(true);
                    onClickCallback();
                }

                invalidate();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mode == DIGITAL_BUTTON_MODE_MOUSE){
                    touchController.mouseMove((event.getX() - lastX) * 0.01f * sense,
                            (event.getY() - lastY)* 0.01f * sense);
                    lastX = event.getX();
                    lastY = event.getY();
                }
                return true;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (mode == DIGITAL_BUTTON_MODE_SWITCH){
                    return true;
                }
                setPressed(false);
                onReleaseCallback();
                invalidate();
                return true;
            }
            default: {
            }
        }
        return true;
    }

    @Override
    public void updatePositionDataBase() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_X,getCentralX());
        contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_Y,getCentralY());
        superConfigDatabaseHelper.updateElement(elementId,contentValues);

    }

    @Override
    protected void updatePageInfo() {
        if (digitalButtonPage != null){
            centralXNumberSeekbar.setValueWithNoCallBack(getCentralX());
            centralYNumberSeekbar.setValueWithNoCallBack(getCentralY());
        }

    }

    @Override
    protected SuperPageLayout getInfoPage() {
        if (digitalButtonPage == null){
            digitalButtonPage = (SuperPageLayout) LayoutInflater.from(getContext()).inflate(R.layout.page_digital_button,null);
            centralXNumberSeekbar = digitalButtonPage.findViewById(R.id.page_digital_button_central_x);
            centralYNumberSeekbar = digitalButtonPage.findViewById(R.id.page_digital_button_central_y);
            widthNumberSeekbar = digitalButtonPage.findViewById(R.id.page_digital_button_width);
            heightNumberSeekbar = digitalButtonPage.findViewById(R.id.page_digital_button_height);
            buttonRadiusNumberSeekbar = digitalButtonPage.findViewById(R.id.button_radius);
        }

        ElementEditText buttonTextEditText = digitalButtonPage.findViewById(R.id.button_text);
        buttonTextEditText.setTextWithNoTextChangedCallBack(elementText);
        buttonTextEditText.setOnTextChangedListener(new ElementEditText.OnTextChangedListener() {
            @Override
            public void textChanged(String text) {
                digitalButton.elementText = text;
                digitalButton.invalidate();
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_STRING_ELEMENT_TEXT,text);
                superConfigDatabaseHelper.updateElement(digitalButton.elementId,contentValues);
            }
        });



        TextView buttonValue = digitalButtonPage.findViewById(R.id.button_value);
        buttonValue.setText(pageDeviceController.getKeyNameByValue(elementValue));
        buttonValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDeviceController.DeviceCallBack deviceCallBack = new PageDeviceController.DeviceCallBack() {
                    @Override
                    public void OnKeyClick(TextView key) {
                        String innerValue = key.getTag().toString();
                        // page页设置值文本
                        ((TextView) v).setText(key.getText());
                        // 保存值
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(COLUMN_STRING_ELEMENT_VALUE,innerValue);
                        superConfigDatabaseHelper.updateElement(elementId,contentValues);
                        // 设置onClickListener
                        ElementController.SendEventHandler sendHandler = elementController.getSendEventHandler(innerValue);
                        buttonListener = new DigitalButtonListener() {
                            @Override
                            public void onClick() {
                                sendHandler.sendEvent(true);
                            }

                            @Override
                            public void onLongClick() {

                            }

                            @Override
                            public void onRelease() {
                                sendHandler.sendEvent(false);
                            }
                        };
                    }

                    @Override
                    public void OnResetKeyClick() {
                        String innerValue = "k29";
                        // page页设置值文本
                        ((TextView) v).setText("A");
                        // 保存值
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(COLUMN_STRING_ELEMENT_VALUE,innerValue);
                        superConfigDatabaseHelper.updateElement(elementId,contentValues);
                        // 设置onClickListener
                        ElementController.SendEventHandler sendHandler = elementController.getSendEventHandler(innerValue);
                        buttonListener = new DigitalButtonListener() {
                            @Override
                            public void onClick() {
                                sendHandler.sendEvent(true);
                            }

                            @Override
                            public void onLongClick() {

                            }

                            @Override
                            public void onRelease() {
                                sendHandler.sendEvent(false);
                            }
                        };
                    }
                };
                pageDeviceController.open(deviceCallBack,View.VISIBLE,View.VISIBLE,View.VISIBLE);
            }
        });
        //TODO:这里可能是因为位置信息小数变整数的原因，位置会有轻微的位移
        centralXNumberSeekbar.setProgressMin(centralXMin);
        centralXNumberSeekbar.setProgressMax(centralXMax);
        centralXNumberSeekbar.setValueWithNoCallBack(getCentralX());
        centralXNumberSeekbar.setOnNumberSeekbarChangeListener(new NumberSeekbar.OnNumberSeekbarChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                setCentralX(progress);
            }

            @Override
            public void onProgressRelease(int lastProgress) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_X,getCentralX());
                superConfigDatabaseHelper.updateElement(elementId,contentValues);
            }
        });
        centralYNumberSeekbar.setProgressMin(centralYMin);
        centralYNumberSeekbar.setProgressMax(centralYMax);
        centralYNumberSeekbar.setValueWithNoCallBack(getCentralY());
        centralYNumberSeekbar.setOnNumberSeekbarChangeListener(new NumberSeekbar.OnNumberSeekbarChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                setCentralY(progress);
            }

            @Override
            public void onProgressRelease(int lastProgress) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_Y,getCentralY());
                superConfigDatabaseHelper.updateElement(elementId,contentValues);
            }
        });


        widthNumberSeekbar.setProgressMax(widthMax);
        widthNumberSeekbar.setProgressMin(widthMin);
        widthNumberSeekbar.setValueWithNoCallBack(getParamWidth());
        widthNumberSeekbar.setOnNumberSeekbarChangeListener(new NumberSeekbar.OnNumberSeekbarChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                setParamWidth(progress);
            }

            @Override
            public void onProgressRelease(int lastProgress) {
                buttonRadiusNumberSeekbar.setProgressMax(Math.min(getParamWidth(),getParamHeight()) / 2);
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_INT_ELEMENT_WIDTH,getParamWidth());
                superConfigDatabaseHelper.updateElement(elementId,contentValues);
            }
        });

        heightNumberSeekbar.setProgressMax(heightMax);
        heightNumberSeekbar.setProgressMin(heightMin);
        heightNumberSeekbar.setValueWithNoCallBack(getParamHeight());
        heightNumberSeekbar.setOnNumberSeekbarChangeListener(new NumberSeekbar.OnNumberSeekbarChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                setParamHeight(progress);
            }

            @Override
            public void onProgressRelease(int lastProgress) {
                buttonRadiusNumberSeekbar.setProgressMax(Math.min(getParamWidth(),getParamHeight()) / 2);
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_INT_ELEMENT_HEIGHT,getParamHeight());
                superConfigDatabaseHelper.updateElement(elementId,contentValues);
            }
        });

        NumberSeekbar buttonSenseNumberSeekbar = digitalButtonPage.findViewById(R.id.button_sense);
        buttonSenseNumberSeekbar.setValueWithNoCallBack(sense);
        buttonSenseNumberSeekbar.setOnNumberSeekbarChangeListener(new NumberSeekbar.OnNumberSeekbarChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                sense = progress;
            }

            @Override
            public void onProgressRelease(int lastProgress) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_INT_ELEMENT_SENSE,sense);
                superConfigDatabaseHelper.updateElement(elementId,contentValues);
            }
        });

        RadioGroup modeRadioGroup = digitalButtonPage.findViewById(R.id.button_mode);
        RadioButton radioButton = (RadioButton) modeRadioGroup.getChildAt(mode);
        if (mode == DIGITAL_BUTTON_MODE_MOUSE){
            buttonSenseNumberSeekbar.setVisibility(VISIBLE);
        } else {
            buttonSenseNumberSeekbar.setVisibility(GONE);
        }
        radioButton.setChecked(true);
        modeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String modeString = group.findViewById(checkedId).getTag().toString();
                mode = Integer.parseInt(modeString);
                System.out.println("mode = " + mode);
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_INT_ELEMENT_MODE,mode);
                superConfigDatabaseHelper.updateElement(elementId,contentValues);
                if (checkedId == DIGITAL_BUTTON_MODE_MOUSE){
                    buttonSenseNumberSeekbar.setVisibility(VISIBLE);
                } else {
                    buttonSenseNumberSeekbar.setVisibility(GONE);
                }
            }
        });




        buttonRadiusNumberSeekbar.setProgressMax(Math.min(getParamWidth(),getParamHeight()) / 2);
        buttonRadiusNumberSeekbar.setValueWithNoCallBack(radius);
        buttonRadiusNumberSeekbar.setOnNumberSeekbarChangeListener(new NumberSeekbar.OnNumberSeekbarChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                radius = progress;
                digitalButton.invalidate();
            }

            @Override
            public void onProgressRelease(int lastProgress) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_INT_ELEMENT_RADIUS,radius);
                superConfigDatabaseHelper.updateElement(elementId,contentValues);
            }
        });

        NumberSeekbar buttonThickNumberSeekbar = digitalButtonPage.findViewById(R.id.button_thick);
        buttonThickNumberSeekbar.setValueWithNoCallBack(thick);
        buttonThickNumberSeekbar.setOnNumberSeekbarChangeListener(new NumberSeekbar.OnNumberSeekbarChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                thick = progress;
                digitalButton.invalidate();
            }

            @Override
            public void onProgressRelease(int lastProgress) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_INT_ELEMENT_THICK,thick);
                superConfigDatabaseHelper.updateElement(elementId,contentValues);
            }
        });

        ElementEditText buttonNormalColorEditText = digitalButtonPage.findViewById(R.id.button_normal_color);
        buttonNormalColorEditText.setTextWithNoTextChangedCallBack(String.format("%08X",normalColor));
        buttonNormalColorEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new Element.HexInputFilter()});
        buttonNormalColorEditText.setOnTextChangedListener(new ElementEditText.OnTextChangedListener() {
            @Override
            public void textChanged(String text) {
                if (text.matches("^[A-F0-9]{8}$")){
                    normalColor = (int) Long.parseLong(text, 16);
                    digitalButton.invalidate();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(COLUMN_INT_ELEMENT_NORMAL_COLOR,normalColor);
                    superConfigDatabaseHelper.updateElement(elementId,contentValues);
                }
            }
        });

        ElementEditText buttonPressedColorEditText = digitalButtonPage.findViewById(R.id.button_pressed_color);
        buttonPressedColorEditText.setTextWithNoTextChangedCallBack(String.format("%08X",pressedColor));
        buttonPressedColorEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new Element.HexInputFilter()});
        buttonPressedColorEditText.setOnTextChangedListener(new ElementEditText.OnTextChangedListener() {
            @Override
            public void textChanged(String text) {
                if (text.matches("^[A-F0-9]{8}$")){
                    pressedColor = (int) Long.parseLong(text, 16);
                    digitalButton.invalidate();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(COLUMN_INT_ELEMENT_PRESSED_COLOR,pressedColor);
                    superConfigDatabaseHelper.updateElement(elementId,contentValues);
                }
            }
        });

        ElementEditText buttonBackgroundColorEditText = digitalButtonPage.findViewById(R.id.button_background_color);
        buttonBackgroundColorEditText.setTextWithNoTextChangedCallBack(String.format("%08X",backgroundColor));
        buttonBackgroundColorEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new Element.HexInputFilter()});
        buttonBackgroundColorEditText.setOnTextChangedListener(new ElementEditText.OnTextChangedListener() {
            @Override
            public void textChanged(String text) {
                if (text.matches("^[A-F0-9]{8}$")){
                    backgroundColor = (int) Long.parseLong(text, 16);
                    digitalButton.invalidate();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(COLUMN_INT_ELEMENT_BACKGROUND_COLOR,backgroundColor);
                    superConfigDatabaseHelper.updateElement(elementId,contentValues);
                }
            }
        });

        digitalButtonPage.findViewById(R.id.page_digital_copy).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_LONG_ELEMENT_ID,System.currentTimeMillis());
                contentValues.put(COLUMN_INT_ELEMENT_TYPE,ELEMENT_TYPE_DIGITAL_BUTTON);
                contentValues.put(COLUMN_STRING_ELEMENT_TEXT,elementText);
                contentValues.put(COLUMN_STRING_ELEMENT_VALUE,elementValue);
                contentValues.put(COLUMN_INT_ELEMENT_MODE,mode);
                contentValues.put(COLUMN_INT_ELEMENT_SENSE,sense);
                contentValues.put(COLUMN_INT_ELEMENT_WIDTH,getParamWidth());
                contentValues.put(COLUMN_INT_ELEMENT_HEIGHT,getParamHeight());
                contentValues.put(COLUMN_INT_ELEMENT_LAYER,layer);
                contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_X,Math.max(Math.min(getCentralX() + getParamWidth(),centralXMax),centralXMin));
                contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_Y,getCentralY());
                contentValues.put(COLUMN_INT_ELEMENT_RADIUS,radius);
                contentValues.put(COLUMN_INT_ELEMENT_THICK,thick);
                contentValues.put(COLUMN_INT_ELEMENT_NORMAL_COLOR,normalColor);
                contentValues.put(COLUMN_INT_ELEMENT_PRESSED_COLOR,pressedColor);
                contentValues.put(COLUMN_INT_ELEMENT_BACKGROUND_COLOR,backgroundColor);
                elementController.copyElement(contentValues);
            }
        });

        digitalButtonPage.findViewById(R.id.page_digital_delete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                elementController.toggleInfoPage(digitalButtonPage);
                elementController.deleteElement(digitalButton);
            }
        });



        return digitalButtonPage;
    }

    public static ContentValues getInitialInfo(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LONG_ELEMENT_ID,System.currentTimeMillis());
        contentValues.put(COLUMN_INT_ELEMENT_TYPE,ELEMENT_TYPE_DIGITAL_BUTTON);
        contentValues.put(COLUMN_STRING_ELEMENT_TEXT,"A");
        contentValues.put(COLUMN_STRING_ELEMENT_VALUE,"k29");
        contentValues.put(COLUMN_INT_ELEMENT_MODE,DIGITAL_BUTTON_MODE_BUTTON);
        contentValues.put(COLUMN_INT_ELEMENT_SENSE,100);
        contentValues.put(COLUMN_INT_ELEMENT_WIDTH,100);
        contentValues.put(COLUMN_INT_ELEMENT_HEIGHT,100);
        contentValues.put(COLUMN_INT_ELEMENT_LAYER,0);
        contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_X,100);
        contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_Y,100);
        contentValues.put(COLUMN_INT_ELEMENT_RADIUS,0);
        contentValues.put(COLUMN_INT_ELEMENT_THICK,5);
        contentValues.put(COLUMN_INT_ELEMENT_NORMAL_COLOR,0xF0888888);
        contentValues.put(COLUMN_INT_ELEMENT_PRESSED_COLOR,0xF00000FF);
        contentValues.put(COLUMN_INT_ELEMENT_BACKGROUND_COLOR,0x00FFFFFF);
        return contentValues;


    }
}
