package com.limelight.binding.input.advance_setting.element;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.limelight.R;
import com.limelight.binding.input.advance_setting.PageDeviceController;
import com.limelight.binding.input.advance_setting.sqlite.SuperConfigDatabaseHelper;
import com.limelight.binding.input.advance_setting.superpage.ElementEditText;
import com.limelight.binding.input.advance_setting.superpage.NumberSeekbar;
import com.limelight.binding.input.advance_setting.superpage.SuperPageLayout;

import java.util.Map;

public class DigitalPad extends Element {


    public final static int DIGITAL_PAD_DIRECTION_NO_DIRECTION = 0;
    int direction = DIGITAL_PAD_DIRECTION_NO_DIRECTION;
    public final static int DIGITAL_PAD_DIRECTION_LEFT = 1;
    public final static int DIGITAL_PAD_DIRECTION_UP = 2;
    public final static int DIGITAL_PAD_DIRECTION_RIGHT = 4;
    public final static int DIGITAL_PAD_DIRECTION_DOWN = 8;
    private DigitalPadListener digitalPadListener;
    private static final int DPAD_MARGIN = 5;

    private SuperConfigDatabaseHelper superConfigDatabaseHelper;
    private PageDeviceController pageDeviceController;
    private DigitalPad digitalPad;

    private ElementController.SendEventHandler upSenderHandler;
    private ElementController.SendEventHandler downSenderHandler;
    private ElementController.SendEventHandler leftSenderHandler;
    private ElementController.SendEventHandler rightSenderHandler;
    private String elementUpValue;
    private String elementDownValue;
    private String elementLeftValue;
    private String elementRightValue;
    private int layer;
    private int thick;
    private int normalColor;
    private int pressedColor;
    private int backgroundColor;

    private SuperPageLayout digitalPadPage;
    private NumberSeekbar centralXNumberSeekbar;
    private NumberSeekbar centralYNumberSeekbar;

    //加上这个防止一些无用的按键多次触发，发送大量数据，导致卡顿
    private int lastDirection = 0;
    private final Paint paintBorder = new Paint();
    private final Paint paintBackground = new Paint();
    private final Path pathBackground = new Path();
    private final Paint paintText = new Paint();

