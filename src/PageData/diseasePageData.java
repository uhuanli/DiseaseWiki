package PageData;

import java.util.ArrayList;

import DataProcess.omimUnit;

public class diseasePageData 
{
	public ArrayList<omimUnit> omList;
	String[] diseaseData;
	final static int iTx = 0;
	final static int iAv = 1;
	final static int iCs = 2;
	final static int iRf = 3;
	public diseasePageData(String[] _data, ArrayList<omimUnit> _om_list)
	{
		diseaseData = _data;
		omList = _om_list;
	}
	
	public void AddOmim(omimUnit _oi)
	{
		omList.add(_oi);
	}
	
	public boolean hasOMIM()
	{
		return !omList.isEmpty();
	}
	
	public String[] getDiseaseData()
	{
		return diseaseData;
	}
	
	public String[][] getOmimField(int _i)
	{
		int _sz = omList.size();
		String[][] _omimField = new String[_sz][2]; 
		for(int i = 0; i < omList.size(); i ++)
		{
			//http://www.omim.org/entry/153100¡£
			_omimField[i][0] = "[http://www.omim.org/entry/" + omList.get(i).fNO + " " + omList.get(i).fNO + "]";
			switch(_i)
			{
			case iTx:
			{
				_omimField[i][1] = omList.get(i).fTX;
				break;
			}
			case iAv:
			{
				_omimField[i][1] = omList.get(i).fAV;
				break;
			}
			case iCs:
			{
				_omimField[i][1] = omList.get(i).fCS;
				break;
			}
			case iRf:
			{
				_omimField[i][1] = omList.get(i).fRF;
				break;
			}
			default:
			{
				System.out.print("bug in switch\n");
				System.exit(0);
			}
			}
		}
		
		return _omimField;
	}
}
