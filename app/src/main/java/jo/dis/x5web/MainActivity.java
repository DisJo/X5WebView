package jo.dis.x5web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;

import jo.dis.library.x5web.FirstLoadingX5Service;
import jo.dis.library.x5web.LoadingInterceptor;
import jo.dis.library.x5web.WebViewStateListener;

public class MainActivity extends AppCompatActivity {

    private X5WebContainerView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preinitX5WebCore();
        preinitX5WithService();// 此方法必须在非主进程执行才会有效果
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView = (X5WebContainerView) findViewById(R.id.x5_web_container);
        webView.addLoadingInterceptor(loadingInterceptor);
        webView.addOnWebViewStateListener(webViewStateListener);
        webView.loadUrl("https://www.baidu.com");
//        webView.loadUrl("https://github.com");
        if (webView.getX5WebView().getX5WebViewExtension() != null)
            Log.d("**************", "********************");
    }

    private LoadingInterceptor loadingInterceptor = new LoadingInterceptor() {
        @Override
        public void interceptor(String loadingUrl) {
            Log.d("loadingUrl", loadingUrl);
            webView.loadUrl(loadingUrl);
        }
    };

    private WebViewStateListener webViewStateListener = new WebViewStateListener() {
        @Override
        public void onStartLoading(String url, Bitmap favicon) {

        }

        @Override
        public void onError(WebView view, int errorCode, String description, String failingUrl) {

        }

        @Override
        public void onFinishLoaded(String loadedUrl) {

        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            setTitle(view.getTitle());
        }
    };

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /*
     * 开启额外进程 服务 预加载X5内核， 此操作必须在主进程调起X5内核前进行，否则将不会实现预加载
     */
    private void preinitX5WithService() {
        Intent intent = new Intent(MainActivity.this, FirstLoadingX5Service.class);
        startService(intent);
    }

    /**
     * X5内核在使用preinit接口之后，对于首次安装首次加载没有效果
     * 实际上，X5webview的preinit接口只是降低了webview的冷启动时间；
     * 因此，现阶段要想做到首次安装首次加载X5内核，必须要让X5内核提前获取到内核的加载条件
     */
    private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {// preinit只需要调用一次，如果已经完成了初始化，那么就直接构造view
            QbSdk.preInit(MainActivity.this, myCallback);// 设置X5初始化完成的回调接口
            // 第三个参数为true：如果首次加载失败则继续尝试加载；
        }
    }

    private QbSdk.PreInitCallback myCallback = new QbSdk.PreInitCallback() {

        @Override
        public void onViewInitFinished() {// 当X5webview 初始化结束后的回调
            new WebView(MainActivity.this);
        }

        @Override
        public void onCoreInitFinished() {
        }
    };
}
