package PageData;

public class genePageData
{
	public String[] gInfo = new String[15]; 
	public String[] gPro = new String[6];
	
	public genePageData(String [][] _gene_info)
	{
		gInfo = _gene_info[0];
	}
	
	public genePageData()
	{
		for(int i = 0; i < gInfo.length; i ++)
		{
			gInfo[i] = "default" + i;
		}
		for(int i = 0; i < gPro.length; i ++)
		{
			gPro[i] = "default" + (i + gInfo.length);
		}
		gPro[2] = "[[" + gPro[2] + "]]";
	}
}