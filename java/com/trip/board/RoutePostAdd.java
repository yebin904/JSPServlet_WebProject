package com.trip.board;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.trip.board.model.RoutePostDAO;
import com.trip.board.model.RoutePostDTO;
import com.trip.member.model.UserDTO;



/**
 * 경로 게시물 추가를 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/routepost/add.do")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,   // 1MB
    maxFileSize = 1024 * 1024 * 10,        // 10MB
    maxRequestSize = 1024 * 1024 * 50      // 50MB
)
public class RoutePostAdd extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * GET 요청을 처리하여 새 경로 게시물 작성 폼 페이지로 이동합니다.
     * 사용자가 로그인되어 있는지 세션을 확인합니다.
     * 로그인 상태가 아니면 로그인 페이지(`/user/login.do`)로 리다이렉트합니다.
     * 로그인 상태이면 `/WEB-INF/views/board/routepost/write.jsp` 페이지로 포워드합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     * @see /WEB-INF/views/board/routepost/write.jsp
     */
    // ✅ GET: 글쓰기 폼 열기
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 로그인 여부 확인
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");

        if (user == null) {
            // 로그인 안 된 경우 → 로그인 페이지로 리다이렉트
            resp.sendRedirect(req.getContextPath() + "/user/login.do");
            return;
        }

        // JSP 페이지 포워드
        req.getRequestDispatcher("/WEB-INF/views/board/routepost/write.jsp").forward(req, resp);
    }

    /**
     * POST 요청을 처리하여 폼에서 전송된 데이터로 새 경로 게시물을 등록합니다.
     * 1. 세션에서 로그인한 사용자 정보를 확인합니다. (로그인 안됐으면 로그인 페이지로 리다이렉트)
     * 2. 폼 파라미터(title, content, satisfaction)를 수집합니다.
     * 3. 텍스트 데이터를 DTO에 담아 DB에 저장(insert)합니다.
     * 4. DB 저장 성공 시, 함께 전송된 이미지 파일("images")들을 서버의
     * `/uploads/routepost` 경로에 저장(part.write)합니다.
     * 5. 저장된 파일 정보를 DB에 추가(insertImage)합니다.
     * 6. 모든 처리가 성공하면 게시물 목록 페이지(`/routepost/list.do`)로 리다이렉트합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (multipart/form-data)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     */
    // ✅ POST: 게시글 등록 처리
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        RoutePostDAO dao = new RoutePostDAO();

        try {
            // 1️⃣ 세션 유저 확인
            UserDTO user = (UserDTO) req.getSession().getAttribute("user");

            if (user == null) {
                // 로그인 안 된 상태에서 접근 → 차단
                resp.sendRedirect(req.getContextPath() + "/user/login.do");
                return;
            }

            int userId = (int)user.getUserId(); // ✅ 실제 로그인 유저 ID 사용

            // 2️⃣파라미터 수집
            String title = req.getParameter("title");
            String content = req.getParameter("content");
            String satisfaction = req.getParameter("satisfaction");

            double satisfactionValue = 0.0;
            try {
                satisfactionValue = Double.parseDouble(satisfaction);
            } catch (NumberFormatException e) {
                satisfactionValue = 0.0;
            }

            // 3️⃣ DTO 세팅
            RoutePostDTO dto = new RoutePostDTO();
            dto.setUserId(userId);
            dto.setRoutepostTitle(title);
            dto.setRoutepostContent(content);
            dto.setRoutepostSatisfaction(satisfactionValue);
            dto.setRoutepostStatus("A");
            dto.setRoutepostViewCount(0);
            dto.setRoutepostReportCount(0);

            // 4️⃣ DB 저장
            int result = dao.insert(dto);

            // 5️⃣ 파일 업로드
            if (result > 0) {
                 // 실제 톰캣 실행경로 기준
                String uploadPath = getServletContext().getRealPath("/uploads/routepost");

                System.out.println("📂 업로드 경로: " + uploadPath);

                File dir = new File(uploadPath);
                if (!dir.exists()) dir.mkdirs();

                int seq = 1;
                for (Part part : req.getParts()) {
                    if ("images".equals(part.getName()) && part.getSize() > 0) {
                        String fileName = new File(part.getSubmittedFileName()).getName();
                        String savedName = UUID.randomUUID().toString() + "_" + fileName;

                        // 실제 파일 저장
                        part.write(uploadPath + File.separator + savedName);

                        // DB에 이미지 정보 등록
                        dao.insertImage(dto.getRoutepostId(), seq++, savedName);
                    }
                }

                // 6️⃣ 성공 시 → 목록으로 이동
                resp.sendRedirect(req.getContextPath() + "/routepost/list.do");

            } else {
               // 실패 시 → 글쓰기 폼으로 되돌리기
                req.setAttribute("error","글 작성에 실패했습니다. 다시 시도해주세요.");
                req.getRequestDispatcher("/WEB-INF/views/board/routepost/write.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 처리 중 오류 발생");
        } finally {
            dao.close();
        }
    }
}
