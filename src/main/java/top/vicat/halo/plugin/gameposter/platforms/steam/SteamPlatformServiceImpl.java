package top.vicat.halo.plugin.gameposter.platforms.steam;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import top.vicat.halo.plugin.gameposter.entity.UserBaseProfile;
import top.vicat.halo.plugin.gameposter.platforms.IPlatformService;
import top.vicat.halo.plugin.gameposter.platforms.steam.client.SteamApiClient;
import top.vicat.halo.plugin.gameposter.platforms.steam.client.SteamProfileClient;
import top.vicat.halo.plugin.gameposter.platforms.steam.converter.UserBaseProfileConverter;
import top.vicat.halo.plugin.gameposter.platforms.steam.dto.PlayerState;
import top.vicat.halo.plugin.gameposter.platforms.steam.scraper.SteamScraper;

@Slf4j
@Component
@RequiredArgsConstructor
public class SteamPlatformServiceImpl implements IPlatformService {

    private final SteamApiClient steamApiClient;
    private final SteamProfileClient steamProfileClient;
    private final SteamScraper steamScraper;

    @Override
    public Mono<UserBaseProfile.UserBaseProfileSpec> getUserState(String accountId) {
        return steamApiClient.getUserState(Collections.singletonList(accountId)).next()
            .map(playerState -> UserBaseProfileConverter.toUserBaseProfileSpec(accountId, this.getPlatformCode(), playerState));
    }

    @Override
    public Mono<UserBaseProfile.UserBaseProfileSpec> getUserBaseProfile(String accountId) {
        record PlayerStateRecord(
            PlayerState playerState,
            UserBaseProfile.UserBaseProfileSpec userBaseProfile
        ) {}
        record BaseAndMiniProfileRecord(
            String miniProfileId,
            UserBaseProfile.UserBaseProfileSpec userBaseProfileSpec
        ) {}
        return steamApiClient.getUserState(Collections.singletonList(accountId)).next()
            .map(playerState -> {
                UserBaseProfile.UserBaseProfileSpec profile = UserBaseProfileConverter.toUserBaseProfileSpec(accountId, this.getPlatformCode(), playerState);
                return new PlayerStateRecord(playerState, profile);
            })
            .doOnNext(playerStateRecord -> log.debug("fetch user state complete"))
            .flatMap(playerStateRecord -> steamProfileClient.getProfile(playerStateRecord.playerState().profileUrl())
                .map(document -> {
                    playerStateRecord.userBaseProfile().setAvatarMask(steamScraper.scrapeAvatarMask(document));
                    playerStateRecord.userBaseProfile().setBackground(steamScraper.scrapePersonalProfileBg(document));
                    playerStateRecord.userBaseProfile().setExtra("xxxxxx");
                    return new BaseAndMiniProfileRecord(steamScraper.scrapeMiniProfileId(document), playerStateRecord.userBaseProfile());
                })
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(throwable -> {
                    if (throwable instanceof TimeoutException) {
                        log.warn("获取个人页面超时！{}", playerStateRecord.playerState().profileUrl());
                        return Mono.just(new BaseAndMiniProfileRecord(
                            null,
                            playerStateRecord.userBaseProfile()
                        ));
                    }
                    return Mono.error(throwable);
                })
                .flatMap(baseAndMiniProfileRecord -> {
                    if (baseAndMiniProfileRecord.miniProfileId() == null) {
                        return Mono.just(baseAndMiniProfileRecord.userBaseProfileSpec());
                    }
                    return steamProfileClient.getMiniProfile(baseAndMiniProfileRecord.miniProfileId())
                        .map(document -> {
                            baseAndMiniProfileRecord.userBaseProfileSpec()
                                .setProfileBackground(steamScraper.scrapeMiniProfileBgVideo(document));
                            return baseAndMiniProfileRecord.userBaseProfileSpec();
                        })
                        .timeout(Duration.ofSeconds(5))
                        .onErrorResume(throwable -> {
                            if (throwable instanceof TimeoutException) {
                                log.warn("获取 mini profile 页面超时！{}", playerStateRecord.playerState().profileUrl());
                                return Mono.just(playerStateRecord.userBaseProfile());
                            }
                            return Mono.error(throwable);
                        });
                })
            );
    }

    @Override
    public String getPlatformCode() {
        return "STEAM";
    }
}
