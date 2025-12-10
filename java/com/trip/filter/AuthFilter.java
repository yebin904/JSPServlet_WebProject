package com.trip.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 사용자 인증을 확인하는 필터 클래스.
 * web.xml에 매핑된 URL 패턴에 접근 시,
 * 로그인이 되어 있지 않으면 로그인 페이지로 리다이렉트시킵니다.
 * * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class AuthFilter implements Filter {

    /**
     * 필터가 웹 컨테이너에 의해 초기화될 때 호출됩니다.
     * @param filterConfig 필터 설정 객체
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화 작업 (필요 시)
    }

    /**
     * 요청을 필터링하여 사용자 인증(로그인) 상태를 확인합니다.
     * <p>
     * 1. 현재 세션을 확인하여 세션 내 "user" 속성이 있는지 검사합니다.
     * 2. (로그인 상태) "user" 속성이 존재하면, 요청을 다음 필터나 서블릿으로 전달합니다 (chain.doFilter).
     * 3. (비로그인 상태) "user" 속성이 없으면, 요청을 중단하고 '로그인이 필요한 서비스입니다.'라는
     * JavaScript 알림창을 띄운 후, 로그인 페이지(/user/login.do)로 리다이렉트시킵니다.
     * </p>
     * * @param request ServletRequest 객체
     * @param response ServletResponse 객체
     * @param chain FilterChain 객체 (다음 필터 또는 서블릿으로 요청을 전달)
     * @throws IOException 입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // false: 세션이 없으면 새로 만들지 않음

        // 세션이 존재하고, 세션에 "user"라는 이름의 속성이 저장되어 있는지 확인
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (loggedIn) {
            // 로그인 상태이면, 요청을 계속 진행
            chain.doFilter(request, response);
        } else {
            // 비로그인 상태이면, 로그인 페이지로 리다이렉트
            
            // 사용자에게 알림 메시지를 보여주기 위한 스크립트 추가
            // (주의: 이 방식은 API 요청이나 비동기 요청(AJAX)에는 적합하지 않을 수 있음)
            httpResponse.setContentType("text/html; charset=UTF-8");
            PrintWriter out = httpResponse.getWriter();
            out.println("<html><body>");
            out.println("<script type='text/javascript'>");
            out.println("alert('로그인이 필요한 서비스입니다.');");
            // 컨텍스트 경로를 포함하여 절대 경로로 리다이렉트
            out.println("location.href='" + httpRequest.getContextPath() + "/user/login.do';");
            out.println("</script>");
            out.println("</body></html>");
            out.close();
        }
    }

    /**
     * 필터가 웹 컨테이너에서 소멸될 때 호출됩니다.
     */
    @Override
    public void destroy() {
        // 필터 소멸 시 정리 작업 (필요 시)
    }
}
