package uk.tojourn.trophydatafinder;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class PlaystationTrophiesScraper implements PageScraper {
    Logger logger = LoggerFactory.getLogger(PlaystationTrophiesScraper.class);
    static final String BASEURL = "https://www.playstationtrophies.org/";

    final String regex = "(?<=_)([a-z]+)";
    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);


    public Set<String> getAllTrophyPathsForGame(String game) throws IOException {
        String path = BASEURL + "game/" + game + "/trophies/";
            Document webpage = Jsoup.connect(path).get();
            if(webpage.body().text().equals("Did not find any game #000")){
                throw new HttpStatusException("This game was not found", 404, path);
            }
            Elements aTag = webpage.select(".achilist__item a");
            return aTag.stream().map(item ->
                    item.attr("href")).collect(Collectors.toSet());
    }

    public Trophy getTrophyData(String path) throws IOException {
        Document webpage = Jsoup.connect(BASEURL + path).get();
        Element trophyElement = webpage.select(".achilist__item").first();
        if(trophyElement == null){
            throw new IOException("Game data not availble");
        }
        String howTo = handleIncompleteGame(trophyElement);
        String description = trophyElement.select(".achilist__data p").get(1).text();
        String title = trophyElement.select(".achilist__title").first().text();
        String rarity = trophyElement.select(".achilist__value-numb img").first().attr("src");
        Matcher matcher = pattern.matcher(rarity);
        if (matcher.find()) {
            rarity = matcher.group(0);
        }
        return new Trophy(title, rarity, description, howTo);


    }

    private String handleIncompleteGame(Element trophyElement){
       Element firstItem = trophyElement.select(".text_res").first();
        if (firstItem == null){
            return "";
        }else{
            return firstItem.text();
        }
    }
}
