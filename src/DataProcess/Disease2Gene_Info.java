package DataProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

class dgStore
{
	public String[] dg;
	dgStore(String[] _dg)
	{
		if(_dg.length != 46)
		{
			System.out.print("bug len dgStore\n");
		}
		dg = new String[46];
		{
			for(int i = 0; i < 46; i ++)
			if(_dg[i] != null)
				dg[i] = _dg[i];
			else
				dg[i] = " ";
		}//here
	}
	public String getString()
	{
		String _tmp = dg[0];
		for(int i = 1; i < 46; i ++)
		{
			if(dg[i] != null)
				_tmp += "\t" + dg[i];
			else
				_tmp += "\t ";
		}
		if(dg[1].indexOf(";") != -1) dg[1] = " ";
		return _tmp;
	}
	
	
}
class dgiUnit
{
	public String[]   _field;
	
	public static String _gene_statusC = "confirmed - observed in at least two laboratories or in several families.";
	public static String _gene_statusP = "provisional - based on evidence from one laboratory or one family.";
	public static String _gene_statusI = "inconsistent - results of different laboratories disagree.";
	public static String _gene_statusL = "limbo - evidence not as strong as that provisional, but included for heuristic reasons. (Same as `tentative'.)";
	public dgiUnit()
	{
		_field = new String[46];
	}
	public void setPre(String[] pre)
	{
		if(pre.length != 43)
		{
			System.out.print("\nhaluo-!39\n");
		}
		for(int i = 0; i < pre.length; i ++)
		{
			_field[i] = pre[i];
		}
	}
	
	public void setLat(String[] lat)
	{
		for(int i = 43; i < 46; i ++)
		{
			_field[i] = lat[i - 42];
		}
		_field[7] = lat[0]; 
		if(_field[32].equals(" ")) _field[32] = "";
		_field[32] += " " + lat[4];
	}
	
	public void set_mg_data
	(String _mdt, String _gs, String _new_id)
	{
		Initial();
		_field[5] = _mdt;
		_field[8] = _gs;
		_field[0] = _new_id;
	}
	
	public void set_gmp_n(String _mim_number, String _gene_symbol, 
		String _cytogenetic_local, String _gene_status,
		String _method, String _mouse_correlate,
		String _comments)
	{
		Initial();
		_field[30] = _mim_number;
		_field[8]  = _gene_symbol;
		_field[7] = _cytogenetic_local;
		_field[43] = _gene_status;
		_field[44] = _method;
		_field[45] = _mouse_correlate;
		_field[32] = _comments;
	}
	
	public String formRow()
	{
		
		if(_field[43].equals("C"))
		{
			_field[43] = _gene_statusC;
		}
		else
		if(_field[43].equals("P"))
		{
			_field[43] = _gene_statusP;
		}
		else
		if(_field[43].equals("I"))
		{
			_field[43] = _gene_statusI;
		}
		else
		if(_field[43].equals("L"))
		{
			_field[43] = _gene_statusL;
		}
		
		String _row = "";
		for(int i = 0; i < _field.length - 1; i ++)
		{
			_row += _field[i] + "\t";
		}
		_row +=  _field[_field.length - 1] + "\n";
		return _row;
	}
	
	public void Initial()
	{
		for(int i = 0; i < _field.length; i ++)
			_field[i] = "";
	}
}

public class Disease2Gene_Info {

	public static BufferedReader brAll;
	public static BufferedReader brDid;
	public static BufferedReader brGmp;
	
	public static BufferedWriter bwGmp_y;
	public static BufferedWriter bwGmp_n;
	public static BufferedWriter bwMdt_n;
	
	
	public static dgiUnit		 _dgiUnit;
	
	public static HashMap<String, Integer> 	MDT_Gene;
	public static HashSet<String> 		  	MDT_GS;
	public static String[]					mg;
	public static dgStore[]					_dgStore;
	public static int						_dgsLen;
	
	public static dgStore getDGS(int _i)
	{
		if(_dgStore[_i] == null)
		{
			System.out.print("bug null dgs\n");
			return null;
		}
		return _dgStore[_i];
	}
	
	public static void Initial() throws IOException
	{
		bwGmp_y = new BufferedWriter(new FileWriter("bwgmp_y"));
		bwGmp_n = new BufferedWriter(new FileWriter("bwgmp_n"));
		bwMdt_n = new BufferedWriter(new FileWriter("bwMdt_n"));
		_dgiUnit = new dgiUnit();
		_dgStore = new dgStore[1010000];
		_dgsLen = 0;
	}
	
