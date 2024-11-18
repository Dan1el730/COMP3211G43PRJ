package model.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tokenizer {
    public static List<Token> tokenizeStrings(String pattern){
        String[] stringParts = pattern.split("(&&|\\|\\|)");
        String[] operatorParts = pattern.split("(\\w+|!)");

        stringParts = Arrays.stream(stringParts)
                .map(String::trim)
                .toArray(String[]::new);

        operatorParts = Arrays.stream(operatorParts)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        List<Token> tokenList = new ArrayList<>();

        int totalLength = stringParts.length + operatorParts.length;
        for (int i = 0; i < totalLength; ++i){
            if (i % 2 == 0) {
                int stringPartsIdx = i / 2;
                String part = stringParts[stringPartsIdx];
                tokenList.add(new Token(part));
            } else {
                int operatorPartsIdx = (i - 1) / 2;
                String part = operatorParts[operatorPartsIdx];
                if (part.equals("&&")){
                    tokenList.add(new Token(Token.Type.And));
                } else if (part.equals("||")) {
                    tokenList.add(new Token(Token.Type.Or));
                }
            }
        }

        return tokenList;
    };
}
