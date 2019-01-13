package Protein;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;


class crUnit
{
	public String ID;
	public String NCR;
	public String CCR;
	public String ICR;
	public String WCR;
	public String Other;
	public String Notes;
	
	public void Initial()
	{
		ID = "";
		NCR = "";
		CCR = "";
		ICR = "";
		WCR = "";
		Other = "";
		Notes = "";
	}
	
	public void Output() throws IOException
	{
		Cross_References.bwCR.write(formRow());
	}
	
	public String formRow()
	{

		
		String[] _sp = Cross_References.NCR_WebSite.get(NCR).split("\t");
		{
			if(_sp.length != 3)
			{
				System.out.print("bug len cr\n");
			}
		}
		
		CCR = _sp[0];
		
		if(ICR.replace(" ", "").equals(""))
		{
			System.err.print("icr bug\n");
		}
			WCR = _sp[2].replace("hello!@#world", ICR);
		
		{
			if(NCR.equals(""))		NCR = " ";
			if(CCR.equals(""))		CCR = " ";
			if(ICR.equals(""))		ICR = " ";
			if(WCR.equals(""))		WCR = " ";
			if(Other.equals(""))	Other = " ";
		}
		
		String _row = (
		NCR + "#@!" + 
		CCR + "#@!" + 
		ICR + "#@!" + 
		WCR + "#@!" + 
		Other + "#@!" + 
		" " + "\n-----\n");
		return _row;
	}
	
}

public class Cross_References {

	
	public static BufferedReader brCR;
	public static BufferedWriter  bwCR;
	public static crUnit		_crUnit;
	public static HashMap<String, String>  	NCR_WebSite; 
	
	public static void Initial() throws IOException
	{
//		BufferedWriter _bw_debug = new BufferedWriter(new FileWriter("debug"));
		
		_crUnit = new crUnit();
		NCR_WebSite = new HashMap<String, String>();
		brCR = new BufferedReader(new FileReader("uniprot_sprot_human.dat"));
		bwCR = new BufferedWriter(new FileWriter("CrossReferences"));
		{
			BufferedReader brWS = new BufferedReader(new FileReader("WebSite.txt"));
			String rLine;
			String[] spLine;
			HashSet<String> _no_site = new HashSet<String>();
			{
				_no_site.add("SMR");
				_no_site.add("eggNOG");
				_no_site.add("HOGENOM");
				_no_site.add("HOVERGEN");
				_no_site.add("OMA");
				_no_site.add("OrthoDB");
				_no_site.add("BRENDA");
				_no_site.add("ProtClustDB");
				_no_site.add("Aarhus/Ghent-2DPAGE");

			}
			while((rLine = brWS.readLine()) != null)
			{
				spLine = rLine.split("\t");
				{
					if(spLine.length != 4 || _no_site.contains(spLine[0]))
					{
						System.out.print("bug len ws: " + spLine.length + "\n");
						spLine = new String[]{
							spLine[0],
							spLine[1],
							spLine[2],
							" "
						};
					}
					
					spLine[3] = spLine[3].replace(spLine[2], "hello!@#world");
					
				}
				NCR_WebSite.put(spLine[0], spLine[1] + "\t" + spLine[2] + "\t" + spLine[3]);
			}
		}
	}
	
	public static void bClose() throws IOException
	{
		brCR.close();
		bwCR.close();
	}

	public static void main(String[] args) throws IOException
	{
		cal_mul();
	}
	
	public static void cal_mul() throws IOException
	{

		Initial();
		int GACount = 0;		

		String rLine = "";
		String _id = "";
		while(rLine != null)
		{
			if(rLine.startsWith("ID   "))
			{
				_id = rLine;
				GACount ++;
				_crUnit.Initial();
				String unitID = rLine.replace("ID   ", "");
				_crUnit.ID = unitID;
				
				{
					String[] _sp_id = unitID.split(";");
					_sp_id = _sp_id[0].split("             ");
				}
				if(GACount % 2000 == 0)
					System.out.print(
							GACount + " => \t"
							);
					

				rLine = "";
				String cont1 = "";
				{
					Cross_References.bwCR.write("==========\n");
					Cross_References.bwCR.write(unitID.split("             ")[0] + "\n");
				}
				while(! rLine.startsWith("ID   "))
				{
					if(rLine.startsWith("DR   "))
					{
						if(rLine.startsWith("DR   GO"))
						{

						}
						else
						{
							String[] _go = rLine.split("; ");
							_crUnit.NCR = _go[0].replace("DR   ", "");
							_crUnit.ICR = _go[1];
							_go = rLine.split(_go[1] + "; ");
							_crUnit.Other = _go[1];
							_crUnit.WCR = "";
							_crUnit.Output();
						}
					}
					rLine = brCR.readLine();
					if(rLine == null) break;
				}
				continue;
			}
			rLine = brCR.readLine();
		}
		bClose();
		{
			System.out.print("count: " + GACount + "\n");
		}
	}
}
