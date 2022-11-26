package com.udacity.jdnd.course3.critter;

import com.udacity.jdnd.course3.critter.pet.Behavior;
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
        ResponseEntity<Behavior[]> responseEntityGetAll = restTemplate.getForEntity(url, Behavior[].class);
        assertEquals(HttpStatus.OK, responseEntityGetAll.getStatusCode());

        int initCount = Objects.requireNonNull(responseEntityGetAll.getBody()).length;

        ResponseEntity<Behavior[]> responseEntityGetAllByCat = restTemplate.getForEntity(url + "/petType?petType=CAT", Behavior[].class);
        assertEquals(HttpStatus.OK, responseEntityGetAllByCat.getStatusCode());
        int initCountByCat = Objects.requireNonNull(responseEntityGetAllByCat.getBody()).length;

        Map<String, String> requestParamsBird = new HashMap<>();
        requestParamsBird.put("petType","BIRD");
        ResponseEntity<Behavior[]> responseEntityGetAllByBird = restTemplate.getForEntity(url + "/petType?petType=BIRD", Behavior[].class, requestParamsBird);
        assertEquals(HttpStatus.OK, responseEntityGetAllByBird.getStatusCode());
        int initCountByBird = Objects.requireNonNull(responseEntityGetAllByBird.getBody()).length;

        Behavior behaviorDTO = new Behavior();
        behaviorDTO.setName("Jump");
        behaviorDTO.setDescription("Jump is a behavior for dogs and cats.");
        behaviorDTO.setPetTypes(List.of(PetType.CAT, PetType.DOG));
        ResponseEntity<Behavior> responseEntityPost = restTemplate.postForEntity(url, behaviorDTO, Behavior.class);
        assertEquals(HttpStatus.OK, responseEntityPost.getStatusCode());
        final Long behaviorId = Objects.requireNonNull(responseEntityPost.getBody()).getId();

        responseEntityGetAll = restTemplate.getForEntity(url, Behavior[].class);
        assertEquals(initCount + 1, Objects.requireNonNull(responseEntityGetAll.getBody()).length);

        responseEntityGetAllByCat = restTemplate.getForEntity(url + "/petType?petType=CAT", Behavior[].class);
        assertEquals(initCount + 1, Objects.requireNonNull(responseEntityGetAllByCat.getBody()).length);
        responseEntityGetAllByBird = restTemplate.getForEntity(url + "/petType?petType=BIRD", Behavior[].class, requestParamsBird);
        assertEquals(initCountByBird, Objects.requireNonNull(responseEntityGetAllByBird.getBody()).length);

        ResponseEntity<Behavior> responseEntityGet = restTemplate.getForEntity(url + "/" + behaviorId, Behavior.class);
        assertEquals(HttpStatus.OK, responseEntityGet.getStatusCode());

        restTemplate.delete(url + "/" + behaviorId);
        responseEntityGetAll = restTemplate.getForEntity(url, Behavior[].class);
        assertEquals(initCount, Objects.requireNonNull(responseEntityGetAll.getBody()).length);
        assertFalse(Arrays.stream(responseEntityGetAll.getBody()).anyMatch(behaviorDTO1 -> behaviorDTO1.getId().equals(behaviorId)));
    }
}
