package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.http.client.ClientProtocolException;

import Category.BuildCategoryPage;
import DataProcess.omimUnit;
import Global.Global;
import Http.http;
import Page.Disease2GenePage;
import Page.DiseasePage;
import Page.GenePage;
import Page.ProteinPage;
import PageData.d2gPageData;
import PageData.diseasePageData;
import PageData.proteinPageData;

public class Main
{
	public static void main(String[] args) throws ClientProtocolException, IOException
	{
		Global _g = new Global();
		
//		/*
		{
//			BuildCategoryPage.Execute();
			Global.bwBug.write("Finish CategoryPage\n");
			buildAllProtein();
			Global.bwBug.write("Finish ProteinPage\n");
//			buildAllGene();
			Global.bwBug.write("Finish GenePage\n");
//			buildAllDisease();
			Global.bwBug.write("Finish DiseasePage\n");
//			buildAllDisease2Gene();
			Global.bwBug.write("Finish ALL!!!!!!!!!!!!\n");
		}
//		*/
		Global.bwBug.close();
	}
	
	/*
	 * Gene
	 */
	
	public static void buildAllGene() throws IOException
	{
		BufferedReader brGN = new BufferedReader(new FileReader("clcGene"));
		String[][] _gn = new String[1][];
		String rLine;
		int _ii = 0;
		boolean btmp = false;
		long StartTime = System.currentTimeMillis(), CurTime;
		while((rLine = brGN.readLine()) != null)
		{
			{
				if(_ii % 128 == 0)
				{
					CurTime = System.currentTimeMillis();
					
					Global.bwBug.write("Gene: " + _ii + "\t" + 
							"TimeConsume: " + (CurTime - StartTime)/1000 + "\n");
					StartTime = CurTime;
				}
				_ii ++;
			}
			_gn[0] = rLine.split("\t");
			{
				if(_gn[0][2].equals("MCOR"))
				{
					btmp = true;
				}
				if(!btmp) continue;
				System.out.print("Gene: " + _gn[0][2] + "\n");
			}
			GenePage _gp = new GenePage(_gn[0][2], _gn);
			_gp.buildGenePage(2);
			http.postTableEditData(_gp);
		}
		brGN.close();
	}
	
	/*
	 * Disease
	 */
	public static HashMap<String, Integer> hmOI = new HashMap<String, Integer>();		
	public static omimUnit[] oiUnit = new omimUnit[2000];
	public static HashSet<String> hsOMIM = new HashSet<String>();
	
