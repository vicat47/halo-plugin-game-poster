package top.vicat.halo.plugin.gameposter.platforms.steam.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import run.halo.app.plugin.ReactiveSettingFetcher;
import top.vicat.halo.plugin.gameposter.client.ProxyWebClientFactory;
import top.vicat.halo.plugin.gameposter.platforms.steam.dto.PlayerOwnGame;
import top.vicat.halo.plugin.gameposter.platforms.steam.dto.PlayerState;

@Slf4j
@Component
@RequiredArgsConstructor
public class SteamApiClient {
    private final ReactiveSettingFetcher settingFetcher;
    private final ProxyWebClientFactory webClientFactory;

    private final ObjectMapper objectMapper = new ObjectMapper();
    public static final String BASE_URL = "http://api.steampowered.com";

    public Flux<PlayerState> getUserState(List<String> userIdList) {
        return this.settingFetcher.get("base")
            .map(base -> base.get("steamKey").asText())
            .flatMapMany(key -> webClientFactory.get(BASE_URL)
                .map(webClient -> webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/ISteamUser/GetPlayerSummaries/v2/")
                        .queryParam("key", key)
                        .queryParam("steamids", String.join(",", userIdList))
                        .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(ObjectNode.class)
                    .flatMapIterable(item -> item.withArray("/response/players")))
            )
            .flatMap(Function.identity())
            .map(jsonNode -> objectMapper.convertValue(jsonNode, PlayerState.class))
            .doOnNext(playerState -> log.debug("request steam player state: {}", playerState));
    }

    public Flux<PlayerOwnGame> getPlayerOwnGame(String steamId) {
        return this.settingFetcher.get("base")
            .map(base -> base.get("steamKey").asText())
            .flatMapMany(key -> webClientFactory.get(BASE_URL)
                .map(webClient -> webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/IPlayerService/GetOwnedGames/v1/")
                        .queryParam("key", key)
                        .queryParam("steamid", steamId)
                        .queryParam("include_appinfo", "true")
                        .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(ObjectNode.class)
                    .flatMapIterable(item -> item.withArray("/response/games")))
            ).flatMap(Function.identity())
            .map(jsonNode -> objectMapper.convertValue(jsonNode, PlayerOwnGame.class));
    }
}
