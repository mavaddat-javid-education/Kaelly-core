package com.github.kaysoro.kaellybot.core.command.model;

import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import org.apache.commons.lang.StringUtils;

public abstract class EmbedCommandArgument extends AbstractCommandArgument {

    public EmbedCommandArgument(Command parent, String subPattern, boolean isDescribed,
                                   Translator translator){
        super(parent, subPattern, isDescribed, PermissionScope.EMBED_PERMISSIONS, translator, Priority.NORMAL);
    }

    public EmbedCommandArgument(Command parent, Translator translator){
        this(parent, StringUtils.EMPTY, false, translator);
    }
}