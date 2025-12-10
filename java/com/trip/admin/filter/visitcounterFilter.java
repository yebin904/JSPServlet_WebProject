package com.trip.admin.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 웹 사이트 방문자 수를 집계하는 필터
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebFilter("/*")
public class visitcounterFilter implements Filter {

    /**
     * 필터 초기화 시 총 방문자 수와 회원 방문자 수를 0으로 설정합니다.
     * @param config 필터 설정 객체
     */
    @Override
    public void init(FilterConfig config) {
        ServletContext context = config.getServletContext();
        context.setAttribute("totalVisits", new AtomicInteger(0));
        context.setAttribute("memberVisits", new AtomicInteger(0));
    }

    /**
     * 새로운 세션의 첫 요청일 경우 방문자 수를 증가시킵니다.
     * 리소스 파일(css, js, 이미지 등) 요청은 집계에서 제외합니다.
     * @param request ServletRequest 객체
     * @param response ServletResponse 객체
     * @param chain FilterChain 객체
     * @throws IOException 입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession();

        // ★★★★★ 핵심 수정: 세션에 방문 기록이 있는지 확인합니다 ★★★★★
        if (session.getAttribute("hasVisited") == null) {
            
            // 리소스 파일(css, js, 이미지 등) 요청은 카운트에서 제외
            String uri = httpReq.getRequestURI();
            if (!uri.matches(".*(\\.(css|js|png|jpg|gif|ico))$")) {
                
                // 이 세션은 이제 방문한 것으로 기록하여 중복 집계를 방지합니다.
                session.setAttribute("hasVisited", true);

                ServletContext context = request.getServletContext();
                
                // 총 방문자 수 증가
                ((AtomicInteger) context.getAttribute("totalVisits")).incrementAndGet();
                
                // 현재 로그인 상태라면 회원 방문자 수도 증가
                if (session.getAttribute("user") != null) {
                    ((AtomicInteger) context.getAttribute("memberVisits")).incrementAndGet();
                }
            }
        }
        
        chain.doFilter(request, response);
    }

    /**
     * 필터가 소멸될 때 호출됩니다.
     */
    @Override
    public void destroy() {}
}