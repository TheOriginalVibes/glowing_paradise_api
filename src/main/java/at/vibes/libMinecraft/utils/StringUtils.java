package at.vibes.libMinecraft.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public final class StringUtils {

    public String[] explode(String delim, String string) {
        return explode(delim, string, true);
    }

    public String[] explode(String delim, String string, boolean keepEmptyParts) {
        int pos, offset = 0, delimLen = delim.length();
        List<String> tokens = new ArrayList<>();
        String part;

        while ((pos = string.indexOf(delim, offset)) > -1) {
            part = string.substring(offset, pos);
            if (part.length() > 0 || keepEmptyParts) {
                tokens.add(part);
            }
            offset = pos + delimLen;
        }
        part = string.substring(offset);
        if (part.length() > 0 || keepEmptyParts) {
            tokens.add(part);
        }

        return tokens.toArray(new String[0]);
    }

    public void parseQueryString(String queryString, Map<String, String> params) {
        if (queryString == null || params == null) {
            return;
        }
        if (queryString.length() > 0) {
            String token;
            int offset;
            StringTokenizer tokenizer = new StringTokenizer(queryString, "&");
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
                if ((offset = token.indexOf("=")) > 0) {
                    params.put(urlDecode(token.substring(0, offset)), urlDecode(token.substring(offset + 1)));
                }else {
                    params.put(urlDecode(token), null);
                }
            }
        }
    }

    public String urlDecode(String string) {
        if (string == null) {
            return null;
        }
        try {
            return URLDecoder.decode(string, StandardCharsets.UTF_8);
        }catch (Exception e) {
            return string;
        }
    }
}