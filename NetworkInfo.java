
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import static android.content.Context.WIFI_SERVICE;

public class NetworkInfo {

    private AsyncTaskCallbacks callbacks = null;

    private Context applicationContext;

    public NetworkInfoTask(Context context, AsyncTaskCallbacks callbacks){
        applicationContext = context;
    }

    //=========================================================================
    //// Network information

    //現在接続されているネットワークタイプを取得
    private void getNetworkType(LinkedHashMap<String, String> networkInfoMap){
        //ネットワーク情報
        ConnectivityManager cm =
                (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        Network network = cm.getActiveNetwork();
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);

        if (capabilities != null){
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                //wifiの場合
            }else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                //携帯電話の場合
            }else {
                //それ以外の場合
            }
        }else {
            //インターネット接続なし
        }
    }

    //=========================================================================
    //// wifi information
    private void getWifiInfo(LinkedHashMap<String, String> networkInfoMap) {
        WifiManager wifiManager = (WifiManager) applicationContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null) {
        }else {
            //format用のlocale指定
            Locale locale = Locale.JAPAN;

            //SSIDを取得
            String ssId = wifiInfo.getSSID();

            // 受信信号強度&信号レベルを取得
            int rssi = wifiInfo.getRssi();
            int numLevels = 5;
            int level = WifiManager.calculateSignalLevel(rssi, numLevels);

            //BSSIDを取得
            String bssId = wifiInfo.getBSSID();

            // IPアドレスを取得
            int ipAdr = wifiInfo.getIpAddress();
            String ipFormat = "%02d.%02d.%02d.%02d";
            Object[] objectArray = {(ipAdr >> 0) & 0xff, (ipAdr >> 8) & 0xff, (ipAdr >> 16) & 0xff, (ipAdr >> 24) & 0xff};

            // MACアドレスを取得
            String strMacAddress = wifiInfo.getMacAddress();
        }
    }

    private void getAroundWifi(Calendar calendar){
        WifiManager wifiManager = (WifiManager) applicationContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            // access pointをスキャン
            wifiManager.startScan();
            // スキャン結果を取得
            List<ScanResult> accessPointList = wifiManager.getScanResults();
            for (int i = 0; i < accessPointList.size(); i++) {
                ScanResult accessPoint = accessPointList.get(i);
            }
        }
    }
  }


    //携帯番号取得
//    public static String getPhoneNumber(Context context){
//        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//        int simState = telephonyManager.getSimState();
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return null;
//        }
//        try {
//            if (simState == TelephonyManager.SIM_STATE_READY) {
//                // SIMカードが刺さっていて利用可能な状態
//                Log.d(TAG, "simState:" + "SIM_STATE_READY");
//
//                //todo android10以降デバイスIDとシリアル番号はREAD_PRIVILEGED_PHONE_STATEの権限が必要になった。しかしこの権限はサードパーティーアプリに許可されていない
//                if (telephonyManager.getLine1Number() != null){
//                    SharedPreferences preferences = context.getSharedPreferences(ApplicationSettingModel.KEY_USER_ID, Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString(ApplicationSettingModel.KEY_PHONE_NUMBER, telephonyManager.getLine1Number());
//                    editor.apply();
//                    return telephonyManager.getLine1Number();
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }


}
