package top.vicat.halo.plugin.gameposter.platforms.steam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.vicat.halo.plugin.gameposter.entity.UserBaseProfile;
import top.vicat.halo.plugin.gameposter.platforms.IPlatformService;
import top.vicat.halo.plugin.gameposter.platforms.steam.client.SteamApiClient;
import top.vicat.halo.plugin.gameposter.platforms.steam.dto.PlayerState;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SteamPlatformServiceImpl implements IPlatformService {

    private final SteamApiClient steamApiClient;

    @Override
    public Mono<UserBaseProfile.UserBaseProfileSpec> getUserBaseProfile(String accountId) {
        Mono<PlayerState> playerStateMono =
            steamApiClient.getUserState(Collections.singletonList(accountId)).next();
        return playerStateMono.map(playerState -> {
            UserBaseProfile.UserBaseProfileSpec profile = new UserBaseProfile.UserBaseProfileSpec();
            profile.setId(accountId);
            profile.setName(playerState.personaName());
            profile.setAvatar(new UserBaseProfile.ProfileMedia(playerState.avatar(),
                UserBaseProfile.ProfileMediaType.IMAGE));
            profile.setGameState(new UserBaseProfile.GameState(
                playerState.gameId(),
                playerState.gameExtraInfo(),
                new UserBaseProfile.ProfileMedia("", UserBaseProfile.ProfileMediaType.IMAGE)
            ));
            return profile;
        });
    }
}
