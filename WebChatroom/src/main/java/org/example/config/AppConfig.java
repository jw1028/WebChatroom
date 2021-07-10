package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.interceptor.LoginInterceptor;
import org.example.config.web.RequestResponseBodyMethodProcessorWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

//定义SpringMVC的启动配置类
@Configuration
public class AppConfig implements WebMvcConfigurer, InitializingBean {

    @Autowired
    private ObjectMapper objectMapper;

    @Resource
    private RequestMappingHandlerAdapter adapter;

    /**
     * 注入一个ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
     */

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {

        return new ServerEndpointExporter();
    }

    //之前以@ControllerAdvice+实现ResponseBodyAdvice接口，完成统一处理返回数据包装：无法解决返回值为null需要包装
    //改用现在这种方式，可以解决返回null包装为自定义类型
    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList(returnValueHandlers);
        for(int i=0; i<handlers.size(); i++){
            HandlerMethodReturnValueHandler handler = handlers.get(i);
            if(handler instanceof RequestResponseBodyMethodProcessor){
                handlers.set(i, new RequestResponseBodyMethodProcessorWrapper(handler));
            }
        }
        adapter.setReturnValueHandlers(handlers);
    }

//    配置Controller中请求映射方法路径匹配规则
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //设置路径前缀的规则，以第二个参数的返回值作为请求映射方法是否添加前缀
        configurer.addPathPrefix("api", c->true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        前端处理逻辑和后端处理逻辑是否一样？
//        前端：敏感资源在拦截器中处理为：没登录跳转首页
//        后端：敏感资源在拦截器中处理为：返回json，401状态码
//        localhost:8080/xxx
        registry.addInterceptor(new LoginInterceptor(objectMapper))
                // *代表路径下一级，**代表路径的所有子级
                //所有后端非/user/开头，只有指定的两个前端资源执行拦截器的逻辑
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/login");

    }

}

