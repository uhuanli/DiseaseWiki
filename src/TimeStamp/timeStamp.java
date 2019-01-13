package TimeStamp;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class timeStamp {
	public String getTimeStamp()
	{
		Calendar cdr = Calendar.getInstance(TimeZone.getTimeZone("GTM"));
		int i_year = cdr.get(Calendar.YEAR);
		int i_month = cdr.get(Calendar.MONTH) + 1;
		int i_day = cdr.get(Calendar.DAY_OF_MONTH);
		int i_hour = cdr.get(Calendar.HOUR_OF_DAY);
		int i_minute = cdr.get(Calendar.MINUTE);
		int i_second = cdr.get(Calendar.SECOND);
		String _year = i_year + "";
		String _month = i_month > 9 ? (i_month + "") : ("0" + i_month);
		String _day = i_day > 9 ? (i_day + "") : ("0" + i_day);
		DecimalFormat df = new  DecimalFormat( "00");
		String _hour = df.format(i_hour);
		String _minute = df.format(i_minute);
		String _second = df.format(i_second);
		String _ts = _year + _month + _day + _hour + _minute + _second;
		return _ts;
	}
}
