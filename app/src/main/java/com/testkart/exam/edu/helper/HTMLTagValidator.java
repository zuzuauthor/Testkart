package com.testkart.exam.edu.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by testkart on 2/5/17.
 */

public class HTMLTagValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String HTML_TAG_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
    private static final String HTML_TAG_PATTERN2 = "<(?=.*? .*?\\/ ?>|br|hr|input|!--|wbr)[a-z]+.*?>|<([a-z]+).*?<\\/\\1>";

    public HTMLTagValidator(){
        pattern = Pattern.compile(HTML_TAG_PATTERN2);
    }

    /**
     * Validate html tag with regular expression
     * @param tag html tag for validation
     * @return true valid html tag, false invalid html tag
     */
    public boolean validate(final String tag){

        matcher = pattern.matcher(tag);
        return matcher.matches();

    }
}
