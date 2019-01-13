package Category;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import Global.Global;
import Http.http;

public class BuildCategoryPage {
	String treeCategoryFile;
	static final String HumanFile = "HumanDO_in_DiseaseInfo";
	static final String element = "Element\\";
	static final String catPath = "Category\\";
	static final String d2gFile = "DG";
	static HashSet<String> hsDFromD2G = new HashSet<String>();
	static HashSet<String> hsGeneD2G = new HashSet<String>();
	static HashSet<String> hsDFromHU = new HashSet<String>();
	static HashMap<String, String> hmD2D = new HashMap<String, String>();
	public static HashSet<String> hsCat = new HashSet<String>();
	
	public static void main(String[] args) throws IOException
	{
		Global _g = new Global();
		Execute();
	}
	
	public static void Execute() throws IOException
	{
//		buildCat();
		LoadD2D();
		buildtreeCategory();
		{
			hsDFromD2G = null;
			hsGeneD2G = null;
			hsDFromHU = null;
			hmD2D = null;
			hsCat = null;
		}
		System.gc();
	}
	
	
	public static void buildCat() throws IOException
	{
		/*
		 * 	hsDFromD2G, hsGeneD2G, hsDFromHU
		 */
		LoadDiseaseFromD2G();
		LoadGeneD2G();
		LoadDiseaseFromHu();
		HashSet<String> hsDisease = new HashSet<String>();
		/*
		 * together
		 */
		{
			hsDisease.addAll(hsDFromD2G);
			hsDisease.addAll(hsDFromHU);
			{
				System.out.print("hsDisease size: " + hsDisease.size() + "\n");
			}
		}
		/*
		 * for disease related category
		 */
		{
			BufferedWriter bwG2Cat = new BufferedWriter(new FileWriter(catPath + "gDCat"));
			BufferedWriter bwD2GCat = new BufferedWriter(new FileWriter(catPath + "d2gDCat"));
			BufferedWriter bw2DCat = new BufferedWriter(new FileWriter(catPath + "d2dCat"));
			BufferedWriter bwSpecial = new BufferedWriter(new FileWriter("bugDisease"));
			{
				for(String iD : hsDisease)
				{
					if(hasSpecial(iD))
						bwSpecial.write(iD + "\n");
					bwG2Cat.write(iD + "gDCat" + "\n");
					bwD2GCat.write(iD + "d2gCat" + "\n");
					bw2DCat.write(iD + "d2dCat" + "\n");
				}
			}
			bwSpecial.close();
			bw2DCat.close();
			bwD2GCat.close();
			bwG2Cat.close();
		}
		/*
		 * gene related category
		 */
		{
			BufferedWriter bwP2Cat = new BufferedWriter(new FileWriter(catPath + "p2GCat"));
			BufferedWriter bwSpecial = new BufferedWriter(new FileWriter("bugGene"));
			{
				for(String iG : hsGeneD2G)
				{
					if(hasSpecial(iG))
						bwSpecial.write(iG + "\n");
					bwP2Cat.write(iG + "p2gCat\n");
				}
			}
			bwSpecial.close();
			bwP2Cat.close();
		}
		/*
		 * Special category
		 */
		{
			String[] _sc = new String[]{
				"DiseaseCat", 
				"GeneCat", 
				"ProteinCat",
				"Diseae2GeneCat"
			};
			BufferedWriter bwSpecialCat = new BufferedWriter(new FileWriter(catPath + "SpecialCat"));
			{
				for(String iSC : _sc)
				{
					bwSpecialCat.write(iSC + "\n");
				}
			}
			bwSpecialCat.close();
		}
		
	}
	
	public static void buildtreeCategory() throws IOException
	{
		File _f = new File(catPath);
		String[] _fNames = _f.list();
		for(String iF : _fNames)
		{
			{
				if(!iF.equals("d2dCat")) continue;
			}
			BufferedReader brCg = new BufferedReader(new FileReader(catPath + iF));
			String rLine;
			int _ii = 0;
			long StartTime = System.currentTimeMillis(), CurTime;
			boolean btmp = true;
			while((rLine = brCg.readLine()) != null)
			{	
				{
					if(_ii  % 256 == 0)
					{
						CurTime = System.currentTimeMillis();
						
						System.out.print("Category: " + _ii + "\t" + 
								"TimeConsume: " + (CurTime - StartTime)/1000 + "\n");
						StartTime = CurTime;
					}
					_ii ++;
				}
				rLine = rLine.replace(" ", "_")
					 .replace("<", "(")
					 .replace(">", ")")
					 .replace("[", "(")
					 .replace("]", ")")
					 .replace("\"", "")
					 .replace("\\", "");
				/*
				 * rewrite this when in en version
				 */
				String _cont = "";
				if(btmp)
				{

					String _get_s = hmD2D.get(rLine.replace("d2dCat", "").toLowerCase());
					if(_get_s != null)
					{
						String[] _sp = _get_s.split("\t");
						for(String iS : _sp)
						{
							_cont += "[[category:" + iS +  "d2dCat]]\n";
						}
						Global.bwBug.write("=====rline: " + rLine + "\n" + _cont);
					}
					else
					{
						continue;
					}
				}
				/*
				 * rewrite when use en version
				 */
				new CategoryPage("category:" + rLine, "Category of " + rLine + "\n" +
						_cont).buildCategoryPage();
	
			}
			long EndTime = System.currentTimeMillis();
			{
				System.out.print("time consume: " + (EndTime - StartTime) + "\n");
			}
			brCg.close();
		}
	}
	
