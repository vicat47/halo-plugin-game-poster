package top.vicat.halo.plugin.gameposter;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.endpoint.CustomEndpoint;
import run.halo.app.extension.GroupVersion;
import run.halo.app.extension.Metadata;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.plugin.ReactiveSettingFetcher;
import top.vicat.halo.plugin.gameposter.entity.UserBaseProfile;
import top.vicat.halo.plugin.gameposter.service.IGamePosterService;

@Slf4j
@RequiredArgsConstructor
@Component
public class GamePosterEndpoint implements CustomEndpoint {

    private final IGamePosterService gamePosterService;
    private final ReactiveSettingFetcher settingFetcher;
    private final ReactiveExtensionClient extensionClient;

    @Override
    public RouterFunction<ServerResponse> endpoint() {
        return route()
            .POST("/refresh/profiles", this::handleRefreshProfiles)
            .build();
    }

    @Override
    public GroupVersion groupVersion() {
        return new GroupVersion("console.api.gameposter.plugin.halo.vicat.top", "v1alpha1");
    }

    private Mono<ServerResponse> handleRefreshProfiles(ServerRequest serverRequest) {
        return  settingFetcher.get("base").map(base -> base.get("accountId").asText())
            .flatMap(gamePosterService::getUserBaseProfiles)
            .doOnNext(item -> log.info(String.valueOf(item)))
            .flatMap(item -> {
                UserBaseProfile userBaseProfile = new UserBaseProfile();
                userBaseProfile.setUserBaseProfileSpec(item);
                userBaseProfile.setMetadata(new Metadata());
                userBaseProfile.getMetadata().setGenerateName("gameposter-");
                userBaseProfile.getMetadata().setName("gameposter-" + userBaseProfile.getIdName());
                return Mono.just(userBaseProfile);
            })
            .flatMap(profile -> extensionClient.fetch(UserBaseProfile.class, profile.getMetadata().getName())
                    .flatMap(qProfile -> {
                        log.info(profile.toString());
                        qProfile.setUserBaseProfileSpec(profile.getUserBaseProfileSpec());
                        return extensionClient.update(qProfile);
                    })
                .doOnError(throwable -> log.error(String.valueOf(throwable)))
                .switchIfEmpty(extensionClient.create(profile))
            )
            .doOnError(throwable -> log.error(String.valueOf(throwable)))
            .doOnNext(userBaseProfile -> log.info("userBaseProfile save success!"))
            .flatMap(userBaseProfile -> ServerResponse.ok().build());
    }
}
