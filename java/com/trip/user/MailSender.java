package com.trip.user;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 이메일 발송 기능을 제공하는 클래스 (JavaMail API 사용)
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class MailSender {
	
	/**
	 * 메인 메소드 (테스트용)
	 * @param args 사용되지 않음
	 */
	public static void main(String[] args) {
		MailSender sender = new MailSender();
		
		Map<String,String> map = new HashMap<String,String>();
		
	}
	
	/**
	 * (미사용으로 추정) 테스트용 메일을 발송합니다.
	 * @param map 수신자 이메일 정보 (key: "email")
	 */
	public void sendMail(Map<String,String> map) {
		//- 보내는 사람(이름,이메일)
		//- 받는 사람(이름,이메일)
		//- 제목
		//- 내용
		
		String username = "dlq4444@gmail.com";
		String password = "pacj yqua rxbp";
		
		//HTTP, Hyper Text Transfer Protocol
		//SMTP, Simple Mail Transfer Protocol
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(username, password);
			}
		});
		
		try {
			
			//메일 
			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(username));//보내는 사람
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(map.get("email")));
			
			message.setSubject("테스트 메일입니다.");
			
			String content = """
					
					<h1>메일 테스트</h1>
					<div>메일 내용입니다.</div>
					
					""";
			
			message.setContent(content, "text/plain; charset=UTF-8");
			
			Transport.send(message);
			
			
			System.out.println("이메일 전송 완료");
		} catch (Exception e) {
			System.out.println("MailSender.sendMail()");
			e.printStackTrace();
		}
	}
	
	/**
	 * 회원가입 또는 본인 확인용 인증번호 메일을 발송합니다.
	 * @param map 수신자 이메일(key: "email")과 인증번호(key: "validNumber")가 담긴 맵
	 */
	public void sendVerificationMail(Map<String,String> map) {
		//- 보내는 사람(이름,이메일)
		//- 받는 사람(이름,이메일)
		//- 제목
		//- 내용
		
		String username = "dlq4444@gmail.com";
		String password = "ilfr fkek jpwe gsru";

		
		//HTTP, Hyper Text Transfer Protocol
				//SMTP, Simple Mail Transfer Protocol
				
				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", "smtp.gmail.com");
				props.put("mail.smtp.port", "587");
				
				Session session = Session.getInstance(props, new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
				
				try {
					
					//메일
					Message message = new MimeMessage(session);
					
					message.setFrom(new InternetAddress(username));//보내는 사람
					
					message.setRecipients(Message.RecipientType.TO
								, InternetAddress.parse(map.get("email")));
					
					message.setSubject("프로젝트에서 발송한 인증 번호입니다.");
					
					String content = """
					
						<h2>인증 번호 발송</h2>
						
						<div style="border: 1px solid #CCC; width: 300px; height: 120px; border-radius: 5px; background-color: #EEE; display: flex; justify-content: center; align-items: center; margin: 20px 0;">
							인증번호: <span style="font-weight: bold;">%s</span>
						</div>
						
						<div>위의 인증 번호를 확인하세요.</div>
									
					""".formatted(map.get("validNumber"));
					
					message.setContent(content, "text/html; charset=UTF-8");
					
					Transport.send(message);
					
					System.out.println("이메일 전송 완료!!");
					
				} catch (Exception e) {
					System.out.println("MailSender.sendMail()");
					e.printStackTrace();
				}
				
			}

	/**
	 * 아이디 찾기 성공 시, 사용자 이메일로 아이디를 발송합니다.
	 * @param email 수신자 이메일 주소
	 * @param id    발송할 사용자 아이디
	 */
	public void sendIdVerificationMail(String email, String id) {
		//- 보내는 사람(이름,이메일)
				//- 받는 사람(이름,이메일)
				//- 제목
				//- 내용
				
				String username = "dlq4444@gmail.com";
				String password = "ilfr fkek jpwe gsru";
				
				
				//HTTP, Hyper Text Transfer Protocol
						//SMTP, Simple Mail Transfer Protocol
						
						Properties props = new Properties();
						props.put("mail.smtp.auth", "true");
						props.put("mail.smtp.starttls.enable", "true");
						props.put("mail.smtp.host", "smtp.gmail.com");
						props.put("mail.smtp.port", "587");
						
						Session session = Session.getInstance(props, new Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(username, password);
							}
						});
						
						try {
							
							//메일
							Message message = new MimeMessage(session);
							
							message.setFrom(new InternetAddress(username));//보내는 사람
							
							message.setRecipients(Message.RecipientType.TO
										, InternetAddress.parse(email));
							
							message.setSubject("아이디 발송");
							
							String content = """
							
								<h2>아이디 발송</h2>
								
								<div style="border: 1px solid #CCC; width: 300px; height: 120px; border-radius: 5px; background-color: #EEE; display: flex; justify-content: center; align-items: center; margin: 20px 0;">
									회원님의 아이디는 <span style="font-weight: bold;">%s</span> 입니다.
								</div>
								
								<div>위의 인증 번호를 확인하세요.</div>
											
							""".formatted(id);
							
							message.setContent(content, "text/html; charset=UTF-8");
							
							Transport.send(message);
							
							System.out.println("이메일 전송 완료!!");
							
						} catch (Exception e) {
							System.out.println("MailSender.sendMail()");
							e.printStackTrace();
						}
		
	}

	/**
	 * 임시 비밀번호 또는 새 비밀번호를 이메일로 발송합니다.
	 * @param email       수신자 이메일 주소
	 * @param validNumber 발송할 새 비밀번호 (또는 인증번호)
	 */
	public void sendPwVerificationMail(String email, String validNumber) {
		// TODO Auto-generated method stub
		//- 보내는 사람(이름,이메일)
		//- 받는 사람(이름,이메일)
		//- 제목
		//- 내용
		
		String username = "dlq4444@gmail.com";
		String password = "ilfr fkek jpwe gsru";
		
		
		//HTTP, Hyper Text Transfer Protocol
				//SMTP, Simple Mail Transfer Protocol
				
				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", "smtp.gmail.com");
				props.put("mail.smtp.port", "587");
				
				Session session = Session.getInstance(props, new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
				
				try {
					
					//메일
					Message message = new MimeMessage(session);
					
					message.setFrom(new InternetAddress(username));//보내는 사람
					
					message.setRecipients(Message.RecipientType.TO
								, InternetAddress.parse(email));
					
					message.setSubject("새 비밀번호");
					
					String content = """
					
						<h2>새 비밀번호 발송</h2>
						
						<div style="border: 1px solid #CCC; width: 300px; height: 120px; border-radius: 5px; background-color: #EEE; display: flex; justify-content: center; align-items: center; margin: 20px 0;">
							회원님의 새 비밀번호 <span style="font-weight: bold;">%s</span> 입니다.
						</div>
						
									
					""".formatted(validNumber);
					
					message.setContent(content, "text/html; charset=UTF-8");
					
					Transport.send(message);
					
					System.out.println("이메일 전송 완료!!");
					
				} catch (Exception e) {
					System.out.println("MailSender.sendMail()");
					e.printStackTrace();
				}
		
	}
			
		}