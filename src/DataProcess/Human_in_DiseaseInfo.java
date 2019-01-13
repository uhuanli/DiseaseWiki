package DataProcess;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Human_in_DiseaseInfo {
	public String 	FileName;
	public huUnit 	diUnit;
	public static BufferedWriter bw;
	public static BufferedReader brHuman;
	public static BufferedReader brOmim;
	public static omimUnit[] oiUnit;
	public static huUnit	 _huUnit;
	public static int oiLen = 22335;
	public static BufferedWriter bwED;
	public static HashMap<String, Integer> hsSI;
	public static HashSet<String> hsOMIM = new HashSet<String>();
	public static HashSet<String> hsDisease = new HashSet<String>();
	
	public static void main(String[] args) throws IOException
	{
		diInitial();
		BuildTable();
//		LoadOmim();
//		{
//			int _i = hsSI.get("100070");
//			System.out.print(oiUnit[_i].formRow());
//		}
		diClose();
	}
	
	public static void diInitial()
	{
		hsSI = new HashMap<String, Integer>();
		hsDisease.clear();
		_huUnit = new huUnit();
		try {
			bwED = new BufferedWriter(new FileWriter("ExternalDatabase"));
			bw = new BufferedWriter(new FileWriter("HumanDO_in_DiseaseInfo"));
			brHuman = new BufferedReader(new FileReader("HumanDO.obo"));
			brOmim  = new BufferedReader(new FileReader("omim.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void diClose() throws IOException
	{
		try {
			bwED.close();
			bw.close();
			brHuman.close();
			brOmim.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter bw_Disease = new BufferedWriter(new FileWriter("DiseaseFormHu"));
		{
			for(String iS: hsDisease)
			{
				bw_Disease.write(iS + "\t");
			}
		}
		bw_Disease.close();
	}
	
	public static void huOutput() throws IOException
	{
			if(_huUnit.Synonym.equals(""))	_huUnit.Synonym = " ";
			if(_huUnit.OMIM_ID.equals(""))	_huUnit.OMIM_ID = " ";
			String _row = (
					_huUnit.DiseaseID.replace("id: ", "") + "|" +
					_huUnit.DiseaseTerm + "|" +
					"disease_ontology" + "|" +
					_huUnit.Comment + "|" +
					_huUnit.Def + "|" +
					_huUnit.Synonym + "|" +
					_huUnit.OMIM_ID + "\n"
					
			);
			hsDisease.add(_huUnit.DiseaseTerm);
			bw.write(_row);
	}

	
	public void set_di_from_oi(int i)
	{

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
	
	public static int m_id = 0;
	public static int m_name = 0;
	public static int m_syn = 0;
	public static int m_def = 0;
	public static int m_com = 0;
	public static int m_xref = 0;
	
	public static String getHumanData() throws IOException
	{
		String aLine = "";
		int n_id = 0;
		int n_name = 0;
		int n_syn = 0;
		int n_def = 0;
		int n_com = 0;
		int n_xref = 0;
		String _id = "";
		while(aLine != null)
		{
			if(aLine.equals("[Term]"))	break;
			aLine = brHuman.readLine();
			if(aLine == null)	return "";
			aLine.replace("\t", "");
			if(aLine.startsWith("id:"))
			{
				n_id ++;
				_id = aLine;
				_huUnit.Initial();
				_huUnit.DiseaseID = _id;
				continue;
			}
			if(aLine.startsWith("name:"))
			{
				n_name ++;
				_huUnit.DiseaseTerm = aLine.replace("name: ", "");
				continue;
			}
			else
			if(aLine.startsWith("synonym:"))
			{
				n_syn ++;
				if(n_syn >= 40)
					System.out.println(_id);
				if(_huUnit.Synonym.equals(""))					
					_huUnit.Synonym = aLine.replace("synonym: ", "");
				else
					_huUnit.Synonym += "; " + aLine.replace("synonym: ", "");
				
				continue;
			}
			else
			if(aLine.startsWith("def:"))
			{
				n_def ++;
				_huUnit.Def = (aLine.replace("def: \"", "")).replace("def: ", "");
				continue;
			}
			else
			if(aLine.startsWith("comment:"))
			{
				n_com ++;
				_huUnit.Comment = aLine.replace("comment: ", "");
				continue;
			}
			else
			if(aLine.startsWith("xref: OMIM:"))
			{
				n_xref++;
				if(n_xref >= 40)
					System.out.println(_id);
				if(_huUnit.OMIM_ID.equals(""))
					_huUnit.OMIM_ID = aLine.replace("xref: OMIM:", "");
				else
				{
					_huUnit.OMIM_ID += "; " + aLine.replace("xref: OMIM:", "");
				}
				hsOMIM.add(aLine.replace("xref: OMIM:", ""));
				continue;
			}
		}
		
		
		huOutput();
		if(m_id < n_id) m_id = n_id;
		if(m_name < n_name) m_name = n_name;
		if(m_syn < n_syn) m_syn = n_syn;
		if(m_def < n_def) m_def = n_def;
		if(m_xref < n_xref) m_xref = n_xref;
		if(m_com < n_com) m_com = n_com;
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
			{
				if(oiCount % 1000 == 0) 
					System.out.print("oic: " + oiCount + "\n");
			}
			if(rLine.startsWith("*FIELD* NO"))
			{
				oiCount ++;
				oiUnit[oiCount] = new omimUnit(); 
				rLine = brOmim.readLine();
				oiUnit[oiCount].fNO = rLine;
				hsSI.put(rLine, oiCount);
				{
//					System.out.print("1\n");
				}
				while(rLine != null)
				{
					{
//						System.out.print("2\n");
					}
					if(rLine.startsWith("*RECORD*"))
						break;
					if(rLine.startsWith("*FIELD*"))
					{
						String _rline = rLine;
						cont = rLine + "\n";
						rLine = "";
						while(true)
						{
							{
//								System.out.print("3\n");
							}
							rLine = brOmim.readLine();
							if(rLine == null)
								break;
							if(rLine.startsWith("*FIELD*") || rLine.startsWith("*RECORD*"))
								break;
							if(rLine.replace(" ", "").replace("\n", "").equals(""))
							{
								rLine = "";
							}
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
	
	public static void BuildTable() throws IOException
	{
		diInitial();
		
		String Row = "";
		while(Row != null)
		{
			if(Row.equals("[Term]"))
			{
				_huUnit.Initial();
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
		
		System.out.print(
				m_id + "--id\n" +
				m_name + "--name\n" +
				m_syn + "--syn\n" +
				m_def + "--def\n" +
				m_com + "--com\n" +
				m_xref + "--xref\n"
		);
		
		System.out.print("size omim: " + hsOMIM.size());
		{
			BufferedWriter _bwOMIM = new BufferedWriter(new FileWriter("OMIM_ID"));
			for(String iS: hsOMIM)
			{
				_bwOMIM.write(iS + "\t");
			}
			_bwOMIM.close();
		}
		
		diClose();
	}
}


