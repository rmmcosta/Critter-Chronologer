package com.udacity.jdnd.course3.critter;

import com.udacity.jdnd.course3.critter.pet.BehaviorDTO;
import com.udacity.jdnd.course3.critter.pet.PetType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BehaviorControllerTests {
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void behaviorCrudOk() {
        String url = "http://localhost:" + port + "/behavior";
        ResponseEntity<BehaviorDTO[]> responseEntityGetAll = restTemplate.getForEntity(url, BehaviorDTO[].class);
        assertEquals(HttpStatus.OK, responseEntityGetAll.getStatusCode());

        int initCount = Objects.requireNonNull(responseEntityGetAll.getBody()).length;

        ResponseEntity<BehaviorDTO[]> responseEntityGetAllByCat = restTemplate.getForEntity(url + "/petType?petType=CAT", BehaviorDTO[].class);
        assertEquals(HttpStatus.OK, responseEntityGetAllByCat.getStatusCode());
        int initCountByCat = Objects.requireNonNull(responseEntityGetAllByCat.getBody()).length;

        Map<String, String> requestParamsBird = new HashMap<>();
        requestParamsBird.put("petType","BIRD");
        ResponseEntity<BehaviorDTO[]> responseEntityGetAllByBird = restTemplate.getForEntity(url + "/petType?petType=BIRD", BehaviorDTO[].class, requestParamsBird);
        assertEquals(HttpStatus.OK, responseEntityGetAllByBird.getStatusCode());
        int initCountByBird = Objects.requireNonNull(responseEntityGetAllByBird.getBody()).length;

        BehaviorDTO behaviorDTO = new BehaviorDTO();
        behaviorDTO.setName("Jump");
        behaviorDTO.setDescription("Jump is a behavior for dogs and cats.");
        behaviorDTO.setPetTypes(List.of(PetType.CAT, PetType.DOG));
        ResponseEntity<BehaviorDTO> responseEntityPost = restTemplate.postForEntity(url, behaviorDTO, BehaviorDTO.class);
        assertEquals(HttpStatus.OK, responseEntityPost.getStatusCode());
        final Long behaviorId = Objects.requireNonNull(responseEntityPost.getBody()).getId();

        responseEntityGetAll = restTemplate.getForEntity(url, BehaviorDTO[].class);
        assertEquals(initCount + 1, Objects.requireNonNull(responseEntityGetAll.getBody()).length);

        responseEntityGetAllByCat = restTemplate.getForEntity(url + "/petType?petType=CAT", BehaviorDTO[].class);
        assertEquals(initCount + 1, Objects.requireNonNull(responseEntityGetAllByCat.getBody()).length);
        responseEntityGetAllByBird = restTemplate.getForEntity(url + "/petType?petType=BIRD", BehaviorDTO[].class, requestParamsBird);
        assertEquals(initCountByBird, Objects.requireNonNull(responseEntityGetAllByBird.getBody()).length);

        ResponseEntity<BehaviorDTO> responseEntityGet = restTemplate.getForEntity(url + "/" + behaviorId, BehaviorDTO.class);
        assertEquals(HttpStatus.OK, responseEntityGet.getStatusCode());

        restTemplate.delete(url + "/" + behaviorId);
        responseEntityGetAll = restTemplate.getForEntity(url, BehaviorDTO[].class);
        assertEquals(initCount, Objects.requireNonNull(responseEntityGetAll.getBody()).length);
        assertFalse(Arrays.stream(responseEntityGetAll.getBody()).anyMatch(behaviorDTO1 -> behaviorDTO1.getId().equals(behaviorId)));
    }
}
