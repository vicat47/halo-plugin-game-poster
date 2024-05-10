package top.vicat.halo.plugin.gameposter.entity;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

@Data
@EqualsAndHashCode(callSuper=true)
@GVK(
    group = "gameposter.plugin.halo.vicat.top",
    version = "v1alpha1",
    kind = "UserBaseProfile",
    singular = "userBaseProfile",
    plural = "userBaseProfiles"
)
public class UserBaseProfile extends AbstractExtension {

    @Schema(requiredMode = REQUIRED)
    private UserBaseProfileSpec userBaseProfileSpec;

    @Data
    public static class UserBaseProfileSpec {
        @Schema
        private String id;
        @Schema
        private String platformCode;
        @Schema
        private String name;
        @Schema
        private String realName;
        @Schema
        private ProfileMedia avatar;
        @Schema
        private ProfileMedia avatarMask;
        @Schema
        private ProfileMedia profileBackground;
        @Schema
        private ProfileMedia background;
        @Schema
        private PlayerState playerState;
        @Schema
        private GameState gameState;
        @Schema
        private String extra;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileMedia {
        @Schema
        private String url;
        @Schema
        private ProfileMediaType type;
    }

    public enum ProfileMediaType {
        IMAGE,
        VIDEO
    }

    @Getter
    @AllArgsConstructor
    public enum PlayerState {
        OFFLINE,
        ONLINE,
        BUSY,
        LEAVE,
        NAPPING,
        WANT_TO_TRADE,
        WANT_TO_PLAY,
        IN_GAME,
        ;
        public static PlayerState from(String state) {
            for (PlayerState value : PlayerState.values()) {
                if (value.name().equalsIgnoreCase(state)) {
                    return value;
                }
            }
            return null;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GameState {
        @Schema
        private String id;
        @Schema
        private String name;
        @Schema
        private ProfileMedia media;
    }

    public String getIdName() {
        Optional<UserBaseProfileSpec> specOpt =
            Optional.ofNullable(this.getUserBaseProfileSpec());
        if (specOpt.map(UserBaseProfileSpec::getPlatformCode).isEmpty() || specOpt.map(UserBaseProfileSpec::getId).isEmpty()) {
            return null;
        }
        return specOpt.map(UserBaseProfileSpec::getPlatformCode).get().toLowerCase() + "-" + specOpt.map(UserBaseProfileSpec::getId).get();
    }

}
