package com.github.kaysoro.kaellybot.core.command.classic;

import com.github.kaysoro.kaellybot.core.command.argument.model.BasicCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Donator;
import com.github.kaysoro.kaellybot.core.util.Translator;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class DonateCommand extends AbstractCommand {

    public DonateCommand() {
        super("donate");

        getArguments().add(new BasicCommandArgument(this,
                message -> message.getChannel()
                        .flatMap(chan -> chan.createEmbed(spec -> {
                            spec.setTitle(Translator
                                    .getLabel(Constants.DEFAULT_LANGUAGE, "about.title")
                                    .replace("{name}", Constants.NAME)
                                    .replace("{version}", Constants.VERSION))
                                    .setDescription(Translator
                                            .getLabel(Constants.DEFAULT_LANGUAGE, "about.desc")
                                            .replace("{game}", Constants.GAME.getName()))
                                    .setColor(Constants.COLOR)
                                    .setThumbnail(Constants.AVATAR);
                            Stream.of(Donator.values())
                                    .forEach(donator -> spec.addField(donator.getName(), Translator
                                            .getLabel(Constants.DEFAULT_LANGUAGE, "donator."
                                                    + donator.name().toLowerCase() + ".desc"), false));
                        }))
                        .subscribe()));
    }
}