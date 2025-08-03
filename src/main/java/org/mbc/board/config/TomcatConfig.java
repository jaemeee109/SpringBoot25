package org.mbc.board.config;

import org.apache.catalina.Context;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addContextCustomizers((Context context) -> {
            context.setAllowCasualMultipartParsing(true);  // 파일 업로드 제한 완화용
            context.getServletContext().setAttribute("org.apache.tomcat.util.http.fileupload.FileUploadBase.fileCountMax", 20); // 20개 제한 설정
        });
        return factory;
    }
}
