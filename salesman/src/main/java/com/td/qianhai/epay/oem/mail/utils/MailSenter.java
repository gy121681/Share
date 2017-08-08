package com.td.qianhai.epay.oem.mail.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


public class MailSenter {

	private String host;
	private String username;
	private String password;

	// private Handler handler;

	// private static final String SMTPHOST = "smtphost";
	// private static final String USERNAME = "username";
	// private static final String PASSWORD = "password";
	// private static MailSenter instance;

	// public static MailSenter getInstance(EditMailActivity context) {
	// if (instance == null) {
	// instance = new MailSenter(PreferencesUtil.getSharedStringData(context,
	// SMTPHOST), PreferencesUtil.getSharedStringData(context, USERNAME),
	// PreferencesUtil.getSharedStringData(context, PASSWORD));
	// }
	// return instance;
	// }

	/**
	 * 构造方法
	 * 
	 * @param host
	 *            邮箱服务器
	 * @param username
	 *            邮箱用户全名。例如：test@qq.com
	 * @param password
	 *            邮箱用户密码
	 */
	public MailSenter(String host, String username, String password) {
		// this.handler = handler;
		this.host = host;
		this.username = username;
		this.password = password;
	}

	/**
	 * 发送邮件
	 * 
	 * @param mailTo
	 *            收件人email地址
	 * @param mailSubject
	 *            邮件标题
	 * @param mailBody
	 *            邮件正文
	 * @throws Exception
	 */
	public void send(String mailTo, String mailSubject, String mailBody)
			throws Exception {
		send(mailTo, mailSubject, mailBody, null, null);
	}

	/**
	 * 发送邮件
	 * 
	 * @param email
	 *            邮件对象
	 * @throws Exception
	 */
//	public void send(Email email) throws Exception {
//		send(email.getTo(), email.getSubject(), email.getContent(),
//				email.getAttachments(), null);
//	}

	/**
	 * 发送邮件
	 * 
	 * @param mailTo
	 *            收件人email地址
	 * @param mailSubject
	 *            邮件标题
	 * @param mailBody
	 *            邮件正文
	 * @param attachments
	 *            附件
	 * @param personalName
	 *            邮件显示的发送人名称
	 * @throws Exception
	 */
	public void send(String mailTo, String mailSubject, String mailBody,
			ArrayList<String> attachments, String personalName)
			throws Exception {
		try {
			Properties props = new Properties();
			Authenticator auth = new Email_Autherticator();
			// 进行邮件服务器用户认证
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			Session session = Session.getInstance(props, auth);
			// 设置session,和邮件服务器进行通讯。
			MimeMessage message = new MimeMessage(session);

			message.setSentDate(new Date()); // 设置邮件发送日期
			message.setFrom(new InternetAddress(username, personalName)); // 设置邮件发送者的地址

			String[] addrs = mailTo.split(",");
			Address[] to = new InternetAddress[addrs.length];
			for (int i = 0; i < addrs.length; i++) {
				to[i] = new InternetAddress(addrs[i]);
			}
			message.addRecipients(Message.RecipientType.TO, to);

			message.setSubject(mailSubject); // 设置邮件主题
			// message.setContent("foobar, "application/x-foobar"); // 设置正文格式
			// message.setText(mailBody); // 设置邮件正文

			Multipart mpRoot = new MimeMultipart();
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(mailBody, "text/html;charset=gb2312");
			mpRoot.addBodyPart(mimeBodyPart);
			for (int i = 0; i < attachments.size(); i++) {
				MimeBodyPart mpAttachments = new MimeBodyPart();
				mpAttachments.setDataHandler(new DataHandler(
						new FileDataSource(attachments.get(i))));
				mpAttachments.setFileName(MimeUtility.encodeText(attachments
						.get(i).substring(
								attachments.get(i).lastIndexOf("/") + 1)));
				mpRoot.addBodyPart(mpAttachments);
			}
			message.setContent(mpRoot);

			// 设置mailcap支持多种格式
			MailcapCommandMap mc = (MailcapCommandMap) CommandMap
					.getDefaultCommandMap();
			mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			CommandMap.setDefaultCommandMap(mc);

			Transport.send(message); // 发送邮件
			// handler.obtainMessage(1).sendToTarget();
		} catch (Exception ex) {
			ex.printStackTrace();
			// handler.obtainMessage(999).sendToTarget();
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * 用来进行服务器对用户的认证
	 */
	public class Email_Autherticator extends Authenticator {
		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	}

}
