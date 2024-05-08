package top.vicat.halo.plugin.gameposter.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

    public enum PlayerState {
        OFFLINE
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
