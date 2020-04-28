package com.github.kaysoro.kaellybot.core.service;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.payload.dofusroom.DofusRoomPreviewDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

@Service
public class DofusRoomService extends AbstractRestClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DofusRoomService.class);
    private static final String ORGANIZATION = "organization";
    private static final String API_KEY = "apiKey";

    @Value("${dofusroom.organization}")
    private String dofusRoomOrganization;

    @Value("${dofusroom.token}")
    private String dofusRoomToken;

    private final WebClient webClient;

    public DofusRoomService(@Value("${dofusroom.url}") String dofusRoomUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(dofusRoomUrl)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .defaultHeader(ORGANIZATION, dofusRoomOrganization)
                .defaultHeader(API_KEY, dofusRoomToken)
                .filter(logRequest(LOGGER))
                .build();
    }

    public Mono<DofusRoomPreviewDto> getDofusRoomPreview(String id, Language language){
        return webClient.post()
                .uri("/{id}", id)
                .header(ACCEPT_LANGUAGE, language.name())
                .retrieve()
                .bodyToMono(DofusRoomPreviewDto.class);
    }
}