	public static void LoadD2D() throws IOException
	{
		hmD2D.clear();
		BufferedReader brD2D = new BufferedReader(new FileReader("Disease2Disease"));
		String rLine;
		String[] _sp;
		String _get_s;
		String sp_lw;
		while((rLine = brD2D.readLine()) != null)
		{
			_sp = rLine.split("\\|");
			sp_lw = _sp[1].toLowerCase().replace(" ", "_")
			 .replace("<", "(")
			 .replace(">", ")")
			 .replace("[", "(")
			 .replace("]", ")")
			 .replace("\"", "")
			 .replace("\\", "");
			{
				if(sp_lw.equals("disease of metabolism"))
				{
					System.out.print("dom\n");
				}
			}
			if((_get_s = hmD2D.get(sp_lw)) != null)
			{
				_get_s += "\t" + _sp[3];
				hmD2D.put(sp_lw, _get_s);
			}
			else
			{
				hmD2D.put(sp_lw, _sp[3]);
			}
		}
		brD2D.close();
 	}
		
	public static void buildGeneCategory() throws IOException
	{
		final String _path = "gene_from\\";
		File _file = new File(_path);
		String[] geneFiles = _file.list();
		HashSet<String> hsGene = new HashSet<String>();
		{
			BufferedReader _br;
			for(String iFile: geneFiles)
			{
				_br = new BufferedReader(new FileReader(_path + iFile));
				String[] _sp = _br.readLine().split("\t");
				for(String iS: _sp)
				{
					hsGene.add(iS);
				}
			}
		}
		{
			System.out.print("size of gene: " + hsGene.size() + "\n");
		}
		BufferedWriter _bw = new BufferedWriter(new FileWriter("treeCategoryFile.txt"));
		for(String iS: hsGene)
		{
			_bw.write(iS + "\n");
		}
		_bw.close();
	}
	
	public static void LoadDiseaseFromHu() throws IOException
	{
		String rLine;
		String[] _sp;
		BufferedReader brHu = new BufferedReader(new FileReader(HumanFile));
		while((rLine = brHu.readLine()) != null)
		{
			_sp = rLine.split("\\|");
			if(_sp[1].replace(" ", "").equals(""))	continue;
			hsDFromHU.add(_sp[1]);
		}
		{
			BufferedWriter bwDFromHU = new BufferedWriter(new FileWriter(element + "DFromHU"));
			for(String iS: hsDFromHU)
			{
				if(iS.replace(" ", "").replace("\"", "").equals(""))
				bwDFromHU.write(iS + "\n");
			}
			bwDFromHU.close();
		}
		{
			{
				System.out.print("hsDFromHU size: " + hsDFromHU.size() + "\n");
			}
		}
	}
	
	public static void LoadGeneD2G() throws IOException
	{
		BufferedWriter bw_d2gGene = new BufferedWriter(new FileWriter(element + "GeneFromD2G"));
		BufferedReader br_d2g = new BufferedReader(new FileReader(d2gFile));
		String rLine; 
		String[] _sp;
		hsGeneD2G.clear();
		while((rLine = br_d2g.readLine()) != null)
		{
			_sp = rLine.split("\t");
			if(_sp[8].replace(" ", "").equals(""))
				continue;
			_sp = _sp[8].split("; ");
			for(String iS : _sp)
			{
				if(iS.replace(" ", "").replace("\"", "").equals("")) continue;
				hsGeneD2G.add(iS);
			}
		}
		
		{
			for(String iS: hsGeneD2G)
			{
				if(iS.replace(" ", "").replace("\"", "").equals(""))
				bw_d2gGene.write(iS + "\n");
			}
		}
		{
			System.out.print("hsGeneD2G size: " + hsGeneD2G.size() + "\n");
		}
		br_d2g.close();
		bw_d2gGene.close();
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
			if(_sp[5].replace(" ", "").replace("\"", "").equals("")) continue;
			hsDFromD2G.add(_sp[5]);
		}
		BufferedWriter bw_DFromD2G = new BufferedWriter(new FileWriter(element + "DiseaseFromD2G"));
		for(String iS: hsDFromD2G)
		{
			if(iS.replace(" ", "").replace("\"", "").equals(""))
				continue;
			bw_DFromD2G.write(iS + "\n");
		}
		{
			System.out.print("hsDFromD2G size: " + hsDFromD2G.size() + "\n");
		}
		bw_DFromD2G.close();
		brD2G.close();
	}
	
	public static boolean hasSpecial(String _s)
	{
		char [] _cr = _s.toCharArray();
		
		for(char _c : _cr)
		{
			if(isSpecial(_c)) return true;
		}
		
		return false;
	}
	
	public static boolean isSpecial(char _c)
	{
		if(((_c <= 'Z' && _c >= 'A') || (_c <= 'z' && _c >= 'a') || (_c >= '0' && _c <= '9')))
			return false;
		char[] cr = new char[]{
				'-', ',', ' ', '/', '.', '(', ')', '\''
				};
		
		boolean _ret = true;
		for(char iC: cr)
		{
			if(_c == iC)
			{
				_ret = false;
				break;
			}
		}
		return _ret;
	}
}
