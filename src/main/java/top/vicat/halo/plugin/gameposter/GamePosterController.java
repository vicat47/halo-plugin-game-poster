package top.vicat.halo.plugin.gameposter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.ApiVersion;
import run.halo.app.plugin.ReactiveSettingFetcher;
import top.vicat.halo.plugin.gameposter.service.IGamePosterService;

@ApiVersion("gameposter.plugin.halo.vicat.top/v1alpha1")
@RequestMapping("/games")
@RestController
@RequiredArgsConstructor
public class GamePosterController {

    private final IGamePosterService gamePosterService;
    private final ReactiveSettingFetcher settingFetcher;

    @PostMapping("/refresh")
    public Mono<Void> refresh() {
        return settingFetcher.get("base").map(base -> base.get("accountId").asText())
            .flatMapMany(gamePosterService::getUserBaseProfiles)
            .doOnNext(System.out::println)
            .then();
    }

}
