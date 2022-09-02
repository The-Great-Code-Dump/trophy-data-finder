package uk.tojourn.trophydatafinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
public class PlayStationAsyncScraperHandler extends Thread {
    private final PlaystationTrophiesScraper scraper;
    private final String gameName;
    Logger logger = LoggerFactory.getLogger(PlayStationAsyncScraperHandler.class);

    public PlayStationAsyncScraperHandler(PlaystationTrophiesScraper scraper, String gameName){
        this.scraper = scraper;
        this.gameName = gameName;
    }

    public void run(){
        Set<String> links = getTrophyWebpages(gameName);
        if(links != null) {
            links.forEach(link -> {
                try {
                    Trophy aTrophy = scraper.getTrophyData(link);
                    //Prevent thinking it's a ddos
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
                    //TODO: Send trophy data to api to save the trophy data for given game.
                    logger.info(String.format("Saved the trophy, %s", aTrophy.title));
                } catch (Exception error) {
                    logger.error(String.format("An error occured trying to find %s \n Error: %s", link, error.getMessage()));
                }
            });
        }


    }

    private Set<String> getTrophyWebpages(String gameName){
        try {
            return scraper.getAllTrophyPathsForGame(gameName);
        } catch (IOException error) {
            logger.error(String.format("An error occured trying to find %s \n Error: %s", gameName, error.getMessage()));
        }
        return null;
    }
}
