package DataProcess;


public class omimUnit
{
	public String fNO;
	public String fTI;
	public String fTX;
	public String fAV;
	public String fRF;
	public String fCS;
	public String fCN;
	public String fCD;
	public String fED;
	public omimUnit()
	{
		Initial();
	}
	public void Initial()
	{
		fNO = "";
		fTI = "";
		fTX = "";
		fAV = "";
		fRF = "";
		fCS = "";
		fCN = "";
		fCD = "";
		fED = "";
	}

	public void Print()
	{
		System.out.print(
				fNO+
				fTI+
				fTX+
				fAV+
				fRF+
				fCS+
				fCN+
				fCD+
				fED
		);
	}
	
	public String formRow()
	{
		String _row;
		
		_row = (
				fNO + "\t" +
				fTI + "\t" + 
				fTX + "\t" + 
				fAV + "\t" + 
				fRF + "\t" + 
				fCS + "\t" + 
				fCN + "\t" + 
				fCD + "\t" + 
				fED + "\n!@#\n"
		);
		
		return _row;
	}
	
}