	public static void LoadOmimData() throws IOException
	{
		/*
		 * load omim id
		 */

		{
			BufferedReader brOmimID = new BufferedReader(new FileReader("OMIM_ID"));
			String[] _sp = brOmimID.readLine().split("\t");
			for(String iS: _sp)
			{
				hsOMIM.add(iS);
			}
			brOmimID.close();
		}

		BufferedReader brOmim = new BufferedReader(new FileReader("tmpOmim.txt"));

		{
			String rLine = "";
			String cont = "";
			int oiCount = -1;
			while(rLine != null)
			{
				if(rLine.startsWith("*FIELD* NO"))
				{
					rLine = brOmim.readLine();
					if(! hsOMIM.contains(rLine))	continue;
					
					oiCount ++;
					oiUnit[oiCount] = new omimUnit(); 
					hmOI.put(rLine, oiCount);
					oiUnit[oiCount].fNO = rLine;
					{
						if(oiCount % 1000 == 0) 
							System.out.print("oic: " + oiCount + "\n");
					}
					while(rLine != null)
					{
						if(rLine.startsWith("*RECORD*"))
							break;
						if(rLine.startsWith("*FIELD*"))
						{
							String _rline = rLine;
							{
								if(_rline.startsWith("*FIELD* TI")||
								   _rline.startsWith("*FIELD* CN")||
								   _rline.startsWith("*FIELD* CD")||
								   _rline.startsWith("*FIELD* ED")
								   )
								{
									rLine = "";
									continue;
								}
							}
							cont = "";
							rLine = "";
							while(true)
							{
								rLine = brOmim.readLine();
								if(rLine == null)
									break;
								if(rLine.startsWith("*FIELD*") || rLine.startsWith("*RECORD*"))
									break;
								if(rLine.replace(" ", "").replace("\n", "").equals(""))
								{
									rLine = "";
								}
								cont += rLine + "\n";
							}
							if(_rline.startsWith("*FIELD* TI"))
								oiUnit[oiCount].fTI = cont;
							if(_rline.startsWith("*FIELD* TX"))
								oiUnit[oiCount].fTX = cont;
							if(_rline.startsWith("*FIELD* AV"))
								oiUnit[oiCount].fAV = cont;
							if(_rline.startsWith("*FIELD* RF"))
								oiUnit[oiCount].fRF = cont;
							if(_rline.startsWith("*FIELD* CS"))
								oiUnit[oiCount].fCS = cont;
							if(_rline.startsWith("*FIELD* CN"))
								oiUnit[oiCount].fCN = cont;
							if(_rline.startsWith("*FIELD* CD"))
								oiUnit[oiCount].fCD = cont;
							if(_rline.startsWith("*FIELD* ED"))
								oiUnit[oiCount].fED = cont;
							continue;
						}					
						rLine = brOmim.readLine();
					}
				}
				rLine = brOmim.readLine();
			}
			brOmim.close();		
		}
	}
	public static String dgFile = "DG";
	public static HashMap<String, String> hmDG = new HashMap<String, String>();
	public static void LoadDGLink() throws IOException
	{
		BufferedReader br_d2g = new BufferedReader(new FileReader(dgFile));
		String rLine;
		String[] _sp;
		String[] dts, gns;
		hmDG.clear();
		while((rLine = br_d2g.readLine()) != null)
		{
			_sp = rLine.split("\t");
			dts = _sp[5].split("\\||; ");
			gns = _sp[8].split("\\||; ");
			for(int i = 0; i < dts.length; i ++)
			{
				String iDT = dts[i].toLowerCase();
				for(int j = 0; j < gns.length; j ++)
				{
					if(gns[j].replace(" ", "").equals("")) continue;
					String iGN = gns[j];
					iDT = iDT.replace("<", "(").replace("\"", "")
					 .replace(">", ")")
					 .replace("[", "(")
					 .replace("]", ")")
					 .replace("\\", "")
					 .replace("+", "PLUS_SIGNAL");
					if(iDT.replace(" ", "").equals("")) continue;
					String _get_s = hmDG.get(iDT);
					if(_get_s == null)
					{
						hmDG.put(iDT, iGN);
					}
					else
					{
						_get_s += "|" + iGN;
						hmDG.put(iDT, _get_s);
					}
				}
			}
		}
		
		BufferedWriter bwDGLink = new BufferedWriter(new FileWriter("DGLink"));
		{
			for(String iDT : hmDG.keySet())
			{
				bwDGLink.write(iDT + "\t" + hmDG.get(iDT) + "\n");
			}
		}
		bwDGLink.close();
	}
	public static void buildAllDisease() throws IOException
	{
		LoadDGLink();
		LoadOmimData();
		BufferedReader brHuman = new BufferedReader(new FileReader("HumanDO_in_DiseaseInfo"));
		String rLine;
		String[] _sp;
		String[] _data;
		ArrayList<omimUnit> _om_list;
		int[] _offset = new int[]{1, 3, 4, 5};
		int _ii = 0;
		boolean btmp = false;
		long StartTime = System.currentTimeMillis(), CurTime;
		{
			while((rLine = brHuman.readLine()) != null)
			{
				{
					if(_ii % 100 == 0)
					{
						CurTime = System.currentTimeMillis();
						
						Global.bwBug.write("Disease: " + _ii + "\t" + 
								"TimeConsume: " + (CurTime - StartTime)/1000 + "\n");
						Global.bwBug.flush();
						StartTime = CurTime;
					} 
					_ii ++;
				}
				_sp = rLine.split("\\|");
				{
					_data = new String[4];
					for(int i = 0; i < 4; i ++)
					{
						_data[i] = _sp[_offset[i]];
					}
					String[] _om = _sp[6].split("; ");
					_om_list = new ArrayList<omimUnit>();
					Integer _get_i;
					for(String iS: _om)
					{
						if(iS.replace(" ", "").equals("") || ((_get_i = hmOI.get(iS)) == null))	continue;
						_om_list.add(oiUnit[_get_i.intValue()]);
					}
				}
				{
					if( _data[0].equals("adenosquamous breast carcinoma"))
					{
						btmp = true;
					}
					if(!btmp) continue;
				}
				diseasePageData _dpd = new diseasePageData(_data, _om_list);
				DiseasePage _dp = new DiseasePage(_data[0].replace("[[", "").replace("]]", ""), _dpd);
				{
					Global.bwBug.write("Diseae: " + _data[0] + "\n");
					Global.bwBug.flush();
				}
				_dp.buildDiseasePage();
				http.postTableEditData(_dp);
			}
		}
		hmOI = null;
		hmDG = null;
		hsOMIM = null;
		{
			for(int i = 0; i < oiUnit.length; i ++)
				oiUnit[i] = null;
		}
		System.gc();
		brHuman.close();
	}

	
	/*
	 * Protein
	 */	
	public static void buildAllProtein() throws IOException
	{
		String[][][] ProteinData;
		BufferedReader[] brProFiles;
		String[] ProteinFiles;
		BufferedWriter bwBugProtein = new BufferedWriter(new FileWriter("BugProtein"));
		{
			/*
			 * Initial
			 */
			ProteinFiles = new String[]{
					"Name_Origin",
					"Protein_Attributes",
					"GeneralAnnotation",
					"GeneralOntology",
					"BinaryInteractions",
					"AlternativeProducts",
					"SequenceAnnotation",
					"Sequences",
					"EntryInformation",
					"CrossReferences",
					"References"
			};
			brProFiles = new BufferedReader[ProteinFiles.length];
			for(int i = 0; i < ProteinFiles.length; i ++)
			{
				brProFiles[i] = new BufferedReader(new FileReader("Protein\\" + ProteinFiles[i]));
			}		
		}
		
		{
			/*
			 * Main Part
			 */
			String[] rLine = new String[11];		
			{
				for(int i = 0; i < 11; i ++)
				{
					rLine[i] = brProFiles[i].readLine();
				}
			}
			String _cont;
			int _ii = 0;
			String _ID = "Haluo";
			String _tmpID;
			boolean btmp = false;
			long StartTime = System.currentTimeMillis(), CurTime;
			while(rLine[0] != null)
			{
				{
//					if(_ii > 103) break;
					if(_ii  % 10 == 0)
					{
						CurTime = System.currentTimeMillis();
						
						Global.bwBug.write("Protein: " + _ii + "\t" + 
								"TimeConsume: " + (CurTime - StartTime)/1000 + "\n");
						Global.bwBug.flush();
						StartTime = CurTime;
					} 
					_ii ++;
				}
				
				ProteinData = new String[11][][];
				for(int i = 0; i < 11; i ++)
				{
					if(rLine[i].equals("=========="))
					{
						_cont = "";
						{
							if(i == 0)
								_ID = brProFiles[i].readLine();
							else
							{
								_tmpID = brProFiles[i].readLine();
								if(!_ID.equals(_tmpID))
								{
									bwBugProtein.write("_ii : " + _ii + "\n" +
											"_ID: " + _ID + "\n" +
											"_tmpID: " + _tmpID + "\n" +
													"i: " + i + "\n");
								}
							}
						}
						while(true)
						{
							rLine[i] = brProFiles[i].readLine();
							if(rLine[i] == null || rLine[i].equals("=========="))
								break;
							_cont += rLine[i] + "\n";
						}
						String[] _sp_cont = _cont.split("\n-----\n");
						ProteinData[i] = new String[_sp_cont.length][];
						for(int j = 0; j < _sp_cont.length; j ++)
						{
							ProteinData[i][j] = _sp_cont[j].split("#@!");
						}
					}
				}
				{
					System.out.print("Protein: " + _ID + "\n");
					if(_ID.equals("ZN783_HUMAN"))
					{
						btmp = true;
					}
					if(_ID.equals("ZNF74_HUMAN"))
					{
						btmp = false;
						System.gc();
					}
					if(!btmp ) continue;
				}
				proteinPageData _ppd = new proteinPageData(ProteinData);
				ProteinPage _pp = new ProteinPage(_ID, _ppd);
				_pp.buildProteinPage();
				http.postTableEditData(_pp);
			}
		}
		
		{
			/*
			 * Close
			 */
			for(int i  = 0; i < ProteinFiles.length; i ++)
			{
				brProFiles[i].close();
			}
		}
		bwBugProtein.close();
	}

