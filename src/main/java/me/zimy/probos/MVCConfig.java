package me.zimy.probos;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;

/**
 * Ignore this, do not touch it at all. That component allows to serve static content.
 *
 * @author Dmitriy &lt;Zimy&gt; Yakovlev
 * @since 12/1/14.
 */

@EnableWebMvc
@Configuration
public class MVCConfig extends WebMvcAutoConfigurationAdapter {
}
