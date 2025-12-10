package com.trip.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 모든 요청에 대해 문자 인코딩을 UTF-8로 설정하는 필터 클래스.
 * POST 방식의 요청 본문이나 URL 파라미터의 한글 깨짐을 방지합니다.
 * * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
//톰캣이 관리함
public class EncodingFilter implements Filter {
	
	/**
     * 필터가 웹 컨테이너에 의해 초기화될 때 호출됩니다.
     * (현재 구현에서는 특별한 초기화 작업 없음)
     * @param filterConfig 필터 설정 객체
     * @throws ServletException 서블릿 예외
     */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//System.out.println("필터 생성");
	
	}
	
	/**
     * 서블릿이 호출되기 전에 매번 요청을 가로채어 실행됩니다.
     * <p>
     * 1. 모든 요청(request)의 문자 인코딩을 "UTF-8"로 설정합니다. (POST 방식의 한글 깨짐 방지)
     * 2. 모든 응답(response)의 문자 인코딩을 "UTF-8"로 설정합니다. (JSP, Servlet 응답의 한글 깨짐 방지)
     * 3. 다음 필터 또는 대상 서블릿으로 요청을 전달합니다 (chain.doFilter).
     * </p>
     * * @param req ServletRequest 객체
     * @param resp ServletResponse 객체
     * @param chain FilterChain 객체
     * @throws IOException 입출력 예외
     * @throws ServletException 서블릿 예외
     */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		//System.out.println("필터 동작");
		//모든페이지에 공통된 코드는 여기서 구현하면 한번만 작성하면됨.
		//System.out.println(((HttpServletRequest)req).getRequestURI());
		
		// 1. 요청 인코딩 설정
		req.setCharacterEncoding("UTF-8");
		
		// 2. 응답 인코딩 설정 (JSP의 pageEncoding과 동일하게)
		resp.setCharacterEncoding("UTF-8");
		
		
		// 3. 다음 필터 또는 서블릿 호출
		// 이 코드가 없으면 요청이 대상 서블릿으로 전달되지 않음.
		chain.doFilter(req, resp);
		
	}

	/**
     * 필터가 웹 컨테이너에서 소멸될 때 호출됩니다.
     * (현재 구현에서는 특별한 리소스 정리 작업 없음)
     */
	@Override
	public void destroy() {
		//System.out.println("필터 소멸");
	}
	
	
}
