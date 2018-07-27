package com.example.webtest.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

/**
 * @author z.h
 * @des commmon tools
 */
public class WA_BaseFragment extends Fragment
{






	private static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/web_auto.log";

	/** 注入需自动执行的JS代码 */
	protected String doAutoTest(String code)
	{
		return "function doAutoTest() { " + code + "}";
	}

	/** 组装整个JS代码 */
	protected String buildTest(String logicStr)
	{
		String js = "var newscript = document.createElement(\"script\");" + "newscript.text = window.onload=doAutoTest();" + logicStr + "document.body.appendChild(newscript);";
		return js;
	}

	/** Load JS代码，然后会自动执行doAutoTest()里的内容 */
	protected void loadUrl(WebView webView, String logicStr)
	{
		String js = buildTest(logicStr);
		Log.e(TAG, "loadUrl: " + js);
		webView.loadUrl("javascript:" + js);
	}

	/** 注入本地文件中的JS方法 */
	protected String getJsFromFile(Activity mContext, String jsPath)
	{
		InputStream in = null;
		try
		{
			in = mContext.getBaseContext().getAssets().open(jsPath);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		byte buff[] = new byte[1024];
		ByteArrayOutputStream fromFile = new ByteArrayOutputStream();
		do
		{
			int numRead = 0;
			try
			{
				numRead = in.read(buff);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			if (numRead <= 0)
			{
				break;
			}
			fromFile.write(buff, 0, numRead);
		} while (true);

		return fromFile.toString();
	}

	/** 创建本地日志文件 */
	protected void createLog(String infoStr)
	{

		FileWriter fw = null;
		try
		{
			fw = new FileWriter(LOG_FILE_PATH, true);
			fw.write("\r\n" + infoStr + "\r\n");
			fw.flush();

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				fw.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	/** 删除本地日志文件 */
	protected void deleteLog()
	{
		File file = new File(LOG_FILE_PATH);
		if (file.exists())
		{
			file.delete();
		} else
		{
			Toast.makeText(getActivity(), "This file is not exist!", Toast.LENGTH_SHORT).show();
		}
	}

	/** 线程延时 */
	protected void doSleep(int time)
	{
		try
		{
			Thread.sleep(time * 1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/** 线程终止 */
	protected void doInterreput()
	{
		Thread.interrupted();
	}

}
