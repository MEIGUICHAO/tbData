package com.example.webtest;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.webtest.base.Constant;
import com.example.webtest.base.MyWebView;
import com.example.webtest.base.WA_YundaFragment;
import com.example.webtest.io.LogUtil;
import com.example.webtest.io.SharedPreferencesUtils;
import com.example.webtest.io.WA_Parameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * @desc 自动化Fragment主调页面
 * @author z.h
 */
public class WA_MainFragment extends WA_YundaFragment implements View.OnClickListener {
	private static final String ARG_CODE = "WebAutoFragment";
	private static final String BASIC_JS_PATH = "basic_inject.js";
	private static final String LOGIC_JS_PATH = "logic_inject.js";

	private LocalMethod mLocalMethod;
	private WA_Parameters parameter;
	private ArrayList<String> mTitleList = new ArrayList<String>();
	private TextView tv_title;
	private String mTitleStr = "---";
	private Button btn_keyword_split, btn_title_result;
	private String[] keywordSplit;
	private int titleCount;
	private int shopCount;
	private ArrayList<String> outputTitleList;
	private EditText et_filter;
	private String mshop = Constant.SHOP_LIST;


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
//		shops = res.getStringArray(R.array.classify);
		shops = mshop.split("\n");
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

		btn_display = (Button) view.findViewById(R.id.btn_display);
		btn_et_displays = (Button) view.findViewById(R.id.btn_et_displays);
		btn_repeat_out = (Button) view.findViewById(R.id.btn_repeat_out);
		btn_title_out = (Button) view.findViewById(R.id.btn_title_out);
		btn_process = (Button) view.findViewById(R.id.btn_process);
		btn_reset = (Button) view.findViewById(R.id.btn_reset);
		btn_keyword_split = (Button) view.findViewById(R.id.btn_keyword_split);
		btn_title_result = (Button) view.findViewById(R.id.btn_title_result);
		ll_title = (LinearLayout) view.findViewById(R.id.ll_title);
        et_title = (EditText) view.findViewById(R.id.et_title);
		et_index = (EditText) view.findViewById(R.id.et_index);
		et_filter = (EditText) view.findViewById(R.id.et_filter);
		tv_title = (TextView) view.findViewById(R.id.tv_title);



		btn_sort_result = (Button) view.findViewById(R.id.btn_sort_result);
		btn_sort_title = (Button) view.findViewById(R.id.btn_sort_title);

		btn_sort_result.setOnClickListener(this);
		btn_sort_title.setOnClickListener(this);

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



