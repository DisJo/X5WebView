package jo.dis.library.x5web;

import android.graphics.Bitmap;

import com.tencent.smtt.sdk.WebView;

/**
 * Created by Dis on 16/3/29.
 */
public interface WebViewStateListener {

    void onStartLoading(String url, Bitmap favicon);

    void onError(WebView view, int errorCode, String description, String failingUrl);

    void onFinishLoaded(String loadedUrl);

    void onProgressChanged(WebView view, int progress);

}
