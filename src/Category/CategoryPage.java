package Category;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import Http.http;
import Page.Page;

public class CategoryPage extends Page
{
	public CategoryPage(String _title)
	{
		super(_title);
	}
	
	public CategoryPage(String _title, String _edit_area)
	{
		super(_title, _edit_area);
	}
	
	
	public void buildCategoryPage() throws ClientProtocolException, IOException
	{
		http.postPage(this);
	}
	
}
