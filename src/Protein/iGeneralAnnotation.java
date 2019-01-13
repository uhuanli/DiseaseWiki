package Protein;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


class gaUnit
{
	public String ID;
	public String Function;
	public String SubunitStructure;
	public String SubcellularLocation;
	public String PTM;
	public String SeqSimilarity;
	
	public void Initial()
	{
		ID = "";
		Function = "";
		SubunitStructure = "";
		SubcellularLocation = "";
		PTM = "";
		SeqSimilarity = "";
	}	
	
	public void Output() throws IOException
	{
		iGeneralAnnotation.bwGA.write(formRow());
	}
	
	public String formRow()
	{
		String _ID = ID.split("             ")[0];
		if(Function.equals(""))		Function = " ";
		if(SubunitStructure.equals(""))		SubunitStructure = " ";
		if(SubcellularLocation.equals(""))		SubcellularLocation = " ";
		if(PTM.equals(""))		PTM = " ";
		if(SeqSimilarity.equals(""))		SeqSimilarity = " ";
		
		String _row = ("==========" + "\n" + 
				_ID + "\n" + 
				Function + "#@!" + 
				SubunitStructure + "#@!" + 
				SubcellularLocation + "#@!" + 
				PTM + "#@!" + 
				SeqSimilarity + "\n-----\n" 
		);
		return _row;
	}
}

public class iGeneralAnnotation {
	
	
	public static BufferedReader brGA;
	public static BufferedWriter bwGA;
	
	public static gaUnit		_gaUnit;

	
	public static void Initial() throws IOException
	{
		_gaUnit = new gaUnit();
		
		brGA = new BufferedReader(new FileReader("uniprot_sprot_human.dat"));
		bwGA = new BufferedWriter(new FileWriter("GeneralAnnotation"));
	}
	
	public static void bClose() throws IOException
	{
		brGA.close();
		bwGA.close();
	}


	
	public static void Output() throws IOException
	{
		bwGA.write(
				_gaUnit.formRow()
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
				_gaUnit.Initial();
				String unitID = rLine.replace("ID   ", "");
				_gaUnit.ID = unitID;
				
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
							if(rLine.startsWith("CC   -!- FUNCTION:"))
							{
								cont1 = rLine.replace("CC   -!- FUNCTION:", "").trim();
								rLine = "CC   ";
								while(!rLine.startsWith("CC   -!-") && rLine.startsWith("CC   "))
								{
									if(!rLine.equals("CC   "))
										cont1 += "\n";
									cont1 += (rLine.replace("CC   ", "")) .replace("    ", "").trim();
									rLine = brGA.readLine();
									if(rLine == null) break;
								}
								_gaUnit.Function = cont1;
								continue;
							}
							if(rLine.startsWith("CC   -!- SUBUNIT:"))
							{
								cont1 = rLine.replace("CC   -!- SUBUNIT:", "").trim();
								rLine = "CC   ";
								while(!rLine.startsWith("CC   -!-") && rLine.startsWith("CC   "))
								{
									if(!rLine.equals("CC   "))
										cont1 += "\n";
									cont1 += (rLine.replace("CC   ", "")) .replace("    ", "").trim();
									rLine = brGA.readLine();
									if(rLine == null) break;
								}
								_gaUnit.SubunitStructure = cont1;
								continue;
							}
							if(rLine.startsWith("CC   -!- SUBCELLULAR LOCATION:"))
							{
								cont1 = rLine.replace("CC   -!- SUBCELLULAR LOCATION:", "").trim();
								rLine = "CC   ";
								while(!rLine.startsWith("CC   -!-") && rLine.startsWith("CC   "))
								{
									if(!rLine.equals("CC   "))
										cont1 += "\n";
									cont1 += (rLine.replace("CC   ", "")) .replace("    ", "").trim();
									rLine = brGA.readLine();
									if(rLine == null) break;
								}
								_gaUnit.SubcellularLocation = cont1;
								continue;
							}
							
							if(rLine.startsWith("CC   -!- PTM:"))
							{
								cont1 = rLine.replace("CC   -!- PTM:", "").trim();
								rLine = "CC   ";
								while(!rLine.startsWith("CC   -!-") && rLine.startsWith("CC   "))
								{
									if(!rLine.equals("CC   "))
										cont1 += "\n";
									cont1 += (rLine.replace("CC   ", "")) .replace("    ", "").trim();
									rLine = brGA.readLine();
									if(rLine == null) break;
								}
								_gaUnit.PTM = cont1;
								continue;
							}
							
							if(rLine.startsWith("CC   -!- SIMILARITY:"))
							{
								cont1 = rLine.replace("CC   -!- SIMILARITY:", "").trim();
								rLine = "CC   ";
								while(!rLine.startsWith("CC   -!-") && rLine.startsWith("CC   "))
								{
									if(!rLine.equals("CC   "))
										cont1 += "\n";
									cont1 += (rLine.replace("CC   ", "")) .replace("    ", "").trim();
									rLine = brGA.readLine();
									if(rLine == null) break;
								}
								_gaUnit.SeqSimilarity = cont1;
								continue;
							}						
							rLine = brGA.readLine();
							if(rLine == null) break;
						}
						_gaUnit.Output();
					}// end if CC

					rLine = brGA.readLine();
					if(rLine == null) break;
				}// end while
				continue;
			}			
			rLine = brGA.readLine();
		}		
		bClose();
		System.out.print(GACount);
	}
}
