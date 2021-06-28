package com.opensource.msgui.commons.response;

import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author gui
 */
@Configuration
public class Swagger2Configuration {
  @Bean
  public Docket createRestApi() {
    return (new Docket(DocumentationType.SWAGGER_2))
      .apiInfo(apiInfo())
      .select()
      .apis(RequestHandlerSelectors.basePackage("com.opensource.msgui"))
      .paths(PathSelectors.any())
      .build()
      .securitySchemes(securitySchemes())
      .securityContexts(securityContexts());
  }
  
  private ApiInfo apiInfo() {
    return (new ApiInfoBuilder())
      .title("msgui")
      .description("msgui")
      .version("1.0.0")
      .build();
  }
  
  private List<ApiKey> securitySchemes() {
    return Lists.newArrayList(new ApiKey("Authorization", "Authorization", "header"));
  }
  
  private List<SecurityContext> securityContexts() {
    return Lists.newArrayList(SecurityContext.builder()
          .securityReferences(defaultAuth())
          .build());
  }
  
  List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Lists.newArrayList(new SecurityReference("Authorization", authorizationScopes));
  }
}
