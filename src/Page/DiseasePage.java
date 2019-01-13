package Page;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import Http.http;
import Main.Main;
import PageData.diseasePageData;

public class DiseasePage extends Page
{
	public DiseasePage(String _title, diseasePageData _dpd)
	{
		super(_title);
		if(_dpd.hasOMIM())
		{
			tableEdit = new TableEdit[5];
			tableEdit[0] = new TableEdit("DiseaseInfo_TableEdit", _dpd.getDiseaseData());
			String[] _template = new String[]{
					"DText",
					"DAllelicVariants",
					"DClinicalSynopsis",
					"DReferences"
			};
			for(int i = 0; i < 4; i ++)
			{
				tableEdit[i + 1] = new TableEdit(_template[i], _dpd.getOmimField(i));
			}
		}
		else
		{
			tableEdit = new TableEdit[1];
			tableEdit[0] = new TableEdit("DiseaseInfo_TableEdit", _dpd.getDiseaseData());
		}
		formEditArea(_title);
		this.AddInCategory(_title + "d2dCat");
		this.AddInCategory("DiseaseCat");
	}
	
	public void formEditArea(String _dt)
	{
		String _gns = Main.hmDG.get(_dt.toLowerCase());
		String proteinTab = "";
		if(_gns == null)
		{
			proteinTab += "no related protein";
		}
		else
		{
			String[] gn = _gns.split("\\|");
			int _ii = 0;
			for(String iGN: gn)
			{
				_ii ++;
				if(_ii > 100) break;
				proteinTab += "<categorytree mode = pages showcount = on>" + iGN + "p2gCat" + "</categorytree>\n";
			}
		}
 		String _edit_area = 
			"<tabber>\n" + 
			"DiseaseTerm=\n" + 
			"== DiseaseTerm ==\n" + 
			"<br/>\n" + 
			"__TOC__\n" + 
			"__SECTIONLINKTOTOP__\n" + 
			"__NOEDITSECTION__\n" + 
			"<br/>\n" + 
			"== Basic info ==\n" + 
			"\n" + 
			"<br/>\n" + 
			"<newTableEdit>Template:DiseaseInfo_TableEdit</newTableEdit>\n" + 
			"\n" + 
			"<br/>\n" + 
			"See " + "[[help:DiseaseInfo]]" + " for helping with entering information in this table.\n" + 
			"<br/>\n" + 
			"== Text ==\n<br/>\n" + 
			"\n" + 
			"<newTableEdit>Template:DText</newTableEdit><br/>\n" +
			"<br/>\n" + 
			"See " + "[[help:Text]]" + " for helping with entering information in this table.\n" + 
			"\n" + 
			"== Allelic Variants ==\n<br/>\n" + 
			"\n" + 
			"<newTableEdit>Template:DAllelicVariants</newTableEdit><br/>\n" + 
			"See " + "[[help:AllelicVariants]]" + " for helping with entering information in this table.\n" + 
			"<br/>\n" + 
			"== Clinical Synopsis ==\n<br/>\n" + 
			"\n" + 
			"<newTableEdit>Template:DClinicalSynopsis</newTableEdit><br/>\n" +
			"See " + "[[help:ClinicalSynopsis]]" + " for helping with entering information in this table.\n" + 
			"<br/>\n" + 
			"== References ==\n<br/>\n" + 
			"\n" + 
			"<newTableEdit>Template:DReferences</newTableEdit><br/>\n" +
			"See " + "[[help:References]]" + " for helping with entering information in this table.\n" +
			"<br/>\n" + 
			"|-|\n" + 
			"Disease2Gene = \n" + 
			"Please unfold "+" to see the relations between " + _dt +
			" and their relevant genes if necessary. \n\tClick each item for the detail of each relation" + 
			"<br/>\n" + 
			"<categorytree mode = pages showcount = on>" + this.getTitle() + "d2gCat" +  "</categorytree>\n" + 
			"<br/>\n" + 
			"|-|\n" + 
			"Gene = \n" +
			"Please unfold "+" to see the disease-related genes if necessary. \n\tClick each item for the detail of each gene." + 
			"<br/>\n" + 
			"<categorytree mode = pages showcount = on>" + this.getTitle() + "gDCat" + "</categorytree>\n" + 
			"<br/>\n" + 
			"|-|\n" + 
			"Protein=\n" + 
			"Please unfold "+" to see the products of each gene. \n\tClick each item for the detail of each gene product. " + 
			proteinTab + "\n" +  
			"</tabber>\n";

			this.setEditArea(_edit_area);
	}
	
	public void buildDiseasePage() throws ClientProtocolException, IOException
	{
		http.postPage(this);
		http.postPage(this);
	}
}
