package rnht.sample.mailservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import rnht.sample.mailservice.mail.Mail;

/**
 * An abstract implementation of email service
 * @author rnht
 *
 */
public abstract class MailService {
	
	public enum Status
	{
		Ok,
		RequestError,
		ServiceDown
	}

	protected final URL url;
	protected final String apiKey;
	
	public MailService(String url, String apiKey) throws MalformedURLException
	{
		this.url = new URL(url);
		this.apiKey = apiKey;
	}
	
	/**
	 * Send the mail to the mail service
	 * @param mail
	 * @return a high-level indicator of what happened to the sending process,
	 * 			null if internal error occurs.
	 */
	public abstract Status send(Mail mail);
	
	protected static Properties getProperties(String resource) throws IOException
	{
		InputStream instr = MailService.class.getClassLoader().getResourceAsStream(resource);
		Properties properties = new Properties();
		properties.load(instr);
		return properties;
	}
	
	/**
	 * Create a specific mail service by loading info from .properties
	 * @param c - subclass of MailService
	 * @return the instance of subclass of MailService
	 */
	protected static <T extends MailService> MailService create(Class<T> c)
	{
		T result;
		try
		{
			Properties prop = getProperties("rnht/sample/mailservice/service/" + c.getSimpleName() + ".properties");
			String url, key;
			url = prop.getProperty("url");
			key = prop.getProperty("key");
			result = c.getConstructor(String.class, String.class).newInstance(url, key);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