	public static void bClose() throws IOException
	{
		bwGmp_y.close();
		bwGmp_n.close();
		bwMdt_n.close();
	}

	public static void main(String[] args) throws IOException
	{
		Initial();
		Load_All();
		Load_Did();
		Load_gene_map();
		bClose();
	}
	
	public static void Load_All() throws IOException
	{
		
		brAll = new BufferedReader(new FileReader("all.txt"));
		BufferedWriter bw_t_all = new BufferedWriter(new FileWriter("t_all.txt"));
		String rLine = null;
		rLine = brAll.readLine();
		rLine = brAll.readLine();
		rLine = brAll.readLine();
		String[] spLine = new String[46];
		
		MDT_Gene = new HashMap<String, Integer>();
		while((rLine = brAll.readLine()) != null)
		{
			String[] _tmp_sl = rLine.split("\t");
			for(int i = 0; i < 46; i ++)
			{
				if(i < _tmp_sl.length)
				{
					spLine[i] = _tmp_sl[i];
					if(spLine[i].equals("")) spLine[i] = " ";
				}
				else
				{
					spLine[i] = " ";
				}
			}
			{
				if(spLine[6].equals("C"))
				{
					spLine[6] = dgiUnit._gene_statusC;
				}
				if(spLine[6].equals("I"))
				{
					spLine[6] = dgiUnit._gene_statusI;
				}
				if(spLine[6].equals("P"))
				{
					spLine[6] = dgiUnit._gene_statusP;
				}
				if(spLine[6].equals("L"))
				{
					spLine[6] = dgiUnit._gene_statusL;
				}
			}
			if(spLine[5].equals(" ") && spLine[8].equals(" "))	continue;
			
			String[] DTs = spLine[5].split("\\||; ");
			String _key ;
			Integer _get_i;
			for(int i = 0; i < DTs.length; i ++)
			{
				spLine[5] = DTs[i];
				{
					if(spLine[5].indexOf("|") != -1 || spLine[5].indexOf("; ") != -1)
					{
						System.out.print("bug: " + spLine[5] + "\n");
					}
				}
				{
					_key = "**" + spLine[5] + "__" + spLine[8] + "**";
					_get_i = MDT_Gene.get(_key);

					if(_get_i == null)
					{
						MDT_Gene.put(_key, _dgsLen);
						_dgStore[_dgsLen] = new dgStore(spLine);	
						_dgsLen ++;
					}
					else
					{
						for(int j = 1; j < spLine.length; j ++)
						{
							if(spLine[j].equals(" ") || _dgStore[_get_i.intValue()].dg[j].indexOf(spLine[j]) != -1)	continue;
							if(_dgStore[_get_i.intValue()].dg[j].equals(" "))
							{
								_dgStore[_get_i.intValue()].dg[j] = spLine[j];
								if(_dgStore[_get_i.intValue()].dg[j] == null) _dgStore[_get_i.intValue()].dg[j] = " ";
							}
							else
							{
								_dgStore[_get_i.intValue()].dg[j] += "; " + spLine[j];
								if(_dgStore[_get_i.intValue()].dg[j] == null) _dgStore[_get_i.intValue()].dg[j] = " ";
							}
							if(j == 5 || j == 8)
							{
								System.out.print("bug" + _dgStore[_get_i.intValue()].dg[j] + "\n");
							}
						}
					}
				}
			}
			
		}
		System.out.print("mdt_gene: " + MDT_Gene.size() + "\n");
		
		brAll.close();
		bw_t_all.close();
		{
			BufferedWriter _pre_bw = new BufferedWriter(new FileWriter("PreDG"));
			for(int i = 0; i < _dgsLen; i ++)
			{
				_pre_bw.write(_dgStore[i].getString() + "\n");
			}
			_pre_bw.close();
		}
	}
	
	public static dgStore[] 	gm_dgStore;
	public static int			gm_dgsLen;
	public static HashMap<String, Integer> BP_Gene;
	
	public static String[] saveLine;
	public static int saveLen;
	
