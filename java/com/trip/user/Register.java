package com.trip.user;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.trip.member.model.UserDAO;
import com.trip.member.model.UserDTO;



/**
 * 사용자 회원가입을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet(value = "/user/register.do")
public class Register extends HttpServlet {

	/**
	 * HTTP GET 요청을 처리합니다.
	 * 회원가입 폼 페이지(`/WEB-INF/views/user/register.jsp`)로 포워딩합니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/register.jsp");
		dispatcher.forward(req, resp);
	}
	
	
	/**
	 * HTTP POST 요청을 처리합니다. (회원가입 폼 제출)
	 * `MultipartRequest`를 사용하여 폼 데이터를 처리합니다. (프로필 사진 등 파일 업로드를 겸할 수 있음)
	 * 1. 폼에서 전송된 모든 파라미터(ID, PW, 이름, 이메일, 주소, 성별, 키, 몸무게 등)를 받습니다.
	 * 2. `UserDTO`에 데이터를 담습니다.
	 * 3. ID 중복 검사 (`dao.idCheck`)를 수행합니다.
	 * 4. 이메일 중복 검사 (`dao.emailCheck`)를 수행합니다.
	 * 5. 중복 검사에 실패하면, 알림창을 띄우고 회원가입 페이지로 다시 리디렉션합니다.
	 * 6. 모든 검증을 통과하면 `UserDAO`를 통해 회원 정보를 등록합니다 (`dao.register`).
	 * 7. 회원가입 성공 시: 메인 페이지(`/trip/main.do`)로 리디렉션합니다.
	 * 8. 회원가입 실패 시: "failed" 알림창을 띄우고 이전 페이지로 돌아갑니다.
	 *
	 * @param req  클라이언트가 서블릿에 보낸 HttpServletRequest 객체
	 * @param resp 서블릿이 클라이언트에 보내는 HttpServletResponse 객체
	 * @throws ServletException 서블릿 처리 중 예외 발생 시
	 * @throws IOException      요청 또는 응답 처리 중 I/O 예외 발생 시
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
try {
			
			MultipartRequest multi = new MultipartRequest(
							req,
							req.getServletContext().getRealPath("/asset/pic"),
							1024 * 1024 * 10,
							"UTF-8",
							new DefaultFileRenamePolicy() 
						);

			System.out.println(req.getServletContext().getRealPath("/asset/pic"));
			
			String id = multi.getParameter("id");
			String pw = multi.getParameter("pw");
			String ssn = multi.getParameter("ssn");
			String name = multi.getParameter("name");
			String email = multi.getParameter("email");
			String phoneNumber = multi.getParameter("phoneNumber");
			String nickName = multi.getParameter("nickName");
			String address = multi.getParameter("address");
			String gender = multi.getParameter("gender");
			String height = multi.getParameter("height");
			String weight = multi.getParameter("weight");
			String healthGoals = multi.getParameter("healthGoals");
			
			UserDAO dao = new UserDAO();
			
			UserDTO dto = new UserDTO();
			
			dto.setId(id);
			dto.setPw(pw);
			dto.setSsn(ssn);
			dto.setName(name);
			dto.setEmail(email);
			dto.setPhoneNumber(phoneNumber);
			dto.setNickName(nickName);
			dto.setAddress(address);
			dto.setGender(gender);
			dto.setHeight(height);
			dto.setWeight(weight);
			dto.setHealthGoals(healthGoals);
			
			

			if (id == null || id.trim().equals("")) {
			    resp.setContentType("text/html; charset=UTF-8");
			    resp.getWriter().print("<script>alert('아이디를 입력해주세요.'); history.back();</script>");
			    resp.getWriter().close();
			    return; // 더 이상 진행하지 않고 메소드 종료
			}
			
			int idCheck = dao.idCheck(id);
			
			
			if (idCheck == 1) {
			    // 
			    // 목적지 URL을 컨텍스트 경로를 포함하여 동적으로 생성합니다.
			    String contextPath = req.getContextPath();
			    String redirectUrl = contextPath + "/user/register.do"; // 회원가입 페이지 주소

			    resp.setContentType("text/html; charset=UTF-8");
			    
			    // 경고창을 띄운 후, 지정된 URL로 페이지를 이동시키는 스크립트
			    String script = "<script>"
			                  + "    alert('이미 사용 중인 아이디입니다.');"
			                  + "    location.href = '" + redirectUrl + "';"
			                  + "</script>";
			    
			    resp.getWriter().print(script);
			    resp.getWriter().close();
			    return;
			}
			
			
	
			 
			
			
			if (email == null || email.trim().equals("")) {
			    resp.setContentType("text/html; charset=UTF-8");
			    resp.getWriter().print("<script>alert('이메일을 입력해주세요.'); history.back();</script>");
			    resp.getWriter().close();
			    return; // 더 이상 진행하지 않고 메소드 종료
			}

			int emailCheck = dao.emailCheck(email);

			if (emailCheck == 1) {
			    // 
			    // 목적지 URL을 컨텍스트 경로를 포함하여 동적으로 생성합니다.
			    String contextPath = req.getContextPath();
			    String redirectUrl = contextPath + "/user/register.do"; // 회원가입 페이지 주소

			    resp.setContentType("text/html; charset=UTF-8");
			    
			    // 경고창을 띄운 후, 지정된 URL로 페이지를 이동시키는 스크립트
			    String script = "<script>"
			                  + "    alert('이미 사용 중인 이메일입니다.');"
			                  + "    location.href = '" + redirectUrl + "';"
			                  + "</script>";
			    
			    resp.getWriter().print(script);
			    resp.getWriter().close();
			    return;
			}
			
				int result = dao.register(dto);
				
				
				if (result > 0) {
					resp.sendRedirect("/trip/main.do");
				} else {
					//resp.setContentType("text/html");
					//resp.setCharacterEncoding("UTF-8");
					resp.getWriter().print("<script>alert('failed');history.back();</script>");
					resp.getWriter().close();
				}			
			
			
			

		} catch (Exception e) {
			System.out.println("Register.doPost()");
			e.printStackTrace();
		}
		
	}

}