package DataProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class collectDisease {
	static final String d2gFile = "PreDG";
	static HashSet<String> hsDFromD2G = new HashSet<String>();
	
	public static void main(String[] args) throws IOException
	{
		LoadDiseaseFromD2G();
	}
	
	public static void LoadDiseaseFromD2G() throws IOException
	{
		BufferedReader brD2G = new BufferedReader(new FileReader(d2gFile));
		String rLine;
		String[] _sp;
		while((rLine = brD2G.readLine()) != null)
		{
			_sp = rLine.split("\t");
			if(_sp[5].indexOf("; ") != -1 || _sp[5].indexOf("|") != -1)
			{
				System.out.print("sp5: " + _sp[5] + " len: " + _sp.length + "\n");
			}
			hsDFromD2G.add(_sp[5]);
		}
		BufferedWriter bw_DFromD2G = new BufferedWriter(new FileWriter("disease_from\\DiseaseFromD2G"));
		for(String iS: hsDFromD2G)
		{
			if(iS.replace(" ", "").replace("\"", "").equals(""))
				continue;
			bw_DFromD2G.write(iS + "\n");
		}
		brD2G.close();
		bw_DFromD2G.close();
	}
}