	public static void Load_gm_dgStore() throws IOException
	{
//		gm_dgStore = new dgStore[1000000];
		saveLine = new String[1000000];
		saveLen = 0;
		BP_Gene = new HashMap<String, Integer>();
		gm_dgsLen = 0;
		BufferedReader gm_br = new BufferedReader(new FileReader("DGS_Did"));
		int _i = 0;
		String rLine = "";
		String[] spLine;
		String[] BPs;
		while((rLine = gm_br.readLine()) != null)
		{
			if(rLine.replace(" ", "").equals(""))	continue;
			spLine = rLine.split("\t");
			{
				if(spLine.length != 46)
				{
					System.out.print("bug in len\n");
				}
			}
			BPs = (spLine[2].replace(" | ", "; ")
						   .replace(" |", "; ")
						   .replace("| ", "; ")
						   .replace("|", "; ")).split("; ");
			

			boolean _in = false;
			for(int i = 0; i < BPs.length; i ++)
			{
//				spLine[2] = BPs[i];
				if(BPs[i].replace(" ", "").equals(""))	continue;
				if(BP_Gene.get("**" + BPs[i] + "__" + spLine[8] + "**") == null)
				{
					BP_Gene.put("**" + BPs[i] + "__" + spLine[8] + "**", saveLen);
					_in = true;
				}
//				BP_Gene.put("**" + spLine[2] + "__" + spLine[8] + "**", gm_dgsLen);
//				gm_dgStore[gm_dgsLen] = new dgStore(spLine);
//				gm_dgsLen ++;
			}
			
			if(_in)
			{
				String formline = spLine[0];
				{
					for(int i = 1; i < 46; i ++)
					{
						formline += "\t" + spLine[i];
					}
				}
				saveLine[saveLen] = formline;
				saveLen ++;
			}
		}
		{
			System.out.print("saveLen: " + saveLen + "\n" + "bp_gene_size: " + BP_Gene.size() + "\n");
		}
	}
	
