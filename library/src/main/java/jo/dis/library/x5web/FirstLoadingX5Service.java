package jo.dis.library.x5web;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

public class FirstLoadingX5Service extends Service {

	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.i("X5WebView", "service is start");
		QbSdk.preInit(this); //这里必须启用非主进程的service来预热X5内核
		super.onCreate();
	}

}
