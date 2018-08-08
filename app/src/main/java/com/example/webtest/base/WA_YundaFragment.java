package com.example.webtest.base;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.webtest.io.LogUtil;
import com.example.webtest.io.SharedPreferencesUtils;
import com.example.webtest.io.WA_Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * @author z.h
 * @desc 存放基本业务逻辑&Js调用本地方法的接口函数
 */
public class WA_YundaFragment extends WA_BaseFragment
{



	public MyWebView listWeb;
	protected WebView detailWeb;
	protected Instrumentation instrumentation;
	private HashMap<String, Float> jzlMap;
	private HashMap<String, Float> rcMap;
	private HashMap<String, Float> titleMap;
	protected String[] shops;
	protected String[] urls;
	protected int index = 0;
	protected int titleIdex = 0;
	protected int randomtime = 1000;
	protected String resultStr = "";
	protected String rcresultStr = "";
	protected String titleresultStr = "";
	protected String TAOBAO = "TAOBAO";
	protected String TAOBAOJZL = "TAOBAOJZL";
	protected String TAOBAORC = "TAOBAORC";
	protected String TAOBAOTITLE = "TAOBAOTITLE";

	protected Button btnRefresh;
	protected Button btnBack;
	protected Button btnSearch;
	protected Button btnGosearch;
	protected Button btnGosearchworld;
	protected Button btnGetchecked,btn_check,btn_biao1;
	protected Button btn_str_result,btn_display,btn_repeat_out,btn_et_displays,btn_reset,btn_process,btn_title_out;
	protected LinearLayout ll_title;
	protected EditText et_title;

	protected Button btn_sort_result;
	protected Button btn_sort_title;
	protected EditText et_index;

	//	protected String Url = "https://s.taobao.com/search?q=%E7%94%9F%E6%97%A5%E7%A4%BC%E7%89%A9%E5%A5%B3%E7%94%9F&imgfile=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20180725&ie=utf8";
	protected String Url = "https://s.taobao.com/search?q=%E8%8B%B9%E6%9E%9C&s_from=newHeader&ssid=s5-e&search_type=item&sourceId=tb.item";
	protected String sameUlrs = "";
	protected ArrayList<String> titleList;
	protected HashMap<String,Integer> titleSortMap;
	private boolean debug = Constant.DEBUG;
	protected String mUrlList,mMinSameUrlList;
	private String renqiUrl;
	private int sameUrlSize;

	protected ArrayList<String> mTempleList;
	protected String mTempleListKeywordStr;
	public boolean CheckAll = false;
	public boolean isInit = true;
	private String minUrlsRecord;
	private int mTempleSize;

	private int goNextIndex = 0;
	private String olderPageUrl;
	private int debugSize = Constant.debugSize;
	private int pageSize = Constant.pageSize;
	protected String injectJS;
	private int METHOD_AFTER_LOAD = -1;
	private String[] oldPageUrlStr = {"0", "44", "88", "132", "176", "220", "264", "308", "352", "392"};


	protected void refreshSearch() {
		mUrlList = "";
		mMinSameUrlList = "";
		goNextIndex = 0;
		if (!CheckAll) {
			minUrlsRecord = "";
		}
		if (null != titleSortMap) {
			titleSortMap.clear();
		}
		if (index == shops.length) {
			index = 0;
		}

		if (index<shops.length-1&&!isInit){
			index++;
		}

		String mIndex = et_index.getText().toString();
		if (!TextUtils.isEmpty(mIndex)) {
			index = Integer.parseInt(mIndex);
		}
		if (isInit) {
			isInit = false;
		}
		btn_sort_title.setText(shops[index]);
		if (!TextUtils.isEmpty(SharedPreferencesUtils.getValue(getActivity(), TAOBAO, shops[index] + "linkUrl", ""))) {
			btn_sort_title.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
		} else {
			btn_sort_title.setBackgroundColor(android.R.drawable.btn_default);
		}



		btnRefresh.setText(index + 1 + "/" + shops.length);

		if (null != mTempleList) {
			mTempleList.clear();
		}
		mTempleListKeywordStr = "";
	}

	protected enum SearchType
	{
		All, Shop, Mall
	}

	public Handler handler = new Handler();