	public static void Load_gene_map() throws IOException
	{
		Load_gm_dgStore();
		brGmp = new BufferedReader(new FileReader("genemap.dat"));
		BufferedWriter bwDG = new BufferedWriter(new FileWriter("DG")); 
		BufferedWriter _bw = new BufferedWriter(new FileWriter("Disorder"));
		
		String rLine;
		String[] spLine = new String[18];
		int i_line = 0;
		
		int no_dt = 0, no_gn = 0, _cout_line = 0;
		
		while((rLine = brGmp.readLine()) != null)
		{
			if(rLine.equals("")) break;
			String[] _tmp_sl = rLine.split("\\|");
			for(int i = 0; i < 18; i ++)
			{
				if(i < _tmp_sl.length)
				{
					spLine[i] = _tmp_sl[i];
					if(spLine[i].equals(""))	spLine[i] = " ";
				}
				else
				{
					spLine[i] = " ";
				}
			}
			String[] Disorder = null;
			String[] GeneSymbol = getGeneSymbol(spLine[5]);
			if(GeneSymbol.length == 0)
			{
				System.out.print("zero!\n");
				return;
			}
			for(int i = 13; i < 16; i ++)
			{
				if((spLine[i].replace(" ", "")).equals(""))
				{
					spLine[i] = " ";
				}
			}
			String _disorders = spLine[13] + " " + spLine[14] + " " + spLine[15];
			Disorder = getDisorders(_disorders);
			if(Disorder == null)
			{
				Disorder = new String[]{" "};
			}
			{
				_bw.write("\n+++++\n" + spLine[0] + "\t\t\t" + _disorders + "\n");
				for(int i = 0; i < Disorder.length; i ++)
				{
					_bw.write(Disorder[i] + "\t\t");
				}
			}
			
			for(int i = 0; i < Disorder.length; i ++)
			for(int j = 0; j < GeneSymbol.length; j ++)
			{
				String _disorder = Disorder[i];
				String _gene_symbol = GeneSymbol[j];

				if(_gene_symbol.replace(" ", "").equals("") || _disorder.replace(" ", "").replace("\"", "").equals(""))	continue;
				
				String _key = "**" + _disorder + "__" + _gene_symbol + "**";
				Integer _get_i = MDT_Gene.get(_key);
				if(_get_i != null)
				{
					_cout_line ++;
					String[] _lat = new String[5];
					{
//						chi-band: 7  |  Gene status: 43  |  Method: 44  |   Mouse correlate: 45   |    Conclusion: 32
					}
					_lat[0] = spLine[4];//Cytogenetic location    	
					_lat[1] = spLine[6];//Gene Status				
					_lat[2] = spLine[10];//Method					
					_lat[3] = spLine[16];//Mouse correlate
					if(spLine[11].equals(" "))	spLine[11] = "";
					if(spLine[12].equals(" "))	spLine[12] = "";
					_lat[4] = (spLine[11] +"; " + spLine[12]).trim();
					_dgiUnit.setLat(_lat);
					bwGmp_y.write(_dgiUnit.formRow());
					
					{
						{
							int tmp_i = _get_i.intValue();
							{
								_dgStore[tmp_i].dg[7] = spLine[4];
								if(_dgStore[tmp_i].dg[32] != null)
									_dgStore[tmp_i].dg[32] += "; " + (spLine[11] +"; " + spLine[12]).trim();
								else _dgStore[tmp_i].dg[32] = (spLine[11] +"; " + spLine[12]).trim();
								_dgStore[tmp_i].dg[43] = spLine[6];
								_dgStore[tmp_i].dg[44] = spLine[10];
								_dgStore[tmp_i].dg[45] = spLine[16];
							}
						}
					}
				}
				else
				{
					_dgiUnit.set_gmp_n(Disorder[i], GeneSymbol[j], spLine[4], spLine[6], spLine[10], spLine[16], spLine[11] + spLine[12]);
					bwGmp_n.write(_dgiUnit.formRow());
					
					String _k_no_gn = "**" + _disorder + "__" + " **";
					String _k_no_dr = "** " + "__" + _gene_symbol + "**";
					Integer _i_no_gn = MDT_Gene.get(_k_no_gn);
					Integer _i_no_dr = MDT_Gene.get(_k_no_dr);
									
					{
						if(_i_no_gn != null)
						{
							no_gn ++;
							int tmp_i = _i_no_gn.intValue();
							/*
							 * add gememap's gene symbol()
							 */
							{
								MDT_Gene.remove(_k_no_gn);
								MDT_Gene.put(_k_no_gn, tmp_i);
							}
							{
							_dgStore[tmp_i].dg[8] = _gene_symbol;	
							_dgStore[tmp_i].dg[7] = spLine[4];
							if(_dgStore[tmp_i].dg[32] != null)
								_dgStore[tmp_i].dg[32] += "; " + (spLine[11] + spLine[12]).trim();
							else _dgStore[tmp_i].dg[32] = (spLine[11] + spLine[12]).trim();
							_dgStore[tmp_i].dg[43] = spLine[6];
							_dgStore[tmp_i].dg[44] = spLine[10];
							_dgStore[tmp_i].dg[45] = spLine[16];
							}							
						}
						
						if(_i_no_dr != null)
						{
							no_dt ++;
							int tmp_i = _i_no_dr.intValue();
							/*
							 * add genemap's diseae term
							 */
							{
								MDT_Gene.remove(_k_no_dr);
								MDT_Gene.put(_k_no_dr, tmp_i);
							}
							{
							_dgStore[tmp_i].dg[5] = _disorder;	
							_dgStore[tmp_i].dg[7] = spLine[4];
							if(_dgStore[tmp_i].dg[32] != null)
								_dgStore[tmp_i].dg[32] += "; " + (spLine[11] + spLine[12]).trim();
							else _dgStore[tmp_i].dg[32] = (spLine[11] + spLine[12]).trim();
							_dgStore[tmp_i].dg[43] = spLine[6];
							_dgStore[tmp_i].dg[44] = spLine[10];
							_dgStore[tmp_i].dg[45] = spLine[16];
							}
						}
					}
					
					if(_i_no_gn == null && _i_no_dr == null)
					{
						i_line ++;
						String[] _tmp_line = new String[46];
						_tmp_line[0] = (_inc_id2++) + "";
						_tmp_line[5] = _disorder;
						_tmp_line[8] = _gene_symbol;
						_tmp_line[7] = spLine[4];
						_tmp_line[32] += (spLine[11] + spLine[12]).trim();
						_tmp_line[43] = spLine[6];
						_tmp_line[44] = spLine[10];
						_tmp_line[45] = spLine[16];
						for(int k = 1; k < 46; k ++)
						{
							if(_tmp_line[k] == null)
							{
								_tmp_line[k] = " ";
								continue;
							}
							if(_tmp_line[k].replace(" ", "").equals(""))
							{
								_tmp_line[k] = " ";
							}
						}
						{
							/*
							 * put
							 */
							_dgStore[_dgsLen] = new dgStore(_tmp_line);
							MDT_Gene.put("**" + _disorder + "__" + _gene_symbol + "**", _dgsLen);
							_dgsLen ++;
						}
					}
				}
			}
		}
		
		{
			System.out.print("iLine: " + i_line + "\n" +
					"i_no_gn : " + no_gn + "\n" +
							"i_no_dr: " + no_dt + "\n" + "count line: " + _cout_line + "\n");
		}
		
		{
			for(int i = 0; i < _dgsLen; i ++)
			{
				_dgStore[i].dg[0] = (1024 + i) + "";
				bwDG.write(_dgStore[i].getString() + "\n");
			}
		}
		_bw.close();
		bwDG.close();
		brGmp.close();
	}
	
