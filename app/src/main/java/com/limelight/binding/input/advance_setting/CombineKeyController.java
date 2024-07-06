package com.limelight.binding.input.advance_setting;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.limelight.R;
import com.limelight.binding.input.KeyboardTranslator;

import java.util.ArrayList;
import java.util.List;

public class CombineKeyController extends Controller{

    private Context context;
    private FrameLayout combineKeyLayout;
    private ControllerManager controllerManager;
    private KeyboardTranslator keyboardTranslator;
    private CombineKeyPreference combineKeyPreference;
    private int visibility = View.GONE;

    public CombineKeyController(FrameLayout combineKeyLayout, ControllerManager controllerManager, Context context) {
        this.context = context;
        this.combineKeyLayout = combineKeyLayout;
        this.controllerManager = controllerManager;
        this.keyboardTranslator = new KeyboardTranslator();

        TextView[] addCombineKeys = new TextView[]{
                combineKeyLayout.findViewById(R.id.add_combine_key_1),
                combineKeyLayout.findViewById(R.id.add_combine_key_2),
                combineKeyLayout.findViewById(R.id.add_combine_key_3),
                combineKeyLayout.findViewById(R.id.add_combine_key_4),
                combineKeyLayout.findViewById(R.id.add_combine_key_5)
        };
        EditText editTextName = combineKeyLayout.findViewById(R.id.add_combine_key_name);
        for (TextView key : addCombineKeys){
            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jumpDeviceWindow(key);
                }
            });
        }

        combineKeyLayout.findViewById(R.id.add_combine_key_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                if (!name.matches("^.{1,10}$")){
                    Toast.makeText(context,"名称只能由1-20个字符组成",Toast.LENGTH_SHORT).show();
                    return;
                }

                CombineKeyPreference combineKeyPreference = new CombineKeyPreference(context);
                Long birthTime = System.currentTimeMillis();
                List<Short> keysValueList = new ArrayList<>();
                List<String> keysNameList = new ArrayList<>();
                for (TextView key : addCombineKeys){
                    short preKeyValue = (short) Integer.parseInt(((String) key.getTag()).substring(1));short keyValue = keyboardTranslator.translate(preKeyValue,-1);
                    if (keyValue != -1){
                        keysValueList.add(keyValue);
                        keysNameList.add(key.getText().toString());
                    }
                }
                short[] keysValue = new short[keysValueList.size()];
                for (int i = 0;i < keysValueList.size();i ++){
                    keysValue[i] = keysValueList.get(i);
                }


                combineKeyPreference.addCombineKey(new CombineKeyBean(
                        "" + birthTime,
                        editTextName.getText().toString(),
                        keysValue,
                        keysNameList.toArray(new String[0]),
                        birthTime));
                combineKeyLayout.setVisibility(View.GONE);

            }
        });

        combineKeyLayout.findViewById(R.id.add_combine_key_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                combineKeyLayout.setVisibility(View.GONE);
            }
        });

    }


    private void jumpDeviceWindow(TextView key){
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

    public void deleteCombineKey(CombineKeyBean bean){
        combineKeyPreference.deleteCombineKey(bean);
        Toast.makeText(context,"指令删除成功",Toast.LENGTH_SHORT).show();
    }


    public List<CombineKeyBean> loadCombineKeyConfig(){
        combineKeyPreference = new CombineKeyPreference(context);
        return combineKeyPreference.loadCombineKeys();
    }

    public void open() {
        visibility = View.VISIBLE;
        combineKeyLayout.setVisibility(visibility);
    }


    public void close() {
        visibility = View.GONE;
        combineKeyLayout.setVisibility(visibility);
    }
}
