package top.vicat.halo.plugin.gameposter.platforms.steam;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
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
    // private static final String PROFILE_BACKGROUND_XPATH = "/html/body/div[contains(@class,'miniprofile_hover')]/div[contains(@class,'miniprofile_hover_inner')]/div/div[1]/video/source[@type='video/mp4']";
    private static final String PROFILE_BACKGROUND_XPATH = "//video[@class='miniprofile_nameplate']/source[@type='video/mp4']";
    private static final String MINI_PROFILE_ID_XPATH = "//*[@id='responsive_page_template_content']//*[@data-miniprofile]";
    private static final Pattern backgroundPattern = Pattern.compile("background-image\\s*:\\s*url\\(\\s*'([^']+)'\\s*\\);\\s*");

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
                                    Matcher matcher = backgroundPattern.matcher(style);
                                    if (matcher.find()) {
                                        String group = matcher.group(1);
                                        log.info(group);
                                        playerStateRecord.userBaseProfile().setBackground(new UserBaseProfile.ProfileMedia(
                                            group,
                                            UserBaseProfile.ProfileMediaType.IMAGE
                                        ));
                                    }
                                });

                    String miniProfileId = docOpt.map(doc -> doc.selectXpath(MINI_PROFILE_ID_XPATH).first())
                            .map(element -> element.attr("data-miniprofile")).orElse(null);
                    playerStateRecord.userBaseProfile().setExtra("xxxxxx");
                    return new BaseAndMiniProfileRecord(miniProfileId, playerStateRecord.userBaseProfile());
                }).timeout(Duration.ofSeconds(10))
            .flatMap(baseAndMiniProfileRecord ->
                steamProfileClient.getMiniProfile(baseAndMiniProfileRecord.miniProfileId())
                    .map(document -> {
                        Optional<Document> docOpt = Optional.ofNullable(document);
                        docOpt.map(doc -> doc.selectXpath(PROFILE_BACKGROUND_XPATH).first())
                            .map(ele -> ele.attr("src"))
                            .ifPresent(src -> baseAndMiniProfileRecord.userBaseProfileSpec().setProfileBackground(new UserBaseProfile.ProfileMedia(
                                src,
                                UserBaseProfile.ProfileMediaType.VIDEO
                            )));
                        return baseAndMiniProfileRecord.userBaseProfileSpec();
                    })
            )
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
