/*
 * Copyright 2013 QAPROSOFT (http://qaprosoft.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tengmoney.foundation.report;

import com.tmoney.foundation.utils.Configuration;
import com.tmoney.foundation.utils.Configuration.Parameter;
import com.tmoney.foundation.utils.R;
import lombok.extern.slf4j.Slf4j;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * EmailManager is used for sending emails.
 * 
 * @author Alex Khursevich
 */
@Slf4j
public class EmailManager
{

	public static void send(String subject, String emailContent, String adresses, String senderEmail, String senderPswd)
	{
		Properties props = initProps();
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator()
		{
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(Configuration.get(Parameter.SENDER_EMAIL), Configuration.get(Parameter.SENDER_PASSWORD));
			}
		});
		try
		{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("QATeam"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adresses));
			message.setSubject(subject);
			message.setContent(emailContent, "text/html");
			Transport.send(message);
			log.info("Reports were successfully sent!");
		}
		catch (MessagingException e)
		{
			log.info("Email with reports was NOT send: " + e.getMessage());
		}
	}

	private static Properties initProps()
	{
		Properties props = new Properties();
		props.put("mail.smtp.host", R.EMAIL.get("mail.smtp.host"));
		props.put("mail.smtp.socketFactory.port", R.EMAIL.get("mail.smtp.socketFactory.port"));
		props.put("mail.smtp.socketFactory.class", R.EMAIL.get("mail.smtp.socketFactory.class"));
		props.put("mail.smtp.auth", R.EMAIL.get("mail.smtp.auth"));
		props.put("mail.smtp.port", R.EMAIL.get("mail.smtp.port"));
		return props;
	}
}
