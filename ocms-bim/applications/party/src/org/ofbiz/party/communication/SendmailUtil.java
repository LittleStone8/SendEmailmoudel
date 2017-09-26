package org.ofbiz.party.communication;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
public class SendmailUtil {
    
//  // ���÷�����
//  private static String KEY_SMTP = "mail.smtp.host";
//  private static String VALUE_SMTP = "smtp.qq.com";
//  // ��������֤
//  private static String KEY_PROPS = "mail.smtp.auth";
//  private static boolean VALUE_PROPS = true;
//  // �������û���������
//  private String SEND_USER = "262627160@qq.com";
//  private String SEND_UNAME = "262627160";
//  private String SEND_PWD = "vmmfscbowzmubjec";
//  // �����Ự
//  private MimeMessage message;
//  private Session s;
//
//  /*
//   * ��ʼ������
//   */
//  public SendmailUtil() {
//      Properties props = System.getProperties();
//      props.setProperty(KEY_SMTP, VALUE_SMTP);
//      props.put(KEY_PROPS, "true");
//      //props.put("mail.smtp.auth", "true");
//      s =  Session.getDefaultInstance(props, new Authenticator(){
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(SEND_UNAME, SEND_PWD);
//            }});
//      s.setDebug(true);
//      message = new MimeMessage(s);
//  }
//
//  /**
//   * �����ʼ�
//   * 
//   * @param headName
//   *            �ʼ�ͷ�ļ���
//   * @param sendHtml
//   *            �ʼ�����
//   * @param receiveUser
//   *            �ռ��˵�ַ
//   */
//  public void doSendHtmlEmail(String headName, String sendHtml,
//          String receiveUser) {
//      try {
//          // ������
//          InternetAddress from = new InternetAddress(SEND_USER);
//          message.setFrom(from);
//          // �ռ���
//          InternetAddress to = new InternetAddress(receiveUser);
//          message.setRecipient(Message.RecipientType.TO, to);
//          // �ʼ�����
//          message.setSubject(headName);
//          String content = sendHtml.toString();
//          // �ʼ�����,Ҳ����ʹ���ı�"text/plain"
//          message.setContent(content, "text/html;charset=GBK");
//          message.saveChanges();
//          Transport transport = s.getTransport("smtp");
//          // smtp��֤���������������ʼ��������û�������
//          transport.connect(VALUE_SMTP, SEND_UNAME, SEND_PWD);
//          // ����
//          transport.sendMessage(message, message.getAllRecipients());
//          transport.close();
//          System.out.println("send success!");
//      } catch (AddressException e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//      } catch (MessagingException e) {
//          e.printStackTrace();
//      }
//  }
//
//  public static void main(String[] args) {
//      SendmailUtil se = new SendmailUtil();
//      se.doSendHtmlEmail("�ʼ�ͷ�ļ���", "�ʼ�����", "625601781@qq.com");
//  }
	
	
	public static void main(String[] args) throws MessagingException {
		Properties prop=new Properties();
		prop.put("mail.host","smtp.163.com" );
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", true);
		//使用java发送邮件5步骤
		//1.创建sesssion
		Session session=Session.getInstance(prop);
		//开启session的调试模式，可以查看当前邮件发送状态
		session.setDebug(true);


		//2.通过session获取Transport对象（发送邮件的核心API）
		Transport ts=session.getTransport();
		//3.通过邮件用户名密码链接
		ts.connect("woshiyuanzhenli@163.com", "aillp775825");


		//4.创建邮件

		Message msg=createSimpleMail(session);


		//5.发送电子邮件

		ts.sendMessage(msg, msg.getAllRecipients());
		
		ts.close();
		}
	
	
	public static MimeMessage createSimpleMail(Session session) throws AddressException,MessagingException{
		//创建邮件对象
		MimeMessage mm=new MimeMessage(session);
		//设置发件人
		mm.setFrom(new InternetAddress("woshiyuanzhenli@163.com"));
		//设置收件人
		mm.setRecipient(Message.RecipientType.TO, new InternetAddress("zhenli.yuan@yiwill.com"));
		//设置抄送人
//		mm.setRecipient(Message.RecipientType.CC, new InternetAddress("用户名@163.com"));

		mm.setSubject("5");
      StringBuffer theMessage = new StringBuffer();
      theMessage.append("<h2><font color=red>这倒霉孩子</font></h2>");
      theMessage.append("<hr>");
      theMessage.append("<i>年年失望年年望</i>");
      theMessage.append("<table border='1'><tr><td>aaa</td><td>bbb</td></tr><tr><td>ccc</td><td>ddd</td></tr></table>");
		
		mm.setContent(theMessage.toString(), "text/html;charset=gbk");

		return mm;

		}
	
	
	
	
	
	
	
	
	
	
	
	
}