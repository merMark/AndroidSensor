public class AccelerationSensorTask implements SensorEventListener {
  private Sensor accelerationSensor;
  private SensorManager sensorManager;
  private AsyncTaskCallbacks callbacks = null;
  
  public AccelerationSensorTask(SensorManager sensorManager, AsyncTaskCallbacks callbacks) {
    if (sensorManager != null){
      this.sensorManager = sensorManager;
      this.accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      this.callbacks = callbacks;
      sensorManager.registerListener(this,accelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

@Override
    public void onSensorChanged(SensorEvent event) {
        float sensorX;
        float sensorY;
        float sensorZ;
        if (event != null){
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                Calendar calendar = Calendar.getInstance();

                sensorX = event.values[0];
                sensorY = event.values[1];
                sensorZ = event.values[2];
                //Math.sqrtは平方根
                double absoluteVectorDouble = Math.sqrt(Double.parseDouble(String.valueOf(sensorX * sensorX + sensorY * sensorY + sensorZ * sensorZ)));
                BigDecimal decimal = new BigDecimal(absoluteVectorDouble);
                //小数点第3位を四捨五入
                BigDecimal absoluteVectorDecimal = decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                LinkedHashMap<String, String> accelerationMap = new LinkedHashMap<>();
                accelerationMap.put(RawDataModel.ACCELERATION, absoluteVectorDecimal.toString());
                accelerationMap.put(RawDataModel.ACCELERATION_X, String.valueOf(sensorX));
                accelerationMap.put(RawDataModel.ACCELERATION_Y, String.valueOf(sensorY));
                accelerationMap.put(RawDataModel.ACCELERATION_Z, String.valueOf(sensorZ));
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
  
}
