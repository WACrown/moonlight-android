package com.limelight.binding.input.virtual_controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Build;

import com.limelight.binding.input.ControllerHandler;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class StreamGyroscopeSensorListener implements SensorEventListener2 {
    private SensorManager mSensorManager;
    private Sensor magneticSensor;
    private Sensor accelerometerSensor;
    private Context mContext;
    private float[] gravity,r,geomagnetic,values;
    private ControllerHandler controllerHandler;


    public StreamGyroscopeSensorListener(Context context, ControllerHandler controllerHandler) {
        mContext = context;
        enableSensor();
        this.controllerHandler = controllerHandler;
    }


    @Override
    public void onFlushCompleted(Sensor sensor) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor == null){
            return;
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = sensorEvent.values;
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = sensorEvent.values;
            getOritation();
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }


    public void enableSensor(){

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this,magneticSensor,SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
        gravity = new float[3];//用来保存加速度传感器的值
        r = new float[9];//
        geomagnetic = new float[3];//用来保存地磁传感器的值
        values = new float[3];
    }

    public void disableSensor(){

        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
            mSensorManager = null;
        }


    }

    public void getOritation() {
        // r从这里返回
        SensorManager.getRotationMatrix(r, null, gravity, geomagnetic);
        //values从这里返回
        SensorManager.getOrientation(r, values);
        //提取数据
        double degreeZ = Math.toDegrees(values[0]);
        double degreeX = Math.toDegrees(values[1]);
        double degreeY = Math.toDegrees(values[2]);

        int stickIndexInt = (int) degreeX * -2184;
        if (stickIndexInt > 32760) {
            stickIndexInt = 32760;
        } else if (stickIndexInt < -32760) {
            stickIndexInt = -32760;
        }
        short stickIndex = (short) stickIndexInt;
        if (stickIndex < 3000 && stickIndex > -3000){
            controllerHandler.sensorStatue((short) 0,(short) 0,(short) 0,(short) 0,(short) 0,(byte) 0,(byte) 0);
        } else {
            controllerHandler.sensorStatue((short) 0,stickIndex ,(short) 0,(short) 0,(short) 0,(byte) 0,(byte) 0);
        }


    }


}
