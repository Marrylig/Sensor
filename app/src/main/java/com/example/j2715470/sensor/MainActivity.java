package com.example.j2715470.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tvAccelerometer;
    private TextView tvMagentic;
    private TextView tvLight;
    private TextView tvOrientation;
    private TextView tvSensors;
    private SensorManager sensorManager;
    private float[] axxelerometerValues;
    private float[] geomagneticValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);

        tvAccelerometer=(TextView)findViewById(R.id.tvAccelerometer);
        tvMagentic=(TextView)findViewById(R.id.tvMagentic);
        tvLight=(TextView)findViewById(R.id.tvLight);
        tvOrientation=(TextView)findViewById(R.id.tvOrientation);
        tvSensors=(TextView)findViewById(R.id.tvSensors);

        List<Sensor> sensors=sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor:sensors){
            tvSensors.append(sensor.getName()+"\n");
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                String accelerometer="加速度\n"+"X:"
                        +event.values[0]+"\n"
                        +"Y:"+event.values[1]+"\n"
                        +"Z:"+event.values[2]+"\n";
                tvAccelerometer.setText(accelerometer);
                axxelerometerValues=event.values.clone();
                break;
            case Sensor.TYPE_LIGHT:
                tvLight.setText("亮度："+event.values[0]);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                String magentic="磁场\n"+"X："+event.values[0]+"\n"
                        +"Y："+event.values[1]+"\n"
                        +"Z："+event.values[2]+"\n";
                break;
            case Sensor.TYPE_ORIENTATION:
                if(geomagneticValues!=null && axxelerometerValues!=null){
                    float[] R=new float[16];
                    float[] I=new float[16];
                    float[] outR=new float[16];
                    sensorManager.getRotationMatrix(R,I,axxelerometerValues,geomagneticValues);
                    sensorManager.remapCoordinateSystem(R,SensorManager.AXIS_X,
                            SensorManager.AXIS_Y,outR);

                    float[] actual_orientation=new float[3];
                    actual_orientation=sensorManager.getOrientation(outR,actual_orientation);
                    String orientation="方向\n"+"X："+event.values[0]+"\n"
                            +"Y："+event.values[1]+"\n"
                            +"Z："+event.values[2]+"\n";
                    tvOrientation.setText(orientation);
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
