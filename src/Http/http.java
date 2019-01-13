package Http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import Global.Global;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import Page.Page;
import Page.TableEdit;
import TimeStamp.timeStamp;

public class http
{
	public static void postPage(Page _page) throws ClientProtocolException, IOException
	{
		if(_page == null || !_page.postAble())
		{
			System.err.print("null bug in postPage\n");
			return;
		}
		String _url = _page.formPageEditURL();
		String _edit_area = _page.getEditArea();
		httpPost.buildArticle(_url, _edit_area);
		return;
	}
	
	public static void TransPaper() throws IOException
	{
		String preTURL = "http://172.31.222.76:5091/mediawiki/index.php?title=";
		String preSURL = "http://172.31.106.89/mediawiki/index.php?title=";
		BufferedReader brTransPaper = new BufferedReader(new FileReader("TransPaper.txt"));
		String rLine;
		while((rLine = brTransPaper.readLine()) != null)
		{
			rLine = "%E6%A8%A1%E6%9D%BF:" + rLine;
			String tURL = preTURL + rLine + "&action=submit";
			String sURL = preSURL + rLine + "&action=submit";
			String _text_area = httpGet.getTextArea(sURL);
			System.out.print("[" + _text_area + "]" + "\n");
		}
		brTransPaper.close();
	}
	
	public static String getPage(String _url) throws ClientProtocolException, IOException
	{
		return httpGet.getContent(_url);
	}
	
	static HashMap<String, String> getTEurlSet(Page _page) throws ClientProtocolException, IOException
	{
		String _page_url = _page.formPageURL();
		{
//			System.out.print("_page_url : " + _page_url + "\n");
		}
		String _page_cont = httpGet.getContent(_page_url);
		{
//			BufferedWriter bwCont = new BufferedWriter(new FileWriter("bwContURLset"));
//			bwCont.write(_page_cont);
//			bwCont.close();
		}
		HashMap<String, String> urlSet = new HashMap<String, String>();
		Scanner sc = new Scanner(_page_cont);
		String _line = "";
		String _url = "";
		while(sc.hasNextLine())
		{
			_line = sc.nextLine();
			if(_line.indexOf("tableEdit_editLink plainlinks") != -1)
			{
				_url = ((_line.split("\""))[3]).replace("&amp;", "&")
											   .replace("?title", "/?title");
				{
//					System.out.print("TE url Set: " + _url + "\n");
				}
				String _key = _url.split("&template=")[1];
				{
//					System.out.print("template: " + _key + "\n");
				}
				urlSet.put(_key, _url);
			}
		}

		return urlSet;
	} 
	
