package top.vicat.halo.plugin.gameposter;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import run.halo.app.extension.Scheme;
import run.halo.app.extension.SchemeManager;
import run.halo.app.plugin.BasePlugin;
import run.halo.app.plugin.PluginContext;
import top.vicat.halo.plugin.gameposter.entity.UserBaseProfile;

@EnableAsync
@Component
public class GamePosterPlugin extends BasePlugin {

    private final SchemeManager schemeManager;

    public GamePosterPlugin(PluginContext pluginContext, SchemeManager schemeManager) {
        super(pluginContext);
        this.schemeManager = schemeManager;
    }

    @Override
    public void start() {
        schemeManager.register(UserBaseProfile.class);
        System.out.println("插件启动成功！");
    }

    @Override
    public void stop() {
        Scheme scheme = schemeManager.get(UserBaseProfile.class);
        schemeManager.unregister(scheme);
        System.out.println("插件停止！");
    }

}
