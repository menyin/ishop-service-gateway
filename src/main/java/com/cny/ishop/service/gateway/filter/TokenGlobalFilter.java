package com.cny.ishop.service.gateway.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

/**
 * @author 蚂蚁课堂创始人-余胜军QQ644064779
 * @title: TokenGlobalFilter
 * @description: 每特教育独创第五期互联网架构课程
 * @date 2020/1/1621:30
 */
@Component
public class TokenGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //如何获取参数呢？
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (StringUtils.isEmpty(token)||!token.equals("123456")) {
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);//应该是未经授权
            String msg = "请传递token参数，值为123456，才能通过验证";
//            String msg = "test charset";
            DataBuffer buffer = null;
                buffer = response.bufferFactory().wrap(msg.getBytes());
            return response.writeWith(Mono.just(buffer));
        }
        // 直接转发到我们真实服务
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
