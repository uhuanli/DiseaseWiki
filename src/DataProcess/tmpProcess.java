package DataProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class tmpProcess 
{
	final static String _path = "E:\\papers\\Tolei\\gene_info";
	static HashMap<String, String> hmGD = new HashMap<String, String>();
	static HashSet<String> hsGene = new HashSet<String>();
	static HashSet<String> hsTCF = new HashSet<String>();
	public static void main(String[] args) throws IOException
	{
		LoadD2G();
		{
			LoadTCF();
		}
		clearGeneInfo();
	}
	
	public static void clearGeneInfo() throws IOException
	{
		BufferedReader brGeneInfo = new BufferedReader(new FileReader(_path));
		BufferedWriter _bw = new BufferedWriter(new FileWriter("tmpclcGene"));
		String rLine;
		String dLine;
		String[] _sp;
		int _ii = 0;
		brGeneInfo.readLine();
		while((rLine = brGeneInfo.readLine()) != null)
		{
			_sp = rLine.split("\t");
			if((dLine = hmGD.get(_sp[2])) == null || hsGene.contains(_sp[2]) || !hsTCF.contains(_sp[2])) continue;
			hsGene.add(_sp[2]);
			{
				String _d = "";
				String[] sp_d = dLine.split("\\||; ");
				_d = sp_d[0];
				for(String _s : sp_d)
				{
					_d += "#@!" + _s;
				}
				rLine += "\t" + _d;
			}
			_bw.write(rLine + "\n");
			_ii ++;
			if(_ii % 10000 == 0) 
				System.out.print(_ii + "=>" + "\t");
			if(_ii %99999 == 0) System.out.print("\n");
		}
		{
			System.out.print("_ii : " + _ii + "\n");
		}
		brGeneInfo.close();
		_bw.close();
	}
	
	public static void LoadD2G() throws IOException
	{
		BufferedReader br_d2g = new BufferedReader(new FileReader("PreDG"));
		String rLine; 
		String[] _sp;
		while((rLine = br_d2g.readLine()) != null)
		{
			_sp = rLine.split("\t");
			if(_sp[8].replace(" ", "").equals(""))
				continue;
			String[] d_sp = _sp[5].split("\\||; ");
			_sp = _sp[8].split("; ");
			String _get_s;
			for(String iS : _sp)
			{
				for(int i = 0; i < d_sp.length; i ++)
				{
					d_sp[i] = d_sp[i].replace("\"", "");
					if(d_sp[i].replace(" ", "").equals("")) continue;
					if((_get_s = hmGD.get(iS)) != null)
					{
						_get_s += "; " + d_sp[i];
						hmGD.put(iS, _get_s);
					}
					else
					{
						hmGD.put(iS, d_sp[i]);
					}
				}
			}
		}
		{
			System.out.print("size of gMd : " + hmGD.size() + "\n");
		}
	}
	
	public static void LoadTCF() throws IOException
	{
		BufferedReader br_tcf = new BufferedReader(new FileReader("tmpTCF.txt"));
		String rLine;
		while((rLine = br_tcf.readLine()) != null)
		{
			hsTCF.add(rLine);
		}
		br_tcf.close();
	}
}