	public static void postTableEditData(Page _page) throws ClientProtocolException, IOException
	{
		HashMap<String, String> TEurlSet = getTEurlSet(_page);
		
		for(TableEdit iTE : _page.tableEdit)
		for(int i_row = 0; i_row < iTE.getFieldLen(); i_row ++)
		{
			String _key = iTE.getTemplate();
			String _te_url = TEurlSet.get(_key);
			{
				if(
				   _key.equals("CrossReferences_TableEdit") || 
				   _key.equals("SequenceAnnotation_TableEdit")||
//				   _key.equals("AlternativeProducts_TableEdit") ||
//				   _key.equals("BinaryInteraction_TableEdit") ||
//				   _key.equals("References_TableEdit") ||
				   _key.equals("GeneOntology_TableEdit")
				  ) 
				{
					if(i_row >= 5) continue;
				}
			}
			String addrowURL = httpGet.getAddRowURL(_te_url);

			/*
			 * save the cookie right away
			 */
			CookieStore _cs = httpGet.contCookieStore;
			addrowURL = (addrowURL.replace("&amp;", "&"))
								  .replace("view=doc", "view=new&foo=");
			/*
			 * http element define
			 */
			DefaultHttpClient httpclient = new DefaultHttpClient();
			List<NameValuePair> paramList;
			UrlEncodedFormEntity entity;
			HttpResponse response;
			HttpPost httppost;
			HttpEntity result;
			InputStream IS;
			BufferedReader reader;
			HashMap<String , String> hm = new HashMap<String, String>();
			/*
			 * Resolve Url
			 */
			{
				String[] _sp = addrowURL.split("\\?|&");
				String [] pair;
				for(String _pair : _sp)
				{
					pair = _pair.split("=");
					if(
//						pair[0].equals("act") ||
						pair[0].equals("browser_tab") ||
						pair[0].equals("conflict") ||
//						pair[0].equals("ff") ||
						pair[0].equals("id") ||
						pair[0].equals("pagename")
//						pair[0].equals("view") 
					)
					{
						if(pair.length == 2)
							hm.put(pair[0], pair[1]);
						else
							hm.put(pair[0], "");
					}
				}//icpfba
			}
			/*
			 * Add Row
			 */
			{
				paramList = new ArrayList<NameValuePair>();
				paramList.add(new BasicNameValuePair("conflict", hm.get("conflict")));
				paramList.add(new BasicNameValuePair("pagename", hm.get("pagename")));
				paramList.add(new BasicNameValuePair("ff", "reloaded"));
				paramList.add(new BasicNameValuePair("browser_tab", hm.get("browser_tab")));;
				paramList.add(new BasicNameValuePair("act", "Add row"));
				paramList.add(new BasicNameValuePair("view", "nav"));
				paramList.add(new BasicNameValuePair("id", hm.get("id")));
				
				entity = new UrlEncodedFormEntity(paramList);
				httppost = new HttpPost(addrowURL);
				httppost.setEntity(entity);
				httpclient.setCookieStore(_cs);
				response = httpclient.execute(httppost);
				result = response.getEntity();
				IS = result.getContent();
				
		        reader = new BufferedReader(new InputStreamReader(IS));		        
		        while(reader.readLine() != null);
		        System.out.print("postAddRow : " + response.getStatusLine() + "\n\n");
			}
			
			/*
			 * Save Table
			 */
			{
				paramList = new ArrayList<NameValuePair>();
				paramList.add(new BasicNameValuePair("id", hm.get("id")));
				paramList.add(new BasicNameValuePair("conflict", hm.get("conflict")));
				paramList.add(new BasicNameValuePair("pagename", hm.get("pagename")));
				paramList.add(new BasicNameValuePair("ff", "reloaded"));
				paramList.add(new BasicNameValuePair("browser_tab", hm.get("browser_tab")));
				paramList.add(new BasicNameValuePair("row_index", i_row + ""));
				{
					String [] _field = iTE.getField(i_row);
					for(int i = 0; i < _field.length; i ++)
					{
						paramList.add(new BasicNameValuePair("field[" + i + "]", _field[i]));
					}
				}
				paramList.add(new BasicNameValuePair("row_owner", "0"));
				paramList.add(new BasicNameValuePair("act", "Save"));
				paramList.add(new BasicNameValuePair("row_style", ""));
				paramList.add(new BasicNameValuePair("view", "edit_row"));
				
				entity = new UrlEncodedFormEntity(paramList);
				httppost = new HttpPost(addrowURL);
				httppost.setEntity(entity);
				httpclient.setCookieStore(_cs);
				response = httpclient.execute(httppost);
				result = response.getEntity();
				IS = result.getContent();
				
		        reader = new BufferedReader(new InputStreamReader(IS));		        
		        while(reader.readLine() != null);
		        System.out.print("postSaveTable : " + response.getStatusLine() + "\n\n");
			}
			
			/*
			 * Save to Wiki
			 */
			{
				paramList = new ArrayList<NameValuePair>();
				paramList.add(new BasicNameValuePair("id", hm.get("id")));
				paramList.add(new BasicNameValuePair("conflict", hm.get("conflict")));
				paramList.add(new BasicNameValuePair("pagename", hm.get("pagename")));
				paramList.add(new BasicNameValuePair("ff", "reloaded"));
				paramList.add(new BasicNameValuePair("browser_tab", hm.get("browser_tab")));;
				paramList.add(new BasicNameValuePair("act", "Save Table to wiki page: " + hm.get("pagename")));
				paramList.add(new BasicNameValuePair("view", "save"));
				
				entity = new UrlEncodedFormEntity(paramList);
				httppost = new HttpPost(addrowURL);
				httppost.setEntity(entity);
				httpclient.setCookieStore(_cs);
				response = httpclient.execute(httppost);
				result = response.getEntity();
				IS = result.getContent();
				
		        reader = new BufferedReader(new InputStreamReader(IS));		        
		        while(reader.readLine() != null);
		        
		        System.out.print("postSavetoWiki : " + response.getStatusLine() + "\n\n");
			}
		}
	}
}

class httpGet
{
	public static CookieStore contCookieStore;
	
	public static String getAddRowURL(String URL) throws ClientProtocolException, IOException
	{
		String _cont = getContent(URL);
		{
//			BufferedWriter bwCont = new BufferedWriter(new FileWriter("bwContAddRow"));
//			bwCont.write(_cont);
//			bwCont.close();
		}
		Scanner sc = new Scanner(_cont);
		String _line = "";
		while(sc.hasNextLine())
		{
			_line = sc.nextLine();
			if(_line.indexOf("span class = 'editsection'") != -1)
			{
				return ((_line.split("'"))[3]);
			}
		}
		return _line;
	}
	
