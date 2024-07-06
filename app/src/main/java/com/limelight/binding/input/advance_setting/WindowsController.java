package com.limelight.binding.input.advance_setting;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.limelight.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowsController extends Controller{

    public interface TextWindowListener{
        boolean onConfirmCLick();

        boolean onCancelClick();

    }

    public interface EditTextWindowListener{
        boolean onConfirmClick(String text);

        boolean onCancelClick(String text);

    }

    public interface DeviceWindowListener{
        void onElementClick(String text, String tag);

        void onResetClick();
    }

    private ControllerManager controllerManager;
    private Context context;
    private FrameLayout layout;
    public static int KEYBOARD_DEVICE_MASK = 1;
    public static int MOUSE_DEVICE_MASK = 2;
    public static int GAMEPAD_DEVICE_MASK = 4;


    //text window
    private FrameLayout textWindow;
    private TextView textWindowText;
    private Button textWindowConfirmButton;
    private Button textWindowCancelButton;
    private TextWindowListener textWindowListener;

    //edittext window
    private FrameLayout editTextWindow;
    private EditText editTextWindowText;
    private TextView editTextWindowTitle;
    private Button editTextWindowConfirmButton;
    private Button editTextWindowCancelButton;
    private EditTextWindowListener editTextWindowListener;

    //device window
    private FrameLayout deviceWindow;
    private Button keyboardButton;
    private Button mouseButton;
    private Button gamepadButton;
    private FrameLayout keyboardLayout;
    private FrameLayout mouseLayout;
    private FrameLayout gamepadLayout;
    private Map<Button,FrameLayout> deviceMap = new HashMap<>();
    private DeviceWindowListener deviceWindowListener;




    public WindowsController(ControllerManager controllerManager, FrameLayout layout, Context context){
        this.controllerManager = controllerManager;
        this.context = context;
        this.layout = layout;


        initTextWindow();
        initEditTextWindow();
        initDeviceWindow();
    }

    private void initTextWindow(){
        textWindow = layout.findViewById(R.id.text_window);
        textWindowText = layout.findViewById(R.id.text_window_text);
        textWindowConfirmButton = layout.findViewById(R.id.text_window_confirm);
        textWindowCancelButton = layout.findViewById(R.id.text_window_canel);

        textWindowConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textWindowListener.onConfirmCLick()){
                    textWindow.setVisibility(View.GONE);
                }
            }
        });
        textWindowCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textWindowListener.onCancelClick()){
                    textWindow.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initEditTextWindow(){
        editTextWindow = layout.findViewById(R.id.edittext_window);
        editTextWindowTitle = layout.findViewById(R.id.edittext_window_title);
        editTextWindowText = layout.findViewById(R.id.edittext_window_text);
        editTextWindowConfirmButton = layout.findViewById(R.id.edittext_window_confirm);
        editTextWindowCancelButton = layout.findViewById(R.id.edittext_window_canel);

        editTextWindowConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextWindowText.getText().toString();
                if (editTextWindowListener.onConfirmClick(text)) {
                    editTextWindow.setVisibility(View.GONE);
                }
            }
        });
        editTextWindowCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextWindowText.getText().toString();
                if (editTextWindowListener.onCancelClick(text)) {
                    editTextWindow.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initDeviceWindow(){
        deviceWindow = layout.findViewById(R.id.device_layout_container);
        keyboardButton = layout.findViewById(R.id.keyboard_open_button);
        mouseButton = layout.findViewById(R.id.mouse_open_button);
        gamepadButton = layout.findViewById(R.id.gamepad_open_button);
        keyboardLayout = layout.findViewById(R.id.keyboard_device_layout);
        mouseLayout = layout.findViewById(R.id.mouse_device_layout);
        gamepadLayout = layout.findViewById(R.id.gamepad_device_layout);
        deviceMap.put(keyboardButton,keyboardLayout);
        deviceMap.put(mouseButton,mouseLayout);
        deviceMap.put(gamepadButton,gamepadLayout);

        View.OnClickListener deviceButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDeviceLayout((Button) v);
            }
        };
        keyboardButton.setOnClickListener(deviceButtonOnClickListener);
        mouseButton.setOnClickListener(deviceButtonOnClickListener);
        gamepadButton.setOnClickListener(deviceButtonOnClickListener);

        layout.findViewById(R.id.device_window_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceWindowListener.onResetClick();
                deviceWindow.setVisibility(View.GONE);
            }
        });
        View.OnClickListener keyListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ((TextView)v).getText().toString();
                String tag = ((TextView)v).getTag().toString();
                deviceWindowListener.onElementClick(text,tag);
                deviceWindow.setVisibility(View.GONE);
            }
        };
        LinearLayout keyboardDrawing = keyboardLayout.findViewById(R.id.keyboard_drawing);
        setListenersForDevice(keyboardDrawing,keyListener);

        LinearLayout mouseDrawing = mouseLayout.findViewById(R.id.mouse_drawing);
        setListenersForDevice(mouseDrawing,keyListener);

        LinearLayout gamepadDrawing = gamepadLayout.findViewById(R.id.gamepad_drawing);
        setListenersForDevice(gamepadDrawing,keyListener);

    }
    private void setListenersForDevice(ViewGroup viewGroup, View.OnClickListener listener) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof TextView) {
                child.setOnClickListener(listener);
            } else if (child instanceof ViewGroup) {
                setListenersForDevice((ViewGroup) child, listener);
            }
        }
    }


    private void switchDeviceLayout(Button button){
        for (Map.Entry<Button, FrameLayout> entry: deviceMap.entrySet()){
            Button deviceButton = entry.getKey();
            if (deviceButton == button){
                deviceButton.setAlpha(1f);
                entry.getValue().setVisibility(View.VISIBLE);
            } else {
                deviceButton.setAlpha(0.4f);
                entry.getValue().setVisibility(View.GONE);
            }
        }

    }

    public void openTextWindow(TextWindowListener textWindowListener, String text){
        this.textWindowListener = textWindowListener;
        if (text != null){
            textWindowText.setText(text);
        }
        textWindow.setVisibility(View.VISIBLE);
    }


    public void openEditTextWindow(EditTextWindowListener editTextWindowListener, String text, String hint, String title,int inputType){
        this.editTextWindowListener = editTextWindowListener;
        if (text != null){
            editTextWindowText.setText(text);
        }
        if (hint != null) {
            editTextWindowText.setHint(hint);
        }
        if (title != null){
            editTextWindowTitle.setText(title);
        }
        editTextWindowText.setInputType(inputType);
        editTextWindow.setVisibility(View.VISIBLE);
    }


    public void openDeviceWindow(DeviceWindowListener deviceWindowListener,boolean openKeyboard,boolean openMouse, boolean openGamepad){
        this.deviceWindowListener = deviceWindowListener;
        keyboardButton.setVisibility(View.INVISIBLE);
        mouseButton.setVisibility(View.INVISIBLE);
        gamepadButton.setVisibility(View.INVISIBLE);
        if (openGamepad){
            gamepadButton.setVisibility(View.VISIBLE);
            switchDeviceLayout(gamepadButton);
        }
        if (openMouse){
            mouseButton.setVisibility(View.VISIBLE);
            switchDeviceLayout(mouseButton);
        }
        if (openKeyboard){
            keyboardButton.setVisibility(View.VISIBLE);
            switchDeviceLayout(keyboardButton);
        }
        deviceWindow.setVisibility(View.VISIBLE);


    }

    @Override
    public void close() {

    }
}
