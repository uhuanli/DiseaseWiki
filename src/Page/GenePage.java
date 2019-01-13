package Page;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import Http.http;

public class GenePage extends Page
{
	final static int GeneInfoTE = 0;
	final static int GeneProTE = 1;
	
	public GenePage(String _title, String[][] fieldGI)
	{
		super(_title);
		tableEdit = new TableEdit[1];
		/*
		 * check length
		 */
		tableEdit[GeneInfoTE] = new TableEdit("Gene_TableEdit", fieldGI);

		
		String _edit_area = 
		"<br/>\n" + 
		"__TOC__\n" + 
		"__NOEDITSECTION__\n" +
		"__SECTIONLINKTOTOP__\n" + 
		"<br/>\n" + 
		"== Gene Information ==\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:" + tableEdit[GeneInfoTE].getTemplate() + "</newTableEdit>\n" + 
		"<br/>\n" + 
		"<br/>\n" + 
		"See " + "[[help:GeneInfo]]" + " for helping with entering information in this table.\n" + 
		"<br/>\n" + 
		"\n" + 
		"== Gene Product==\n" + 
		"<br/>\n" + 
		"<categorytree  mode = pages showcount = on>" + _title + "p2gCat" + "</categorytree>" + "\n" + 
		"<br/>\n" + 
		"<br/>\n";
		
		this.setEditArea(_edit_area);	
		{
			String[] dCat = tableEdit[GeneInfoTE].getField(0)[15].split("#@!");
			
			for(String iD : dCat)
			{
				this.AddInCategory(iD + "gDCat");
			}
			
			this.AddInCategory("GeneCat");
		}
	}
	
	public void buildGenePage() throws ClientProtocolException, IOException
	{
		http.postPage(this);
		http.postPage(this);
	}
	public void buildGenePage(int _i) throws ClientProtocolException, IOException
	{
		for(int i = 0; i < _i; i ++)
			http.postPage(this);
	}
}
