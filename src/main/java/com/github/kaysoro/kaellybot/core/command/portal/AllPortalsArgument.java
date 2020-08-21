package com.github.kaysoro.kaellybot.core.command.portal;

import com.github.kaysoro.kaellybot.core.command.model.AbstractCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.mapper.PortalMapper;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.model.constant.Server;
import com.github.kaysoro.kaellybot.core.service.PortalService;
import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

@Component
@Qualifier(PortalCommand.COMMAND_QUALIFIER)
public class AllPortalsArgument extends AbstractCommandArgument {

    private final PortalService portalService;
    private final PortalMapper portalMapper;

    public AllPortalsArgument(@Qualifier(PortalCommand.COMMAND_QUALIFIER) Command parent, PortalService portalService,
                              PortalMapper portalMapper, Translator translator){
        super(parent, "\\s+(\\w+)", true, PermissionScope.EMBED_PERMISSIONS, translator);
        this.portalService = portalService;
        this.portalMapper = portalMapper;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Matcher matcher) {
       // TODO determine server in the message
        Server server = Server.MERIANA;
        return translator.getLanguage(message)
                .flatMapMany(language -> portalService.getPortals(server, language)
                        .flatMap(portal -> message.getChannel().flatMap(channel -> channel
                                .createEmbed(spec -> portalMapper.decorateSpec(spec, portal, language)))))
                .onErrorResume(error -> manageUnknownException(message, error));
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " server` : "
                + translator.getLabel(lg, "pos.all_portals");
    }
}