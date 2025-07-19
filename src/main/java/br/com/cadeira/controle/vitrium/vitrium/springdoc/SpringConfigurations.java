package br.com.cadeira.controle.vitrium.vitrium.springdoc;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfigurations {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Controle de Cadeira de Rodas")
                        .description("API Rest desenvolvida para fazer o controle de cadeira de rodas do condomínio Vitrium")
                        .contact(new Contact()
                                .name("Gabriel Rodrigues")
                                .email("gabriel.ro.ol@hotmail.com"))
                        .version("1.0"));
    }
}
