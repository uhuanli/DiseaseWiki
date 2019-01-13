package PageData;

public class proteinPageData 
{
	public String[][][] PPD;
	static final int iNameOrigin = 6;
	static final int iProteinAttributes = 4;
	static final int iGeneralAnnotation = 6;
	static final int iGeneOntology = 4;
	static final int iBinaryInteraction = 7;
	static final int iAlternativeProducts = 6;
	static final int iSequenceAnnotation = 6;
	static final int iSequences = 4;
	static final int iEntryInformation = 6;
	static final int iCrossReferences = 5;
	static final int iReferences = 8;
	static final int[] iPPD = new int[]{6, 4, 6, 4, 7, 6, 6, 4, 6, 5, 8};
	
	public proteinPageData(String[][][] _ppd)
	{
		PPD = _ppd;
	}
	
	public proteinPageData()
	{
		int _ii = 0;
		PPD = new String[11][][];
		for(int i = 0; i < PPD.length; i ++)
		{
			PPD[i] = new String[1][iPPD[i]];
			for(int j = 0; j < PPD[i].length; j ++)
			{
				for(int k = 0; k < PPD[i][j].length; k ++)
					PPD[i][j][k] = "default" + (_ii ++);
			}
		}
		{
			for(int i = 0; i < PPD[0].length; i ++)
				PPD[0][i][1] = "[[" + "GeneCat" + "]]";
		}
	}	
}
