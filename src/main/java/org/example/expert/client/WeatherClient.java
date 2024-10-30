package org.example.expert.client;

import org.example.expert.client.dto.WeatherDto;
import org.example.expert.domain.common.exception.ServerException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class WeatherClient {

    private final RestTemplate restTemplate;

    public WeatherClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    // 오늘의 날씨 데이터를 가져오는 메서드
    public String getTodayWeather() {

        ResponseEntity<WeatherDto[]> responseEntity =
            restTemplate.getForEntity(buildWeatherApiUri(), WeatherDto[].class);

        WeatherDto[] weatherArray = responseEntity.getBody();

        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            throw new ServerException("날씨 데이터를 가져오는데 실패했습니다. 상태 코드: " + responseEntity.getStatusCode());
        }

        if (weatherArray == null || weatherArray.length == 0) {
            throw new ServerException("날씨 데이터가 없습니다.");
        }

        String today = getCurrentDate();

        //오늘 날짜에 해당하는 날씨 데이터를 찾음
        for (WeatherDto weatherDto : weatherArray) {
            if (today.equals(weatherDto.getDate())) {
                return weatherDto.getWeather();
            }
        }

        // 오늘 날짜에 해당하는 데이터가 없을 경우 예외 발생
        throw new ServerException("오늘에 해당하는 날씨 데이터를 찾을 수 없습니다.");
    }

    // Weather API URI를 생성하는 메서드
    private URI buildWeatherApiUri() {
        return UriComponentsBuilder
            .fromUriString("https://f-api.github.io") // 기본 URI 설정
            .path("/f-api/weather.json") // 경로 추가
            .encode()
            .build()
            .toUri();
    }

    // 오늘 날짜를 MM-dd 형식의 문자열로 반환하는 메서드
    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        return LocalDate.now().format(formatter);
    }
}
