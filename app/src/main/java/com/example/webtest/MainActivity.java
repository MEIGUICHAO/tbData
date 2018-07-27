package com.example.webtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.webtest.io.WA_Parameters;

public class MainActivity extends Activity
{
	private static final String[] TMALL_FLAG ={ "否", "是" };

	private EditText keywordEt;
	private EditText titleEt;
	private Spinner storeTypeSp;

	private String keywordStr;
	private String titleStr;
	private boolean isTmall = false;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_main);
		findViews();
		initData();
		setListener();
		start();
	}

	private void findViews()
	{
		keywordEt = (EditText) findViewById(R.id.et_keyword);
		titleEt = (EditText) findViewById(R.id.et_title);
		storeTypeSp = (Spinner) findViewById(R.id.sp_store_type);
	}

	private void initData()
	{
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, TMALL_FLAG);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		storeTypeSp.setAdapter(adapter);
	}

	private void setListener()
	{
		storeTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				switch (position)
				{
					case 0:
						isTmall = false;
						break;
					case 1:
						isTmall = true;
						break;
					default:
						break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}

	public void start()
	{
		//拼装目标请求数据
		keywordStr = keywordEt.getText().toString();
		titleStr = titleEt.getText().toString();

		WA_Parameters parameter = new WA_Parameters();
		parameter.setKeywordStr(keywordStr);
		parameter.setTitleStr(titleStr);
		parameter.setIsTMall(isTmall);

		//开启执行自动化的Fragment页面
		WA_MainFragment.start(MainActivity.this,R.id.container, parameter);
	}

}
