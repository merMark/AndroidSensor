public class Battery {
  private AsyncTaskCallbacks callbacks = null;
  private Context applicationContext;

  public BatteryInfoTask(Context context, AsyncTaskCallbacks callbacks){
    applicationContext = context;
    this.callbacks = callbacks;
  }

 //バッテリーステータスの総取得
  public void getBatteryInfo(){
    IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Intent batteryStatus = applicationContext.registerReceiver(null, batteryFilter);
    try{
        //チャージ状態の取得
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        //チャージ種別の取得
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        double batteryPct = level * 100 / (float)scale;
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
  
}
