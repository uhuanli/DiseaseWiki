package Disease;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class edUnit
{
	public static int  ID = 1000;
	public String DiseaseID;
	public String sExternalDatabase;
	public String Website;
	
	public void Output() throws IOException
	{
		ExternalDatabase.bwED.write(formRow());
	}
	
	public void Initial()
	{
		DiseaseID = "";
		sExternalDatabase = "";
		Website = "";
	}
	
	public String formRow()
	{
		String _row = (
				ID + "#@!" +
				DiseaseID + "#@!" +
				sExternalDatabase + "#@!" +
				Website + "\n"
		);
		ID ++;
		return _row;
	}
}

public class ExternalDatabase {
	public String 	FileName;
	public static edUnit	_edUnit;
	public static BufferedReader brHuman;
	public static int oiLen = 22335;
	public static BufferedWriter bwED;
	
	public static void main(String[] args) throws IOException
	{
		diInitial();
		String aLine = "";
		{
			String _id = "";
			while(aLine != null)
			{
				aLine = brHuman.readLine();
				if(aLine == null)	return;
				if(aLine.startsWith("id: "))
				{
					_id = aLine;
					_edUnit.Initial();
					_edUnit.DiseaseID = _id.replace("id: ", "");
					continue;
				}
				if(aLine.startsWith("xref: "))
				{
					String [] _sp_line = aLine.split(": ");
					_edUnit.sExternalDatabase = _sp_line[1];
					String _link_data = _sp_line[1].split(":")[1];
					if(aLine.startsWith("xref: ICD9CM:"))
					{
						_edUnit.Website = 
							"http://icd9cm.chrisendres.com/index.php?action=search&srchtext="
							+ _link_data;
					}
					else
					if(aLine.startsWith("xref: MSH:"))
					{
						_edUnit.Website = 
							"http://www.nlm.nih.gov/cgi/mesh/2011/MB_cgi?exact&field=uid&term="
							+ _link_data;
					}
					else
					if(aLine.startsWith("xref: NCI:"))
					{
						_edUnit.Website = 
							"http://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary"
							+"=NCI%%20Thesaurus&code=" + _link_data;
					}
					else
					if(aLine.startsWith("xref: SNOMEDCT_2010_1_31:"))
					{
						_edUnit.Website = "http://vtsl.vetmed.vt.edu";
					}
					else
					if(aLine.startsWith("xref: OMIM:"))
					{
						_edUnit.Website = "http://www.omim.org/entry/" + _link_data;
					}
					else
					if(aLine.startsWith("xref: UMLS_CUI:"))
					{
						_edUnit.Website = "https://uts.nlm.nih.gov/home.html#";
					}
					_edUnit.Output();
					continue;
					}
				}
			}
		diClose();
	}
	
	public static void diInitial()
	{
		_edUnit = new edUnit();
		try {
			bwED = new BufferedWriter(new FileWriter("ExternalDatabase"));
			brHuman = new BufferedReader(new FileReader("HumanDO.obo"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void diClose()
	{
		try {
			bwED.close();
			brHuman.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getHumanData() throws IOException
	{
		String aLine = "";
		int n_id = 0;
		int n_name = 0;
		int n_syn = 0;
		int n_def = 0;
		int n_com = 0;
		int n_xref = 0;

		if(aLine.equals("[Term]"))			
			return aLine;
		return "";
	}
}