	/*
	 * Disease2Gene
	 */
	public static void buildAllDisease2Gene() throws IOException
	{
		BufferedReader brD2G = new BufferedReader(new FileReader("DG"));
		String rLine;
		String[] _sp;
		int _ii = 0;
		long StartTime = System.currentTimeMillis(), CurTime = 0;
		int _ct = 0;
		boolean btmp = false;
		while((rLine = brD2G.readLine()) != null)
		{
			
			_sp = rLine.split("\t");
			d2gPageData _dpd = new d2gPageData(_sp);
			if(_ii  % 256 == 0)
			{
				CurTime = System.currentTimeMillis();
				
				Global.bwBug.write("D2G: " + _ii + "\t" + 
						_dpd.formTitle() + "\n" + 
						"TimeConsume: " + (CurTime - StartTime)/1000 + "\n");
				Global.bwBug.flush();
				StartTime = CurTime;
			} 
			_ii ++;
			Disease2GenePage _dgp = new Disease2GenePage(_dpd.formTitle(), _dpd);
			{
				Global.bwBug.write("dpd: " + _dpd.formTitle() + "\n");
				Global.bwBug.flush();
			}
			{
				if(_dpd.formTitle().equals("Colorectal Neoplasms_TO_SLC22A4"))
				{
					btmp = true;
				}
				if(! btmp)
				{
					continue;
				}
				if(_dpd.formTitle().indexOf("\n") != -1)
				{
					continue;
				}
			}
			_dgp.buildDisease2GenePage();
			http.postTableEditData(_dgp);
		}
		brD2G.close();
		{
			System.out.print("ct: " + _ct + "\n");
		}
	}
}
