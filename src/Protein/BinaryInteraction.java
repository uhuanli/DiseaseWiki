package Protein;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



class biUnit
{
	public String ID;
	public String With;
	public String Entry;
	public String Evidence_Total;
	public String Evidence_Detail;
	public String IntAct;
	public String iaWebsite;
	public String Notes;
	
	public void Output() throws IOException
	{
		BinaryInteraction.bwBI.write(formRow());
		BinaryInteraction.FinishItem = true;
	}
	public void Initial()
	{
		ID = "";
		With = "";
		Entry = "";
		Evidence_Total = "";
		Evidence_Detail = "";
		IntAct = "";
		iaWebsite = "";
		Notes = "";
		BinaryInteraction.FinishItem = false;
	}
	public String formRow()
	{
		if(With.equals(""))					With = " ";
		if(Entry.equals(""))				Entry = " ";
		if(Evidence_Total.equals(""))		Evidence_Total = " ";
		if(Evidence_Detail.equals(""))		Evidence_Detail = " ";
		if(IntAct.equals(""))				IntAct = " ";
		if(iaWebsite.equals(""))			iaWebsite = " ";
		
		String _row = (
				With + "#@!" + 
				Entry + "#@!" + 
				Evidence_Total + "#@!" + 
				Evidence_Detail + "#@!" + 
				IntAct + "#@!" + 
				iaWebsite + "#@!" + 
				" " + "\n-----\n"
		);
		return _row;
	}
	
}

public class BinaryInteraction {
	
	public static BufferedReader brBI;
	public static BufferedWriter bwBI;
	public static biUnit		_biUnit;
	public static boolean FinishItem;
	
	public static void Initial() throws IOException
	{
		_biUnit = new biUnit();
		FinishItem = true;
		bwBI = new BufferedWriter(new FileWriter("BinaryInteractions"));
		brBI = new BufferedReader(new FileReader("uniprot_sprot_human.dat"));
	}
	
	public static void bClose() throws IOException
	{
		bwBI.close();
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
		_biUnit = new biUnit();
		while(rLine != null)
		{
			if(rLine.startsWith("ID   "))
			{
				_id = rLine;
				GACount ++;
				String unitID = rLine.replace("ID   ", "");
				_biUnit.Initial();
				_biUnit.ID = unitID;
				bwBI.write("==========\n" + unitID.split("             ")[0] + "\n");
				{
					String[] _sp_id = unitID.split(";");
					_sp_id = _sp_id[0].split("             ");
				}
				if(GACount % 2000 == 0)
					System.out.print(
							GACount + " => \t"
							);
					

				rLine = "";
				while(! rLine.startsWith("ID   "))
				{
					String cont1 = "";
					if(rLine.startsWith("CC   "))
					{
						while(rLine != null)
						{
							if(!rLine.startsWith("CC")) break;
							if(rLine.startsWith("CC   -!- INTERACTION:"))
							{
								cont1 = rLine.replace("CC   -!- INTERACTION:", "");
								rLine = brBI.readLine();
								while(!rLine.startsWith("CC   -!-") && rLine.startsWith("CC   "))
								{
									cont1 = (rLine.replace("CC   ", "")) .replace("    ", "").trim(); 
									String[] _cont1 = cont1.split("; ");
									if(rLine.startsWith("CC       Self;"))
									{
										String[] _firstItem = {new String(""), new String("itself")};
										_biUnit.Entry = _firstItem[0].trim();
										_biUnit.With = _firstItem[1].trim();
										_biUnit.Evidence_Detail = "";
										_biUnit.Evidence_Total = _cont1[1].replace("NbExp=", "").trim();
										_biUnit.IntAct = (_cont1[2].replace("IntAct=", "")).replace(";", "");
										_biUnit.iaWebsite = (
												"http://www.ebi.ac.uk/intact/"+
												"pages/details/details.xhtml?binary="+
												_biUnit.IntAct
										);
										_biUnit.Output();
									}
									else
									if(_cont1[0].indexOf(":") != -1 && _cont1.length>=3)
									{
										String[] _firstItem = _cont1[0].split(":");
										_biUnit.Entry = _firstItem[0];
										_biUnit.With = _firstItem[1];
										_biUnit.Evidence_Detail = "";
										_biUnit.Evidence_Total = _cont1[1].replace("NbExp=", "");
										_biUnit.IntAct = (_cont1[2].replace("IntAct=", "")).replace(";", "").replace(", ", ",");
										_biUnit.iaWebsite = (
												"http://www.ebi.ac.uk/intact/"+
												"pages/details/details.xhtml?binary="+
												_biUnit.IntAct
										);
										_biUnit.Output();
									}
									rLine = brBI.readLine();
									if(rLine == null) break;
								}
								continue;
							}								
							rLine = brBI.readLine();
							if(rLine == null) break;
						}
					}	
					rLine = brBI.readLine();
					if(rLine == null) break;
				}	
				if(! FinishItem)
				{
					_biUnit.Output();
				}
				continue;
			}		
		rLine = brBI.readLine();
		}
		bClose();
		{
			System.out.print("count: " + GACount + "\n");
		}
	}
}
