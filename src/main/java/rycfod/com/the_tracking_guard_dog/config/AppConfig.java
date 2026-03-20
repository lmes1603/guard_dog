package rycfod.com.the_tracking_guard_dog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.h2.server.web.JakartaWebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Wires infrastructure beans: a pre-configured {@link RestTemplate} whose
 * every request automatically carries the ShipEngine API-Key header.
 *
 * <p>Note: {@code RestTemplateBuilder} was removed in Spring Boot 4.x.
 * Timeouts are set via {@link SimpleClientHttpRequestFactory} and the
 * API-Key header is injected through a {@code ClientHttpRequestInterceptor}.</p>
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final ShipEngineProperties props;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * Registers the H2 web console as a servlet at {@code /h2-console/*}.
     *
     * <p>Spring Boot's {@code H2ConsoleAutoConfiguration} does not register the
     * servlet when the app runs as a WAR on an external Tomcat. This explicit
     * bean ensures the console is always available in development.</p>
     */
    @Bean
    public ServletRegistrationBean<JakartaWebServlet> h2ConsoleServlet() {
        var bean = new ServletRegistrationBean<>(new JakartaWebServlet(), "/h2-console", "/h2-console/*");
        bean.addInitParameter("webAllowOthers", "true");
        bean.setLoadOnStartup(1);
        return bean;
    }

    /**
     * Plain {@link RestTemplate} for Twilio calls.
     * No interceptors — each request sets its own Basic Auth header.
     */
    @Bean("twilioRestTemplate")
    public RestTemplate twilioRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5_000);
        factory.setReadTimeout(10_000);
        return new RestTemplate(factory);
    }

    @Bean("shipEngineRestTemplate")
    public RestTemplate shipEngineRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(props.getConnectTimeoutMs());
        factory.setReadTimeout(props.getReadTimeoutMs());

        RestTemplate restTemplate = new RestTemplate(factory);

        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().set("API-Key", props.getApiKey());
            request.getHeaders().set("Content-Type", "application/json");
            return execution.execute(request, body);
        }));

        return restTemplate;
    }
}
