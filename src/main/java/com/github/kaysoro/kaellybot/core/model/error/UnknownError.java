package com.github.kaysoro.kaellybot.core.model.error;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.Translator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnknownError implements Error {

    public String getLabel(Translator translator, Language language){
        return translator.getLabel(language, "error.unknown");
    }
}