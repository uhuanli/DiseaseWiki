package Protein;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class saUnit
{
	public String ID;
	public String FeatureKey;
	public String Position;
	public String Start;
	public String End;
	public String Description;
	public String FeatureIdentifier;
	
	public String formRow()
	{
		if(FeatureKey.equals(""))			FeatureKey = " ";
		if(Position.equals(""))				Position = " ";
		if(Start.equals(""))				Start = " ";
		if(End.equals(""))					End = " ";
		if(Description.equals(""))			Description = " ";
		if(FeatureIdentifier.equals(""))	FeatureIdentifier = " ";
		
		String _row = (
				FeatureKey + "#@!" +
				Position + "#@!" +
				Start + "#@!" +
				End + "#@!" +
				Description + "#@!" +
				FeatureIdentifier + "\n-----\n"
		);
		
		return _row;
	}
	
	public void InitialPart()
	{
		FeatureKey = "";
		Position = "";
		Start = "";
		End = "";
		Description = "";
		FeatureIdentifier = "";
	}
	
	public void Initial()
	{
		ID = "";
		FeatureKey = "";
		Position = "";
		Start = "";
		End = "";
		Description = "";
		FeatureIdentifier = "";
	}
	
	public void Output() throws IOException
	{
		SequenceAnnotation.bwSA.write(formRow());
	}
	
}

public class SequenceAnnotation {

	public static BufferedReader brSA;
	public static BufferedWriter bwSA;
	public static saUnit		_saUnit;
	
	public static void Initial() throws IOException
	{
		_saUnit = new saUnit();
		
		brSA = new BufferedReader(new FileReader("uniprot_sprot_human.dat"));
		bwSA = new BufferedWriter(new FileWriter("SequenceAnnotation"));
	}
	
	public static void bClose() throws IOException
	{
		brSA.close();
		bwSA.close();
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
				_saUnit.ID = unitID;
				
				{
					String[] _sp_id = unitID.split(";");
					_sp_id = _sp_id[0].split("             ");
				}
				if(GACount % 2000 == 0)
					System.out.print(
							GACount + " => \t"
							);
					
				rLine = "";
				bwSA.write("==========\n" + 
						unitID.split("             ")[0] + "\n");
				while(! rLine.startsWith("ID   "))
				{
					String cont1 = "";
					if(rLine.startsWith("FT   "))
					{
						if(rLine.startsWith("FT         "))
						{
							System.out.print("\nbug\n" + _id+"\n"+rLine);
						}
						_saUnit.InitialPart();
						String[] _sFT = new String[5];
						int i_sft = 0;
						String[] spFT;
						spFT = rLine.split(" ");
						int s_count = 0;
						for(int i = 0; i < spFT.length; i ++)
						{
							if(spFT[i].equals(""))	continue;
							if(i_sft <= 3)
								_sFT[i_sft] = spFT[i];
							s_count ++;
							i_sft ++;
						}
						
						_saUnit.FeatureKey = _sFT[1];
						_saUnit.Start = _sFT[2];
						_saUnit.End = _sFT[3];
						_saUnit.Position = _sFT[2] + "-" + _sFT[3];
						if(s_count >4)
						{
							spFT = rLine.split("       ");
							_sFT[4] = spFT[spFT.length - 1];
						}
						else
						{
							_sFT[4]= "";
						}
						
						_saUnit.Description = _sFT[4];
						rLine = brSA.readLine();
						while(rLine.startsWith("FT                                ")
							&& !rLine.startsWith("FT                                /FTId="))
						{
							_saUnit.Description += rLine.replace("FT                                ", "").trim();
							rLine = brSA.readLine();
						}
						if(rLine.startsWith("FT                                /FTId"))
						{
							_saUnit.FeatureIdentifier = rLine.replace("FT                                /FTId=", "");
							rLine = brSA.readLine();
						}
						_saUnit.Output();
						continue;
					}
					rLine = brSA.readLine();
					if(rLine == null) break;
				}
				continue;
			}
		rLine = brSA.readLine();
		}
		bClose();
		{
			System.out.print("count: " + GACount + "\n");
		}
	}
}
