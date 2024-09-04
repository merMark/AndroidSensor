import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;


public class LightSensor implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;

    public LightSensorTask(SensorManager sensorManager, AsyncTaskCallbacks callbacks) {
        if (sensorManager != null){
            this.sensorManager = sensorManager;
            this.lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            //センサー取得のListener登録
            sensorManager.registerListener(this,lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event != null){
            if (event.sensor.getType() == Sensor.TYPE_LIGHT){
                String illuminanceValue = String.valueOf(event.values[0]);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
