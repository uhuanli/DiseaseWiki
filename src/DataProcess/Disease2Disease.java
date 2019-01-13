package DataProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Disease2Disease {
	
	public static BufferedReader brHuman;
	public static BufferedWriter bwD2D;
	public static ddUnit	d2d;
	
	public static void main(String[] args) throws IOException
	{
		get_d2d_data();
	}
	
	public static void Output() throws IOException
	{
		String _row = d2d.formRow();
		bwD2D.write(_row);
		bwD2D.newLine();
	}
	
	public static void get_d2d_data() throws IOException
	{
		int ddCount = 0;
		brHuman = new BufferedReader(new FileReader("HumanDO.obo"));
		bwD2D = new BufferedWriter(new FileWriter("Disease2Disease"));
		String rLine = "";
		
		while(rLine != null)
		{
			if(rLine.startsWith("id: DOID:"))
			{
				d2d = new ddUnit();
				
				d2d.dID1 = rLine.replace("id: ", "");
				rLine = "";
				while(!rLine.startsWith("[Term]"))
				{
					if(rLine.startsWith("name: "))
					{
						d2d.term1 = rLine.replace("name: ", "");
					}
					else
					if(rLine.startsWith("is_a: "))
					{
						String[] d2 = rLine.split(" ! ", 2);
						d2d.dID2 = d2[0].replace("is_a: ", "");
						d2d.term2 = d2[1];
						ddCount ++;
						Output();
					}
					rLine = brHuman.readLine();
					if(rLine == null)	break;
				}
			}
			rLine = brHuman.readLine();
		}	
		
		System.out.print(ddCount + "");
		bwD2D.close();
		brHuman.close();
	}
}


class ddUnit
{
	public String dID1;
	public String dID2;
	public String term1;
	public String term2;
	public String relation;
	public void Initial()
	{
		dID1 = "";
		term1 = "";
		dID2 = "";
		term2 = "";
		relation = "";
	}

	public String formRow()
	{
		String _row = (
				dID1 + "|" +
				term1 + "|" +
				dID2 + "|" +
				term2 + "|" +
				"Is a"
						);
		return _row;
	}
	
	
}
