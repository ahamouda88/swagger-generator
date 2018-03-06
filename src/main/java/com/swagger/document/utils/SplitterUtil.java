package com.swagger.document.utils;

import com.google.common.base.Splitter;

public interface SplitterUtil {

    Splitter COMMA_SEP = Splitter.on(",").omitEmptyStrings().trimResults();
}
