package top.vicat.halo.plugin.gameposter.platforms;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import top.vicat.halo.plugin.gameposter.entity.UserBaseProfile;

@Component
@RequiredArgsConstructor
public class PlatformManager {
    private final List<IPlatformService> platformServices;

    public Mono<UserBaseProfile.UserBaseProfileSpec> getProfileByAccountId(String accountId, String platformCode) {
        return platformServices.stream()
            .filter(service -> platformCode.equals(service.getPlatformCode()))
            .findFirst()
            .orElseThrow()
            .getUserBaseProfile(accountId);
    }

}
