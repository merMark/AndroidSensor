
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;


public class Location{

    ArrayList<RawDataModel> rawDataModels;

    public AsyncTaskCallbacks callbacks = null;
    // コンテキスト
    private Context applicationContext;

    //前回と今回で位置情報の変化を監視するフラグ(変化したらtrue)
    public static boolean changeLocationFlag = false;

    //コンストラクタ
    public LocationDetectTask(Context context, AsyncTaskCallbacks callbacks) {
        applicationContext = context;
        this.callbacks = callbacks;
    }


    //------------Google Location Service APIを使った位置情報取得---------------------------

    FusedLocationProviderClient fusedLocationClient;

    //位置情報を1回だけ取得する
    public void getLatestLocation() {
        logUseUtil.debug(TAG, TAG + "," + "getLocation");
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(applicationContext);
        // パーミッションが許可されているか確認
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            //位置情報の取得要求をして5秒たってもコールバックが動かなかった場合
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                //位置情報を取得できたか確認
                if (rawDataModels == null) {
                    Location location;
                    double latitude = 0;
                    double longitude = 0;
                    double altitude = 0;
                    String provider = null;
                    LocationManager locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location == null){   //GPSチップも電話基地局からの取得も出来なかった場合
                            //前の取得したGPS情報で保管する
                            latitude = ApplicationSettingModel.oldLatitude;
                            longitude = ApplicationSettingModel.oldLongitude;
                            altitude = ApplicationSettingModel.oldAltitude;
                            provider = ApplicationSettingModel.oldProvider;
                        }else { // 位置情報を取得できた場合
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            altitude = location.getAltitude();
                            provider = location.getProvider();
                        }
                    }else {
                        // 位置情報を取得できた場合
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        altitude = location.getAltitude();
                        provider = location.getProvider();
                    }
                }
            }, 5000);

        }
    }

    @SuppressLint("MissingPermission")
    //位置情報を更新
    private void startLocationUpdate(FusedLocationProviderClient fusedLocationClient) {
        // 位置情報の取得方法を設定
        LocationRequest.Builder locationRequest = new LocationRequest.Builder(5000);
        // この位置情報要求の優先度
        locationRequest.setWaitForAccurateLocation(true);
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient.requestLocationUpdates(locationRequest.build(), new MyLocationCallback(), new Handler().getLooper());
    }

    /**
     * 位置情報受取コールバッククラス
     */
    public class MyLocationCallback extends LocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult == null) {
                Log.i(TAG, "locationResult is null");
                return;
            }

            Location lastLocation = locationResult.getLastLocation();
            if (lastLocation == null) {
                return;
            }
            // 位置情報を取得できた場合
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            double altitude = lastLocation.getAltitude();
            String provider = lastLocation.getProvider();

            // 現在地だけ欲しいので、1回取得したらすぐに外す
            fusedLocationClient.removeLocationUpdates(this);
        }
    }


}
