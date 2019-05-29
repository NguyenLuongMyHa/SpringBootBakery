package com.bakery.utility;


import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.bakery.entity.User;

@Component
public class MailConstructor {
	@Autowired
	private Environment env;
	
	public SimpleMailMessage constructResetTokenEmail(
			String contextPath, Locale locale, String token, User user, String password
			) {
		
		String url = contextPath + "/newUser?token="+token;
		String message = "\nNhấn vào đường link sau để xác nhận email của bạn và chỉnh sửa thông tin cá nhân. Mật khẩu của bạn là:  \n"+password;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject("MyHa's Bakery - Welcome New User");
		email.setText(url+message);
		email.setFrom(env.getProperty("support.email"));
		return email;
		
	}
}

