package nl.lunatech.movie.imgdb.config;

/**
 * Swagger Web UI Configuration
 *
 * @author alikhandani
 * @created 27/05/2020
 * @project imgdb
 */

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${info.app.name}")
    private String serviceName;
    @Value("${info.app.desc}")
    private String serviceDesc;
    @Value("${info.app.contact.email}")
    private String email;
    @Value("${info.app.contact.url}")
    private String url;
    @Value("${info.app.contact.name}")
    private String contactName;
    @Value("${info.app.version}")
    private String version;
    @Value("${info.app.license}")
    private String license;

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(serviceName)
                        .description(serviceDesc)
                        .version(version)
                        .license(new License().name(license).url("http://lobox.org"))
                        .termsOfService(url + "/terms"))
                .externalDocs(new ExternalDocumentation()
                        .description("Lobox IMGDB")
                        .url("https://lobox.org/docs"));
    }

}
