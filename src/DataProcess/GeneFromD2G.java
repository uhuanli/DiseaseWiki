package DataProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class GeneFromD2G 
{
	static final String d2gFile = "DG";
	public static void main(String[] args) throws IOException
	{
		LoadGeneD2G();
	}
	
	public static void LoadGeneD2G() throws IOException
	{
		BufferedWriter bw_d2gGene = new BufferedWriter(new FileWriter("gene_from\\gene_from_d2g"));
		BufferedReader br_d2g = new BufferedReader(new FileReader("DG"));
		HashSet<String> hsGeneD2G = new HashSet<String>();
		String rLine; 
		String[] _sp;
		while((rLine = br_d2g.readLine()) != null)
		{
			_sp = rLine.split("\t");
			if(_sp[8].replace(" ", "").equals(""))
				continue;
			_sp = _sp[8].split("; ");
			for(String iS : _sp)
			{
				hsGeneD2G.add(iS);
			}
		}
		
		{
			for(String iS: hsGeneD2G)
			{
				bw_d2gGene.write(iS + "\t");
			}
		}
		br_d2g.close();
		bw_d2gGene.close();
	}
}
