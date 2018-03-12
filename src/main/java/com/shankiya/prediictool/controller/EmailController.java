package com.shankiya.prediictool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shankiya.prediictool.model.Mail;
import com.shankiya.prediictool.services.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

	@Autowired
	private EmailService emailService;

	@RequestMapping(value="/emailSend",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public Mail sendMailService(@RequestBody Mail email) {
		Mail mail = new Mail();
		mail.setFrom("shankiyamahatics.com");
		mail.setTo("lakshmi.muralikrishnan@gmail.com");
		mail.setSubject("Reset Password");
		mail.setContent("shankiyamahatiics");
		emailService.sendSimpleMessage(mail);
		return mail;
	}
}
