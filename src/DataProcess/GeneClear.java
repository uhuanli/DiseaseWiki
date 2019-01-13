package DataProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;



public class GeneClear {
	final static String _path = "E:\\papers\\Tolei\\gene_info";
	static HashMap<String, String> hmGD = new HashMap<String, String>();
	static HashSet<String> hsGene = new HashSet<String>();
	public static void main(String[] args) throws IOException
	{
		LoadD2G();
		clearGeneInfo();
	}
	
	public static void clearGeneInfo() throws IOException
	{
		BufferedReader brGeneInfo = new BufferedReader(new FileReader(_path));
		BufferedWriter _bw = new BufferedWriter(new FileWriter("clcGene"));
		String rLine;
		String[] _sp;
		int _ii = 0;
		brGeneInfo.readLine();
		int _countDup = 0;
		while((rLine = brGeneInfo.readLine()) != null)
		{
			_sp = rLine.split("\t");
			if(hmGD.get(_sp[2]) == null) continue;
			if(hsGene.contains(_sp[2]))
			{
				_countDup ++;
				continue;
			}
			hsGene.add(_sp[2]);
			String _add = "";
			{
				String _get_s = hmGD.get(_sp[2]);
				String[] tmp_sp = _get_s.split("; ");
				_add = "\t" + tmp_sp[0];
				for(int tmpi = 1; tmpi < tmp_sp.length; tmpi ++)
				{
					_add += "#@!" + tmp_sp[tmpi];
				}
			}
			_bw.write(rLine + _add + "\n");
			_ii ++;
			if(_ii % 10000 == 0) 
				System.out.print(_ii + "=>" + "\t");
			if(_ii %99999 == 0) System.out.print("\n");
		}
		{
			System.out.print("_ii : " + _ii + "\n" + "dup: " + _countDup + "\n");
		}
		brGeneInfo.close();
		_bw.close();
	}
	
	public static void LoadD2G() throws IOException
	{
		BufferedReader br_d2g = new BufferedReader(new FileReader("DG"));
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
}
