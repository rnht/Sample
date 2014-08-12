package rnht.sample.mailservice.test;

import org.junit.Assert;
import org.junit.Test;

import rnht.sample.mailservice.mail.Mail;
import rnht.sample.mailservice.service.MailService.Status;
import rnht.sample.mailservice.service.MailgunService;
import rnht.sample.mailservice.service.MandrillService;

public class TestMailServiceRequest {
	
	
	private Mail createMail()
	{
		Mail mail = new Mail();
		mail.subject = "title";
		mail.text = "body";
		mail.addresses.put("to", "rnht@outlook.com");
		mail.addresses.put("from", "rnht@outlook.com");
		return mail;
	}

	@Test
	public void TestSendToMailgun()
	{
		MailgunService service = MailgunService.newInstance();
		Assert.assertEquals(Status.Ok, service.send(createMail()));
	}
	
	@Test
	public void TestSendToMandrill()
	{
		MandrillService service = MandrillService.newInstance();
		Assert.assertEquals(Status.Ok, service.send(createMail()));
	}

}
