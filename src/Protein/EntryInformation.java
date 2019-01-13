package Protein;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


class eiUnit
{
	public String ID;
	public String EntryName;
	public String Accession;
	public String EntryHistory;
	public String EntryStatus;
	
	
	public void Output() throws IOException
	{
		EntryInformation.FinishItem = true;
		EntryInformation.bwEI.write(formRow());
	}
	
	public void Initial()
	{
		ID = "";
		EntryName = "";
		Accession = "";
		EntryHistory = "";
		EntryStatus = "";
	}
	
	public String formRow()
	{
		if(EntryName.equals(""))	EntryName = " ";
		if(Accession.equals(""))	Accession = " ";
		if(EntryHistory.equals(""))	EntryHistory = " ";
		if(EntryStatus.equals(""))	EntryStatus = " ";
		String _ID = ID.split("             ")[0];
		String _row = ("==========" + "\n" + 
		_ID + "\n" +
		EntryName + "#@!" +
		Accession + "#@!" +
		EntryHistory + "#@!" +
		EntryStatus + "#@!" +
		"Chordata Protein Annotation Program" + "#@!" +
		"Any medical or genetic information present \n" +
			"in this entry is provided for research, \n" +
			"educational and informational purposes only. \n" +
			"It is not in any way intended to be used as \n" +
			"a substitute for professional medical advice, \n" +
			"diagnosis, treatment or care." + "\n-----\n"
		);
		return _row;
	}
	
}

public class EntryInformation {
	
	public static BufferedWriter  bwEI;	
	public static BufferedReader  brEI;
	public static eiUnit		_eiUnit;
	
	public static boolean FinishItem = true;
	
	public static void Initial() throws IOException
	{
		FinishItem = false;
		_eiUnit = new eiUnit();
		brEI = new BufferedReader(new FileReader("uniprot_sprot_human.dat"));
		bwEI = new BufferedWriter(new FileWriter("EntryInformation"));
	}
	
	public static void bClose() throws IOException
	{
		brEI.close();
		bwEI.close();
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
				_eiUnit.Initial();
				String unitID = rLine.replace("ID   ", "");
				_eiUnit.ID = unitID;
				
				{
					String[] _sp_id = unitID.split(";");
					_sp_id = _sp_id[0].split("             ");
					_eiUnit.EntryName = _sp_id[0].replace("ID   ", "");
					_eiUnit.EntryStatus = _sp_id[1];
				}
				if(GACount % 2000 == 0)
					System.out.print(
							GACount + " => \t"
							);
					

				rLine = "";
				while(! rLine.startsWith("ID   "))
				{
					String cont1 = "";
					if(rLine.startsWith("AC   "))
					{
						String cont_ac = rLine.replace("AC   ", "").trim();
						while((rLine = brEI.readLine()).startsWith("AC   "))
						{
							cont_ac += "\n" + (rLine.replace("AC   ", "")).trim();
						}
						_eiUnit.Accession = cont_ac;
						continue;
					}
					else
					if(rLine.startsWith("DT   "))
					{
						String cont_dt = rLine.replace("DT   ", "").trim();
						while((rLine = brEI.readLine()).startsWith("DT   "))
						{
							cont_dt += "\n" + (rLine.replace("DT   ", "")).trim();
						}
						_eiUnit.EntryHistory = cont_dt;
						_eiUnit.Output();
						continue;
					}
					rLine = brEI.readLine();
					if(rLine == null) break;
				}
				continue;
			}
			rLine = brEI.readLine();
		}
		bClose();
		{
			System.out.print("count: " + GACount + "\n");
		}
	}
}
