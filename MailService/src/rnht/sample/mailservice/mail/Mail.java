package rnht.sample.mailservice.mail;

import java.util.Map;
import java.util.TreeMap;

public class Mail {
	public final Map<String, String> addresses = new TreeMap<String, String>();
	
	public String subject = "", text = "";

}
