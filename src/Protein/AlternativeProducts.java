package Protein;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


class apUnit
{
	public String ID;
	public String Event; 
	public String NamedIsoforms;
	public String Name;
	public String Isold;
	public String Sequence;
	public String Note;
	
	public void Output() throws IOException
	{
		AlternativeProducts.FinishItem = true;
		AlternativeProducts.bwAP.write(formRow());
	}
	
	public void InitialPart()
	{
		Name = "";
		Isold = "";
		Sequence = "";
		Note = "";
	}
	
	public void Initial()
	{
		ID = "";
		Event = ""; 
		NamedIsoforms = "";
		Name = "";
		Isold = "";
		Sequence = "";
		Note = "";
		AlternativeProducts.FinishItem = false;
	}
	
	public String formRow()
	{
		if(Event.equals(""))			Event = " ";
		if(NamedIsoforms.equals(""))	NamedIsoforms = " ";
		if(Name.equals(""))				Name = " ";
		if(Isold.equals(""))			Isold = " ";
		if(Sequence.equals(""))			Sequence = " ";
		if(Note.equals(""))				Note = " ";
		
		String _row = (
				Event + "#@!" + 
				NamedIsoforms + "#@!" +
				Name + "#@!" +
				Isold + "#@!" +
				Sequence + "#@!" +
				Note + "\n-----\n"
		);
		return _row;
	}
}

public class AlternativeProducts {

	
	public static BufferedReader brAP;
	public static BufferedWriter bwAP;
	public static apUnit		_apUnit;
	
	public static boolean FinishItem; 
	
	public static void Initial() throws IOException
	{
		_apUnit = new apUnit();
		FinishItem = true;
		brAP = new BufferedReader(new FileReader("uniprot_sprot_human.dat"));
		bwAP = new BufferedWriter(new FileWriter("AlternativeProducts"));
	}
	
	public static void bClose() throws IOException
	{
		bwAP.close();
		brAP.close();
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
				_apUnit.Initial();
				_apUnit.ID = unitID;	
				bwAP.write("==========\n" + unitID.split("             ")[0] + "\n");
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
							if(rLine.startsWith("CC   -!- ALTERNATIVE PRODUCTS"))
							{
								cont1 = rLine.replace("CC   -!- ALTERNATIVE PRODUCTS:", "").trim();
								rLine = brAP.readLine();
								if(rLine.startsWith("CC       Event="))
								{
									String _rline = rLine.replace("CC       Event=", "");
									String[] _event = _rline.split("; Named isoforms=");
									
									_apUnit.Event = _event[0].trim();
									_apUnit.NamedIsoforms = _event[1].replace(";", "").trim();
									rLine = brAP.readLine();
									while(!rLine.startsWith("CC   -!-") && rLine.startsWith("CC       Name="))
									{
										_apUnit.InitialPart();
										cont1 = rLine.replace("CC       Name=", "").trim();
										String[] ns = cont1.split(";"); 
										_apUnit.Name = ns[0];
										String _tmp_ = brAP.readLine();
										while(!_tmp_.startsWith("CC         IsoId="))
										{
											_tmp_ = brAP.readLine();
										}
										String[] is = (_tmp_).split(";");										
										_apUnit.Isold = is[0].replace("CC         IsoId=", "");
										if(is.length >=2)
										{
											is = is[1].split("=");
										}
										else
										{
											is = (brAP.readLine()).split("=");
										}
										_apUnit.Sequence = is[1].replace(";", "");
										rLine = brAP.readLine();
										if(rLine.startsWith("CC         Note="))
										{
											cont1 = rLine.replace("CC         Note=", "").trim();
											rLine = brAP.readLine();
											while(rLine.startsWith("CC         "))
											{
												cont1 += "\n" + (rLine.replace("CC         ", "")).replace(";", "").trim();
												rLine = brAP.readLine();
												if(rLine == null) break;
											}				
											_apUnit.Note = cont1;
										}
										_apUnit.Output();
										if(rLine == null) break;
									}
								}
								continue;
							}							
							rLine = brAP.readLine();
							if(rLine == null) break;
						}
					}
					rLine = brAP.readLine();
					if(rLine == null) break;
				}
				if(! FinishItem)
				{
					_apUnit.Output();
				}
				continue;
			}
			rLine = brAP.readLine();
		}
		bClose();
		{
			System.out.print("count: " + GACount + "\n");
		}
	}
}
