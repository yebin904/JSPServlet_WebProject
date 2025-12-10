package com.trip.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trip.main.model.WeatherDTO;

/**
 * 외부 API(OpenWeatherMap)를 통해 날씨 데이터를 조회하는 서비스 클래스.
 * * @author jsg
 * @version 1.0
 * @since 2025.10.24
 */
public class WeatherService {

    /** * OpenWeatherMap API 키.
     * ❗️❗️ 중요: 사용 전 유효한 API 키로 변경해야 합니다.
     */
    private final String API_KEY = "13bb8c2a66fad2d40d64cb393bf58006";

    /**
     * 조회할 도시의 이름과 OpenWeatherMap에서 사용하는 고유 ID를 관리하는 내부 정적 클래스.
     */
    private static class City {
        String name; // 한글 도시명
        String id;   // OpenWeatherMap에서 사용하는 도시 ID
        
        /**
         * City 객체를 생성합니다.
         * @param name 한글 도시명
         * @param id OpenWeatherMap 도시 ID
         */
        City(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }

    /**
     * 날씨 정보를 조회할 주요 관광지 도시 ID 리스트.
     */
    private final List<City> cities = List.of(
        new City("서울", "1835848"),
        new City("부산", "1838524"),
        new City("강릉", "1843125"),
        new City("전주", "1845457"),
        new City("제주", "1846266")
    );

    /**
     * OpenWeatherMap API를 호출하여 {@link #cities} 리스트에 정의된 주요 도시들의 현재 날씨 데이터를 조회합니다.
     * <p>
     * 각 도시별로 API를 호출하여 다음 작업을 수행합니다:
     * 1. API 요청 URL을 생성합니다.
     * 2. HTTP GET 요청을 보내고 JSON 응답을 받습니다.
     * 3. Gson 라이브러리를 사용해 JSON 응답을 파싱합니다.
     * 4. 응답 코드가 200이 아닌 경우(예: API 키 오류) 해당 도시를 건너뜁니다.
     * 5. 파싱한 데이터(기온, 날씨 상태 ID)를 {@link WeatherDTO} 객체에 저장합니다.
     * 6. OpenWeatherMap의 날씨 ID를 내부 DTO 표준(하늘 상태, 강수 형태)에 맞게 변환합니다.
     * </p>
     * * @return 조회된 도시별 날씨 정보({@link WeatherDTO})를 담은 리스트. API 호출 실패 시 빈 리스트나 부분적인 리스트가 반환될 수 있습니다.
     * @throws Exception API 호출 또는 데이터 처리(URL 생성, 연결, JSON 파싱) 중 오류 발생 시.
     */
    public List<WeatherDTO> getWeatherData() throws Exception {
        List<WeatherDTO> weatherList = new ArrayList<>();

        for (City city : cities) {
            // 1. API 요청 URL 생성 (units=metric: 섭씨, lang=kr: 한국어)
            String urlString = String.format(
                "https://api.openweathermap.org/data/2.5/weather?id=%s&appid=%s&units=metric&lang=kr",
                city.id, API_KEY
            );

            // 2. HTTP 요청 및 응답 수신
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            // 응답 코드 확인 (200~300이 아닌 경우 ErrorStream으로 읽음)
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            
            String jsonString = rd.readLine();
            rd.close();
            conn.disconnect();
            
            if (jsonString == null) continue; // 응답이 없는 경우 다음 도시로

            // 3. 수신한 JSON 데이터 파싱 (Gson 라이브러리 사용)
            JsonObject jsonResponse = JsonParser.parseString(jsonString).getAsJsonObject();
            
            // API 키가 잘못되거나 오류 발생 시 "cod" 필드에 200이 아닌 값이 옴
            if (jsonResponse.get("cod").getAsInt() != 200) {
                 System.out.println("날씨 API 오류 (" + city.name + "): " + jsonResponse.get("message").getAsString());
                 continue; // 오류 발생 시 다음 도시로
            }

            // 4. 파싱한 데이터를 WeatherDTO에 저장
            WeatherDTO dto = new WeatherDTO();
            dto.setCity(city.name);

            // 온도 추출 (main -> temp)
            JsonObject main = jsonResponse.getAsJsonObject("main");
            double temp = main.get("temp").getAsDouble();
            dto.setTemperature(String.format("%.1f", temp)); // 소수점 첫째 자리까지 표시

            // 날씨 상태 추출 (weather 배열 -> 0번째 요소 -> main, id)
            JsonObject weather = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject();
            // String weatherCondition = weather.get("main").getAsString(); // ex) "Clear", "Clouds", "Rain"
            int weatherId = weather.get("id").getAsInt(); // 날씨 상태 코드 ex) 800(맑음), 501(비)

            // 5. OpenWeatherMap의 날씨 코드를 기존 DTO 구조(기상청 기준)에 맞게 변환
            // 강수(Rain, Snow, Drizzle 등) 상태인지, 아니면 하늘 상태(Clear, Clouds)인지 구분
            if (weatherId >= 200 && weatherId < 600) { // 비, 소나기, 번개 등 (2xx, 3xx, 5xx)
                 dto.setPrecipitationForm("1"); // "비"로 통일 (기상청 코드 1)
                 dto.setSkyCondition(""); // 강수 상태일 때는 하늘 상태는 비워둠
            } else if (weatherId >= 600 && weatherId < 700) { // 눈 (6xx)
                 dto.setPrecipitationForm("3"); // "눈"으로 설정 (기상청 코드 3)
                 dto.setSkyCondition("");
            } else { // 강수가 아닐 때 (맑음, 구름, 안개 등 7xx, 8xx)
                dto.setPrecipitationForm("0"); // "없음"으로 설정 (기상청 코드 0)
                if (weatherId == 800) { // 맑음
                    dto.setSkyCondition("1"); // 기상청 코드 1
                } else if (weatherId > 800) { // 구름 (801 ~ 804)
                    dto.setSkyCondition("3"); // "구름많음"으로 통일 (기상청 코드 3)
                } else { // 안개 등 (7xx)
                	dto.setSkyCondition("4"); // "흐림"으로 통일 (기상청 코드 4)
                }
            }
            weatherList.add(dto);
        }
        return weatherList;
    }
}
