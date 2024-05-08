package top.vicat.halo.plugin.gameposter.platforms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import top.vicat.halo.plugin.gameposter.entity.UserBaseProfile;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlatformManager {
    private final List<IPlatformService> platformServices;

    public Mono<UserBaseProfile.UserBaseProfileSpec> getByAccountId(String accountId) {
        return platformServices.get(0).getUserBaseProfile(accountId);
    }
}
