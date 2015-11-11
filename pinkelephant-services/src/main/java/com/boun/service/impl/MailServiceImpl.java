package com.boun.service.impl;

import org.springframework.stereotype.Service;

import com.boun.data.common.pool.PinkElephantThreadPool;
import com.boun.data.util.MailSender;
import com.boun.service.MailService;
import com.boun.service.PinkElephantService;

@Service
public class MailServiceImpl extends PinkElephantService implements MailService{

	@Override
	public void sendMail(final String email, final String subject, final String body) {
		
		PinkElephantThreadPool.EMAIL_POOL.runTask(new Runnable() {

			@Override
			public void run() {
				MailSender.getInstance().sendMail(email, subject, body);
			}
		});
	}

}
