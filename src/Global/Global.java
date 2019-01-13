package Global;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.mime.content.StringBody;

public class Global {
	
	public static String IP_ADDR = "192.168.46.130";
	public static StringBody WP_SECTION;
	public static StringBody WP_SCROLL_TOP;
	public static StringBody WP_AUTO_SUMMARY;
	public static StringBody WP_SAVE;
	public static StringBody WP_EDIT_TOKEN;
	public static StringBody WP_SUMMARY;
	public Global() throws IOException
	{
		WP_SECTION = new StringBody("");
		WP_SCROLL_TOP = new StringBody("0");
		WP_AUTO_SUMMARY = new StringBody("d41d8cd98f00b204e9800998ecf8427e");
		WP_SAVE = new StringBody("±£´æ±¾Ò³");
		WP_EDIT_TOKEN = new StringBody("+\\");
		WP_SUMMARY = WP_SECTION;
		bwBug = new BufferedWriter(new FileWriter("Exe_Log"));
	}
	public static BufferedWriter bwBug;
}
