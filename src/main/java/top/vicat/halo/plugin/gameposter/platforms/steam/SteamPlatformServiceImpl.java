package top.vicat.halo.plugin.gameposter.platforms.steam;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import top.vicat.halo.plugin.gameposter.entity.UserBaseProfile;
import top.vicat.halo.plugin.gameposter.platforms.IPlatformService;
import top.vicat.halo.plugin.gameposter.platforms.steam.client.SteamApiClient;
import top.vicat.halo.plugin.gameposter.platforms.steam.client.SteamProfileClient;
import top.vicat.halo.plugin.gameposter.platforms.steam.dto.PlayerState;

@Slf4j
@Component
@RequiredArgsConstructor
public class SteamPlatformServiceImpl implements IPlatformService {

    private final SteamApiClient steamApiClient;
    private final SteamProfileClient steamProfileClient;

    private static final String AVATAR_MASK_XPATH = "//*[@id=\"responsive_page_template_content\"]/div/div[1]/div/div/div/div[2]/div/div/img";
    private static final String BACKGROUND_XPATH = "//*[@id=\"responsive_page_template_content\"]/div";
    private static final Pattern backgroundPattern = Pattern.compile("background-image\\s*:\\s*url\\(\\s*'([^']+)'\\s*\\)");

    @Override
    public Mono<UserBaseProfile.UserBaseProfileSpec> getUserBaseProfile(String accountId) {
        record PlayerStateRecord(
            PlayerState playerState,
            UserBaseProfile.UserBaseProfileSpec userBaseProfile
        ) {}
        Mono<PlayerState> playerStateMono =
            steamApiClient.getUserState(Collections.singletonList(accountId)).next();
        return playerStateMono.map(playerState -> {
            UserBaseProfile.UserBaseProfileSpec profile = new UserBaseProfile.UserBaseProfileSpec();
            profile.setId(accountId);
            profile.setPlatformCode(this.getPlatformCode());
            profile.setRealName(playerState.realName());
            profile.setPlayerState(UserBaseProfile.PlayerState.OFFLINE);
            profile.setName(playerState.personaName());
            profile.setAvatar(new UserBaseProfile.ProfileMedia(playerState.avatar(),
                UserBaseProfile.ProfileMediaType.IMAGE));
            if (!Objects.isNull(playerState.gameId())) {
                profile.setGameState(new UserBaseProfile.GameState(
                    playerState.gameId(),
                    playerState.gameExtraInfo(),
                    new UserBaseProfile.ProfileMedia("", UserBaseProfile.ProfileMediaType.IMAGE)
                ));
            }
            return new PlayerStateRecord(playerState, profile);
        }).flatMap(playerStateRecord -> steamProfileClient.getProfile(playerStateRecord.playerState().profileUrl())
                .map(document -> {
                    System.out.println(document);
                    Optional<Document> docOpt = Optional.ofNullable(document);
                    docOpt.map(doc -> doc.selectXpath(AVATAR_MASK_XPATH).first())
                        .map(element -> element.attr("src"))
                            .ifPresent(resource -> playerStateRecord.userBaseProfile().setAvatarMask(new UserBaseProfile.ProfileMedia(
                                resource,
                                UserBaseProfile.ProfileMediaType.IMAGE
                            )));
                    docOpt.map(doc -> doc.selectXpath(BACKGROUND_XPATH).first())
                            .map(element -> element.attr("style"))
                                .ifPresent(style -> {
                                    String group = backgroundPattern.matcher(style).group(1);
                                    log.info(group);
                                });
                    playerStateRecord.userBaseProfile().setExtra("xxxxxx");
                    return playerStateRecord.userBaseProfile();
                }).timeout(Duration.ofSeconds(5))
            .onErrorResume(throwable -> {
                if (throwable instanceof TimeoutException) {
                    log.warn("获取个人页面超时！{}", playerStateRecord.playerState().profileUrl());
                    return Mono.just(playerStateRecord.userBaseProfile());
                }
                return Mono.error(throwable);
            })
        );
    }

    @Override
    public String getPlatformCode() {
        return "STEAM";
    }
}
