package site.nomoreparties.stellarburgers.responses;

import java.util.List;

public class LoginDataInResponse {
    private boolean success;
    private List<UserDataInResponse> users;
    private String accessToken;
    private String refreshToken;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<UserDataInResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserDataInResponse> users) {
        this.users = users;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
