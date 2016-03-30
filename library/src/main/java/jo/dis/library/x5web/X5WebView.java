package jo.dis.library.x5web;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import jo.dis.library.R;

/**
 * Created by Dis on 16/3/29.
 */
public class X5WebView extends WebView {

    private WebViewState state = WebViewState.STOP;

    private List<WebViewStateListener> webViewStateListeners = new ArrayList<>();

    private List<LoadingInterceptor> loadingInterceptors = new ArrayList<>();

    public X5WebView(Context context) {
        super(context);
        initialize();
    }

    public X5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(attributeSet);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(attributeSet);
    }

    private void initialize() {
        setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        setWebViewClient(new WebServiceViewClient());
        setWebChromeClient(new WebServiceChromeClient());
    }

    private void initialize(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.x5);
        setupWebSettings(typedArray);
        typedArray.recycle();
        initialize();
    }

    private void setupWebSettings(TypedArray array) {
        boolean allowContentAccess = array.getBoolean(R.styleable.x5_allow_content_access, true);
        boolean allowFileAccess = array.getBoolean(R.styleable.x5_allow_file_access, true);
        boolean allowFileAccessFromFileURLs = array.getBoolean(R.styleable.x5_allow_file_access_from_file_urls, true);
        boolean allowUniversalAccessFromFileURLs = array.getBoolean(R.styleable.x5_allow_universal_access_from_file_urls, false);
        boolean appCacheEnabled = array.getBoolean(R.styleable.x5_app_cache_enabled, false);
        boolean blockNetworkImage = array.getBoolean(R.styleable.x5_block_network_image, false);
        boolean blockBlockNetworkLoads = array.getBoolean(R.styleable.x5_block_network_loads, false);
        boolean builtInZoomControls = array.getBoolean(R.styleable.x5_built_in_zoom_controls, false);
        int cacheMode = array.getInt(R.styleable.x5_cache_mode, WebSettings.LOAD_DEFAULT);
        boolean databaseEnabled = array.getBoolean(R.styleable.x5_database_enabled, false);
        boolean displayZoomControls = array.getBoolean(R.styleable.x5_display_zoom_controls, false);
        boolean domStorageEnabled = array.getBoolean(R.styleable.x5_dom_storage_enabled, false);
        boolean geolocationEnabled = array.getBoolean(R.styleable.x5_geolocation_enabled, true);
        boolean javaScriptCanOpenWindowsAutomatically = array.getBoolean(R.styleable.x5_java_script_can_open_windows_automatically, false);
        boolean jsEnabled = array.getBoolean(R.styleable.x5_java_script_enabled, false);
        boolean loadWithOverviewMode = array.getBoolean(R.styleable.x5_load_with_overview_mode, false);
        boolean loadsImagesAutomatically  = array.getBoolean(R.styleable.x5_loads_images_automatically, true);
        boolean needInitialFocus = array.getBoolean(R.styleable.x5_need_initial_focus, false);
        boolean saveFormEnabled = array.getBoolean(R.styleable.x5_save_form_data, true);
        boolean supportMultipleWindows = array.getBoolean(R.styleable.x5_support_multiple_windows,false);
        boolean supportZoom = array.getBoolean(R.styleable.x5_support_zoom, true);
        boolean useWideViewPort = array.getBoolean(R.styleable.x5_use_wide_view_port, true);

        WebSettings setting = getSettings();
        setting.setAllowContentAccess(allowContentAccess);
        setting.setAllowFileAccess(allowFileAccess);    // 设置可以访问文件
        setting.setAllowFileAccessFromFileURLs(allowFileAccessFromFileURLs);
        setting.setAllowUniversalAccessFromFileURLs(allowUniversalAccessFromFileURLs);
        setting.setAppCacheEnabled(appCacheEnabled);
        setting.setBlockNetworkImage(blockNetworkImage);
        setting.setBlockNetworkLoads(blockBlockNetworkLoads);
        setting.setBuiltInZoomControls(builtInZoomControls);    // 缩放支持
        setting.setCacheMode(cacheMode);
        setting.setDatabaseEnabled(databaseEnabled);
        setting.setDisplayZoomControls(displayZoomControls);
        setting.setDomStorageEnabled(domStorageEnabled);
        setting.setGeolocationEnabled(geolocationEnabled);  // 启用地理定位
        setting.setJavaScriptCanOpenWindowsAutomatically(javaScriptCanOpenWindowsAutomatically);    // 支持通过JS打开新窗口
        setting.setJavaScriptEnabled(jsEnabled);
        setting.setLoadWithOverviewMode(loadWithOverviewMode);
        setting.setLoadsImagesAutomatically(loadsImagesAutomatically);  // 支持自动加载图片
        setting.setNeedInitialFocus(needInitialFocus);  // 当webview调用requestFocus时为webview设置节点
        setting.setSaveFormData(saveFormEnabled);
        setting.setSupportMultipleWindows(supportMultipleWindows);
        setting.setSupportZoom(supportZoom);    // 支持缩放
        setting.setUseWideViewPort(useWideViewPort);
    }

    private class WebServiceChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView webView, int newProgress) {
            super.onProgressChanged(webView, newProgress);
            if (state == WebViewState.LOADING) {
                for (WebViewStateListener listener : webViewStateListeners) {
                    listener.onProgressChanged(webView, newProgress);
                }
            }
        }
    }

    private class WebServiceViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            super.onPageStarted(webView, url, favicon);
            if (state == WebViewState.STOP) {
                state = WebViewState.LOADING;
                for (WebViewStateListener listener : webViewStateListeners) {
                    listener.onStartLoading(url, favicon);
                }
            }
        }

        @Override
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
            super.onReceivedError(webView, errorCode, description, failingUrl);
            state = WebViewState.ERROR;
            for (WebViewStateListener listener : webViewStateListeners) {
                listener.onError(webView, errorCode, description, failingUrl);
            }
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);
            if (state == WebViewState.LOADING) {
                for (WebViewStateListener listener : webViewStateListeners) {
                    listener.onProgressChanged(webView, 100);
                    listener.onFinishLoaded(url);
                }
            }
            state = WebViewState.STOP;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (url == null || loadingInterceptors == null) {
                return false;
            } else {
                for (LoadingInterceptor loadingInterceptor : loadingInterceptors) {
                    loadingInterceptor.interceptor(url);
                }
                // return super.shouldOverrideUrlLoading(webView, url);
                return true;
            }
        }
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }
}