	public static String[] getDisorders(String _str)
	{
		String[] _ret = null;
		if(_str.equals(""))
		{
			System.out.print("buggmim\n");
			return null;
		}
		{
			if(_str.replace(" ", "").equals(""))
				return null;
		}
		String[] _seg = _str.split("; ");
		_ret = new String[_seg.length];
		String[] spSeg;
		int _i = 0;
		for(int i = 0; i < _seg.length; i ++)
		{
			spSeg = _seg[i].split(" \\(");
			spSeg[0].replace("    ", " ");
			spSeg[0].replace("   ", " ");
			spSeg[0].replace("  ", " ");
			spSeg = spSeg[0].split(", ");
			_i = spSeg.length - 1;
			try
			{
				Integer.parseInt(spSeg[spSeg.length - 1]);
			}
			catch(NumberFormatException e)
			{
				_i = spSeg.length;
			}
			_ret[i] = spSeg[0];
			for(int j = 1; j < _i; j ++)
			{
				_ret[i] += ", " + spSeg[j];
			}
			_ret[i] = _ret[i].replace("{", "")
				   .replace("[", "")
				   .replace("]", "")
				   .replace("}", "").replace("\"", "")
				   .replace("\\", "");
		}
		return _ret;
	}
	
	public static String[] getGeneSymbol(String _str)
	{
		String[] _ret = null;
		_ret = _str.split(", ");
		return _ret;
	}
	
	
	public static void Load_Did() throws IOException
	{
		brDid = new BufferedReader(new FileReader("Disease-GeneID.txt"));
		MDT_GS = new HashSet<String>();
		String rLine = null;
		rLine = brDid.readLine();
		rLine = brDid.readLine();
		rLine = brDid.readLine();
		rLine = brDid.readLine();
		String[] spDid;
		int _i = 4;
		while((rLine = brDid.readLine()) != null)
		{
			_i ++;
			spDid = rLine.split("\t");
			String[] p1;
			String[] p2;
			p2 = spDid[0].split("\\(C");
			if(p2.length != 2)
			{
				System.out.print("\nbug0" + "\t" + _i+"\n");
				break;
			}
			for(int i = 1; i < spDid.length; i ++)
			{
				p1 = spDid[i].split("\\(");
				if(p1.length != 2)
				{
					System.out.print("bug2");
					break;
				}
				MDT_GS.add("**" + p2[0] + "__" + p1[0] + "**");
			}
		}

		mg = new String[MDT_GS.size()];
		MDT_GS.toArray(mg);
		MDT_GS = null;
		System.out.print("\nmg, len: " + mg.length + "\n");
		mdt_Data();
	}
	
	public static int _inc_id = 1000000;
	public static int _inc_id2 = 2000000;
	