	//人气
	protected void goSearchClick() {
		olderPageUrl = listWeb.getUrl() + "&sort=renqi";
		loadMyUrl(olderPageUrl);
//        goSearch(shops[index]);
	}
	//销量
	protected void saleDesc() {
		loadMyUrl(listWeb.getUrl()+"&sort=sale-desc");
		METHOD_AFTER_LOAD = Constant.AFTER_SALES_DESC;

	}
	protected void goSearchWord() {

		handlerJs("titleCombination();");
	}
	public void biao1() {
//		listWeb.reload();
		handlerJs("relativeTitle();");
//		handlerJs("relativeTitle();");
	}
	protected void goGetChecked() {

		handlerJs("goGetChecked();");
	}
	protected void check(String url) {

//		handlerJs("check();");
		handlerJs("check(\"" + url + "\");");
	}


	protected void goNextPage() {
		handlerJs("nextPage();");
	}

	protected void goSearch(final String search) {
//		handlerJs("setSearchWord(\""+search+"\",\""+randomtime+"\");");
		goNextIndex = 0;
		sameUlrs = "";
		handlerJs("setSearchWord(\"" + search + "\");");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				saleDesc();
			}
		}, 3000);
	}

	private void handlerJs(final String strlogic) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				String logicStr = strlogic;
				String completeJs = doAutoTest(logicStr);
				loadUrl(listWeb, completeJs);
			}
		});
	}

	public void handlerJs(final String strlogic,long time) {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				String logicStr = strlogic;
				String completeJs = doAutoTest(logicStr);
				loadUrl(listWeb, completeJs);
			}
		},time);
	}

	public void handlerActionDelay(final String url, final String lastUrl, int time) {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				loadMyUrl(url);
				check(lastUrl);
				//TODO

			}
		},time);
	}

	/** Function：选择商品所在的商铺类型(天猫或淘宝) */
	protected String selectSearchType(boolean isTMall)
	{
		String str = "var sortType= doGetTextByCN(\"s-input-tab-txt\");" + "if(!" + isTMall + "){" + "if(sortType!=\"天猫\"){" + "doClickByCN(\"s-input-tab-txt\",1);" + "doClickByCN(\"all\",2);" + "doClickByCN(\"s-input-tab-txt\",2);" + "}}else{" + "if(sortType!=\"宝贝\"){" + "doClickByCN(\"s-input-tab-txt\",1);" + "doClickByCN(\"mall\",2);" + "doClickByCN(\"s-input-tab-txt\",2);" + "}}";
		return str;
	}

	/** Function：点击进入搜索(BelongTo Step1) */
	protected void doEnterSearchPage()
	{
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				enterSearchPage(listWeb);
			}
		});
	}

	/** Function：选择商铺类型(BelongTo Step2) */
	protected void doSelectStoreType(final WA_Parameters parameter)
	{
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				selectStoreType(listWeb, parameter.getIsTMall());
			}
		});
	}

	/** Function：进行商品搜索(BelongTo Step2) */
	protected void doSearch(final WA_Parameters parameter)
	{
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				searchFor(listWeb, parameter.getKeywordStr());
			}
		});
	}

	/** Function：首次进行商品浏览(BelongTo Step3) */
	protected void doScan(final WA_Parameters parameter)
	{
		handler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				scanGoods(listWeb, parameter.getTitleStr());
			}
		}, 4000);

	}

	/** Function：根据销量排序(BelongTo Step4) */
	protected void doOrderBySellAmount()
	{

		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				orderBySellAmount(listWeb);
			}
		});
	}

	/** Function：若当前页中不存在该商铺则翻页，同时另一个页面进行随机商品浏览，浏览时长随机(BelongTo Step5) */
	protected void doFlipAndScan(final WA_Parameters parameter, final int randomTime)
	{
		// 跳转到下一页
		handler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				getNextPage(listWeb);
			}
		}, 2000);

		// 在当前页查找，若没查到则翻到下一页递归查找
		handler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				scanGoods(listWeb, parameter.getTitleStr());
			}
		}, 5000 + randomTime * 1000);
	}

	/** Function：不翻页，在当前页进行随机商品浏览，浏览时长随机(BelongTo Step5) */
	protected void doScanForLongTime(final WA_Parameters parameter, final int randomTime)
	{
		handler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				scanGoods(listWeb, parameter.getTitleStr());
			}
		}, 5000 + randomTime * 1000);

	}

	/** Function：关闭提示框(BelongTo Step5) */
	protected void doAlertHide()
	{
		handler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				alertHide(detailWeb);
			}
		}, 2000);
	}

	/** Function：根据给定的URL进入执行商铺(BelongTo Step5) */
	protected void doEnterShop(final String url)
	{
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				enterShop(detailWeb, url);
			}
		});

	}

	/** Function：选择商品SKU */
	protected void doSelectSku()
	{

	}

	/** 点击进入搜索页面(主页面) */
	private void enterSearchPage(WebView webView)
	{
		// 拼接业务逻辑
//		String logicStr = "doClickByRI(\"search-placeholder\",2);";
		//侧滑菜单
//		String logicStr = "doClickByCN(\"button button-icon button-clear\",2);";
		String logicStr = "selectNumRange(3,2);";
//		String logicStr = "selectNumRange(\"col col-50 bet ok\",2);";
//		String logicStr = "selectNumRange(2);";
		String completeJs = doAutoTest(logicStr);
		loadUrl(webView, completeJs);
	}

	/** 选择店铺类型 */
	private void selectStoreType(WebView webView, boolean isTMall)
	{
		// 拼接业务逻辑
//		String logicStr = selectSearchType(isTMall);
		String logicStr = "doComfir();";
		String completeJs = doAutoTest(logicStr);
		loadUrl(webView, completeJs);
	}

	/** 输入搜索内容，然后查找 */
	private void searchFor(WebView webView, String keywordStr)
	{
		// 拼接业务逻辑
		String logicStr = "doInputByCN(\"J_autocomplete\",\"" + keywordStr + "\",2);" + "doClickByCN(\"icons-search\",4);";

		String completeJs = doAutoTest(logicStr);
		loadUrl(webView, completeJs);
	}

	/** 点击筛选按钮 */
	private void filterGoods(WebView webView)
	{
		// 拼接业务逻辑
		String logicStr = "doTapByRI(\"J_Sift\");";

		String completeJs = doAutoTest(logicStr);
		loadUrl(webView, completeJs);
	}

	/** 确定筛选条件 */
	private void confirmFilter(WebView webView)
	{
		// 拼接业务逻辑
		String logicStr = "doClickByRI(\"J_SiftCommit\",2);";

		String completeJs = doAutoTest(logicStr);
		loadUrl(webView, completeJs);
	}

	/** 按销量优先排序 */
	private void orderBySellAmount(WebView webView)
	{
		// 拼接业务逻辑
		String logicStr = "doTapByParentCN(\"sort-tab\",\"sort\");";

		String completeJs = doAutoTest(logicStr);
		loadUrl(webView, completeJs);
	}

	/** 浏览商铺 */
	private void scanGoods(WebView webView, String titleStr)
	{
		// 拼接业务逻辑
		String logicStr = "var currentPage=doGetTextByCNByInner(\"currentPage\");" + "var totalSize=getSize(\"list-item\");"
				// +"localMethod.JI_showToast(totalSize);"
				// + "localMethod.JI_showToast(currentPage);"
				+ "doTapForScanGoodsByTitle(\"list-item\",\"d-title\",\"" + titleStr + "\",currentPage,totalSize);";

		String completeJs = doAutoTest(logicStr);
		loadUrl(webView, completeJs);
	}

	/** 关闭提示框 */
	private void alertHide(WebView webView)
	{
		// 拼接业务逻辑
		String logicStr = "doClickByCN(\"btn-hide\",2);";

		String completeJs = doAutoTest(logicStr);
		loadUrl(webView, completeJs);
	}

	/** 进入目标商铺 */
	private void enterShop(WebView webView, String url)
	{
		webView.loadUrl("https:" + url);
	}

	private void skuSelect(WebView webView)
	{
		// 拼接业务逻辑
		String logicStr = "doTapByCN02(); ";

		String completeJs = doAutoTest(logicStr);
		loadUrl(webView, completeJs);
	}

	/** 翻页 */
	private void getNextPage(WebView mWebView)
	{
		String logicStr = "doTapByCN(\"J_PageNext\"); ";

		String completeJs = doAutoTest(logicStr);
		loadUrl(mWebView, completeJs);
	}

	/* 暴露给JavaScript脚本调用的方法* */
	public class LocalMethod
	{
		Context mContext;
		private WA_Parameters parameter;

		public LocalMethod(Context c, WA_Parameters parameter)
		{
			this.mContext = c;
			this.parameter = parameter;
		}

		public WA_Parameters getParameter()
		{
			return parameter;
		}

		public void setParameter(WA_Parameters parameter)
		{
			this.parameter = parameter;
		}

		@JavascriptInterface
		public void showKeyboard()
		{
//			listWeb.requestFocus(View.FOCUS_DOWN);
//			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			showGuide();

		}


		@JavascriptInterface
		public void titleResult(String name,String count)
		{

			if (null == titleMap) {
				titleMap = new HashMap<String, Float>();
			}

			if (getWordCount(name)>2){
				titleMap.put(name,Float.parseFloat(count));
			}

		}

		@JavascriptInterface
		public void shopResult(String name,String jzl,String rc)
		{

			if (null == jzlMap) {
				jzlMap = new HashMap<String, Float>();
			}
			if (null == rcMap) {
				rcMap = new HashMap<String, Float>();
			}
			if (Float.parseFloat(jzl)>Float.parseFloat("0.2")){
				jzlMap.put(name, Float.parseFloat(jzl));
			}
			if (Float.parseFloat(rc)>Float.parseFloat("0.2")){
				rcMap.put(name, Float.parseFloat(rc));
			}



		}


		@JavascriptInterface
		public void getHotShopResult()
		{
			Log.e(TAG, "------------------------------------------------");
			sortMap(jzlMap,"---------------------zjl---------------------------"+shops[index]+"\n");
			Log.e(TAG, "*************************************************");
			sortMap(rcMap,"---------------------rc---------------------------"+shops[index]+"\n");
			mapClear();

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					biao1();
				}
			},randomtime);
