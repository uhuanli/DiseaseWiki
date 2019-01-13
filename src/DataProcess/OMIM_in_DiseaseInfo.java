package DataProcess;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class OMIM_in_DiseaseInfo {
	public static BufferedReader brHuman;
	public static BufferedWriter bwHuman;
	public static BufferedReader brOmim;
	public static BufferedWriter bwOmim;
	public static omimUnit[] oiUnit;
	
	public static void main(String[] args) throws IOException
	{		
		formOmim();	
	}
	
	public static void Output() throws IOException
	{
		String _row = oiUnit[0].formRow();
		bwOmim.write(_row);
	}
	
	public static void formOmim() throws IOException
	{
		oiUnit = new omimUnit[22335];
		bwOmim = new BufferedWriter(new FileWriter("OMIM_in_DiseaseInfo"));
		brOmim = new BufferedReader(new FileReader("omim.txt"));
		String rLine = "";
		String cont = "";
		String cont2 = "";
		int oiCount = -1;
		int tmpCount = 0;
		oiUnit[0] = new omimUnit();
		int maxTI = 0;
		int maxTX = 0;
		int maxAV = 0;
		int maxRF = 0;
		int maxCS = 0;
		int maxCN = 0;
		int maxCD = 0;
		int maxED = 0;
		Human_in_DiseaseInfo.diInitial();
		Human_in_DiseaseInfo.BuildTable();
		while(rLine != null)
		{
			if(rLine.startsWith("*FIELD* NO"))
			{
				oiCount = 0;

//				if(tmpCount >= 3) break;
				oiUnit[oiCount].Initial(); 
				rLine = brOmim.readLine();
				oiUnit[oiCount].fNO = rLine;
				if(! Human_in_DiseaseInfo.hsOMIM.contains(rLine))		continue;
				tmpCount ++;
				while(rLine != null)
				{
					if(rLine.startsWith("*RECORD*"))
						break;
					if(rLine.startsWith("*FIELD*"))
					{
						String _rline = rLine;
						cont = "";
						rLine = "";
						while(true)
						{
							rLine = brOmim.readLine();
							if(rLine == null)
								break;
							if(rLine.startsWith("*FIELD*") || rLine.startsWith("*RECORD*"))
								break;
							else
							if(!cont.equals(""))
								cont += "\n";
							cont += rLine;
						}
						if(_rline.startsWith("*FIELD* TI"))
						{
							oiUnit[oiCount].fTI = cont;
							if(maxTI < cont.length())
								maxTI = cont.length();
						}
						if(_rline.startsWith("*FIELD* TX"))
						{
							oiUnit[oiCount].fTX = cont;
							if(maxTX < cont.length())
								maxTX = cont.length();
						}
						if(_rline.startsWith("*FIELD* AV"))
						{
							oiUnit[oiCount].fAV = cont;
							if(maxAV < cont.length())
								maxAV = cont.length();
						}
						if(_rline.startsWith("*FIELD* RF"))
						{
							oiUnit[oiCount].fRF = cont;
							if(maxRF < cont.length())
								maxRF = cont.length();
						}
						if(_rline.startsWith("*FIELD* CS"))
						{
							oiUnit[oiCount].fCS = cont;
							if(maxCS < cont.length())
								maxCS = cont.length();
						}
						if(_rline.startsWith("*FIELD* CN"))
						{
							oiUnit[oiCount].fCN = cont;
							if(maxCN < cont.length())
								maxCN = cont.length();
						}
						if(_rline.startsWith("*FIELD* CD"))
						{
							oiUnit[oiCount].fCD = cont;
							if(maxCD < cont.length())
								maxCD = cont.length();
						}
						if(_rline.startsWith("*FIELD* ED"))
						{
							oiUnit[oiCount].fED = cont;
							if(maxED < cont.length())
								maxED = cont.length();
						}
						continue;
					}					
					rLine = brOmim.readLine();
				}
				Output();
			}
			rLine = brOmim.readLine();
			
		}
		Human_in_DiseaseInfo.diClose();
		System.out.print(
				maxTI  + "\n" + 
				maxTX  + "\n" +
				maxAV  + "\n" + 
				maxRF  + "\n" + 
				maxCS  + "\n" + 
				maxCN  + "\n" + 
				maxCD  + "\n" + 
				maxED  + "\n" +
				"count: " + tmpCount
				);
		/*
		 * 991
211761
255192
256098
4152
7322
40
7576
count: 22334
		 */
		brOmim.close();
		bwOmim.close();
	}
	
	public static void getHumanID() throws IOException
	{
		brHuman = new BufferedReader(new FileReader("HumanDO.obo"));
		bwHuman = new BufferedWriter(new FileWriter("HumanID.obo"));
		String rLine = "";
		int idCount = 0;
		while(rLine != null)
		{
			if(rLine.startsWith("id: DOID"))
			{
				String id = rLine.replaceAll("id: ", "");
				bwHuman.write(id);
				bwHuman.newLine();
				idCount ++;
			}
			rLine = brHuman.readLine();
		}
		System.out.print(idCount + "\n");
		brHuman.close();
		bwHuman.close();	
	}
	
}
