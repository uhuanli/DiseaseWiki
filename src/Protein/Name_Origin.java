package Protein;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Name_Origin {
	
	public static BufferedReader brNO;
	public static BufferedWriter bwNO;
	public static noUnit _noUnit;
	
	public static void Output() throws IOException
	{
		if(_noUnit.iPN == 0) _noUnit.iPN = 1;
		if(_noUnit.iGN == 0) _noUnit.iGN = 1;
		if(_noUnit.iTL == 0) _noUnit.iTL = 1;
		
		String _TLs = " ";
		for(int i = 0; i < _noUnit.iTL; i ++)
		{
			if(i == 0)
			{
				_TLs = _noUnit.TaxonomicLineage[i];
			}
			else
			{
				_TLs += "\n" + _noUnit.TaxonomicLineage[i];
			}
		}
		String _GNs = " ";
		for(int i = 0; i < _noUnit.iGN; i ++)
		{
			if(i == 0)
			{
				_GNs = "[[" + _noUnit.GeneName[i] + "]]";
			}
			else
			{
				_GNs += "\n[[" + _noUnit.GeneName + "]]";
			}
		}
		String _PNs = " ";
		for(int i = 0; i < _noUnit.iPN; i ++)
		{
			if(i == 0)
			{
				_PNs = _noUnit.ProteinName[i];
			}
			else
			{
				_PNs += "\n" + _noUnit.ProteinName[i];
			}
		}

		String _ID = _noUnit.ID.split("             ")[0];
		
		{
			if(_noUnit.Organism.equals(" ")) 			_noUnit.Organism = " ";
			if(_noUnit.TaxonomicIdentifier.equals(" ")) _noUnit.TaxonomicIdentifier = " ";
			if(_noUnit.Taxonomic_URL.equals(" ")) 		_noUnit.Taxonomic_URL = " ";
		}
		
		String _row = ("==========" + "\n" +
		_ID + "\n" +  
		_PNs + "#@!" +
		_GNs + "#@!" + 
		_noUnit.Organism + "#@!" +
		_noUnit.TaxonomicIdentifier + "#@!" + 
		_noUnit.Taxonomic_URL + "#@!" + 
		_TLs + "\n-----\n"
		);
		bwNO.write(_row);
//		*/
	}
	
	public static void Initial() throws IOException
	{
		_noUnit = new noUnit();
		brNO = new BufferedReader(new FileReader("uniprot_sprot_human.dat"));
		bwNO = new BufferedWriter(new FileWriter("Name_Origin"));
	}
	
	public static void bClose() throws IOException
	{
		brNO.close();
		bwNO.close();
	}
	
	public static void main(String[] args) throws IOException
	{
		NameOrigin();
	}
	
	public static void NameOrigin() throws IOException
	{
		Initial();
		int noCount = 0;
		int i_pn = 0;
		int i_gn = 0;
		int i_og = 0;
		int i_ti = 0;
		int i_tu = 0;
		int i_tl = 0;
		
		int m_id = 0;
		int m_pn = 0;
		int m_gn = 0;
		int m_og = 0;
		int m_ti = 0;
		int m_tu = 0;
		int m_tl = 0;
		String rLine = "";
		String _id = "";
		while(rLine != null)
		{
			if(rLine.startsWith("ID   "))
			{
				_id = rLine;
				noCount ++;
				if(noCount % 2000 == 0)
					System.out.print(
							noCount + " => \t"
							);
					
				i_pn = 0;
				i_gn = 0;
				i_og = 0;
				i_ti = 0;
				i_tu = 0;
				i_tl = 0;
				_noUnit.Initial();
				_noUnit.ID = rLine.replace("ID   ", "");
				rLine = "";
				while(! rLine.startsWith("ID   "))
				{
					if(rLine.startsWith("DE   "))
					{
						_noUnit.setPN(rLine.replace("DE   ", "").trim());
						i_pn = rLine.length();;
					}
					else
					if(rLine.startsWith("GN   Name="))
					{
						String[] _rline = (rLine.replace("GN   Name=", "")).split(";");//Name=
						_noUnit.setGN(_rline[0].trim());
						i_gn = rLine.length();
					}
					else
					if(rLine.startsWith("OS   "))
					{
						i_og = rLine.length();;
						_noUnit.Organism = rLine.replace("OS   ", "");
					}
					else
					if(rLine.startsWith("OX   "))
					{
						String _tmprl = rLine.replace("OX   ", "");//NCBI_TaxID=
						_noUnit.TaxonomicIdentifier = _tmprl.replace(";", "");
						_noUnit.Taxonomic_URL = 
							(
									"http://www.ncbi.nlm.nih.gov/Taxonomy/"+
									"Browser/wwwtax.cgi?lvl=0&id="+
									_noUnit.TaxonomicIdentifier.replace("NCBI_TaxID=", "")
							);
						i_ti = rLine.length();;
						i_tu = rLine.length();;
					}
					else
					if(rLine.startsWith("OC   "))
					{
						i_tl = rLine.length();;
						_noUnit.setTL(rLine.replace("OC   ", "").trim());
					}
					rLine = brNO.readLine();
					if(rLine == null) break;
				}

				if(m_pn < i_pn) m_pn = i_pn;
				if(m_og < i_og) m_og = i_og;
				if(m_gn < i_gn) m_gn = i_gn;
				if(m_ti < i_ti) m_ti = i_ti;
				if(m_tu < i_tu) m_tu = i_tu;
				if(m_tl < i_tl) m_tl = i_tl;
				Output();
				continue;
			}
			
			rLine = brNO.readLine();
		}
		
		System.out.print(
				"Count: = " + noCount + "#\t\t" +
				m_pn + "\t\t" +
				m_gn + "\t\t" +
				m_og + "\t\t" +
				m_ti + "\t\t" +
				m_tu + "\t\t" +
				m_tl + "\t\t"
		);
		
		bClose();
	}
}


class noUnit
{
	public String ID;
	public String[] ProteinName = new String[68];
	public String[] GeneName = new String[28];
	public String Organism;
	public String TaxonomicIdentifier;
	public String Taxonomic_URL;
	public String[] TaxonomicLineage = new String[4];
	
	public int iPN;
	public int iGN;
	public int iTL;	
	
	public void setPN(String _pn)
	{
		ProteinName[iPN] = _pn;
		iPN ++;
	}
	public void setGN(String _gn)
	{
		GeneName[iGN] = _gn;
		iGN ++;
	}
	public void setTL(String _tl)
	{
		TaxonomicLineage[iTL] = _tl;
		iTL ++;
	}
	
	public noUnit()
	{
		Initial();
	}
	
	public void Initial(){
		iPN = iGN = iTL = 0;
		ID = "";
		for(int i = 0; i < ProteinName.length; i ++)
			ProteinName[i] = "";
		
		 for(int i = 0; i < GeneName.length; i ++)
			GeneName[i] = "";
		 
		Organism = "";
		TaxonomicIdentifier = "";
		Taxonomic_URL = "";
		
		for(int i = 0; i < TaxonomicLineage.length; i ++)
			TaxonomicLineage[i] = "";
	}
		
}


