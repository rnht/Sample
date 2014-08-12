package rnht.sample.mailservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rnht.sample.mailservice.mail.Mail;

/**
 * An implementation of MailService using Mandrill api
 * @author rnht
 *
 */
public class MandrillService extends MailService {

	public MandrillService(String url, String apiKey)
			throws MalformedURLException {
		super(url, apiKey);
	}

	/**
	 * @see MailService#send(Mail)
	 */
	@Override
	public Status send(Mail mail) {
		try
		{
			JSONObject requestBody = assembleJsonRequest(mail);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; charset=utf8");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			writer.write(requestBody.toJSONString());
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

	@SuppressWarnings("unchecked")
	private JSONObject assembleJsonRequest(Mail mail)
	{
		JSONObject master = new JSONObject();
		JSONObject message = new JSONObject();
		JSONObject to = new JSONObject();
		master.put("key", apiKey);
		to.put("email", mail.addresses.get("to"));
		JSONArray array = new JSONArray();
		array.add(to);
		message.put("to", array);
		message.put("from_email", mail.addresses.get("from"));
		message.put("subject", mail.subject);
		message.put("text", mail.text);
		master.put("message", message);
		return master;
	}
	
	private JSONArray parseJsonResponse(InputStream stream) throws UnsupportedEncodingException, IOException, ParseException
	{
		
		JSONParser parser = new JSONParser();
		JSONArray json = (JSONArray) parser.parse(new InputStreamReader(stream, "UTF-8"));
		return json;
	}
	
	public static MandrillService newInstance()
	{
		return (MandrillService) create(MandrillService.class);
	}

}
