package DataProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


class hah
{
	public String ID;
	public String DR;
}


public class CalMultiple {
	
	public static BufferedReader brNO;
	public static BufferedWriter bwNO;
	
	public static void Initial() throws IOException
	{
		brNO = new BufferedReader(new FileReader("uniprot_sprot_human.dat"));
		bwNO = new BufferedWriter(new FileWriter("Name_Origin"));
	}
	
	public static void bClose() throws IOException
	{
		brNO.close();
		bwNO.close();
	}
	
	public static void main(String[] args) throws IOException
	{
		cal_mul();
	}
	
	public static void cal_mul() throws IOException
	{

		Initial();
		int noCount = 0;
		int i_pe = 0;
		int i_cc = 0;
		int i_dr = 0;
		int i_ft = 0;
		int i_sq = 0;
		int i_dt = 0;
		int i_rx = 0;
		int i_rc = 0;
		int i_ra = 0;
		int i_rt = 0;
		int i_rl = 0;
		int i_rp = 0;

		int l_pe = 0;
		int l_cc = 0;
		int l_dr = 0;
		int l_ft = 0;
		int l_sq = 0;
		int l_dt = 0;
		int l_rx = 0;
		int l_rc = 0;
		int l_ra = 0;
		int l_rt = 0;
		int l_rl = 0;
		int l_rp = 0;
		
		int m_pe = 0;
		int m_cc = 0;
		int m_dr = 0;
		int m_ft = 0;
		int m_sq = 0;
		int m_dt = 0;
		int m_rx = 0;
		int m_rc = 0;
		int m_ra = 0;
		int m_rt = 0;
		int m_rl = 0;
		int m_rp = 0;

		String rLine = "";
		String _id = "";
		while(rLine != null)
		{
			if(rLine.startsWith("ID   "))
			{
				_id = rLine;
				noCount ++;
				if(noCount % 2000 == 0)
					System.out.print(
							noCount + " => \t"
							);
					
				i_pe = 0;
				i_cc = 0;
				i_dr = 0;
				i_ft = 0;
				i_sq = 0;
				i_dt = 0;
				i_rx = 0;
				i_rc = 0;
				i_ra = 0;
				i_rt = 0;
				i_rl = 0;
				i_rp = 0;
				
				l_pe = 0;
				l_cc = 0;
				l_dr = 0;
				l_ft = 0;
				l_sq = 0;
				l_dt = 0;
				l_rx = 0;
				l_rc = 0;
				l_ra = 0;
				l_rt = 0;
				l_rl = 0;
				l_rp = 0;
				
				rLine = "";
				while(! rLine.startsWith("ID   "))
				{
					if(rLine.startsWith("PE   "))
					{
						i_pe ++;
						if(l_pe < rLine.length())
							l_pe = rLine.length();;
					}
					else
					if(rLine.startsWith("CC   "))
					{
						i_cc ++;
						if(l_cc < rLine.length())
							l_cc = rLine.length();;
					}
					else
					if(rLine.startsWith("DR   "))
					{
						i_dr ++;
						if(l_dr < rLine.length())
							l_dr = rLine.length();;
					}
					if(rLine.startsWith("FT   "))
					{
						i_ft ++;
						if(l_ft < rLine.length())
							l_ft = rLine.length();;
					}
					else
					if(rLine.startsWith("SQ   "))
					{
						i_sq ++;
						if(l_sq < rLine.length())
							l_sq = rLine.length();;
					}
					else
					if(rLine.startsWith("DT   "))
					{
						i_dt ++;
						if(l_dt < rLine.length())
							l_dt = rLine.length();;
					}
					if(rLine.startsWith("RX   "))
					{
						i_rx ++;
						if(l_rx < rLine.length())
							l_rx = rLine.length();;
					}
					else
					if(rLine.startsWith("RC   "))
					{
						i_rc ++;
						if(l_rc < rLine.length())
							l_rc = rLine.length();;
					}
					else
					if(rLine.startsWith("RA   "))
					{
						i_ra ++;
						if(l_ra < rLine.length())
							l_ra = rLine.length();;
					}
					if(rLine.startsWith("RT   "))
					{
						i_rt ++;
						if(l_rt < rLine.length())
							l_rt = rLine.length();;
					}
					else
					if(rLine.startsWith("RL   "))
					{
						i_rl ++;
						if(l_rl < rLine.length())
							l_rl = rLine.length();;
					}
					else
					if(rLine.startsWith("RP   "))
					{
						i_rp ++;
						if(l_rp < rLine.length())
							l_rp = rLine.length();;
					}


					rLine = brNO.readLine();
					if(rLine == null) break;
				}

				if(m_pe < i_pe)
				{
					m_pe = i_pe;
				}
				if(m_cc < i_cc)
				{
					m_cc = i_cc;
				}
				if(m_dr < i_dr)
				{
					m_dr = i_dr;
				}
				if(m_ft < i_ft)
				{
					m_ft = i_ft;
				}
				if(m_sq < i_sq)
				{
					m_sq = i_sq;
				}
				if(m_dt < i_dt)
				{
					m_dt = i_dt;
				}
				if(m_rx < i_rx)
				{
					m_rx = i_rx;
				}
				if(m_rc < i_rc)
				{
					m_rc = i_rc;
				}
				if(m_ra < i_ra)
				{
					m_ra = i_ra;
				}
				if(m_rt < i_rt)
				{
					m_rt = i_rt;
				}
				if(m_rl < i_rl)
				{
					m_rl = i_rl;
				}
				if(m_rp < i_rp)
				{
					m_rp = i_rp;
				}
				
				if(i_dr == 864)
				{
					System.out.print(_id + "\n\n");
				}
				
				
				continue;
			}
			
			rLine = brNO.readLine();
		}
		
//		System.out.print(
//				"\n\n#Len" 
//				+ "\t\t" + l_pe
//				+ "\t\t" + l_cc
//				+ "\t\t" + l_dr
//				+ "\t\t" + l_ft
//				+ "\t\t" + l_sq
//				+ "\t\t" + l_dt
//				+ "\t\t" + l_rx
//				+ "\t\t" + l_rc
//				+ "\t\t" + l_ra
//				+ "\t\t" + l_rt
//				+ "\t\t" + l_rl
//				+ "\t\t" + l_rp + "\n\n"
//		);
//		
//		System.out.print(
//				"#Freq" 
//				+ "\t\t" + m_pe
//				+ "\t\t" + m_cc
//				+ "\t\t" + m_dr
//				+ "\t\t" + m_ft
//				+ "\t\t" + m_sq
//				+ "\t\t" + m_dt
//				+ "\t\t" + m_rx
//				+ "\t\t" + m_rc
//				+ "\t\t" + m_ra
//				+ "\t\t" + m_rt
//				+ "\t\t" + m_rl
//				+ "\t\t" + m_rp + "\n\n"
//		);
		
		
		bClose();
	
	}
}
