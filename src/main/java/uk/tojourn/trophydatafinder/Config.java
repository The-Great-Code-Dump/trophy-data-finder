package uk.tojourn.trophydatafinder;

import com.apollographql.apollo3.ApolloClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${persistence-api.save-url}")
    private String saveUrl;


    @Bean
    public ApolloClient client (){
        ApolloClient.Builder builder = new ApolloClient.Builder()
                .serverUrl(saveUrl);
        return builder.build();
    }



}
