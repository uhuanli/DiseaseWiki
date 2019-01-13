package forDebug;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class forDebug {
	public static void main() throws IOException
	{
		{
			String _str = "1193422	12978934	ORF14	A326_gp17	-	-	-	-	protein ORF14	protein-coding	-	-	-	-	20120529";
			String[] _sp = _str.split("\t");
			{
				System.out.print("length: " + _sp.length + "\n");
			}
			for(int i = 0; i < _sp.length; i ++)
				System.out.print(_sp[i] + "\n");
		}
		
//		BufferedReader _br = new BufferedReader(new FileReader("DG"));
//		BufferedWriter _bw = new BufferedWriter(new FileWriter("DGpart"));
//		String rLine;
//		int _ii = 0;
//		while((rLine = _br.readLine()) != null)
//		{
//			_ii ++;
//			if(_ii < 10000000)
//			{
//				_bw.write(rLine + "\n");
//			}
//		}
//		{
//			System.out.print("nLine : " + _ii);
//		}
	}
}
