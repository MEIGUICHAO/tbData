package com.example.webtest;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.webtest.base.MyWebView;
import com.example.webtest.base.WA_YundaFragment;
import com.example.webtest.io.SharedPreferencesUtils;
import com.example.webtest.io.WA_Parameters;

/**
 * @desc 自动化Fragment主调页面
 * @author z.h
 */
public class WA_MainFragment extends WA_YundaFragment
{
	private static final String ARG_CODE = "WebAutoFragment";
	private static final String BASIC_JS_PATH = "basic_inject.js";
	private static final String LOGIC_JS_PATH = "logic_inject.js";

	private LocalMethod mLocalMethod;
	private WA_Parameters parameter;
	private String injectJS;

	/**  通过静态方法实例化自动化Fragment*/
	public static void start(Activity mContext, int containerRsID, WA_Parameters parameter)
	{
		WA_MainFragment mCurrentFragment = WA_MainFragment.getInstence(parameter);
		mContext.getFragmentManager().beginTransaction().replace(containerRsID, mCurrentFragment).commit();
	}

	private static WA_MainFragment getInstence(WA_Parameters parameter)
	{
		WA_MainFragment webAutoFragment = new WA_MainFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(ARG_CODE, parameter);
		webAutoFragment.setArguments(bundle);
		return webAutoFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		instrumentation = new Instrumentation();
		if (null != bundle)
		{
			parameter = (WA_Parameters) bundle.getSerializable(ARG_CODE);
		}
		Resources res = getResources();
//		shops = res.getStringArray(R.array.classify);
		shops = res.getStringArray(R.array.classify);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.wa_fragment_main, container, false);
		findViews(view);
		initData();
		setListener(view);
		return view;
	}

	private void findViews(View view)
	{
		listWeb = (MyWebView) view.findViewById(R.id.wa_webview_list);
		detailWeb = (WebView) view.findViewById(R.id.wa_webview_detail);
		btnRefresh = (Button) view.findViewById(R.id.btn_refresh);
		btnBack = (Button) view.findViewById(R.id.btn_back);
		btnSearch = (Button) view.findViewById(R.id.btn_search);
		btnGosearch = (Button) view.findViewById(R.id.btn_gosearch);
		btnGosearchworld = (Button) view.findViewById(R.id.btn_gosearchworld);
		btnGetchecked = (Button) view.findViewById(R.id.btn_getchecked);
		btn_check = (Button) view.findViewById(R.id.btn_check);
		btn_biao1 = (Button) view.findViewById(R.id.btn_biao1);
		btn_str_result = (Button) view.findViewById(R.id.btn_str_result);
	}

	/** 初始化两个不同功用的WebView */
	private void initData()
	{
		initListWeb();
		initDetailWeb();
		//deleteLog();
		injectJS = getJsFromFile(getActivity(), BASIC_JS_PATH) + getJsFromFile(getActivity(), LOGIC_JS_PATH);
	}

	private void initListWeb()
	{
		WebSettings webSetting = listWeb.getSettings();


		// 支持获取手势焦点
		listWeb.requestFocusFromTouch();
		listWeb.setHorizontalFadingEdgeEnabled(true);
		listWeb.setVerticalFadingEdgeEnabled(false);
		listWeb.setVerticalScrollBarEnabled(false);
		// 支持JS
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setDisplayZoomControls(true);
		webSetting.setLoadWithOverviewMode(true);
		// 支持插件
		webSetting.setPluginState(WebSettings.PluginState.ON);
		webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		// 自适应屏幕
		webSetting.setUseWideViewPort(true);
		// 支持缩放
		webSetting.setSupportZoom(false);//就是这个属性把我搞惨了，
		// 隐藏原声缩放控件
		webSetting.setDisplayZoomControls(false);
		// 支持内容重新布局
//		webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

		webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.supportMultipleWindows();
		webSetting.setSupportMultipleWindows(true);
		// 设置缓存模式
		webSetting.setDomStorageEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
		webSetting.setAppCacheEnabled(true);
		webSetting.setAppCachePath(listWeb.getContext().getCacheDir().getAbsolutePath());
		// 设置可访问文件
		webSetting.setAllowFileAccess(true);
		webSetting.setNeedInitialFocus(true);
		// 支持自定加载图片
		if (Build.VERSION.SDK_INT >= 19) {
			webSetting.setLoadsImagesAutomatically(true);
		} else {
			webSetting.setLoadsImagesAutomatically(false);
		}
		webSetting.setNeedInitialFocus(true);
		// 设定编码格式
		webSetting.setDefaultTextEncodingName("UTF-8");



		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
//支持js


		webSetting.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
//自适应屏幕
//自动缩放
		webSetting.setBuiltInZoomControls(true);
		webSetting.setSupportZoom(true);

//支持获取手势焦点


//
//		webSetting.setJavaScriptEnabled(true);
//		webSetting.setDefaultTextEncodingName("utf-8");
//		webSetting.setAllowFileAccess(true);
//		webSetting.setUseWideViewPort(true);
//		webSetting.setLoadWithOverviewMode(true);
		webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);



		listWeb.loadUrl(Url);
		listWeb.setWebViewClient(new MyListWebViewClient());
		mLocalMethod = new WA_YundaFragment.LocalMethod(getActivity(), parameter);
		listWeb.addJavascriptInterface(mLocalMethod, "localMethod");
