package Page;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import Http.http;
import PageData.d2gPageData;

public class Disease2GenePage extends Page
{
	public Disease2GenePage(String _title, d2gPageData _d2g)
	{
		super(_title);
		tableEdit = new TableEdit[1];
		tableEdit[0] = new TableEdit("Disease2Gene_TableEdit", _d2g.d2g_data);
		formEditArea();
		this.AddInCategory(_d2g.d2g_data[5].replace("[[", "").replace("]]", "") + "d2gCat");
		this.AddInCategory("Disease2GeneCat");
	}
	
	void formEditArea()
	{
		String _edit_area = 
		"<br/>\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:Disease2Gene_TableEdit</newTableEdit>\n" + 
		"<br/>\n" + 
		"See " + "[[help:Disease2Gene]]" + " for helping with entering information in this table.\n";
		
		this.setEditArea(_edit_area);
	}
	
	public void buildDisease2GenePage() throws ClientProtocolException, IOException
	{
		http.postPage(this);
		http.postPage(this);
	}
}
