package cn.eby.book;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@MapperScan("cn.eby.book.mapper")
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableSwagger2
public class EbyBookApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EbyBookApplication.class, args);
    }
}
