package top.vicat.halo.plugin.gameposter.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

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
        OFFLINE(0, "离线"),
        ONLINE(1, "在线"),
        BUSY(2, "忙碌"),
        LEAVE(3, "离开"),
        NAPPING(4, "打盹"),
        WANT_TO_TRADE(5, "想要交易"),
        WANT_TO_PLAY(6, "想要玩"),
        IN_GAME(50, "游戏中"),
        ;
        @JsonValue
        private final Integer code;
        private final String description;
        public static PlayerState getByCode(Integer code) {
            for (PlayerState state : PlayerState.values()) {
                if (state.getCode().equals(code)) {
                    return state;
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

}
