package Protein;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class rfUnit
{
	public String ID;
	public String PubMed_ID;
	public String DOI;
	public String Medline;
	public String Tissue;
	public String Author;
	public String Title;
	public String Literature;
	public String Citation;
	
	public void Output() throws IOException
	{
		References.bwRF.write(formRow());
	}
	
	public String formRow()
	{
		if(PubMed_ID.equals(""))	PubMed_ID = " ";
		if(DOI.equals(""))			DOI = " ";
		if(Medline.equals(""))		Medline = " ";
		if(Tissue.equals(""))		Tissue = " ";
		if(Author.equals(""))		Author = " ";
		if(Title.equals(""))		Title = " ";
		if(Literature.equals(""))	Literature = " ";
		if(Citation.equals(""))		Citation = " ";
		
		String _pbID = " ";
		if(!PubMed_ID.equals(" "))
		{
			_pbID = "[http://www.ncbi.nlm.nih.gov/pubmed/" + PubMed_ID + " " + PubMed_ID + "]";
		}
		
		String _row = (
				_pbID + "#@!" +
				DOI + "#@!" +
				Medline + "#@!" +
				Tissue + "#@!" +
				Author + "#@!" +
				Title + "#@!" +
				Literature + "#@!" +
				Citation + "\n-----\n"
		);
		return _row;
	}
	
	public void Initial()
	{
		ID = "";
		PubMed_ID = "";
		DOI = "";
		Medline = "";
		Tissue = "";
		Author = "";
		Title = "";
		Literature = "";
		Citation = "";
	}
	
	public void partInitial()
	{
		PubMed_ID = "";
		DOI = "";
		Medline = "";
		Tissue = "";
		Author = "";
		Title = "";
		Literature = "";
		Citation = "";
	}
}

public class References {
	
	public static BufferedReader brRF;
	public static BufferedWriter  bwRF;
	public static rfUnit		_rfUnit;
	
	public static void Initial() throws IOException
	{
		_rfUnit = new rfUnit();
		
		brRF = new BufferedReader(new FileReader("uniprot_sprot_human.dat"));
		bwRF = new BufferedWriter(new FileWriter("References"));
	}
	
	public static void bClose() throws IOException
	{
		brRF.close();
		bwRF.close();
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
				_rfUnit.Initial();
				String unitID = rLine.replace("ID   ", "");
				_rfUnit.ID = unitID;
				
				if(GACount % 2000 == 0)
					System.out.print(
							GACount + " => \t"
							);
					

				rLine = "";
				{
					References.bwRF.write("==========\n");
					References.bwRF.write(unitID.split("             ")[0] + "\n");
				}
				while(! rLine.startsWith("ID   "))
				{
					String cont1 = "";
					if(rLine.startsWith("RN   "))
					{
						rLine = brRF.readLine();
						_rfUnit.partInitial();
						while(rLine.startsWith("R") && !rLine.startsWith("RN   "))
						{
							if(rLine.startsWith("RX"))
							{
								String _cont_r = rLine.replace("RX   ", "").trim();
								while((rLine = brRF.readLine()).startsWith("RX"))
								{
									_cont_r += rLine.replace("RX   ", "").trim();
								}
								String[] _sp_r = _cont_r.split("; ");
								for(int i = 0; i < _sp_r.length; i ++)
								{
									if(_sp_r[i].indexOf("PubMed") != -1)
									{
										_rfUnit.PubMed_ID = (_sp_r[i].replace("PubMed=", "")).replace(";", "");
									}
									
									if(_sp_r[i].indexOf("DOI=") != -1)
									{
										_rfUnit.DOI = (_sp_r[i].replace("DOI=", "")).replace(";", "");
									}
									
									if(_sp_r[i].indexOf("MEDLINE=") != -1)
									{
										_rfUnit.Medline = (_sp_r[i].replace("MEDLINE=", "")).replace(";", "");
									}
								}	
								continue;
							}
							
							if(rLine.startsWith("RC"))
							{
								String _rc = rLine.replace("RC   ", "").trim();
								String[] _sp_rc = _rc.split(";");
								for(int i = 0; i < _sp_rc.length; i ++)
								{
									if(_sp_rc[i].indexOf("TISSUE") != -1)
									{
										_rfUnit.Tissue = (_sp_rc[i].replace("TISSUE=", "")).replace(";", "");
									}
								}
								rLine = brRF.readLine();
								continue;
							}
							
							if(rLine.startsWith("RA   "))
							{
								String _cont_r = rLine.replace("RA   ", "").trim();
								while((rLine = brRF.readLine()).startsWith("RA"))
								{
									_cont_r += "\n" + rLine.replace("RA", "").trim();
								}
								_rfUnit.Author = _cont_r;
								continue;
							}
							
							if(rLine.startsWith("RT   "))
							{
								String _cont_r = rLine.replace("RT   ", "").trim();
								while((rLine = brRF.readLine()).startsWith("RT"))
								{
									_cont_r += "\n" + rLine.replace("RT", "").trim();
								}
								_rfUnit.Title = _cont_r;
								continue;
							}
							
							if(rLine.startsWith("RL   "))
							{
								String _cont_r = rLine.replace("RL   ", "").trim();
								while((rLine = brRF.readLine()).startsWith("RL"))
								{
									_cont_r += "\n" + rLine.replace("RL", "").trim();
								}
								_rfUnit.Literature = _cont_r;
								continue;
							}
							
							if(rLine.startsWith("RP   "))
							{
								String _cont_r = rLine.replace("RP   ", "").trim();
								while((rLine = brRF.readLine()).startsWith("RP"))
								{
									_cont_r += "\n" + rLine.replace("RP", "").trim();
								}
								_rfUnit.Citation = _cont_r;
								continue;
							}
							rLine = brRF.readLine();
							if(rLine == null) break;
						}
						_rfUnit.Output();
						continue;
					}
					rLine = brRF.readLine();
					if(rLine == null) break;
				}
				continue;
			}
			rLine = brRF.readLine();
		}
		bClose();
		{
			System.out.print("count: " + GACount + "\n");
		}
	}
}
