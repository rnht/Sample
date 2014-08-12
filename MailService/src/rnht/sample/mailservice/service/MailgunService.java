package rnht.sample.mailservice.service;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Map.Entry;

import org.apache.tomcat.util.codec.binary.Base64;

import rnht.sample.mailservice.mail.Mail;

/**
 * An implementation of MailService using MailgunService api
 * @author rnht
 *
 */
public class MailgunService extends MailService {

	public MailgunService(String url, String apiKey) throws MalformedURLException {
		super(url, apiKey);
	}

	/**
	 * @see MailService#send(Mail)
	 */
	@Override
	public Status send(Mail mail) {
		try
		{
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("Authorization", "Basic " + new String(new Base64().encode(("api:" + apiKey).getBytes())));
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
			for (Entry<String, String> entry : mail.addresses.entrySet())
			{
				writer.write(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
			}
			writer.write("subject=" + URLEncoder.encode(mail.subject, "UTF-8") + "&");
			writer.write("text=" + URLEncoder.encode(mail.text, "UTF-8"));
			writer.flush();
			writer.close();
			int statusCode = connection.getResponseCode();
			switch (statusCode)
			{
			case 200: 	return Status.Ok;
			case 400: 	return Status.RequestError;
			default: 	return Status.ServiceDown;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static MailgunService newInstance()
	{
		return (MailgunService) create(MailgunService.class);
	}
}
