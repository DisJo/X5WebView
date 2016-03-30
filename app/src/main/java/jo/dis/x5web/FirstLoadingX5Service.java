package jo.dis.x5web;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

public class FirstLoadingX5Service extends Service {

	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i("yuanhaizhou", "service is start");
		QbSdk.preInit(this); //这里必须启用非主进程的service来预热X5内核
		super.onCreate();
	}

}
