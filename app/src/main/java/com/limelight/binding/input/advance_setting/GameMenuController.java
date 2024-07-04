package com.limelight.binding.input.advance_setting;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.limelight.R;
import com.limelight.binding.input.KeyboardTranslator;

import java.util.ArrayList;
import java.util.List;

public class GameMenuController {

    private Context context;
    private LinearLayout addCombineKeyBox;
    private LinearLayout floatKeyboardBox;
    private ControllerManager controllerManager;
    private KeyboardTranslator keyboardTranslator;
    private GameMenuPreference gameMenuPreference;

    public GameMenuController(FrameLayout frameLayout,ControllerManager controllerManager,Context context) {
        this.context = context;
        this.floatKeyboardBox = frameLayout.findViewById(R.id.float_keyboard_box);
        this.addCombineKeyBox = frameLayout.findViewById(R.id.add_combine_box);
        this.controllerManager = controllerManager;
        this.keyboardTranslator = new KeyboardTranslator();

        initFloatKeyboard();
        initAddCombineKey();
    }

    private void initFloatKeyboard(){

        SeekBar opacitySeekbar = floatKeyboardBox.findViewById(R.id.float_keyboard_seekbar);
        LinearLayout keyboardLayout = floatKeyboardBox.findViewById(R.id.float_keyboard);
        LinearLayout keyboard = floatKeyboardBox.findViewById(R.id.keyboard_drawing);


        opacitySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float alpha = (float) (progress * 0.1);
                keyboardLayout.setAlpha(alpha);
                opacitySeekbar.setAlpha(alpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String keyString = (String) v.getTag();
                int keyCode = Integer.parseInt(keyString.substring(1));
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 处理按下事件
                        controllerManager.getElementController().sendKeyEvent(true,(short) keyCode);
                        v.setBackgroundResource(R.drawable.confirm_square_border);
                        return true;
                    case MotionEvent.ACTION_UP:
                        // 处理释放事件
                        controllerManager.getElementController().sendKeyEvent(false,(short) keyCode);
                        v.setBackgroundResource(R.drawable.square_border);
                        return true;
                }
                return false;
            }
        };
        for (int i = 0; i < keyboard.getChildCount(); i++){
            LinearLayout keyboardRow = (LinearLayout) keyboard.getChildAt(i);
            for (int j = 0; j < keyboardRow.getChildCount(); j++){
                keyboardRow.getChildAt(j).setOnTouchListener(touchListener);
            }
        }




    }

    private void initAddCombineKey(){
        TextView[] addCombineKeys = new TextView[]{
                addCombineKeyBox.findViewById(R.id.add_combine_key_1),
                addCombineKeyBox.findViewById(R.id.add_combine_key_2),
                addCombineKeyBox.findViewById(R.id.add_combine_key_3),
                addCombineKeyBox.findViewById(R.id.add_combine_key_4),
                addCombineKeyBox.findViewById(R.id.add_combine_key_5)
        };
        EditText editTextName = addCombineKeyBox.findViewById(R.id.add_combine_key_name);
        for (TextView key : addCombineKeys){
            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jumpDeviceWindow(key);
                }
            });
        }

        addCombineKeyBox.findViewById(R.id.add_combine_key_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                if (!name.matches("^.{1,10}$")){
                    Toast.makeText(context,"名称只能由1-20个字符组成",Toast.LENGTH_SHORT).show();
                    return;
                }

                GameMenuPreference gameMenuPreference = new GameMenuPreference(context);
                Long birthTime = System.currentTimeMillis();
                List<Short> keysValueList = new ArrayList<>();
                List<String> keysNameList = new ArrayList<>();
                for (TextView key : addCombineKeys){
                    short preKeyValue = (short) Integer.parseInt(((String) key.getTag()).substring(1));
                    short keyValue = keyboardTranslator.translate(preKeyValue,-1);
                    if (keyValue != -1){
                        keysValueList.add(keyValue);
                        keysNameList.add(key.getText().toString());
                    }
                }
                short[] keysValue = new short[keysValueList.size()];
                for (int i = 0;i < keysValueList.size();i ++){
                    keysValue[i] = keysValueList.get(i);
                }


                gameMenuPreference.addGameMenuSpecialKey(new GameMenuSpecialKeyBean(
                        "" + birthTime,
                        editTextName.getText().toString(),
                        keysValue,
                        keysNameList.toArray(new String[0]),
                        birthTime));
                addCombineKeyBox.setVisibility(View.GONE);

            }
        });

        addCombineKeyBox.findViewById(R.id.add_combine_key_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCombineKeyBox.setVisibility(View.GONE);
            }
        });


    }

    public void jumpDeviceWindow(TextView key){
        WindowsController.DeviceWindowListener keySelectListener = new WindowsController.DeviceWindowListener() {
            @Override
            public void onElementClick(String text, String tag) {
                key.setText(text);
                key.setTag(tag);
            }

            @Override
            public void onResetClick() {
                key.setText("");
                key.setTag("-1");
            }
        };
        controllerManager.getWindowsController().openDeviceWindow(keySelectListener,true,false,false);
    }

    public void showAddGameMenuSpecialKey(){
        addCombineKeyBox.setVisibility(View.VISIBLE);
    }

    public void ToggleKeyboard(){
        if (floatKeyboardBox.getVisibility() == View.VISIBLE){
            System.out.println("wg_debug keyboard close");
            floatKeyboardBox.setVisibility(View.GONE);
        } else {
            System.out.println("wg_debug keyboard open");
            floatKeyboardBox.setVisibility(View.VISIBLE);
        }
    }

    public void deleteSpecialKey(GameMenuSpecialKeyBean bean){
        gameMenuPreference.deleteGameMenuSpecialKey(bean);
        Toast.makeText(context,"指令删除成功",Toast.LENGTH_SHORT).show();
    }


    public List<GameMenuSpecialKeyBean> loadGameMenuConfig(String configId){
        gameMenuPreference = new GameMenuPreference(context);
        return gameMenuPreference.loadGameMenuSpecialKeys();
    }
}
