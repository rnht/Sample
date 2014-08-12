package rnht.sample.mailservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rnht.sample.mailservice.mail.Mail;
import rnht.sample.mailservice.service.MailService;
import rnht.sample.mailservice.service.MailgunService;
import rnht.sample.mailservice.service.MailService.Status;
import rnht.sample.mailservice.service.MandrillService;

/**
 * Servlet implementation class MailServiceServlet
 */
@WebServlet("/MailServiceServlet")
public class MailServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<MailService> mailServices;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MailServiceServlet() {
        super();
        mailServices = new ArrayList<MailService>();
    	mailServices.add(MailgunService.newInstance());
    	mailServices.add(MandrillService.newInstance());
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Mail mail = createMailFromRequest(request);
    	Boolean ok = false, badRequest = false, internalError = false;
    	for (MailService service : mailServices)//find a service that can successfully send
    	{
    		Status status = service.send(mail);
    		if (status == Status.Ok)
    		{
    			ok = true;
    			break;
    		}
    		else if (status == Status.RequestError)
    		{
    			badRequest = true;
    		}
    		else if (status == null)
    		{
    			internalError = true;
    		}
    	}
    	if (ok)
    	{
    		response.setStatus(HttpServletResponse.SC_OK);
    	}
    	else if (badRequest)//there is a miss match btw mail service api(s) and info passed in from web page.
    	{
    		response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    	}
    	else if (internalError)//exception happened on the server side.
    	{
    		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    	}
    	else//None of the mail services is available
    	{
    		response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "All mail services being used are not available at this moment.");
    	}
    }
	
    /**
     * Parser info needed to construct an email object from post request 
     * @param request
     * @return Mail
     */
	private Mail createMailFromRequest(HttpServletRequest request)
	{
		Mail mail = new Mail();
		mail.subject = request.getParameter("subject");
		mail.text = request.getParameter("text");
		mail.addresses.put("to", request.getParameter("to"));
		mail.addresses.put("from", request.getParameter("from"));

		String cc = request.getParameter("cc");
		String bcc = request.getParameter("bcc");
		if (cc != null)
		{
			mail.addresses.put("cc", request.getParameter("cc"));
		}
		if (bcc != null)
		{
			mail.addresses.put("bcc", request.getParameter("bcc"));
		}
		return mail;
	}

}
