package com.example.ballnet.facility;

import com.example.ballnet.facility.models.SportInfo;
import com.example.ballnet.services.HttpService;

import java.io.IOException;
import java.util.HashMap;

public class FacilityService {

    private static final FacilityService INSTANCE = new FacilityService();

    public static FacilityService getInstance() {
        return INSTANCE;
    }

    private FacilityService() {
    }

    private final HttpService httpService = HttpService.getInstance();

    public SportInfo getSportInfo(Long sportID) throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", sportID);

        return httpService.jsonGet("http://ball.net:8080/api/v1/facility/sport-info",
                params,
                SportInfo.class);
    }

}
