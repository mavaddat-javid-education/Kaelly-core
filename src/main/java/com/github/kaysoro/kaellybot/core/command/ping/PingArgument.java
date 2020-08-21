package com.github.kaysoro.kaellybot.core.command.ping;

import com.github.kaysoro.kaellybot.core.command.model.TextCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;

@Component
@Qualifier(PingCommand.COMMAND_QUALIFIER)
public class PingArgument extends TextCommandArgument {

    public PingArgument(@Qualifier(PingCommand.COMMAND_QUALIFIER) Command parent, Translator translator) {
        super(parent, translator);
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Matcher matcher) {
        return message.getChannel()
                .flatMap(chan -> chan.createMessage(
                        Math.abs(Duration.between(message.getTimestamp(), Instant.now()).toMillis()) + "ms!"))
                .flatMapMany(Flux::just);
    }
}