//		webSetting.setDefaultZoom(WebSettings.ZoomDensity.FAR);
	}

	private void initDetailWeb()
	{
		WebSettings webSetting = detailWeb.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setDefaultTextEncodingName("utf-8");
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAllowFileAccess(true);

		detailWeb.setWebViewClient(new MyDetailWebViewClient());
	}

	private void setListener(View view)
	{

		btnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listWeb.reload();
			}
		});
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listWeb.goBack();
			}
		});
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				TAOBAO = shops[0];
				if (index == shops.length) {
					index = 0;
				}
				goSearch(shops[index]);
				if (index<shops.length){
					index++;
				}
			}
		});
		btnGosearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				goSearchClick();
			}
		});
		btnGosearchworld.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				goSearchWord();
			}
		});
		btnGetchecked.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				goGetChecked();
				saleDesc();
			}
		});
		btn_check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				saleDesc();
			}
		});
		btn_biao1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				biao1();
			}
		});
		btn_str_result.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {


//				SharedPreferencesUtils.putValue(getActivity(), TAOBAO, shops[index]+"rc", str);
//				SharedPreferencesUtils.putValue(getActivity(), TAOBAO, shops[index]+"zjl", str);
//				SharedPreferencesUtils.putValue(getActivity(), TAOBAO, shops[index]+"title", str);
//				TAOBAO = shops[0];
				for (int i = 0; i < shops.length; i++) {
					String str = SharedPreferencesUtils.getValue(getActivity(),TAOBAO,shops[i]+"zjl","");
					String rcstr = SharedPreferencesUtils.getValue(getActivity(),TAOBAO,shops[i]+"rc","");
					String titlestr = SharedPreferencesUtils.getValue(getActivity(),TAOBAO,shops[i]+"title","");
					Log.e("resultStr!!! ",str );
					Log.e("rcstr!!! ",rcstr );
					Log.e("titlestr!!! ",titlestr );
				}

			}
		});
//		startBtn.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				new Thread()
//				{
//					@Override
//					public void run()
//					{
//						setPswName();
//						doSleep(2);
		// step1：点击搜索框进行商品搜索##########################（Page1-首页）
//						doEnterSearchPage();
////						doSleep(2);
////
////						// step2：选择店铺类型####################################（Page2-搜索页面）
////						doSelectStoreType(parameter);
////						doSleep(2);
////
////						// step3：输入搜索关键字并点击搜索按钮#####################（Page2-搜索页面）
////						doSearch(parameter);
////						doSleep(6);
////
////						// step4：根据筛选条件和销量优先顺序排序###################（Page2-搜索页面）
////						doOrderBySellAmount();
////						doSleep(3);
////
////						// step5：浏览选择指定商品#################################（Page2-搜索页面——>Page3-详情页面）
////						doScan(parameter);
//					}
//				}.start();
//			}
//		});
	}

	/** ListWebView加载完注入基本JS函数 */
	private class MyListWebViewClient extends WebViewClient
	{
		@Override
		public void onPageFinished(WebView view, String url)
		{
			view.loadUrl("javascript:" + injectJS);

			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			super.onPageStarted(view, url, favicon);
		}
	}

	/** DetailWebView加载完注入基本JS函数并且关闭对话框 */
	private class MyDetailWebViewClient extends WebViewClient
	{
		@Override
		public void onPageFinished(WebView view, String url)
		{
			view.loadUrl("javascript:" + injectJS);
			doAlertHide();
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			super.onPageStarted(view, url, favicon);
		}
	}

}
