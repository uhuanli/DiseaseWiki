package Page;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import Http.http;
import PageData.proteinPageData;

public class ProteinPage extends Page
{
	final String[] _template = new String[]{
		"NameOrigin_TableEdit",
		"ProteinAttributes_TableEdit",
	    "GeneralAnnotation_TableEdit",
	    "GeneOntology_TableEdit",
	    "BinaryInteraction_TableEdit",
	    "AlternativeProducts_TableEdit",
	    "SequenceAnnotation_TableEdit",
	    "Sequences_TableEdit",
	    "EntryInformation_TableEdit",
	    "CrossReferences_TableEdit",
	    "References_TableEdit"
	};
	public ProteinPage(String _title, proteinPageData _ppd)
	{
		super(_title);
		tableEdit = new TableEdit[11];
//		{
//			tableEdit = new TableEdit[1];
//		}
		for(int i = 0; i < tableEdit.length; i ++)
		{
			tableEdit[i] = new TableEdit(_template[i], _ppd.PPD[i]);
		}
		formEditArea();
		/*
		 * add category can't not exe before when EditArea formed;
		 */
		{
			System.out.print(_ppd.PPD[0][0][1] + "\n\n");
		}
		String[] _sp_gene = _ppd.PPD[0][0][1].split("\\\n");
		String _category;
		
		for(int i = 0; i < _sp_gene.length; i ++)
		{
			_category = _sp_gene[i].replace("[[", "").replace("]]", "") + "p2gCat";
			this.AddInCategory(_category);
			{
//				System.out.print("cat: " + _category + "\n");
			}
		}
		
		{
			_category = "ProteinCat";
			this.AddInCategory(_category);
		}
		
		
	}
	void formEditArea()
	{
		String _edit_area = 
		"__TOC__\n" + 
		"__SECTIONLINKTOTOP__\n" + 
		"__NOEDITSECTION__\n" +
		"<br/>\n" + 
		"== Name Origin ==\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:NameOrigin_TableEdit</newTableEdit>\n" + 
		"<br/>\n" + 
		"See " + "[[help:NameOrigin]]" + " for helping with entering information in this table.\n" + 
		"\n" + 
		"== Protein Attributes ==\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:ProteinAttributes_TableEdit</newTableEdit>\n" + 
		"<br/>\n" + 
		"See " + "[[help:ProteinAttributes]]" + " for helping with entering information in this table.\n" + 
		"\n" + 
		"== General annotation ==\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:GeneralAnnotation_TableEdit</newTableEdit>\n" + 
		"<br/>\n" + 
		"See " + "[[help:GeneralAnnotation]]" + " for helping with entering information in this table.\n" + 
		"\n" + 
		"== Gene Ontology ==\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:GeneOntology_TableEdit</newTableEdit>\n" + 
		"<br/>\n" + 
		"See " + "[[help:GeneOntology]]" + " for helping with entering information in this table.\n" + 
		"\n" + 
		"== Binary Interaction ==\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:BinaryInteraction_TableEdit</newTableEdit>\n" + 
		"<br/>\n" + 
		"See " + "[[help:BinaryInteraction]]" + " for helping with entering information in this table.\n" + 
		"\n" + 
		"== Alternative Products ==\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:AlternativeProducts_TableEdit</newTableEdit>\n" + 
		"<br/>\n" + 
		"See " + "[[help:AlternativeProducts]]" + " for helping with entering information in this table.\n" + 
		"\n" + 
		"== Sequence Annotation ==\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:SequenceAnnotation_TableEdit</newTableEdit>\n" + 
		"<br/>\n" + 
		"See " + "[[help:SequenceAnnotation]]" + " for helping with entering information in this table.\n" + 
		"\n" + 
		"== Sequences ==\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:Sequences_TableEdit</newTableEdit>\n" + 
		"<br/>\n" + 
		"See " + "[[help:Sequences]]" + " for helping with entering information in this table.\n" + 
		"\n" + 
		"== Entry Information ==\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:EntryInformation_TableEdit</newTableEdit>\n" + 
		"<br/>\n" + 
		"See " + "[[help:EntryInformation]]" + " for helping with entering information in this table.\n" + 
		"\n" + 
		"== Cross References ==\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:CrossReferences_TableEdit</newTableEdit>\n" + 
		"<br/>\n" + 
		"See " + "[[help:CrossReferences]]" + " for helping with entering information in this table.\n" + 
		"\n" + 
		"== References ==\n" + 
		"<br/>\n" + 
		"<newTableEdit>Template:References_TableEdit</newTableEdit>\n" + 
		"<br/>\n" + 
		"See " + "[[help:References]]" + " for helping with entering information in this table.\n";
		
		this.setEditArea(_edit_area);
	}

	
	public void buildProteinPage() throws ClientProtocolException, IOException
	{
		http.postPage(this);
		http.postPage(this);
	}
	
	public void buildProteinPage(int _i) throws ClientProtocolException, IOException
	{
		for(int i = 0; i < _i; i ++)
			http.postPage(this);
	}
}
