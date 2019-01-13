package DataProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


class Unit{
	public String DiseaseID;
	public String DiseaseTerm;
	public String NameSpace;
	public String Comment;
	public String Def;
	public String Synonym;
	public String Title;
	public String Text;
	public String AllelicVariants;
	public String Reference;
	public String ClinicalSynopsis;
	public String Contributors;
	public String CreationDate;
	public String EditHistory;
	
	public void omimInitial()
	{
		Title = "";
		Text = "";
		AllelicVariants = "";
		Reference = "";
		ClinicalSynopsis = "";
		Contributors = "";
		CreationDate = "";
		EditHistory = "";	
	}
	public String formRow()
	{
		String row;
		row = 
		(
			      DiseaseID+ '|'
				+ DiseaseTerm+ '|'
				+ "disease_ontology"+ '|'
				+ Comment+ '|'
				+ Def+ '|'
				+ Synonym+ '|'
				+ Title+ '|'
				+ Text+ '|'
				+ AllelicVariants+ '|'
				+ Reference+ '|'
				+ ClinicalSynopsis+ '|'
				+ Contributors+ '|'
				+ CreationDate+ '|'
				+ EditHistory	+ "\n"	
		);
		return row;
	}
	public void Initial()
	{
		DiseaseID = "";
		DiseaseTerm = "";
		NameSpace = "";
		Comment = "";
		Def = "";
		Synonym = "";
		Title = "";
		Text = "";
		AllelicVariants = "";
		Reference = "";
		ClinicalSynopsis = "";
		Contributors = "";
		CreationDate = "";
		EditHistory = "";
	}
}

public class DiseaseInfo {
	public String 	FileName;
	public Unit 	diUnit;
	public BufferedWriter bw;
	public BufferedReader brHuman;
	public static BufferedReader brOmim;
	public static omimUnit[] oiUnit;
	public static int oiLen = 22335;
	
	public void diInitial()
	{
		try {
			bw = new BufferedWriter(new FileWriter("DiseaseInfo"));
			brHuman = new BufferedReader(new FileReader("HumanDO.obo"));
			brOmim  = new BufferedReader(new FileReader("omim.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void diClose()
	{
		try {
			bw.close();
			brHuman.close();
			brOmim.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void OutPut()
	{
		String opRow = diUnit.formRow();
		try {
			bw.write(opRow);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void set_di_from_oi(int i)
	{
		diUnit.AllelicVariants = oiUnit[i].fAV;
		diUnit.ClinicalSynopsis = oiUnit[i].fCS;
		diUnit.Contributors = oiUnit[i].fCN;
		diUnit.CreationDate = oiUnit[i].fCD;
		diUnit.EditHistory = oiUnit[i].fED;
		diUnit.Text = oiUnit[i].fTX;
		diUnit.Title = oiUnit[i].fTI;
	}
	
	public void getOmimData(String OmimID)
	{
		for(int i = 0; i < oiLen; i ++)
		{
			if(oiUnit[i] != null)
			{
				if(oiUnit[i].fNO.equals(OmimID))
				{
					set_di_from_oi(i);
				}
			}
		}
	}
	
	public String getHumanData() throws IOException
	{
		String aLine = "";
		while(aLine != null && !(aLine.equals("[Term]")))
		{
			aLine = brHuman.readLine();
			if(aLine == null)	return "";
			if(aLine.startsWith("id:"))
			{
				
			}
			else
			if(aLine.startsWith("name:"))
			{
				
			}
			else
			if(aLine.startsWith("synonym:"))
			{
				
			}
			else
			if(aLine.startsWith("def:"))
			{
				
			}
			else
			if(aLine.startsWith("comment:"))
			{
				
			}
			else
			if(aLine.startsWith("xref: OMIM:"))
			{
				
			}
		}
		if(aLine.equals("[Term]"))
			return aLine;
		return "";
	}
	
	public static void LoadOmim() throws IOException
	{
		oiUnit = new omimUnit[oiLen];
		brOmim = new BufferedReader(new FileReader("omim.txt"));
		String rLine = "";
		String cont = "";
		int oiCount = -1;
		while(rLine != null)
		{
			if(rLine.startsWith("*FIELD* NO"))
			{
				oiCount ++;
				if(oiCount % 1000 == 0)
				{
					System.out.print(oiCount + "");
				}
				if(oiCount > 22000 && oiCount % 20 == 0)
				{
					System.out.print(oiCount +"\t");
				}
				oiUnit[oiCount] = new omimUnit(); 
				rLine = brOmim.readLine();
				oiUnit[oiCount].fNO = rLine;

				while(rLine != null)
				{
					if(rLine.startsWith("*RECORD*"))
						break;
					if(rLine.startsWith("*FIELD*"))
					{
						String _rline = rLine;
						cont = rLine + "\n";
						rLine = "";
						while(true)
						{
							rLine = brOmim.readLine();
							if(rLine == null)
								break;
							if(rLine.startsWith("*FIELD*") || rLine.startsWith("*RECORD*"))
								break;
							cont += rLine + "\n";
						}
						if(_rline.startsWith("*FIELD* TI"))
							oiUnit[oiCount].fTI = cont;
						if(_rline.startsWith("*FIELD* TX"))
							oiUnit[oiCount].fTX = cont;
						if(_rline.startsWith("*FIELD* AV"))
							oiUnit[oiCount].fAV = cont;
						if(_rline.startsWith("*FIELD* RF"))
							oiUnit[oiCount].fRF = cont;
						if(_rline.startsWith("*FIELD* CS"))
							oiUnit[oiCount].fCS = cont;
						if(_rline.startsWith("*FIELD* CN"))
							oiUnit[oiCount].fCN = cont;
						if(_rline.startsWith("*FIELD* CD"))
							oiUnit[oiCount].fCD = cont;
						if(_rline.startsWith("*FIELD* ED"))
							oiUnit[oiCount].fED = cont;
						continue;
					}					
					rLine = brOmim.readLine();
				}
			}
			rLine = brOmim.readLine();
		}
		brOmim.close();
	}
	
	public void BuildTable() throws IOException
	{
		diInitial();
		
		String Row = "";
		while(Row != null)
		{
			if(Row.equals("[Term]"))
			{
				Row = getHumanData();
			}
			else
			{
				try {
					Row = brHuman.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
		
		diClose();
	}
}


