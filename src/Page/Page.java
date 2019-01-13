package Page;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import Global.Global;
import Http.http;

public class Page
{
	private String title = null;
	public String editArea = null;
	public TableEdit[] tableEdit;
	
	public Page(String _title)
	{
		if(_title == null) 
		{
			System.err.print("null bug in create page\n");
		}
		title = _title.replace(" ", "_")
						 .replace("<", "(")
						 .replace(">", ")")
						 .replace("[", "(")
						 .replace("]", ")")
						 .replace("\"", "")
						 .replace("\\", "")
						 .replace("+", "PLUS_SIGNAL");
	}
	
	public Page(String _title, String _edit_area)
	{
		title = _title;
		editArea = _edit_area;
	}
	
	public boolean postAble()
	{
		return title != null && editArea != null;
	}
	
	public String getPage() throws ClientProtocolException, IOException
	{
		return http.getPage(formPageURL());
	}
	
	public boolean pageExist()
	{
		/*
		 * check exist
		 */
		return true;
	}
	
	public String formPageURL()
	{
		return "http://" + Global.IP_ADDR + "/mediawiki/index.php/" + title;
	}
	
	public void AddInCategory(String _category)
	{
		_category = _category.replace("<", "(").replace("\"", "")
		 .replace(">", ")")
		 .replace("[", "(")
		 .replace("]", ")")
		 .replace("\\", "")
		 .replace("+", "PLUS_SIGNAL");
		editArea += "[[Category:" + _category + "]]\n";
	}
	
	public void AddInCategories(String[] _categories)
	{
		for(int i = 0; i < _categories.length; i ++)
		{
			AddInCategory(_categories[i]);
		}
	}
	
	public String formPageEditURL()
	{
		return "http://" + Global.IP_ADDR + "/mediawiki/index.php?title="+ title +"&action=submit";
	}
	
	public void setEditArea(String _edit_area)
	{
		editArea = _edit_area;
	}
	
	public String getEditArea()
	{
		return editArea;
	}
	
	public void setTitle(String _title)
	{
		if(title != null)	System.err.print("title not null : " + _title);
		title = _title.replace(" ", "_");
	}
	
	public String getTitle()
	{
		return title;
	}
}