//			listWeb.reload();
//			handlerJs("relativeTitle();",3000);

		}

		@JavascriptInterface
		public void getTitleResult()
		{
			Log.e(TAG, "--------------------title----------------------------");
			sortTitleMap(titleMap,"---------------------title---------------------------"+shops[index]+"\n");
			titleMap.clear();
//			index++;
			randomtime =3000+(int)(Math.random()*2000);		//返回大于等于m小于m+n（不包括m+n）之间的随机数
			if (index<shops.length){
				goSearch(shops[index]);
			}

		}





		@JavascriptInterface
		public void afterNextClick()
		{

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {

					olderPageUrl = listWeb.getUrl();
					LogUtil.e("after_urlIndex:" + goNextIndex + "olderPageUrl:" + olderPageUrl);

				}
			});

		}



		@JavascriptInterface
		public void loadRenqi()
		{

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					loadMyUrl(renqiUrl);
				}
			});
		}

		@JavascriptInterface
		public void JI_showToast(String content)
		{
			Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
		}

		@JavascriptInterface
		public void JI_LOG(String content)
		{
//			Log.e(TAG, "JI_LOG: " + content);
			LogUtil.e(TAG, "JI_LOG: " + content);
//			Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
		}

		@JavascriptInterface
		public void JI_scrollView()
		{
			listWeb.scrollBy(0, 1800);
		}

		@JavascriptInterface
		public void JI_doGetNextPage(int randomTime)
		{
			doFlipAndScan(parameter, randomTime);
		}

		@JavascriptInterface
		public void JI_doScanCurrentPage(int randomTime)
		{

			doScanForLongTime(parameter, randomTime);
		}

		@JavascriptInterface
		public void JI_doCloseAlert()
		{
			doAlertHide();
		}

		@JavascriptInterface
		public void JI_doEnterShop(String url)
		{
			doEnterShop(url);
		}

		@JavascriptInterface
		public void JI_createLog(String infoStr) throws IOException
		{
			createLog(infoStr);
		}


		@JavascriptInterface
		public void getTargetIndex() throws IOException
		{
			LogUtil.e("------------getTargetIndex------------");
			handlerJs("operaSearch();");
		}

		@JavascriptInterface
		public void sameResult(String resultStr) throws IOException
		{
			LogUtil.e("------------resultStr------------" + shops[index] + "\n" + resultStr);

		}
		@JavascriptInterface
		public void sameResultForSort(String title,String paynum) throws IOException
		{
			if (null == titleSortMap) {
				titleSortMap = new HashMap<String, Integer>();
			}
			if (null == titleSortMap.get(title)) {
				titleSortMap.put(title, Integer.parseInt(paynum));
			} else {
				if (titleSortMap.get(title) > Integer.parseInt(paynum)) {
					titleSortMap.put(title, Integer.parseInt(paynum));
				}
			}
		}


		@JavascriptInterface
		public void afterSearch() throws IOException
		{
			handlerJs("relativeTitle();",2000);
		}


		@JavascriptInterface
		public void sameResultRecord(String url) throws IOException
		{
			if (TextUtils.isEmpty(mUrlList)) {
				mUrlList = url;
			} else if (!mUrlList.contains(url)) {
				mUrlList = mUrlList + "###" + url;
			}
		}

		@JavascriptInterface
		public void minSameRecord(String str,String minUrl) throws IOException
		{

			if (TextUtils.isEmpty(mMinSameUrlList)) {
				mMinSameUrlList = str;
			} else if (!mMinSameUrlList.contains(str)) {
				mMinSameUrlList = mMinSameUrlList + "###" + str;
			}
//			boolean jixu = true;
//			if (TextUtils.isEmpty(minUrl)) {
//				minUrlsRecord = minUrl;
//			} else if (!minUrlsRecord.contains(minUrl)) {
//				minUrlsRecord = minUrlsRecord + "###" + str;
//			} else {
//				jixu = false;
//			}
//			if (jixu) {
//			}
		}

		@JavascriptInterface
		public void minPricesUrl(String minPricesUrl) throws IOException
		{

		}


		@JavascriptInterface
		public void titleSave(String str) throws IOException
		{
			if (null == titleList) {
				titleList = new ArrayList<String>();
			}
			titleList.add(str);

		}


		@JavascriptInterface
		public void linkArray(final String[] array) throws IOException
		{
			LogUtil.e("length:" + array.length + "");
//			LogUtil.e("length:" + array[0] + "");
			urls = array;

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (array.length < 1) {
						if (index != shops.length - 1) {
							Toast.makeText(getActivity(), "同款链接为空", Toast.LENGTH_SHORT).show();
							if (goNextIndex > pageSize) {
								sortTitle();
								refreshSearch();
								LogUtil.e("779~~~~~~~~~~refreshSearch:" + shops[index]);
								goSearch(shops[index]);
							} else {
								goNextRenqiPage();
							}
						}

					}
					try {
						getSameStytleResult(array);
					} catch (Exception e) {

					}

				}
			});
		}
	}

	//TODO
	private void getSameStytleResult(String[] linkUrls) {
		String lastUrl = "";
		ArrayList<String> urlList = new ArrayList<String>();
		sameUrlSize = linkUrls.length;
		for (int i = 0; i < linkUrls.length; i++) {

			if (linkUrls[i].contains("taobao.com")&&!sameUlrs.contains(linkUrls[i])) {
				urlList.add(linkUrls[i]);
			}

		}

		if (debug) {
			mTempleSize = debugSize;
		} else {
			mTempleSize = urlList.size();
		}
		afterSameUrl(urlList);
//		LogUtil.e("resultStr-----------end");

	}

	private void afterSameUrl(ArrayList<String> urlList) {
		String lastUrl;
		for (int i = 0; i < mTempleSize; i++) {
			if (urlList.get(i).contains("taobao.com") && !sameUlrs.contains(urlList.get(i))) {
				if (i > 0) {
					lastUrl = urlList.get(i - 1);
				} else {
					lastUrl = urlList.get(0);
				}
				handlerActionDelay(urlList.get(i), lastUrl, 3000 * i);
				sameUlrs = sameUlrs + "------" + urlList.get(i);
				if (i == mTempleSize - 1) {
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {

							sortTitle();

							goNextIndex++;
							if (goNextIndex > pageSize) {
								if (index == shops.length - 1) {
									index = 0;
									return;
								}
								refreshSearch();
								LogUtil.e("845~~~~~~~~~~refreshSearch:" + shops[index]);
								goSearch(shops[index]);
							} else {
//								handler.post(new Runnable() {
//									@Override
//									public void run() {
//									}
//								});

								goNextRenqiPage();
//								METHOD_AFTER_LOAD = Constant.GO_NEXT_PAGE;
//								loadMyUrl(olderPageUrl);

							}
						}
					}, 3000*i+2000);
				}

			}
		}
	}

	private void goNextRenqiPage() {
		if (goNextIndex == 1 && !olderPageUrl.contains("&bcoffset=0&p4ppushleft=%2C44&s")) {
			olderPageUrl = olderPageUrl + "&bcoffset=0&p4ppushleft=%2C44&s=44";
		} else if (goNextIndex < pageSize && goNextIndex > 0) {
			olderPageUrl = olderPageUrl.replace("4ppushleft=%2C44&s=" + oldPageUrlStr[goNextIndex - 1], "4ppushleft=%2C44&s=" + oldPageUrlStr[goNextIndex]);
		}
		LogUtil.e("before_urlIndex:" + goNextIndex + "olderPageUrl:" + olderPageUrl);
		loadMyUrl(olderPageUrl);
		METHOD_AFTER_LOAD = Constant.GET_SAMEURL;
	}


	public void loadMyUrl(String url) {
		listWeb.loadUrl(url);
	}

	public int getWordCount(String s)
	{
		int length = 0;
		for(int i = 0; i < s.length(); i++)
		{
			int ascii = Character.codePointAt(s, i);
			if(ascii >= 0 && ascii <=255)
				length++;
			else
				length += 2;

		}
		return length;

	}


	public void sortTitle() {
		if (ll_title.getVisibility() == View.VISIBLE) {
			Toast.makeText(getActivity(), "请切换", Toast.LENGTH_SHORT).show();
			return;
		}
		LogUtil.e("存sp健值：" + TAOBAO + "-" + shops[index]);
		try {
			SharedPreferencesUtils.putValue(getActivity(), TAOBAO, shops[index] + "titleSort", sortTitleMap(titleSortMap));
			SharedPreferencesUtils.putValue(getActivity(), TAOBAO, shops[index]+"linkUrl", mUrlList);
			SharedPreferencesUtils.putValue(getActivity(), TAOBAO, shops[index]+"minSameUrlList", mMinSameUrlList);
			btn_sort_title.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
		} catch (Exception e) {
			LogUtil.e("sp健值错误:" + e.toString());
		}
		try {
			sortResult();
		} catch (Exception e) {
			LogUtil.e("sp健值错误:" + e.toString());
		}
	}

	public void sortResult() {
		LogUtil.e("取sp健值：" + "\n" + TAOBAO + "-" + index + "---" + shops[index]);
		LogUtil.e("------------resultStr------------" + "\n" + TAOBAO + "-" + index + "---" + shops[index]);

		String value = SharedPreferencesUtils.getValue(getActivity(), "TAOBAO", shops[index] + "titleSort", "");
		String urls = SharedPreferencesUtils.getValue(getActivity(), "TAOBAO", shops[index] + "linkUrl", "");
		String minSameUrlList = SharedPreferencesUtils.getValue(getActivity(), "TAOBAO", shops[index] + "minSameUrlList", "");
		String[] split = value.split("###");
		String[] linkUrl = urls.split("###");
		String[] minUrl = minSameUrlList.split("###");
		if (null == titleList) {
			titleList = new ArrayList<String>();
		} else {
			titleList.clear();

		}
		String str = "------------resultStr------------" + "\n";
		String linkStr = "------------resultStr------------" + "\n";
		String minStr = "------------resultStr------------" + "\n";
		for (int i = 0; i < split.length; i++) {
			titleList.add(split[i]);
			str = str + split[i]+ "\n";
		}
//		for (int i = 0; i < linkUrl.length; i++) {
//			if (!linkStr.contains(linkUrl[i])) {
//				linkStr = linkStr + linkUrl[i] + "\n";
//			}
//		}
		for (int i = 0; i < minUrl.length; i++) {
			String[] url = minUrl[i].split("minPricesUrl:");
			if (!minStr.contains(url[1])) {
				minStr = minStr + minUrl[i] + "\n" + "-----------------------------------------------------------" + "\n";
			}
		}
//		LogUtil.e("sp健值：" + str);
//		LogUtil.e("sp健值：" + linkStr);
		LogUtil.e("sp健值：" + minStr);

	}

	private void sortMap(Map map,String str) {
		List<Map.Entry<String,Float>> list = new ArrayList<Map.Entry<String,Float>>(map.entrySet());
		Collections.sort(list,new Comparator<Map.Entry<String,Float>>() {
			//升序排序
			public int compare(Map.Entry<String, Float> o1,
							   Map.Entry<String, Float> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}

		});

		for(Map.Entry<String,Float> mapping:list){
			str = str + mapping.getKey()+"######"+mapping.getValue()+"\n";
		}
		putSp(str);
		Log.e("sortMap: ",str);
	}

	private void putSp(String str) {
		if (str.contains("------title")){
//			titleresultStr = titleresultStr + str;
//			Log.e("titlestr: ",titleresultStr);

			SharedPreferencesUtils.putValue(getActivity(), TAOBAO, shops[index]+"title", str);
		} else if (str.contains("------zjl")){
//			resultStr = resultStr + str;
//			SharedPreferencesUtils.putValue(getActivity(), TAOBAO, TAOBAOJZL, resultStr);
//			Log.e("resultStr: ",resultStr);
			SharedPreferencesUtils.putValue(getActivity(), TAOBAO, shops[index]+"zjl", str);
		} else if (str.contains("------rc")){
//			rcresultStr = rcresultStr + str;
//			SharedPreferencesUtils.putValue(getActivity(), TAOBAO, TAOBAORC, rcresultStr);
//			Log.e("rcresultStr: ",rcresultStr);
			SharedPreferencesUtils.putValue(getActivity(), TAOBAO, shops[index]+"rc", str);
		}
	}

	public String sortTitleMap(Map map) {
		List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
		Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
			//升序排序
			public int compare(Map.Entry<String, Integer> o1,
							   Map.Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}

		});
		String str = "";
		for(Map.Entry<String,Integer> mapping:list){
			str = str + mapping.getKey()+"###";
		}


