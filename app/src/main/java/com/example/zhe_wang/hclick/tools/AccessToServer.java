package com.example.zhe_wang.hclick.tools;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/*完成发送HTTP请求方法类*/
public class AccessToServer {	
	private String result="";
	private String url="";
	/*1.构造方法
	 * 		实现动态对url赋值
	 */
	public AccessToServer(String url)
	{
		this.url=url;
	}
	
	/*2.doGet方法
	 * 		向服务器发送GET请求，获得服务器响应数据*/
	
//	public String doGet(String name,String psd)
	public String doGet(String keys[],String values[])
	{
		HttpClient httpClient = new DefaultHttpClient();						//a.获取代表客户端的HttpClient对象
		//处理UI传递过来的键值数据
		String urlStr=url;
		if(keys!=null && values!= null)
		{
			 urlStr +="?";
			for(int i=0;i<keys.length;i++)	//一次读取多个键值对
			{
				urlStr +=keys[i]+ "=" +values[i];
				if(i!=keys.length-1)	//如果不是最后一个，则用&将数据连接起来
				{
					urlStr += "&";
				}
			}
		}
        Log.d("AccessToServer", urlStr);
//		String urlStr=url+"?username="+name+"&psd="+psd; 			//b.加载要传输的数据到URL
		HttpGet httpGet=new HttpGet(urlStr);										//c.根据URL创建一个HttPGet对象
		try{
			HttpResponse httpResponse = httpClient.execute(httpGet);  //d.发送GET请求，并返回一个HttpResponse对象
			/*响应的请求码正确(200)，则读取HttpResponse对象中的数据*/
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				HttpEntity httpEntity = httpResponse.getEntity();				    //e.获得响应数据的HttpEntity对象
				InputStream is=httpEntity.getContent();							    //f.获得实体中的数据,并返回数据的输入流对象
				BufferedReader br = new BufferedReader(new InputStreamReader(is)); //g.通过包装类将字节流数据转换为字符流
				String readLine=null;
				while((readLine=br.readLine()) != null)									//g.循环读取一行字符数据并存放到result字符串对象中
				{
					result =result+readLine;
				}
				is.close();																				//h.关闭资源输入流
			}
			/*响应异常*/
			else
			{
				result= "error";
			}
		}catch(ClientProtocolException ce){
			ce.printStackTrace();
		}
		catch( IOException e){
			e.printStackTrace();
		}
		
		return result;		//返回响应结果
	}
}