		webSetting.setJavaScriptEnabled(true);
		webSetting.setDefaultTextEncodingName("utf-8");
		webSetting.setAllowFileAccess(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setLoadWithOverviewMode(true);
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


				refreshSearch();
//				listWeb.reload();
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
//				TAOBAO = shops[0];
				goSearch(shops[index]);
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
//				saleDesc();
				goNextPage();
			}
		});
		btn_display.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				etShow();

			}
		});
		btn_process.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					getProcess();
				} catch (Exception e) {

				}
			}
		});
		btn_et_displays.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (ll_title.getVisibility() == View.VISIBLE) {
//					ll_title.setVisibility(View.GONE);
//				} else {
//					ll_title.setVisibility(View.VISIBLE);
//				}
				getLastTitle(mTempleList);

			}
		});
		btn_repeat_out.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					repeatOut();
				} catch (Exception e) {

				}
			}
		});
		btn_reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != titleSortMap) {
					titleSortMap.clear();
				}
				if (null != titleList) {
					titleList.clear();
				}
				if (null != mTitleList) {
					mTitleList.clear();
				}
				mTitleStr = "";
				titleIdex = 0;
			}
		});
		btn_title_out.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = "";
				for (int i = 0; i < mTitleList.size(); i++) {
					str = str + mTitleList.get(i) + "\n";
				}
				LogUtil.e("-------titleSize-------:" + mTitleList.size() + "\n" + str);
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
		btn_keyword_split.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				keywordSplite();
				new Thread(new Runnable() {
					@Override
					public void run() {
						keywordAndTitle();
					}
				}).start();



			}
		});
		btn_title_result.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					getTitleResutl();
				} catch (Exception e) {
					LogUtil.e(e.toString());
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

	private void keywordAndTitle() {
		String value = SharedPreferencesUtils.getValue(getActivity(), "TAOBAO", shops[index] + "titleSort", "");
		String[] titles = value.split("###");
		String resutlStr = "¥¥¥¥";
		String result1 = "";
		String result2 = "";
		String result3 = "";
		String result4 = "";
		String result5 = "";
		String result6 = "";
		String result7 = "";
		String result8 = "";
		String result9 = "";
		String result10 = "";
		int length = titles.length;
		if (length > 200) {
			length = 200;
		}
		for (int i = 0; i < length; i++) {
			for (int j = i+1; j < length; j++) {
				result1 = maxSubstring(titles[i], titles[j]);

				if (!TextUtils.isEmpty(result1)&&!resutlStr.contains(result1)) {
					resutlStr = resutlStr + "###" + result1;
				}
			}
        }
		LogUtil.e(resutlStr);
		String[] keywordStrs = resutlStr.split("###");
		LogUtil.e(resutlStr.split("###").length + "");

		ArrayList<String> list = new ArrayList<String>();
		for (int i = 1; i < keywordStrs.length; i++) {
            list.add(keywordStrs[i]);
        }

		//去重
		for (int i = 0; i < list.size(); i++) {
            for(int j = i + 1;j < list.size();j++){
                if(list.get(i).contains(list.get(j))||list.get(j).contains(list.get(i))){
                    if (strLength(list.get(i)) > strLength(list.get(j))) {
                        list.remove(j); //remove(int index)
						j--;
                    } else {
                        list.remove(i);
						if (i != 0) {
							i--;
						}
                    }
                                //一定要记住j--，不然会出错
                }
            }
        }

		//去重
		for (int i = 0; i < list.size(); i++) {
            for(int j = i + 1;j < list.size();j++){
                if (!TextUtils.isEmpty(list.get(i)) && !TextUtils.isEmpty(list.get(j))) {

                    String samestr = maxSubstring(list.get(i), list.get(j));
                    int iStr = strLength(list.get(i));
                    int jStr = strLength(list.get(j));
                    if (!TextUtils.isEmpty(samestr)) {
                        if (iStr >= jStr && jStr < 1.5 * strLength(samestr)) {
                            String replace = list.get(j).replace(samestr, "");
                            list.remove(j);


                            list.add(j, replace);
                        } else if (iStr < 1.5 * strLength(samestr)){
                            String replace = list.get(i).replace(samestr, "");
                            list.remove(i);
                            list.add(i, replace);
                        }
                    }
                }
            }
        }




		mTempleList = list;

		try {
			getLastTitle(list);
		} catch (Exception e) {

		}
	}

	private void getLastTitle(ArrayList<String> list) {

		String keywordStr = "";
		String filter = et_filter.getText().toString();

		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			String str = it.next();
			if (TextUtils.isEmpty(str.trim())||filter.contains(str)) {
				it.remove();
			}
		}
		for (int i = 0; i < list.size(); i++) {

            if (TextUtils.isEmpty(keywordStr)) {
                keywordStr = list.get(i) + "\n";
            } else {
                keywordStr = keywordStr + list.get(i) + "\n";
            }
        }
		LogUtil.e(list.size() + "");
		LogUtil.e(keywordStr);
		String templeStr = "";
		String titleOutPut1 = "---------title-----------" + "\n";
		String titleOutPut2 = "---------title-----------" + "\n";
		for (int i = 0; i < 50; i++) {
            int[] ints = randomArray(list.size()-1);
            templeStr = "";
            for (int j = 0; j < list.size(); j++) {
                if (strLength(templeStr) + strLength(list.get(ints[j]))< 120) {
                    if (ints[j] < list.size()) {
                        templeStr = templeStr + list.get(ints[j]);
                    }
				} else if (strLength(templeStr) < 100) {
					continue;
				} else {
					break;
				}
            }
			if (i <= 24) {
				titleOutPut1 = titleOutPut1 + templeStr + "\n";
			} else {
				titleOutPut2 = titleOutPut2 + templeStr + "\n";
			}
		}

		LogUtil.e(titleOutPut1);
		LogUtil.e(titleOutPut2);
	}

	private void keywordSplite() {
		try {

            String etString = et_title.getText().toString();
            String[] part = etString.split("##");
            keywordSplit = part[0].split("#");
            String[] titleShop = part[1].split("#");
            titleCount = Integer.parseInt(titleShop[0]);
            shopCount = Integer.parseInt(titleShop[1]);
            LogUtil.e("etString:" + etString + ",keywordSplit_size:" + keywordSplit.length + ",titleCount:" + titleCount
                    + ",shopCount:" + shopCount);
        } catch (Exception e) {
            LogUtil.e(e.toString());

        }
	}


	private void getProcess() {
		if (titleIdex + 1 == titleList.size()) {
            titleIdex = 0;
        } else {
            titleIdex++;
        }
		btn_process.setText(titleIdex + "/" + titleList.size());
		etShow();
	}

	private void repeatOut() {
		String titles = et_title.getText().toString();
		String[] split = titles.split("#");
//				String testStr = "jfdlskjfkdlsjfldksjf123ouiouiou";
//				testStr = testStr.replace(testStr, "123");
//				LogUtil.e(testStr);
		titles = "";
		if (null == mTitleList) {
            mTitleList = new ArrayList<String>();
        }
		for (int i = 0; i < split.length; i++) {
            if (!mTitleStr.contains(split[i])) {
                mTitleStr = mTitleStr + "#" +split[i];
                mTitleList.add(split[i]);
            }
            for (int j = 0; j < titleList.size(); j++) {
                String replace = titleList.get(j).replace(split[i], "");
                titleList.remove(j);
                titleList.add(j,replace);
                titles = titles + j + "、" + replace + "\n";

            }

        }
		tv_title.setText(titles);
	}

	private void getTitleResutl() {

		int time = titleCount / shopCount;
		outputTitleList = new ArrayList<String>();
		String templeStr = "";

		String result1 = "";
		String result2 = "";
		String result3 = "";
		String result4 = "";
		String result5 = "";
		if (null != keywordSplit) {
			for (int h = 0; h < keywordSplit.length; h++) {
				for (int i = 0; i < time; i++) {
					int[] ints = randomArray(mTitleList.size());
					Random rand = new Random();
					int num = rand.nextInt(); //int范围类的随机数
					num = rand.nextInt(15); //生成0-100以内的随机数
					num = (int)(Math.random() * 15); //0-100以内的随机数，用Matn.random()方式
					ArrayList<String> titleResult = new ArrayList<String>();
					for (int j = 0; j < ints.length; j++) {
						if (!keywordSplit[h].contains(mTitleList.get(ints[j]))) {
							titleResult.add(mTitleList.get(ints[j]));
						}
					}
					titleResult.add(num,keywordSplit[h]);
					templeStr = "";

					for (int j = 0; j < titleResult.size(); j++) {
						if (strLength(templeStr) < 100) {
							templeStr = templeStr + titleResult.get(j);
						} else {
							break;
						}
					}
					outputTitleList.add(templeStr);
					switch (i) {
						case 0:
						case 5:
						case 10:
						case 15:
						case 20:
							result1 = result1 + templeStr + "\n";
							break;
						case 1:
						case 6:
						case 11:
						case 16:
						case 21:
							result2 = result2 + templeStr + "\n";
							break;
						case 2:
						case 7:
						case 12:
						case 17:
						case 22:
							result3 = result3 + templeStr + "\n";
							break;
						case 3:
						case 8:
						case 13:
						case 18:
						case 23:
							result4 = result4 + templeStr + "\n";
							break;
						case 4:
						case 9:
						case 14:
						case 19:
						case 24:
							result5 = result5 + templeStr + "\n";
							break;
					}
				}
			}


		}
//				new ArrayList<String[]>()
		String result = "-------------------------"+"\n";
		for (int i = 0; i < outputTitleList.size(); i++) {
			result = result + outputTitleList.get(i) + "\n";
		}
		LogUtil.e(result);

		LogUtil.e("------------result------------1");
		LogUtil.e(result1);
		LogUtil.e("------------result------------2");
		LogUtil.e(result2);
		LogUtil.e("------------result------------3");
		LogUtil.e(result3);
		LogUtil.e("------------result------------4");
		LogUtil.e(result4);
		LogUtil.e("------------result------------5");
		LogUtil.e(result5);
	}

	public int strLength(String value) {
		if (TextUtils.isEmpty(value)) {
			return 0;
		}
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	private void etShow() {
		try {
			btn_process.setText(titleIdex + "/" + titleList.size());
			if (titleList.size() > 0) {
				et_title.setText(titleList.get(titleIdex));
			}
		} catch (Exception e) {

		}
	}


	//TODO  点击事件
	@Override
	public void onClick(View v) {

		try {
			switch (v.getId()) {
				case R.id.btn_sort_result:

					new Thread(new Runnable() {
						@Override
						public void run() {


							try {
								sortResult();
							} catch (Exception e) {
								LogUtil.e("sp健值错误:" + e.toString());
							}
							keywordAndTitle();
						}
					}).start();
					break;
				case R.id.btn_sort_title:
//					sortTitle();
					break;
			}
		} catch (Exception e) {

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

	public int[] randomArray(int size){
		int splitNum = 30;
		int max = mTitleList.size() - 1;
		if (null == mTitleList || mTitleList.size() < 1) {
			max = size;
		}
		int len = max - 0 + 1;

		if (splitNum > len) {
			splitNum = len;
		}
		if(max < 0 || splitNum > len){
			return null;
		}

		//初始化给定范围的待选数组
		int[] source = new int[len];
		for (int i = 0; i < 0+len; i++){
			source[i-0] = i;
		}

		int[] result = new int[splitNum];

		Random rd = new Random();
		int index = 0;
		for (int i = 0; i < result.length; i++) {
			//待选数组0到(len-2)随机一个下标
			index = Math.abs(rd.nextInt() % len--);
			//将随机到的数放入结果集
			result[i] = source[index];
			//将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
			source[index] = source[len];
		}
		return result;
	}

}
