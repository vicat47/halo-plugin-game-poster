package top.vicat.halo.plugin.gameposter.platforms;

import reactor.core.publisher.Mono;
import top.vicat.halo.plugin.gameposter.entity.UserBaseProfile;

public interface IPlatformService {

    Mono<UserBaseProfile.UserBaseProfileSpec> getUserState(String accountId);

    Mono<UserBaseProfile.UserBaseProfileSpec> getUserBaseProfile(String accountId);

    String getPlatformCode();

}
