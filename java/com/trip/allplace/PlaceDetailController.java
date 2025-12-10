package com.trip.allplace;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.allplace.model.PlaceDTO;
import com.trip.allplace.model.TouristSpotDTO;

@WebServlet("/allplace/detail.do")
public class PlaceDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        PlaceService service = new PlaceService();

        // 1. 로그인 상태 확인
        HttpSession session = req.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("auth") != null);
        req.setAttribute("isLoggedIn", isLoggedIn);

        try {
            // 2. place_id 파라미터 가져오기
            int placeId = Integer.parseInt(req.getParameter("place_id"));

            // 3. 기본 정보와 상세 정보를 모두 가져오기
            Map<String, Object> details = service.getPlaceDetails(placeId);
            PlaceDTO place = (PlaceDTO) details.get("place");
            TouristSpotDTO touristSpot = (TouristSpotDTO) details.get("touristSpot");
            
            if (place == null) {
                // 장소 정보가 없을 경우 에러 페이지나 목록으로 리다이렉트
                resp.sendRedirect(req.getContextPath() + "/allplace/map.do");
                return;
            }

            // 4. 키워드 기반 연관 장소 목록 가져오기
            List<PlaceDTO> relatedPlaces = service.getRelatedPlacesByKeyword(placeId);

            // 5. request에 데이터 저장
            req.setAttribute("place", place);
            req.setAttribute("touristSpot", touristSpot);
            req.setAttribute("relatedPlaces", relatedPlaces);

            // 6. 상세 페이지로 포워딩
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/allplace/detail.jsp");
            dispatcher.forward(req, resp);

        } catch (NumberFormatException e) {
            // place_id가 숫자가 아닌 경우 등 예외 처리
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Place ID");
        }
    }
}