	public static void mdt_Data() throws IOException
	{
		Integer m_g;
		String[] sp_mg;
		BufferedWriter _tmp_bw = new BufferedWriter(new FileWriter("mdt_data_tmp"));
		int _ii = 0;
		int _ii_nogn = 0, _ii_nodt = 0;
		int _line_count = 0;
		for(int i = 0; i < mg.length; i ++)
		{
			m_g = MDT_Gene.get(mg[i]);
			if(m_g == null)
			{
				_ii ++;
				sp_mg = mg[i].split("__");
				if(sp_mg.length <= 1)
				{
					System.out.print("\n" + mg[i] + "\ti : " + i + "\n");
					continue;
				}
				String _dt = sp_mg[0].replace("**", "");
				String _gn = sp_mg[1].replace("**", "");
				
				String _key_no_gn = sp_mg[0] + "__" + " **";
				String _key_no_dt = "** " + "__" + sp_mg[1];
				Integer _mg_no_gn = MDT_Gene.get(_key_no_gn);
				Integer _mg_no_dt = MDT_Gene.get(_key_no_dt);
				{
					if(_mg_no_gn != null)
					{
						_dgStore[_mg_no_gn.intValue()].dg[8] = _gn;
						MDT_Gene.remove(_key_no_gn);
						MDT_Gene.put(sp_mg[0] + "__" + _gn + "**", _mg_no_gn.intValue());
						_tmp_bw.write(_dgStore[_mg_no_gn.intValue()].getString() + "\n");
						/*
						 * remove and put
						 */
						_line_count ++;
						_ii_nogn ++;
					}
					if(_mg_no_dt != null)
					{
						_dgStore[_mg_no_dt.intValue()].dg[5] = _dt;
						MDT_Gene.remove(_key_no_dt);
						MDT_Gene.put("**" + _dt + "__" + sp_mg[1], _mg_no_dt.intValue());
						_tmp_bw.write(_dgStore[_mg_no_dt.intValue()].getString() + "\n");
						_line_count ++;
						_ii_nodt ++;
					}
					
					if(_mg_no_gn == null && _mg_no_dt == null)
					{
						_dgiUnit.set_mg_data(_dt, _gn, ""+_inc_id);
						bwMdt_n.write(_line_count + "|" + _dgiUnit.formRow());
						_inc_id ++;
						_line_count ++;
						
						
						String[] _dgs = new String[46];
						_dgs[5] = _dt;
						_dgs[8] = _gn;
						_dgStore[_dgsLen] = new dgStore(_dgs);
						MDT_Gene.put("**" + _dt + "__" + _gn + "**", _dgsLen);
						_dgsLen ++;						
					}
				}
			}
		}
		
		{
			BufferedWriter _dgs_bw = new BufferedWriter(new FileWriter("DGS_Did"));
			for(int i = 0; i < _dgsLen; i ++)
			{
				_dgs_bw.write(_dgStore[i].getString() + "\n");
			}
			_dgs_bw.close();
		}
		{
			System.out.print("not null case: " + _ii + "\nline count: " + _line_count + "\n"
					+ "_ii_nogn: " + _ii_nogn + "\n" + "_ii_nodt: " + _ii_nodt + "\n");
		}
		_tmp_bw.close();
	}
}

//public String 	ID;
//public String 	Association_Y_N;
//public String 	Broad_Phenotype;
//public String 	Disease_Class;
//public String 	Disease_Class_Code;
//public String 	MeSH_Disease_Terms;
//public String 	Chromosom;
//public String 	Chr_Band;
//public String 	Gene;
//public String 	DNA_Start;
//public String 	DNA_End;
//public String 	P_Value;
//public String 	Reference;
//public String 	Pubmed_ID;
//public String 	Allele_Author_Description;
//public String 	Allele_Functional_Effects;
//public String 	Polymophism_Class;
//public String 	Gene_Name;
//public String 	RefSeq;
//public String 	Population;
//public String 	MeSH_Geolocation;
//public String 	Submitter;
//public String 	Locus_Number;
//public String 	Unigene;
//public String   Narrow_Phenotype;
//public String 	Mole_Phenotype;
//public String 	Journal;
//public String 	Title;
//public String 	rs_Number;
//public String 	OMIM_ID;
//public String 	Year;
//public String 	Conclusion;
//public String 	Study_Info;
//public String 	Env_Factor;
//public String 	GI_Gene_A;
//public String 	GI_Allele_of_Gene_A;
//public String 	GI_Gene_B;
//public String 	GI_Allele_of_Gene_B;
//public String 	GI_Gene_C;
//public String 	GI_Allele_of_Gene_C;
//public String 	GI_Association;
//public String 	GI_combine_Env_Factor;
//public String 	GI_relevant_to_Disease;
/*
 * 
 * 	public static void AddIn_MG(String _dt, String _gn, int _index, BufferedWriter _aim_bw) throws IOException
	{

		Integer _get_i = MDT_Gene.get(_key);
		if(_get_i == null)
		{
			MDT_Gene.put(_key, _get_i);
			{
				
			}
			return;
		}
		
		String[] _line1 = _getline.split("\t");
		String[] _line2 = _line.split("\t");
		{
			if(_line1.length != 46 || _line2.length != 46)
			{
				System.out.print("line bug" + _line1.length + " " + _line2.length);
			}
		}
		for(int i = 0; i < 46; i ++)
		{
			if(_line1[i].equals(_line2[i]) || _line2[i].equals(" "))		continue;
			if(_line1[1].equals(" "))
			{
				_line1[i] = _line2[i];
			}
			else
			{
				_line1[i] += "; " + _line2[i];
			}
		}
		String newLine = _line1[0];
		for(int i = 1; i < 46; i ++)
		{
			newLine += "\t" + _line1[i];
		}
		MDT_Gene.put(_key, newLine + "\n");
		_aim_bw.write(newLine);
	}
 */
