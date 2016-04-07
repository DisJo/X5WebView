package jo.dis.x5web;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tencent.smtt.sdk.WebView;

import jo.dis.library.x5web.LoadingInterceptor;
import jo.dis.library.x5web.WebViewStateListener;
import jo.dis.library.x5web.X5WebView;

/**
 * Created by dis on 16/4/7.
 */
public class X5WebContainerView extends RelativeLayout {

    private X5WebView x5WebView;
    private ProgressBar progressBar;
    private ViewGroup errorView;

    private final Animation animation = new AlphaAnimation(1f, 0f);

    public X5WebContainerView(Context context) {
        super(context);
        initalize();
    }

    public X5WebContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initalize();
        setupWebSetting(attrs);
    }

    public X5WebContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initalize();
        setupWebSetting(attrs);
    }
    
    private void initalize() {
        bindViews();
        bindWebViewState();
        animation.setDuration(1000);
    }

    private void setupWebSetting(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.x5);
        x5WebView.setupWebSettings(typedArray);
        typedArray.recycle();
    }

    private void bindViews() {
        View.inflate(getContext(), R.layout.layout_x5_web_container, this);
        x5WebView = (X5WebView) findViewById(R.id.web_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorView = (ViewGroup) findViewById(R.id.error_view);
        Button reloadBtn = (Button) findViewById(R.id.reload_button);
        reloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (x5WebView != null) {
                    x5WebView.reload();
                }
            }
        });
    }

    private void bindWebViewState() {
        x5WebView.addOnWebViewStateListener(new WebViewStateListener() {
            @Override
            public void onStartLoading(String url, Bitmap favicon) {
                progressBar.clearAnimation();
                progressBar.setProgress(0);
                progressBar.setVisibility(VISIBLE);
                errorView.setVisibility(GONE);
            }

            @Override
            public void onError(WebView view, int errorCode, String description, String failingUrl) {
                progressBar.setVisibility(GONE);
                x5WebView.setVisibility(GONE);
                errorView.setVisibility(VISIBLE);
            }

            @Override
            public void onFinishLoaded(String loadedUrl) {
                progressBar.setAnimation(animation);
                progressBar.setVisibility(GONE);
                x5WebView.setVisibility(VISIBLE);
                errorView.setVisibility(GONE);
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (x5WebView.getVisibility() != VISIBLE && progress > 80) {
                    x5WebView.setVisibility(VISIBLE);
                }
                progressBar.setProgress(progress);
            }
        });
    }

    public void addOnWebViewStateListener(WebViewStateListener webViewStateListener) {
        x5WebView.addOnWebViewStateListener(webViewStateListener);
    }

    public void addLoadingInterceptor(LoadingInterceptor loadingInterceptor) {
        x5WebView.addLoadingInterceptor(loadingInterceptor);
    }

    public void loadUrl(String url) {
        x5WebView.loadUrl(url);
    }

    public void reloadUrl() {
        x5WebView.reload();
    }

    public boolean canGoBack() {
        return x5WebView.canGoBack();
    }

    public void goBack() {
        x5WebView.goBack();
    }

    public String getTitle() {
        return x5WebView.getTitle();
    }

    public String getUrl() {
        return x5WebView.getUrl();
    }

    public String getUserAgentString() {
        return x5WebView.getSettings().getUserAgentString();
    }

    public void setUserAgentString(String ua) {
        x5WebView.getSettings().setUserAgentString(ua);
    }

    public com.tencent.smtt.sdk.WebSettings getSettings() {
        return x5WebView.getSettings();
    }

    public X5WebView getX5WebView() {
        return x5WebView;
    }
}
