package com.kang.webflux.filter;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        System.out.println("필터 실행 됨");

        HttpServletResponse servletResponse = (HttpServletResponse) response;
        // stream이기 때문에 flush할 떄, 하나씩 데이터를 보내 준다.
        servletResponse.setContentType("text/event-stream; charset=utf-8");

        PrintWriter out = servletResponse.getWriter();

        for (int i = 0; i < 5; i++) {
            out.println("응답" + i);
            out.flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        while (true) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        chain.doFilter(request, servletResponse);
    }

    
}