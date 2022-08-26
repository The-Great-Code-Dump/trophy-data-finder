package uk.tojourn.trophydatafinder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("game")
public class GameController {
    Logger logger = LoggerFactory.getLogger(GameController.class);
    private final PlaystationTrophiesScraper scraper;


    @Autowired
    public GameController(PlaystationTrophiesScraper scraper){
        this.scraper = scraper;
    }
    @GetMapping("/{gameName}")
    public ResponseEntity<String> getGame(@PathVariable String gameName){
        if (gameName == null || gameName.isEmpty()){
            //metrics.incrementStatusCode(HttpStatus.BAD_REQUEST);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("That was not a valid game name");
        }
        //TODO: Get game id from api for game name if it doesn't exist create it.
        //TODO: Check game does not have trophy data already if it exists return HTTP 409 conflict (we won't need to update this once the data is in)
        try {
            //TODO: parse game id for api
            PlayStationAsyncScraperHandler scraperHandler = new PlayStationAsyncScraperHandler(scraper, gameName);
            scraperHandler.start();
        }catch (Exception error){
            logger.error("Error occured when calling playstation trophies.");
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occured when calling playstation trophies.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Processing request it will be availble on the api in a few minutes!");
    }




}
