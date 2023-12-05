package com.unimelb.swen90007.reactexampleapi.api.objects.Token;

public class Token {
    private String accessToken;
    private String refreshTokenId;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshTokenId;
    }

    public void setRefreshToken(String refreshTokenId) {
        this.refreshTokenId = refreshTokenId;
    }
}
