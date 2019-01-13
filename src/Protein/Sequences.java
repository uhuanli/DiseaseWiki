package Protein;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class sqUnit
{
	public String ID;
	public String Length;
	public String Mass_Da;
	public String Checksum;
	public String Sequence;
	
	public String formRow()
	{
		if(Length.equals(""))	Length = " ";
		if(Mass_Da.equals(""))	Mass_Da = " ";
		if(Checksum.equals(""))	Checksum = " ";
		if(Sequence.equals(""))	Sequence = " ";
		String _ID = ID.split("             ")[0];
		String _row = ("==========" + "\n" +
				_ID + "\n" +
				Length + "#@!" +
				Mass_Da + "#@!" +
				Checksum + "#@!" +
				Sequence + "\n-----\n"
		);
		return _row;
	}
	
	public void Initial()
	{
		Length = "";
		Mass_Da = "";
		Checksum = "";
		Sequence = "";
	}
	
	public void Output() throws IOException
	{
		Sequences.bwSQ.write(formRow());
	}
	
}

public class Sequences {
	
	public static BufferedWriter  bwSQ;
	public static BufferedReader  brSQ;
	public static sqUnit		_sqUnit;
	
	public static void Initial() throws IOException
	{
		_sqUnit = new sqUnit();
		
		brSQ = new BufferedReader(new FileReader("uniprot_sprot_human.dat"));
		bwSQ = new BufferedWriter(new FileWriter("Sequences"));
	}
	
	public static void bClose() throws IOException
	{
		brSQ.close();
		bwSQ.close();
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
				_sqUnit.Initial();
				String unitID = rLine.replace("ID   ", "");
				_sqUnit.ID = unitID;				
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
					if(rLine.startsWith("SQ   "))
					{
						String[] _sq = rLine.split("; ");
						if(_sq.length != 3)
						{
							System.out.print("bug\n");
						}
						_sqUnit.Initial();
						_sqUnit.Length = _sq[0].replace("SQ   SEQUENCE   ", "").trim();
						_sqUnit.Mass_Da = _sq[1].trim();
						_sqUnit.Checksum = _sq[2].replace(";", "").trim();
						rLine = brSQ.readLine();
						if(rLine == null)
						{
							_sqUnit.Output();
							break;
						}
						int _ii = 0;
						while(rLine.startsWith("     "))
						{
							if(_ii ++ != 0)
								_sqUnit.Sequence += "\n";
							_sqUnit.Sequence += rLine.replace("     ", "").trim();
							rLine = brSQ.readLine();
							if(rLine == null) break;
						}
						_sqUnit.Output();
					}
					rLine = brSQ.readLine();
					if(rLine == null) break;
				}
				continue;
			}
			rLine = brSQ.readLine();
		}
		bClose();
		{
			System.out.print("count: " + GACount + "\n");
		}
	}
}
