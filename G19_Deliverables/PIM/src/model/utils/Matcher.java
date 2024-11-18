package model.utils;
import java.util.List;

public class Matcher {
    public static boolean match(String str, String pattern){
        List<Token> tokens = Tokenizer.tokenizeStrings(pattern);
        Token.Type lastTokenType = null;

        boolean isMatched = false;
        Token firstToken = tokens.get(0);
        if (firstToken.getType() == Token.Type.String){
            isMatched = str.contains(tokens.get(0).getValue());
        } else if (firstToken.getType() == Token.Type.NegateString) {
            isMatched = !str.contains(tokens.get(0).getValue());
        }

        for (int tokenIdx = 1; tokenIdx < tokens.size(); ++tokenIdx){
            Token token = tokens.get(tokenIdx);
            if (token.getType() == Token.Type.And || token.getType() == Token.Type.Or){
                lastTokenType = token.getType();
            } else if (token.getType() == Token.Type.String) {
                if (lastTokenType == Token.Type.And) {
                    isMatched = isMatched && str.contains(token.getValue());
                } else if (lastTokenType == Token.Type.Or) {
                    isMatched = isMatched || str.contains(token.getValue());
                }
            } else if (token.getType() == Token.Type.NegateString) {
                if (lastTokenType == Token.Type.And) {
                    isMatched = isMatched && !str.contains(token.getValue());
                } else if (lastTokenType == Token.Type.Or) {
                    isMatched = isMatched || !str.contains(token.getValue());
                }
            }
        }
        return isMatched;
    }
}
