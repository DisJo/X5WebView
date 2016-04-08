package jo.dis.x5web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.smtt.sdk.WebView;

import jo.dis.library.x5web.LoadingInterceptor;
import jo.dis.library.x5web.WebViewStateListener;

public class MainActivity extends AppCompatActivity {

    private X5WebContainerView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (X5WebContainerView) findViewById(R.id.x5_web_container);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

}
