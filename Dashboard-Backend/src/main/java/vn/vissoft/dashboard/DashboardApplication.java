package vn.vissoft.dashboard;

import java.io.IOException;
import java.util.Locale;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import vn.vissoft.dashboard.config.CustomLocaleResolver;
import vn.vissoft.dashboard.config.CustomPasswordEncoder;
import vn.vissoft.dashboard.config.CustomerStringEncryptor;


@SpringBootApplication
@EnableEncryptableProperties
@EnableJpaRepositories
public class DashboardApplication{

	@Value("${jasypt.encryptor.password}")
	private String passwordSecretKey;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DashboardApplication.class, args);
		LogManager.getLogger(DashboardApplication.class).info("Test log4j");
	}

	@Bean
	public LocaleResolver localeResolver(){
		CustomLocaleResolver localeResolver = new CustomLocaleResolver();
		LocaleContextHolder.setDefaultLocale(new Locale("vi"));
		localeResolver.setDefaultLocale(new Locale("vi"));
		return  localeResolver;
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
		rs.setBasename("messages");
		rs.setDefaultEncoding("UTF-8");
		rs.setUseCodeAsDefaultMessage(true);
		return rs;
	}

	@Bean
	public PasswordEncoder encoder(){
		return new CustomPasswordEncoder(passwordSecretKey);
	}

	@Bean
	public StringEncryptor jasyptStringEncryptor() {
		return new CustomerStringEncryptor(passwordSecretKey);
	}
}
