package top.vicat.halo.plugin.gameposter.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;
import run.halo.app.plugin.ReactiveSettingFetcher;
import top.vicat.halo.plugin.gameposter.vo.ProxyConfig;

@Component
@RequiredArgsConstructor
public class ProxyWebClientFactory {
    private final ConcurrentHashMap<String, WebClientRecord> webClient = new ConcurrentHashMap<>();
    private final ReactiveSettingFetcher settingFetcher;
    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private record WebClientRecord(
        ProxyConfig proxy,
        WebClient webClient
    ) {}

    public Mono<WebClient> get() {
        return this.get(null);
    }

    public Mono<WebClient> get(String baseUrl) {
        return settingFetcher.get("proxy").map(proxyConfig -> {
            ProxyConfig config;
            if (!proxyConfig.get("enabled").asBoolean()) {
                config = null;
            } else {
                config = objectMapper.convertValue(proxyConfig, ProxyConfig.class);
            }
            var url = baseUrl == null? "null": baseUrl;
            WebClientRecord recordPresent = webClient.computeIfPresent(url, (k, v) -> {
                if (Objects.equals(v.proxy(), config)) {
                    return v;
                }
                ReactorClientHttpConnector conn = getProxyReactorClientHttpConnector(config);
                return new WebClientRecord(config, WebClient.builder()
                    .baseUrl(k)
                    .clientConnector(conn)
                    .build());
            });
            return Objects.requireNonNullElseGet(recordPresent,
                () -> webClient.computeIfAbsent(url,
                    k -> new WebClientRecord(config, WebClient.builder()
                        .baseUrl(k)
                        .clientConnector(getProxyReactorClientHttpConnector(config))
                        .build()))).webClient();
        });
    }

    private static ReactorClientHttpConnector getProxyReactorClientHttpConnector(ProxyConfig config) {
        if (config == null) {
            return new ReactorClientHttpConnector();
        }
        HttpClient httpClient = HttpClient.create()
            .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                .host(config.address())
                .port(config.port())
                .username(config.username())
                .password(username -> config.password()));
        return new ReactorClientHttpConnector(httpClient);
    }

}
