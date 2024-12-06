package ueh.config;


import ueh.model.HtmlData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class QueueConfig {
    @Bean
    public BlockingQueue<HtmlData> htmlQueue() {
        return new LinkedBlockingQueue<>();
    }
}
