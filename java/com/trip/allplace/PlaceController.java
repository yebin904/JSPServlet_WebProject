package com.trip.allplace;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.trip.allplace.model.PlaceDTO;
import com.trip.allplace.model.PlaceSearchCriteria;

@WebServlet("/allplace/map.do")
public class PlaceController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. 로그인 상태 확인
        HttpSession session = req.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("auth") != null);
        req.setAttribute("isLoggedIn", isLoggedIn);

        // 2. Service를 통해 '모든' 장소 목록을 가져옵니다.
        PlaceService service = new PlaceService();
        List<PlaceDTO> allPlaceList = service.getPlaceList(new PlaceSearchCriteria());
        
        // 3. DTO List를 JSON 형식으로 변환합니다.
        JSONArray jsonArray = new JSONArray();
        for (PlaceDTO p : allPlaceList) {
            JSONObject placeJson = new JSONObject();
            placeJson.put("id", p.getPlace_id());
            placeJson.put("name", p.getPlace_name());
            placeJson.put("address", p.getPlace_address());
            placeJson.put("lat", p.getPlace_lat());
            placeJson.put("lng", p.getPlace_lng());
            placeJson.put("imageUrl", p.getPlace_main_image_url());
            placeJson.put("typeId", p.getPlace_type_id());
            
            // 기타 임의 데이터
            placeJson.put("popularity", (int) (Math.random() * 500));
            placeJson.put("reviews", (int) (Math.random() * 200));
            placeJson.put("rating", String.format("%.1f", (Math.random() * 2 + 3)));
            placeJson.put("price", (int) (Math.random() * 3) + 1);
            
            jsonArray.add(placeJson);
        }
        
        // 4. JSON 문자열을 request에 저장합니다.
        req.setAttribute("allPlaceListJson", jsonArray.toJSONString());

        // 5. 화면으로 포워딩
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/allplace/map.jsp");
        dispatcher.forward(req, resp);
    }
    
}


