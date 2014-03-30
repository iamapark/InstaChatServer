package com.springapp.mvc;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HelloController {

    Contact contact;

    @RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public void register(HttpServletRequest req) {

        String email = req.getParameter(Constants.FROM);
        String regId = req.getParameter(Constants.REG_ID);

        System.out.println(regId);
        contact = new Contact(email, regId);

        /*EntityManager em = EMFService.get().createEntityManager();
        try {
            Contact contact = Contact.find(email, em);
            if (contact == null) {
                contact = new Contact(email, regId);
            } else {
                contact.setRegId(regId);
            }
            em.persist(contact);
            //logger.log(Level.WARNING, "Registered: " + contact.getId());
        } finally {
            em.close();
        }*/
    }

    @RequestMapping(value = "unregister", method = RequestMethod.POST)
    protected void unregister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter(Constants.FROM);

        EntityManager em = EMFService.get().createEntityManager();
        try {
            Contact contact = Contact.find(email, em);
            if (contact != null) {
                em.remove(contact);
                //logger.log(Level.WARNING, "Unregistered: " + contact.getId());
            }
        } finally {
            em.close();
        }
    }


    @RequestMapping(value = "send", method = RequestMethod.POST)
    protected void send(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msg = req.getParameter(Constants.MSG);
        String from = req.getParameter(Constants.FROM);
        String to = req.getParameter(Constants.TO);

        System.out.println(from);

        String regId = contact.getRegId();
        Sender sender = new Sender(Constants.API_KEY);
        Message message = new Message.Builder()
//          .delayWhileIdle(true)
                .addData(Constants.TO, to).addData(Constants.FROM, from).addData(Constants.MSG, msg)
                .build();

        try {
            //com.google.android.gcm.server.Result result = sender.send(message, regId, 5);
            List<String> regIds = new ArrayList<String>();
            regIds.add(regId);
            MulticastResult result = sender.send(message, regIds, 5);

            System.out.println(result);

            //logger.log(Level.WARNING, "Result: " + result.toString());
        } catch (IOException e) {
            e.printStackTrace();
            //logger.log(Level.SEVERE, e.getMessage());
        }
    }
}