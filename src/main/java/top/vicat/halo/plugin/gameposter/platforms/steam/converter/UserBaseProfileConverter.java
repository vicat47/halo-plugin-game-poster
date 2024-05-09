package top.vicat.halo.plugin.gameposter.platforms.steam.converter;

import top.vicat.halo.plugin.gameposter.entity.UserBaseProfile;
import top.vicat.halo.plugin.gameposter.platforms.steam.dto.PlayerState;
import top.vicat.halo.plugin.gameposter.platforms.steam.enums.SteamPersonalStateEnum;
import java.util.Objects;

public class UserBaseProfileConverter {
    public static UserBaseProfile.PlayerState toPlayerState(final PlayerState playerState) {
        if (playerState.gameId() != null) {
            return UserBaseProfile.PlayerState.IN_GAME;
        }
        return UserBaseProfile.PlayerState.from(playerState.personaState().name());
    }

    public static UserBaseProfile.UserBaseProfileSpec toUserBaseProfileSpec(final String accountId, final String platformCode, final PlayerState playerState) {
        UserBaseProfile.UserBaseProfileSpec profile = new UserBaseProfile.UserBaseProfileSpec();
        profile.setId(accountId);
        profile.setPlatformCode(platformCode);
        profile.setRealName(playerState.realName());
        profile.setPlayerState(UserBaseProfileConverter.toPlayerState(playerState));
        profile.setName(playerState.personaName());
        profile.setAvatar(new UserBaseProfile.ProfileMedia(playerState.avatar(),
            UserBaseProfile.ProfileMediaType.IMAGE));
        if (!Objects.isNull(playerState.gameId())) {
            profile.setGameState(new UserBaseProfile.GameState(
                playerState.gameId(),
                playerState.gameExtraInfo(),
                null
            ));
        }
        return profile;
    }
}
