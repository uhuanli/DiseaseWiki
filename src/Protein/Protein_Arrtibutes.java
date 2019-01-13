package Protein;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


class paUnit
{
	public String ID;
	public String ProteinExistence;
	public String SeqLength;
	
	public String formRow()
	{
		String _ID = ID.split("             ")[0];
		if(SeqLength.equals(""))	SeqLength = " ";
		if(ProteinExistence.equals(""))	ProteinExistence = " ";
		
		String _row = ("==========" + "\n" +
				_ID + "\n" + 
				SeqLength + "#@!" + 
				" " + "#@!" + 
				" " + "#@!" + 
				ProteinExistence + "\n-----\n" 
		);
		return _row;
	}
}

public class Protein_Arrtibutes {
	
	
	public static BufferedReader brPA;
	public static BufferedWriter bwPA;
	public static paUnit		_paUnit;
	
	public static void Initial() throws IOException
	{
		_paUnit = new paUnit();
		brPA = new BufferedReader(new FileReader("uniprot_sprot_human.dat"));
		bwPA = new BufferedWriter(new FileWriter("Protein_Attributes"));
	}
	
	public static void bClose() throws IOException
	{
		brPA.close();
		bwPA.close();
	}
	
	public static void Output() throws IOException
	{
		bwPA.write(
				_paUnit.formRow()
				);
	}
	
	public static void main(String[] args) throws IOException
	{
		cal_mul();
	}
	
	public static void cal_mul() throws IOException
	{

		Initial();
		int PACount = 0;
		int i_pe = 0;
		int i_cc = 0;
		int i_dr = 0;
		int i_ft = 0;
		int i_sq = 0;
		int i_dt = 0;
		int i_rx = 0;
		int i_rc = 0;
		int i_ra = 0;
		int i_rt = 0;
		int i_rl = 0;
		int i_rp = 0;

		int l_pe = 0;
		int l_cc = 0;
		int l_dr = 0;
		int l_ft = 0;
		int l_sq = 0;
		int l_dt = 0;
		int l_rx = 0;
		int l_rc = 0;
		int l_ra = 0;
		int l_rt = 0;
		int l_rl = 0;
		int l_rp = 0;
		

		String rLine = "";
		String _id = "";
		while(rLine != null)
		{
			if(rLine.startsWith("ID   "))
			{
				_id = rLine;
				PACount ++;
				
				_paUnit.ID = rLine.replace("ID   ", "");
				
				String[] _rline = rLine.split(";");
				_paUnit.SeqLength = (_rline[1].replace(" ", "")) .replace(".", "");

				
				if(PACount % 2000 == 0)
					System.out.print(
							PACount + " => \t"
							);
					

				rLine = "";
				while(! rLine.startsWith("ID   "))
				{
					if(rLine.startsWith("PE   "))
					{
						_paUnit.ProteinExistence = rLine.replace("PE   ", "");
						Output();
						break;
					}
					else
					if(rLine.startsWith("CC   "))
					{

					}
					else
					if(rLine.startsWith("DR   "))
					{

					}
					if(rLine.startsWith("FT   "))
					{

					}
					else
					if(rLine.startsWith("SQ   "))
					{

					}
					else
					if(rLine.startsWith("DT   "))
					{

					}
					if(rLine.startsWith("RX   "))
					{

					}
					else
					if(rLine.startsWith("RC   "))
					{

					}
					else
					if(rLine.startsWith("RA   "))
					{

					}
					if(rLine.startsWith("RT   "))
					{

					}
					else
					if(rLine.startsWith("RL   "))
					{

					}
					else
					if(rLine.startsWith("RP   "))
					{

					}


					rLine = brPA.readLine();
					if(rLine == null) break;
				}

				
//				Output();
				
				continue;
			}
			
			rLine = brPA.readLine();
		}
		
		bClose();
		{
			System.out.print("count: " + PACount + "\n");
		}
	}
}