//		LogUtil.e(str);
		return str;

	}

	private void sortTitleMap(Map map,String str) {
		List<Map.Entry<String,Float>> list = new ArrayList<Map.Entry<String,Float>>(map.entrySet());
		Collections.sort(list,new Comparator<Map.Entry<String,Float>>() {
			//升序排序
			public int compare(Map.Entry<String, Float> o1,
							   Map.Entry<String, Float> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}

		});

		for(Map.Entry<String,Float> mapping:list){
			str = str + mapping.getKey()+"\n";
		}
		putSp(str);
		Log.e("sortMap: ",str);
	}

	public void mapClear()
	{

		if (null != jzlMap) {
			jzlMap.clear();
		}
		if (null != rcMap) {
			rcMap.clear();
		}
	}


	private void showGuide( ){
		new Thread( new Runnable( ) {
			@Override
			public void run() {
				try {
					Thread.sleep( 1000 );
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				// “旋转”的拼音
//				int[] keyCodeArray = new int[]{KeyEvent.KEYCODE_X,KeyEvent.KEYCODE_U,KeyEvent.KEYCODE_A,KeyEvent.KEYCODE_N,KeyEvent.KEYCODE_SPACE,KeyEvent.KEYCODE_Z,KeyEvent.KEYCODE_H,KeyEvent.KEYCODE_U,KeyEvent.KEYCODE_A,KeyEvent.KEYCODE_N};
				int[] keyCodeArray = new int[]{KeyEvent.KEYCODE_X,KeyEvent.KEYCODE_DEL};
				for( int keycode : keyCodeArray ){
					try {
						typeIn( keycode );
						Thread.sleep( 200 );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start( );
	}

	private void typeIn( final int KeyCode ){
		try {
			Instrumentation inst = new Instrumentation();
			inst.sendKeyDownUpSync( KeyCode );
		} catch (Exception e) {
			Log.e("Exception：", e.toString());
		}
	}




	/** ListWebView加载完注入基本JS函数 */
	public class MyListWebViewClient extends WebViewClient
	{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			view.loadUrl("javascript:" + injectJS);
			switch (METHOD_AFTER_LOAD) {
				case Constant.AFTER_SALES_DESC:
					renqiUrl = listWeb.getUrl().replace("&sort=sale-desc", "&sort=renqi");
					olderPageUrl = renqiUrl;
					LogUtil.e("init_urlIndex:" + goNextIndex + "olderPageUrl:" + olderPageUrl);

					handlerJs("getTitleList();");
					METHOD_AFTER_LOAD = Constant.RENQI_BEGIN;
					break;
				case Constant.RENQI_BEGIN:

					handler.post(new Runnable() {
						@Override
						public void run() {

							handlerJs("relativeTitle();");
						}
					});
					METHOD_AFTER_LOAD = -1;
					break;
				case Constant.GO_NEXT_PAGE:

					break;
				case Constant.GET_SAMEURL:
					biao1();
					METHOD_AFTER_LOAD = -1;
					break;
			}
			LogUtil.e("urlIndex:!!!!!!@@@@@####$" + METHOD_AFTER_LOAD);

			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			super.onPageStarted(view, url, favicon);
		}
	}


}
