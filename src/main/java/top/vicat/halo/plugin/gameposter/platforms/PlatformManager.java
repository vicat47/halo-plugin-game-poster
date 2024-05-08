package top.vicat.halo.plugin.gameposter.platforms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlatformManager {
    private final List<IPlatformService> platformServices;
}
