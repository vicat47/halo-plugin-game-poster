package top.vicat.halo.plugin.gameposter.platforms.steam.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import top.vicat.halo.plugin.gameposter.client.ProxyWebClientFactory;
import top.vicat.halo.plugin.gameposter.platforms.steam.dto.PlayerState;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class SteamProfileClient {

    private final ProxyWebClientFactory webClientFactory;
    private final SteamApiClient steamApiClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<Document> getProfile(String steamId) {
        return steamApiClient.getUserState(Collections.singletonList(steamId))
            .map(jsonNode -> objectMapper.convertValue(jsonNode, PlayerState.class))
            .next()
            .flatMap(playerState -> webClientFactory.get().flatMap(webClient -> webClient.get()
                .uri(playerState.profileUrl())
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .bodyToMono(String.class)
                .map(Jsoup::parse)));
    }

}
