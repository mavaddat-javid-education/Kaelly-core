package com.github.kaysoro.kaellybot.core.trigger;

import com.github.kaysoro.kaellybot.core.mapper.DofusRoomPreviewMapper;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.payload.dofusroom.StatusDto;
import com.github.kaysoro.kaellybot.core.service.DofusRoomService;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DofusRoomTrigger implements Trigger {

    private List<Pattern> dofusRoomUrlPatterns;

    private DofusRoomService dofusRoomService;

    private DofusRoomPreviewMapper dofusRoomPreviewMapper;

    public DofusRoomTrigger(DofusRoomService dofusRoomService, DofusRoomPreviewMapper dofusRoomPreviewMapper){
        this.dofusRoomService = dofusRoomService;
        this.dofusRoomPreviewMapper = dofusRoomPreviewMapper;
        this.dofusRoomUrlPatterns = Constants.DOFUS_ROOM_BUILD_URL.parallelStream()
                .map(url -> Pattern.compile(url + "(\\d+)"))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isTriggered(Message message) {
        return dofusRoomUrlPatterns.parallelStream()
                .map(pattern -> pattern.matcher(message.getContent()).find())
                .reduce(Boolean::logicalOr)
                .orElse(false);
    }

    @Override
    public void execute(Message message) {
        Flux.fromStream(dofusRoomUrlPatterns.parallelStream()
                .map(pattern -> pattern.matcher(message.getContent()))
                .flatMap(this::findAllDofusRoomIds)
                .distinct())
                .flatMap(id -> dofusRoomService.getDofusRoomPreview(id, Constants.DEFAULT_LANGUAGE))
                .filter(preview -> StatusDto.SUCCESS.equals(preview.getStatus()))
                .collectList()
                .zipWith(message.getChannel())
                .flatMapMany(tuple -> Flux.fromIterable(tuple.getT1())
                        .flatMap(preview -> tuple.getT2().createMessage(spec -> dofusRoomPreviewMapper
                                .decorateSpec(spec, preview, Constants.DEFAULT_LANGUAGE))))
                .subscribe();
    }

    private Stream<String> findAllDofusRoomIds(Matcher m){
        List<String> ids = new ArrayList<>();
        while(m.find()) ids.add(m.group(1));
        return ids.parallelStream();
    }
}