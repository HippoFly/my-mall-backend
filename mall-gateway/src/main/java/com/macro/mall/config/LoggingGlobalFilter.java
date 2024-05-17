package com.macro.mall.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // 打印请求地址
        logger.info("Request Path: {}", request.getPath());
        logger.info("Request Address: {}", request.getRemoteAddress());
        // 打印请求方法
        logger.info("Request Method: {}", request.getMethod());
        // 如果需要打印请求头或请求体，请注意这可能涉及大量数据和潜在的安全风险
        // logger.info("Request Headers: {}", request.getHeaders());
        // logger.info("Request Body: {}", ...);

        // 继续执行过滤器链
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // 打印响应信息（可选）
            // 注意：此处获取响应信息较为复杂，因为响应是在下游服务响应之后才有的
            // 你可能需要自定义一个响应处理器或者使用其他方式来捕获响应内容
            logger.info("Response completed for request: {}", request.getPath());
        }));
    }

    @Override
    public int getOrder() {
        // 设置过滤器的执行顺序，数值越小优先级越高
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}