package top.vicat.halo.plugin.gameposter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import run.halo.app.theme.TemplateNameResolver;

import java.util.HashMap;
import java.util.List;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class GamePosterRouter {

    private final TemplateNameResolver templateNameResolver;

    @Bean
    RouterFunction<ServerResponse> momentRouterFunction() {
        return route(GET("/games"), this::renderGamePage);
    }

    Mono<ServerResponse> renderGamePage(ServerRequest request) {
        // 或许你需要准备你需要提供给模板的默认数据，非必须
        var model = new HashMap<String, Object>();
        model.put("moments", List.of());
        return templateNameResolver.resolveTemplateNameOrDefault(request.exchange(), "moments")
            .flatMap(templateName -> ServerResponse.ok().render(templateName, model));
    }
}
