package top.vicat.halo.plugin.gameposter.platforms.steam.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import top.vicat.halo.plugin.gameposter.client.ProxyWebClientFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class SteamProfileClient {

    private final ProxyWebClientFactory webClientFactory;

    public Mono<Document> getProfile(String profileUrl) {
        return webClientFactory.get().flatMap(webClient -> webClient.get()
                .uri(profileUrl)
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .bodyToMono(String.class)
                .map(Jsoup::parse));
    }

    /**
     * 通过请求获取个人悬浮页
     * @param miniProfileId miniProfile id，从 profile 页面可以取到
     * @return 返回页面 html
     */
    public Mono<Document> getMiniProfile(String miniProfileId) {
        return webClientFactory.get().flatMap(webClient -> webClient.get()
            .uri("https://steamcommunity.com/miniprofile/" + miniProfileId)
            .accept(MediaType.TEXT_HTML)
            .retrieve()
            .bodyToMono(String.class)
            .map(Jsoup::parse));
    }

}