    public DigitalPad(Map<String,Object> attributesMap,
                      ElementController controller,
                      PageDeviceController pageDeviceController, Context context) {
        super((Long) attributesMap.get(Element.COLUMN_LONG_ELEMENT_ID),(Long)attributesMap.get(Element.COLUMN_LONG_CONFIG_ID),((Long) attributesMap.get(Element.COLUMN_INT_ELEMENT_TYPE)).intValue(),controller,context);
        this.superConfigDatabaseHelper = controller.getSuperConfigDatabaseHelper();
        this.pageDeviceController = pageDeviceController;
        this.digitalPad = this;

        super.centralXMax  = controller.getElementsParentWidth();
        super.centralXMin  = 0;
        super.centralYMax  = controller.getElementsParentHeight();
        super.centralYMin  = 0;
        super.widthMax  = controller.getElementsParentWidth() / 2;
        super.widthMin  = 150;
        super.heightMax  = controller.getElementsParentWidth() / 2;
        super.heightMin  = 150;

        paintText.setTextAlign(Paint.Align.CENTER);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBackground.setStyle(Paint.Style.FILL);

        layer = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_LAYER)).intValue();
        thick = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_THICK)).intValue();
        normalColor = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_NORMAL_COLOR)).intValue();
        pressedColor = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_PRESSED_COLOR)).intValue();
        backgroundColor = ((Long) attributesMap.get(COLUMN_INT_ELEMENT_BACKGROUND_COLOR)).intValue();
        elementUpValue = (String) attributesMap.get(COLUMN_STRING_ELEMENT_UP_VALUE);
        elementDownValue = (String) attributesMap.get(COLUMN_STRING_ELEMENT_DOWN_VALUE);
        elementLeftValue = (String) attributesMap.get(COLUMN_STRING_ELEMENT_LEFT_VALUE);
        elementRightValue = (String) attributesMap.get(COLUMN_STRING_ELEMENT_RIGHT_VALUE);

        upSenderHandler = controller.getSendEventHandler(elementUpValue);
        downSenderHandler = controller.getSendEventHandler(elementDownValue);
        leftSenderHandler = controller.getSendEventHandler(elementLeftValue);
        rightSenderHandler = controller.getSendEventHandler(elementRightValue);
        digitalPadListener = new DigitalPad.DigitalPadListener() {
            @Override
            public void onDirectionChange(int direction) {
                int directionChange = lastDirection ^ direction;
                if ((directionChange & DIGITAL_PAD_DIRECTION_LEFT) != 0 ){
                    if ((direction & DIGITAL_PAD_DIRECTION_LEFT) != 0) {
                        leftSenderHandler.sendEvent(true);
                    }
                    else {
                        leftSenderHandler.sendEvent(false);
                    }
                }
                if ((directionChange & DIGITAL_PAD_DIRECTION_RIGHT) != 0 ){
                    if ((direction & DIGITAL_PAD_DIRECTION_RIGHT) != 0) {
                        rightSenderHandler.sendEvent(true);
                    }
                    else {
                        rightSenderHandler.sendEvent(false);
                    }
                }
                if ((directionChange & DIGITAL_PAD_DIRECTION_UP) != 0 ){
                    if ((direction & DIGITAL_PAD_DIRECTION_UP) != 0) {
                        upSenderHandler.sendEvent(true);
                    }
                    else {
                        upSenderHandler.sendEvent(false);
                    }
                }
                if ((directionChange & DIGITAL_PAD_DIRECTION_DOWN) != 0 ){
                    if ((direction & DIGITAL_PAD_DIRECTION_DOWN) != 0) {
                        downSenderHandler.sendEvent(true);
                    }
                    else {
                        downSenderHandler.sendEvent(false);
                    }
                }
                lastDirection = direction;
            }
        };

    }




    @Override
    protected void onElementDraw(Canvas canvas) {
        paintBorder.setStrokeWidth(thick);
        int correctedBorderPosition = thick + DPAD_MARGIN;
        
        paintBackground.setStyle(Paint.Style.FILL);
        paintBackground.setStrokeWidth(10);
        paintBackground.setColor(backgroundColor);
        pathBackground.reset();
        pathBackground.moveTo(getPercent(getWidth(), 33),correctedBorderPosition);
        pathBackground.lineTo(getPercent(getWidth(), 66),correctedBorderPosition);
        pathBackground.lineTo(getWidth() - correctedBorderPosition,getPercent(getHeight(), 33));
        pathBackground.lineTo(getWidth() - correctedBorderPosition,getPercent(getHeight(), 66));
        pathBackground.lineTo(getPercent(getWidth(), 66),getHeight() - correctedBorderPosition);
        pathBackground.lineTo(getPercent(getWidth(), 33),getHeight() - correctedBorderPosition);
        pathBackground.lineTo(correctedBorderPosition,getPercent(getHeight(), 66));
        pathBackground.lineTo(correctedBorderPosition,getPercent(getHeight(), 33));
        pathBackground.lineTo(getPercent(getWidth(), 33),correctedBorderPosition);
        canvas.drawPath(pathBackground,paintBackground);


        if (direction == DIGITAL_PAD_DIRECTION_NO_DIRECTION) {
            // draw no direction rect
            paintBorder.setStyle(Paint.Style.STROKE);
            paintBorder.setColor(normalColor);
            canvas.drawRect(
                    getPercent(getWidth(), 36), getPercent(getHeight(), 36),
                    getPercent(getWidth(), 63), getPercent(getHeight(), 63),
                    paintBorder
            );
        }

        // draw left rect
        paintBorder.setColor(
                (direction & DIGITAL_PAD_DIRECTION_LEFT) > 0 ? pressedColor : normalColor);
        paintBorder.setStyle(Paint.Style.STROKE);
        canvas.drawRect(
                correctedBorderPosition, getPercent(getHeight(), 33),
                getPercent(getWidth(), 33), getPercent(getHeight(), 66),
                paintBorder
        );


        // draw up rect
        paintBorder.setColor(
                (direction & DIGITAL_PAD_DIRECTION_UP) > 0 ? pressedColor : normalColor);
        paintBorder.setStyle(Paint.Style.STROKE);
        canvas.drawRect(
                getPercent(getWidth(), 33), correctedBorderPosition,
                getPercent(getWidth(), 66), getPercent(getHeight(), 33),
                paintBorder
        );

        // draw right rect
        paintBorder.setColor(
                (direction & DIGITAL_PAD_DIRECTION_RIGHT) > 0 ? pressedColor : normalColor);
        paintBorder.setStyle(Paint.Style.STROKE);
        canvas.drawRect(
                getPercent(getWidth(), 66), getPercent(getHeight(), 33),
                getWidth() - correctedBorderPosition, getPercent(getHeight(), 66),
                paintBorder
        );

        // draw down rect
        paintBorder.setColor(
                (direction & DIGITAL_PAD_DIRECTION_DOWN) > 0 ? pressedColor : normalColor);
        paintBorder.setStyle(Paint.Style.STROKE);
        canvas.drawRect(
                getPercent(getWidth(), 33), getPercent(getHeight(), 66),
                getPercent(getWidth(), 66), getHeight() - correctedBorderPosition,
                paintBorder
        );

        // draw left up line
        paintBorder.setColor((
                        (direction & DIGITAL_PAD_DIRECTION_LEFT) > 0 &&
                                (direction & DIGITAL_PAD_DIRECTION_UP) > 0
                ) ? pressedColor : normalColor
        );
        paintBorder.setStyle(Paint.Style.STROKE);
        canvas.drawLine(
                correctedBorderPosition, getPercent(getHeight(), 33),
                getPercent(getWidth(), 33), correctedBorderPosition,
                paintBorder
        );

        // draw up right line
        paintBorder.setColor((
                        (direction & DIGITAL_PAD_DIRECTION_UP) > 0 &&
                                (direction & DIGITAL_PAD_DIRECTION_RIGHT) > 0
                ) ? pressedColor : normalColor
        );
        paintBorder.setStyle(Paint.Style.STROKE);
        canvas.drawLine(
                getPercent(getWidth(), 66), correctedBorderPosition,
                getWidth() - correctedBorderPosition, getPercent(getHeight(), 33),
                paintBorder
        );

        // draw right down line
        paintBorder.setColor((
                        (direction & DIGITAL_PAD_DIRECTION_RIGHT) > 0 &&
                                (direction & DIGITAL_PAD_DIRECTION_DOWN) > 0
                ) ? pressedColor : normalColor
        );
        paintBorder.setStyle(Paint.Style.STROKE);
        canvas.drawLine(
                getWidth()-paintBorder.getStrokeWidth(), getPercent(getHeight(), 66),
                getPercent(getWidth(), 66), getHeight()-correctedBorderPosition,
                paintBorder
        );

        // draw down left line
        paintBorder.setColor((
                        (direction & DIGITAL_PAD_DIRECTION_DOWN) > 0 &&
                                (direction & DIGITAL_PAD_DIRECTION_LEFT) > 0
                ) ? pressedColor : normalColor
        );
        paintBorder.setStyle(Paint.Style.STROKE);
        canvas.drawLine(
                getPercent(getWidth(), 33), getHeight()-correctedBorderPosition,
                correctedBorderPosition, getPercent(getHeight(), 66),
                paintBorder
        );
    }

    private void newDirectionCallback(int direction) {

        // notify listeners
        digitalPadListener.onDirectionChange(direction);
    }

    @Override
    public boolean onElementTouchEvent(MotionEvent event) {
        // get masked (not specific to a pointer) action
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                direction = 0;

                if (event.getX() < getPercent(getWidth(), 33)) {
                    direction |= DIGITAL_PAD_DIRECTION_LEFT;
                }
                if (event.getX() > getPercent(getWidth(), 66)) {
                    direction |= DIGITAL_PAD_DIRECTION_RIGHT;
                }
                if (event.getY() > getPercent(getHeight(), 66)) {
                    direction |= DIGITAL_PAD_DIRECTION_DOWN;
                }
                if (event.getY() < getPercent(getHeight(), 33)) {
                    direction |= DIGITAL_PAD_DIRECTION_UP;
                }
                newDirectionCallback(direction);
                invalidate();

                return true;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                direction = 0;
                newDirectionCallback(direction);
                invalidate();

                return true;
            }
            default: {
            }
        }

        return true;
    }

    public interface DigitalPadListener {
        void onDirectionChange(int direction);
    }

    @Override
    protected SuperPageLayout getInfoPage() {
        if (digitalPadPage == null){
            digitalPadPage = (SuperPageLayout) LayoutInflater.from(getContext()).inflate(R.layout.page_digital_pad,null);
            centralXNumberSeekbar = digitalPadPage.findViewById(R.id.page_digital_pad_central_x);
            centralYNumberSeekbar = digitalPadPage.findViewById(R.id.page_digital_pad_central_y);
        }

        NumberSeekbar widthNumberSeekbar = digitalPadPage.findViewById(R.id.page_digital_pad_width);
        NumberSeekbar heightNumberSeekbar = digitalPadPage.findViewById(R.id.page_digital_pad_height);


        TextView upValueTextView = digitalPadPage.findViewById(R.id.page_digital_pad_up_value);
        upValueTextView.setText(pageDeviceController.getKeyNameByValue(elementUpValue));
        upValueTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDeviceController.DeviceCallBack deviceCallBack = new PageDeviceController.DeviceCallBack() {
                    @Override
                    public void OnKeyClick(TextView key) {
                        elementUpValue = key.getTag().toString();
                        // page页设置值文本
                        ((TextView) v).setText(key.getText());
                        // 保存值
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(COLUMN_STRING_ELEMENT_UP_VALUE,elementUpValue);
                        superConfigDatabaseHelper.updateElement(elementId,contentValues);
                        // 设置onClickListener
                        upSenderHandler = elementController.getSendEventHandler(elementUpValue);
                    }
                };
                pageDeviceController.open(deviceCallBack,View.VISIBLE,View.VISIBLE,View.VISIBLE);
            }
        });
        TextView downValueTextView = digitalPadPage.findViewById(R.id.page_digital_pad_down_value);
        downValueTextView.setText(pageDeviceController.getKeyNameByValue(elementDownValue));
        downValueTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDeviceController.DeviceCallBack deviceCallBack = new PageDeviceController.DeviceCallBack() {
                    @Override
                    public void OnKeyClick(TextView key) {
                        elementDownValue = key.getTag().toString();
                        // page页设置值文本
                        ((TextView) v).setText(key.getText());
                        // 保存值
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(COLUMN_STRING_ELEMENT_DOWN_VALUE,elementDownValue);
                        superConfigDatabaseHelper.updateElement(elementId,contentValues);
                        // 设置onClickListener
                        downSenderHandler = elementController.getSendEventHandler(elementDownValue);
                    }
                };
                pageDeviceController.open(deviceCallBack,View.VISIBLE,View.VISIBLE,View.VISIBLE);
            }
        });
        TextView leftValueTextView = digitalPadPage.findViewById(R.id.page_digital_pad_left_value);
        leftValueTextView.setText(pageDeviceController.getKeyNameByValue(elementLeftValue));
        leftValueTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDeviceController.DeviceCallBack deviceCallBack = new PageDeviceController.DeviceCallBack() {
                    @Override
                    public void OnKeyClick(TextView key) {
                        elementLeftValue = key.getTag().toString();
                        // page页设置值文本
                        ((TextView) v).setText(key.getText());
                        // 保存值
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(COLUMN_STRING_ELEMENT_LEFT_VALUE,elementLeftValue);
                        superConfigDatabaseHelper.updateElement(elementId,contentValues);
                        // 设置onClickListener
                        leftSenderHandler = elementController.getSendEventHandler(elementLeftValue);
                    }
                };
                pageDeviceController.open(deviceCallBack,View.VISIBLE,View.VISIBLE,View.VISIBLE);
            }
        });
        TextView rightValueTextView = digitalPadPage.findViewById(R.id.page_digital_pad_right_value);
        rightValueTextView.setText(pageDeviceController.getKeyNameByValue(elementRightValue));
        rightValueTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDeviceController.DeviceCallBack deviceCallBack = new PageDeviceController.DeviceCallBack() {
                    @Override
                    public void OnKeyClick(TextView key) {
                        elementRightValue = key.getTag().toString();
                        // page页设置值文本
                        ((TextView) v).setText(key.getText());
                        // 保存值
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(COLUMN_STRING_ELEMENT_RIGHT_VALUE,elementRightValue);
                        superConfigDatabaseHelper.updateElement(elementId,contentValues);
                        // 设置onClickListener
                        rightSenderHandler = elementController.getSendEventHandler(elementRightValue);
                    }
                };
                pageDeviceController.open(deviceCallBack,View.VISIBLE,View.VISIBLE,View.VISIBLE);
            }
        });

        centralXNumberSeekbar.setProgressMin(centralXMin);
        centralXNumberSeekbar.setProgressMax(centralXMax);
        centralXNumberSeekbar.setValueWithNoCallBack(getParamCentralX());
        centralXNumberSeekbar.setOnNumberSeekbarChangeListener(new NumberSeekbar.OnNumberSeekbarChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                setParamCentralX(progress);
            }

            @Override
            public void onProgressRelease(int lastProgress) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_X,getParamCentralX());
                superConfigDatabaseHelper.updateElement(elementId,contentValues);
            }
        });
        centralYNumberSeekbar.setProgressMin(centralYMin);
        centralYNumberSeekbar.setProgressMax(centralYMax);
        centralYNumberSeekbar.setValueWithNoCallBack(getParamCentralY());
        centralYNumberSeekbar.setOnNumberSeekbarChangeListener(new NumberSeekbar.OnNumberSeekbarChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                setParamCentralY(progress);
            }

            @Override
            public void onProgressRelease(int lastProgress) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_Y,getParamCentralY());
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
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_INT_ELEMENT_HEIGHT,getParamHeight());
                superConfigDatabaseHelper.updateElement(elementId,contentValues);
            }
        });


        NumberSeekbar buttonThickNumberSeekbar = digitalPadPage.findViewById(R.id.page_digital_pad_thick);
        buttonThickNumberSeekbar.setValueWithNoCallBack(thick);
        buttonThickNumberSeekbar.setOnNumberSeekbarChangeListener(new NumberSeekbar.OnNumberSeekbarChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                thick = progress;
                digitalPad.invalidate();
            }

            @Override
            public void onProgressRelease(int lastProgress) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_INT_ELEMENT_THICK,thick);
                superConfigDatabaseHelper.updateElement(elementId,contentValues);
            }
        });

        ElementEditText buttonNormalColorEditText = digitalPadPage.findViewById(R.id.page_digital_pad_normal_color);
        buttonNormalColorEditText.setTextWithNoTextChangedCallBack(String.format("%08X",normalColor));
        buttonNormalColorEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new Element.HexInputFilter()});
        buttonNormalColorEditText.setOnTextChangedListener(new ElementEditText.OnTextChangedListener() {
            @Override
            public void textChanged(String text) {
                if (text.matches("^[A-F0-9]{8}$")){
                    normalColor = (int) Long.parseLong(text, 16);
                    digitalPad.invalidate();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(COLUMN_INT_ELEMENT_NORMAL_COLOR,normalColor);
                    superConfigDatabaseHelper.updateElement(elementId,contentValues);
                }
            }
        });

        ElementEditText buttonPressedColorEditText = digitalPadPage.findViewById(R.id.page_digital_pad_pressed_color);
        buttonPressedColorEditText.setTextWithNoTextChangedCallBack(String.format("%08X",pressedColor));
        buttonPressedColorEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new Element.HexInputFilter()});
        buttonPressedColorEditText.setOnTextChangedListener(new ElementEditText.OnTextChangedListener() {
            @Override
            public void textChanged(String text) {
                if (text.matches("^[A-F0-9]{8}$")){
                    pressedColor = (int) Long.parseLong(text, 16);
                    digitalPad.invalidate();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(COLUMN_INT_ELEMENT_PRESSED_COLOR,pressedColor);
                    superConfigDatabaseHelper.updateElement(elementId,contentValues);
                }
            }
        });

        ElementEditText buttonBackgroundColorEditText = digitalPadPage.findViewById(R.id.page_digital_pad_background_color);
        buttonBackgroundColorEditText.setTextWithNoTextChangedCallBack(String.format("%08X",backgroundColor));
        buttonBackgroundColorEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new Element.HexInputFilter()});
        buttonBackgroundColorEditText.setOnTextChangedListener(new ElementEditText.OnTextChangedListener() {
            @Override
            public void textChanged(String text) {
                if (text.matches("^[A-F0-9]{8}$")){
                    backgroundColor = (int) Long.parseLong(text, 16);
                    digitalPad.invalidate();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(COLUMN_INT_ELEMENT_BACKGROUND_COLOR,backgroundColor);
                    superConfigDatabaseHelper.updateElement(elementId,contentValues);
                }
            }
        });

        digitalPadPage.findViewById(R.id.page_digital_pad_copy).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_LONG_ELEMENT_ID,System.currentTimeMillis());
                contentValues.put(COLUMN_INT_ELEMENT_TYPE, ELEMENT_TYPE_DIGITAL_PAD);
                contentValues.put(COLUMN_STRING_ELEMENT_UP_VALUE,elementUpValue);
                contentValues.put(COLUMN_STRING_ELEMENT_DOWN_VALUE,elementDownValue);
                contentValues.put(COLUMN_STRING_ELEMENT_LEFT_VALUE,elementLeftValue);
                contentValues.put(COLUMN_STRING_ELEMENT_RIGHT_VALUE,elementRightValue);
                contentValues.put(COLUMN_INT_ELEMENT_WIDTH,getParamWidth());
                contentValues.put(COLUMN_INT_ELEMENT_HEIGHT,getParamHeight());
                contentValues.put(COLUMN_INT_ELEMENT_LAYER,layer);
                contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_X,Math.max(Math.min(getParamCentralX() + getParamWidth(),centralXMax),centralXMin));
                contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_Y,getParamCentralY());
                contentValues.put(COLUMN_INT_ELEMENT_THICK,thick);
                contentValues.put(COLUMN_INT_ELEMENT_NORMAL_COLOR,normalColor);
                contentValues.put(COLUMN_INT_ELEMENT_PRESSED_COLOR,pressedColor);
                contentValues.put(COLUMN_INT_ELEMENT_BACKGROUND_COLOR,backgroundColor);
                elementController.copyElement(contentValues);
            }
        });

        digitalPadPage.findViewById(R.id.page_digital_pad_delete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                elementController.toggleInfoPage(digitalPadPage);
                elementController.deleteElement(digitalPad);
            }
        });

        return digitalPadPage;
    }

    @Override
    public void updateDataBase() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_X,getParamCentralX());
        contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_Y,getParamCentralY());
        superConfigDatabaseHelper.updateElement(elementId,contentValues);

    }

    @Override
    protected void updatePageInfo() {
        if (digitalPadPage != null){
            centralXNumberSeekbar.setValueWithNoCallBack(getParamCentralX());
            centralYNumberSeekbar.setValueWithNoCallBack(getParamCentralY());
        }

    }

    public static ContentValues getInitialInfo(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LONG_ELEMENT_ID,System.currentTimeMillis());
        contentValues.put(COLUMN_INT_ELEMENT_TYPE, ELEMENT_TYPE_DIGITAL_PAD);
        contentValues.put(COLUMN_STRING_ELEMENT_UP_VALUE,"k51");
        contentValues.put(COLUMN_STRING_ELEMENT_DOWN_VALUE,"k47");
        contentValues.put(COLUMN_STRING_ELEMENT_LEFT_VALUE,"k29");
        contentValues.put(COLUMN_STRING_ELEMENT_RIGHT_VALUE,"k32");
        contentValues.put(COLUMN_INT_ELEMENT_WIDTH,300);
        contentValues.put(COLUMN_INT_ELEMENT_HEIGHT,300);
        contentValues.put(COLUMN_INT_ELEMENT_LAYER,0);
        contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_X,100);
        contentValues.put(COLUMN_INT_ELEMENT_CENTRAL_Y,100);
        contentValues.put(COLUMN_INT_ELEMENT_THICK,5);
        contentValues.put(COLUMN_INT_ELEMENT_NORMAL_COLOR,0xF0888888);
        contentValues.put(COLUMN_INT_ELEMENT_PRESSED_COLOR,0xF00000FF);
        contentValues.put(COLUMN_INT_ELEMENT_BACKGROUND_COLOR,0x00FFFFFF);
        return contentValues;


    }

}
