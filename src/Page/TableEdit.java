package Page;


public class TableEdit
{
	String template;
	String[][] field;

	public TableEdit(String _t, String[][] _f)
	{
		/*
		 * check exist
		 */
		template = _t;
		setField(_f);
	}
	
	public TableEdit(String _t, String[] _fi)
	{
		template = _t;
		field = new String[1][];
		field[0] = _fi;
	}
	
	private void setField(String [][] _f)
	{
		field = _f;
	}
	
	public String[][] getField()
	{
		return field;
	}
	
	public String[] getField(int i)
	{
		{
			if(i >= field.length || i < 0)
			{
				System.err.print("field len bug\n");
			}
		}
		return field[i];
	}
	
	public int getFieldLen()
	{
		return field.length;
	}
	
	public String getTemplate()
	{
		return template;
	}
	
}
