package uk.tojourn.trophydatafinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class TrophyRepository {

    private final RestTemplate client;

    @Value("${persistence-api.save-url}")
    private String saveUrl;


    @Autowired
    public TrophyRepository(RestTemplate client) {
        this.client = client;
    }

    public URI persistTrophy(Trophy trophy){
        String trophyString = String.format("""
                   { "query": "mutation {
                   addAchievement(achievement:{
                     gameName: "%s",
                     name: "%s",
                     description: "%s",
                     solution: "%s",
                     imageUrl: null,
                     iconUrl: null,
                     rarity: %s,
                   }) {
                     name,
                     gameName,
                     description,
                     solution,
                     imageUrl,
                     iconUrl,
                     rarity,
                   }
                 }"} """, trophy.gameName, trophy.title, trophy.description, trophy.howTo, trophy.rarity);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(trophyString, headers);
       return client.postForLocation(saveUrl, request, String.class);
    }
}
