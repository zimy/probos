package me.zimy.probos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Base class
 *
 * @author Dmitriy &lt;Zimy&gt; Yakovlev
 * @since 12/1/14.
 */
@EnableAsync
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ProbOS {
    public static void main(String[] args) {
        SpringApplication.run(ProbOS.class, args);
    }

    /*@Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }*/
}
