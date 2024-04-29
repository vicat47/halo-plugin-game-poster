package top.vicat.halo.plugin.gameposter;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import run.halo.app.plugin.BasePlugin;
import run.halo.app.plugin.PluginContext;

@EnableAsync
@Component
public class GamePosterPlugin extends BasePlugin {

    public GamePosterPlugin(PluginContext pluginContext) {
        super(pluginContext);
    }

    @Override
    public void start() {
        System.out.println("插件启动成功！");
    }

    @Override
    public void stop() {
        System.out.println("插件停止！");
    }

}
