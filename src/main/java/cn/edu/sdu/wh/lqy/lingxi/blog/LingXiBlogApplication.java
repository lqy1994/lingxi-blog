package cn.edu.sdu.wh.lqy.lingxi.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
public class LingXiBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(LingXiBlogApplication.class, args);
	}
}
