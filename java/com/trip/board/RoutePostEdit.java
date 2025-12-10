package com.trip.board;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.trip.board.model.RoutePostDAO;
import com.trip.board.model.RoutePostDTO;
import com.trip.board.model.RoutePostImageDTO;

/**
 * 경로 게시물 수정을 처리하는 서블릿
 * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
@WebServlet("/routepost/edit.do")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  // 1MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class RoutePostEdit extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * GET 요청을 처리하여 기존 경로 게시물을 수정하기 위한 폼 페이지로 이동합니다.
     * 게시물 ID(id)에 해당하는 기존 게시글 정보(dto)와 이미지 목록(imageList)을 조회(get, getImages)하여
     * request에 저장한 후, `/WEB-INF/views/board/routepost/edit.jsp` 페이지로 포워드합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (id: 게시물 ID)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     * @see /WEB-INF/views/board/routepost/edit.jsp
     */
    // ✅ GET: 수정 페이지 이동
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int postId = Integer.parseInt(req.getParameter("id"));
        RoutePostDAO dao = new RoutePostDAO();

        RoutePostDTO dto = dao.get(postId);
        List<RoutePostImageDTO> imageList = dao.getImages(postId);

        req.setAttribute("dto", dto);
        req.setAttribute("imageList", imageList);

        RequestDispatcher dispatcher =
                req.getRequestDispatcher("/WEB-INF/views/board/routepost/edit.jsp");
        dispatcher.forward(req, resp);

        dao.close();
    }

    /**
     * POST 요청을 처리하여 폼에서 전송된 데이터로 경로 게시물을 수정합니다.
     * 텍스트 데이터(제목, 내용, 만족도)를 DB에 업데이트(update)합니다.
     * 새 이미지 파일("images")이 전송된 경우, 기존 이미지를 DB와 서버에서 삭제(deleteImages)하고
     * 새 이미지 파일을 서버에 업로드 및 DB에 저장(insertImage)합니다.
     * 성공 시, 해당 게시물의 상세 보기 페이지(`/routepost/view.do`)로 리다이렉트합니다.
     *
     * @param req  클라이언트의 요청 정보를 담은 HttpServletRequest 객체 (multipart/form-data)
     * @param resp 클라이언트에게 응답을 보내기 위한 HttpServletResponse 객체
     * @throws ServletException 서블릿 처리 중 오류가 발생한 경우
     * @throws IOException      입출력 오류가 발생한 경우
     */
    // ✅ POST: 수정 처리
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        int postId = Integer.parseInt(req.getParameter("id"));

        RoutePostDAO dao = new RoutePostDAO();

        try {
            String title = req.getParameter("title");
            String content = req.getParameter("content");
            double satisfaction = Double.parseDouble(req.getParameter("satisfaction"));

            RoutePostDTO dto = new RoutePostDTO();
            dto.setRoutepostId(postId);
            dto.setRoutepostTitle(title);
            dto.setRoutepostContent(content);
            dto.setRoutepostSatisfaction(satisfaction);

            int result = dao.update(dto);

            // ✅ 새 이미지가 있을 때만 교체
            Collection<Part> parts = req.getParts();
            boolean hasNewImages = parts.stream()
                    .anyMatch(p -> p.getName().equals("images") && p.getSize() > 0);

            if (hasNewImages) {
                dao.deleteImages(postId);

                String uploadPath = getServletContext().getRealPath("/uploads/routepost");
                File dir = new File(uploadPath);
                if (!dir.exists()) dir.mkdirs();

                int seq = 1;
                for (Part part : parts) {
                    if ("images".equals(part.getName()) && part.getSize() > 0) {
                        String fileName = new File(part.getSubmittedFileName()).getName();
                        String savedName = UUID.randomUUID().toString() + "_" + fileName;
                        part.write(uploadPath + File.separator + savedName);
                        dao.insertImage(postId, seq++, savedName);
                    }
                }
            }

            if (result > 0) {
                resp.sendRedirect(req.getContextPath() + "/routepost/view.do?id=" + postId);
            } else {
                req.setAttribute("error", "게시글 수정 실패");
                req.getRequestDispatcher("/WEB-INF/views/board/routepost/edit.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "게시글 수정 중 오류 발생");
        } finally {
            dao.close();
        }
    }
}
