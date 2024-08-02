package com.limelight.binding.input.advance_setting.combinekey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.limelight.R;
import com.limelight.binding.input.KeyboardTranslator;
import com.limelight.binding.input.advance_setting.ControllerManager;
import com.limelight.binding.input.advance_setting.PageDeviceController;
import com.limelight.binding.input.advance_setting.superpage.SuperPageLayout;

import java.util.ArrayList;
import java.util.List;

public class PageCombineKeyController {

    private Context context;
    private SuperPageLayout combineKeyPage;
    private ControllerManager controllerManager;
    private KeyboardTranslator keyboardTranslator;
    private CombineKeyPreference combineKeyPreference;

    public PageCombineKeyController(ControllerManager controllerManager, Context context) {
        this.context = context;
        this.combineKeyPage = (SuperPageLayout) LayoutInflater.from(context).inflate(R.layout.page_combine_key,null);
        this.controllerManager = controllerManager;
        this.keyboardTranslator = new KeyboardTranslator();

        TextView[] addCombineKeys = new TextView[]{
                combineKeyPage.findViewById(R.id.add_combine_key_1),
                combineKeyPage.findViewById(R.id.add_combine_key_2),
                combineKeyPage.findViewById(R.id.add_combine_key_3),
                combineKeyPage.findViewById(R.id.add_combine_key_4),
                combineKeyPage.findViewById(R.id.add_combine_key_5)
        };




        for (TextView key : addCombineKeys){
            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    PageDeviceController.DeviceCallBack deviceCallBack = new PageDeviceController.DeviceCallBack() {
                        @Override
                        public void OnKeyClick(TextView key) {
                            ((TextView) v).setText(key.getText());
                            v.setTag(key.getTag());
                        }

                        @Override
                        public void OnResetKeyClick() {
                            ((TextView) v).setText("");
                            v.setTag("k-1");
                        }
                    };
                    controllerManager.getDevicePageController().open(deviceCallBack,View.VISIBLE,View.GONE,View.GONE);
                }
            });
        }


        EditText editTextName = combineKeyPage.findViewById(R.id.add_combine_key_name);
        combineKeyPage.findViewById(R.id.add_combine_key_ensure).setOnClickListener(new View.OnClickListener() {
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
                controllerManager.getSuperPagesController().close();
            }
        });

        combineKeyPage.findViewById(R.id.add_combine_key_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerManager.getSuperPagesController().close();
            }
        });

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
        controllerManager.getSuperPagesController().open(combineKeyPage);
    }


    public void close() {

    }
}
