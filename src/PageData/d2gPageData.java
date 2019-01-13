package PageData;

public class d2gPageData 
{
	public String[] d2g_data;
	final static int[] _link_data = new int[]{
		5,
		8
	};
	
	public d2gPageData(String[] _data)
	{
		{
			if(_data.length != 46)
			{
				System.out.print("bug d2g len\n");
			}
		}
		d2g_data = _data;
		{
			for(int i = 0; i < d2g_data.length; i ++)
			{
				d2g_data[i] = d2g_data[i].trim().replace("; ", "\n"); 
			}
			{
				String[] _sp = d2g_data[29].split("\\\n");
				d2g_data[29] = "[http://www.omim.org/entry/" + _sp[0] + " " + _sp[0] + "]";
				for(int _i = 1; _i < _sp.length; _i ++)
				{
					_sp[_i] = "[http://www.omim.org/entry/" + _sp[_i] + " " + _sp[_i] + "]";
				}
			}
		}
		for(int i = 0; i < _link_data.length; i ++)
		{
			d2g_data[_link_data[i]] = "[[" + d2g_data[_link_data[i]] + "]]";
		}
	}
	
	public String formTitle()
	{
		String _title = d2g_data[5].replace("[[", "").replace("]]", "")
						+ "_TO_" + d2g_data[8].replace("[[", "").replace("]]", "");
		_title = _title.replace("\"", "").replace("[", "(").replace("]", ")");
		return _title;
	}
	
	public d2gPageData()
	{
		d2g_data = new String[47];
		for(int i = 0; i < d2g_data.length; i ++)
		{
			{
				d2g_data[i] = "default" + i;
			}
		}
		for(int i = 0; i < _link_data.length; i ++)
		{
			d2g_data[_link_data[i]] = "[[" + d2g_data[_link_data[i]] + "]]";
		}
	}
}
