package top.vicat.halo.plugin.gameposter.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

@Data
@EqualsAndHashCode(callSuper = true)
@GVK(
    group = "gameposter.plugin.halo.vicat.top",
    version = "v1alpha1",
    kind = "Game",
    singular = "game",
    plural = "games"
)
public class Game extends AbstractExtension {
    private String id;
    private String name;
    private String description;

}
