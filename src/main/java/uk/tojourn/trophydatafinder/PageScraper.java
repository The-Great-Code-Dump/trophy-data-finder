package uk.tojourn.trophydatafinder;

import java.io.IOException;

public interface PageScraper {
    Trophy getTrophyData(String url, String game) throws IOException;
}
