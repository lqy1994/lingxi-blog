package cn.edu.sdu.wh.lqy.lingxi.blog.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.UnknownHostException;

@Configuration
public class BeanConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

//    @Bean
//    @ConditionalOnMissingBean(name = "lxRedisTemplate")
//    public <K,V> RedisTemplate<K, V> lxRedisTemplate(
//            RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//        RedisTemplate<K, V> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        return template;
//    }
}
