package uk.tojourn.trophydatafinder;

import com.apollographql.apollo3.ApolloCall;
import com.apollographql.apollo3.ApolloClient;
import com.apollographql.apollo3.api.ApolloResponse;
import com.apollographql.apollo3.api.ExecutionOptions;
import com.apollographql.apollo3.rx2.Rx2Apollo;
import io.reactivex.Single;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.tojourn.MutateTrophyMutation;
import uk.tojourn.type.Rarity;

@Component
public class TrophyRepository {

    private final ApolloClient client;

    @Autowired
    public TrophyRepository(ApolloClient client) {
        this.client = client;
    }

    public void persistTrophy(Trophy trophy) {
        ApolloCall callBack = client.mutation(
                new MutateTrophyMutation(
                        trophy.title,
                        trophy.gameName,
                        trophy.description,
                        trophy.howTo,
                        null,
                        null,
                        Rarity.safeValueOf(trophy.rarity.toUpperCase()))
        );
        Single<ApolloResponse> mutationResponse = Rx2Apollo.single(callBack);
        mutationResponse.subscribe();
    }
}
