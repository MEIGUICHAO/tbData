package com.example.webtest.io;

import java.io.Serializable;

public class WA_Parameters implements Serializable
{
	private static final long serialVersionUID = 1486887432706614685L;

	/** ��Ʒ�ؼ���*/
	private String keywordStr;

	/** ��Ʒ����(��֤ΪΨһ��ʶ)*/
	private String titleStr;

	/** �Ƿ�Ϊ��è��Ʒ*/
	private Boolean isTMall;


	public String getKeywordStr()
	{
		return keywordStr;
	}

	public void setKeywordStr(String keywordStr)
	{
		this.keywordStr = keywordStr;
	}

	public String getTitleStr()
	{
		return titleStr;
	}

	public void setTitleStr(String titleStr)
	{
		this.titleStr = titleStr;
	}

	public Boolean getIsTMall()
	{
		return isTMall;
	}

	public void setIsTMall(Boolean isTMall)
	{
		this.isTMall = isTMall;
	}

}