	public static String getContent(String URL) throws ClientProtocolException, IOException
	{
        String aLine = "", _cont = "";
		DefaultHttpClient httpclient = new DefaultHttpClient();
		 // Prepare a request object
		 HttpGet httpget = null;
		 try{
		 	httpget = new HttpGet(URL);
		 }catch(IllegalArgumentException e)
		 {
			 System.out.print("bug URL : " + URL + "\n");
			 System.exit(0);
		 }

		 // Execute the request
		 HttpResponse response = httpclient.execute(httpget);
		 contCookieStore = httpclient.getCookieStore();
		 // Examine the response status
		 
		 {
				String _status = response.getStatusLine().toString();
				if(! _status.equals("HTTP/1.1 200 OK")) 
				{
					System.out.print("url" + URL + "\n");
				}
			 System.out.print("getcontent: " + response.getStatusLine() + "\n");
		 }
		 // Get hold of the response entity
		 HttpEntity entity = response.getEntity();
		 
		 // If the response does not enclose an entity, there is no need
		 // to worry about connection release
		 if (entity != null) {
		     InputStream instream = entity.getContent();
		     try {

		         BufferedReader reader = new BufferedReader(
		                 new InputStreamReader(instream));
		         // do something useful with the response
		         while(aLine != null)
		         {
		        	_cont += aLine + "\n";
//		        	 System.out.println(aLine);
		        	 aLine = reader.readLine();
		         }

		     } catch (IOException ex) {

		         // In case of an IOException the connection will be released
		         // back to the connection manager automatically
		         throw ex;

		     } catch (RuntimeException ex) {

		         // In case of an unexpected exception you may want to abort
		         // the HTTP request in order to shut down the underlying
		         // connection and release it back to the connection manager.
		         httpget.abort();
		         throw ex;

		     } finally {

		         // Closing the input stream will trigger connection release
		         instream.close();

		     }

		     // When HttpClient instance is no longer needed,
		     // shut down the connection manager to ensure
		     // immediate deallocation of all system resources
		     httpclient.getConnectionManager().shutdown();
		 }
		 return _cont;
	}
	
	public static String getTextArea(String URL) throws ClientProtocolException, IOException
	{
		String _cont = getContent(URL);
		Scanner sc = new Scanner(_cont);
		String _line = "";
		String _part_cont = "";
		String _text_area = "";
		{
			BufferedWriter bwCont = new BufferedWriter(new FileWriter("cont"));
			bwCont.write(_cont);
			bwCont.close();
		}
		while(sc.hasNextLine())
		{
			_line = sc.nextLine();
			if(_line.indexOf("<textarea") != -1)
			{
				_part_cont += _line + "\n";
				while((_line = sc.nextLine()).indexOf("</textarea>") == -1)
				{
					_part_cont += _line;
				}
				_part_cont += _line;
			}
			_text_area = (_part_cont.split("\\<|\\>"))[1];
			_text_area = _part_cont.replace("&lt;", "<")
								   .replace("&gt;", ">");
		}
		return _text_area;
	}

	public static String getEditTime(String URL) throws ClientProtocolException, IOException
	{
		String _cont = getContent(URL);
		{
//			System.out.print(URL + "\t" + _cont + "\n");
		}
		Scanner sc = new Scanner(_cont);
		String _line = "";
		while(sc.hasNextLine())
		{
			_line = sc.nextLine();
			if(_line.indexOf("wpEdittime") != -1)
			{
				return ((_line.split("\""))[1]);
			}
		}
		return _line;
	}
}

class httpPost
{	
	
	public static void buildArticle(String _url, String _cont) throws ClientProtocolException, IOException
	{
		String _ts = new timeStamp().getTimeStamp();
		String _edittime = httpGet.getEditTime(_url);
		
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		entity.addPart("wpStarttime", new StringBody(_ts));
		entity.addPart("wpEdittime", new StringBody(_edittime));
		entity.addPart("wpSection", Global.WP_SECTION);
		entity.addPart("wpScrolltop", Global.WP_SCROLL_TOP);
		entity.addPart("wpAutoSummary", Global.WP_AUTO_SUMMARY);
		entity.addPart("wpTextbox1", new StringBody(_cont));				
		entity.addPart("wpSave", Global.WP_SAVE);				
		entity.addPart("wpEditToken", Global.WP_EDIT_TOKEN);
		entity.addPart("wpSummary", Global.WP_SUMMARY);
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(_url);
		httpPost.setEntity(entity);
		HttpResponse response = httpclient.execute(httpPost);
		{
			String aLine = "";
			 // Get hold of the response entity
			 HttpEntity entity1 = response.getEntity();
			 
			 // If the response does not enclose an entity, there is no need
			 // to worry about connection release
			 if (entity1 != null) {
			     InputStream instream = entity1.getContent();
			     try {

			         BufferedReader reader = new BufferedReader(
			                 new InputStreamReader(instream));
			         // do something useful with the response
			         while((reader.readLine()) != null);

			     } catch (IOException ex) {

			         // In case of an IOException the connection will be released
			         // back to the connection manager automatically
			         throw ex;

			     } catch (RuntimeException ex) {

			         // In case of an unexpected exception you may want to abort
			         // the HTTP request in order to shut down the underlying
			         // connection and release it back to the connection manager.
			         httpPost.abort();
			         throw ex;

			     } finally {

			         // Closing the input stream will trigger connection release
			         instream.close();

			     }

			     // When HttpClient instance is no longer needed,
			     // shut down the connection manager to ensure
			     // immediate deallocation of all system resources
			     httpclient.getConnectionManager().shutdown();
			 }
		}
		String _status = response.getStatusLine().toString();
		if(! _status.equals("HTTP/1.1 302 Found")) 
		{
			Global.bwBug.write("url: " + _url + "\n");
			Global.bwBug.write("et: " + _edittime + "\n");
			Global.bwBug.write("cont: " + _cont + "\n");
			Global.bwBug.flush();
		}
		System.out.print("buildArticle: " + _status + "\n\n");
	}
}