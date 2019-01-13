package Protein;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


class goUnit
{
	public String ID;
	public String GO_ID;
	public String Name;
	public String Source;
	public String Website;
	public String Notes;
	
	public void Output() throws IOException
	{
		GeneOntology.bwGO.write(formRow());
	}
	
	public String formRow()
	{
		if(GO_ID.equals(""))	GO_ID = " ";
		if(Name.equals(""))		Name = " ";
		if(Source.equals(""))	Source = " ";
		if(Website.equals(""))	Website = " ";
		
		String _row = (
				GO_ID + "#@!" + 
				Name + "#@!" + 
				Source + "#@!" + 
				Website + "\n-----\n"
		);
		return _row;
	}
	
	void Initial()
	{
		ID = "";
		GO_ID = "";
		Name = "";
		Source = "";
		Website = "";
	}
}

public class GeneOntology {
	
	public static BufferedReader brGO;
	public static BufferedWriter bwGO;
	public static goUnit		_goUnit;
	
	public static void Initial() throws IOException
	{
		_goUnit = new goUnit();
		
		brGO = new BufferedReader(new FileReader("uniprot_sprot_human.dat"));
		bwGO = new BufferedWriter(new FileWriter("GeneralOntology"));
	}
	
	public static void bClose() throws IOException
	{
		brGO.close();
		bwGO.close();
	}


	
	public static void Output() throws IOException
	{
		bwGO.write(
				_goUnit.formRow()
				);
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
				String unitID = rLine.replace("ID   ", "");
				_goUnit.ID = unitID;
				
				{
					String[] _sp_id = unitID.split(";");
					_sp_id = _sp_id[0].split("             ");
				}
				if(GACount % 2000 == 0)
					System.out.print(
							GACount + " => \t"
							);
					

				rLine = "";
				{
					GeneOntology.bwGO.write("==========\n");
					GeneOntology.bwGO.write(unitID.split("             ")[0] + "\n");
				}
				while(! rLine.startsWith("ID   "))
				{
					if(rLine.startsWith("DR   "))
					{
						if(rLine.startsWith("DR   GO"))
						{
							String[] _go = rLine.split("; ");
							_goUnit.GO_ID = _go[1];
							_goUnit.Name = _go[2];
							_goUnit.Source = _go[3];
							_goUnit.Website = (
									"http://www.ebi.ac.uk/"+
									"QuickGO/GTerm?id="+
									_go[1]
							);
							_goUnit.Output();
						}
					}
					rLine = brGO.readLine();
					if(rLine == null) break;
				}
				{
					GeneOntology.bwGO.write("\n");
				}
				continue;
			}
			
			rLine = brGO.readLine();
		}		
		bClose();
		System.out.print(GACount);
	}
}
