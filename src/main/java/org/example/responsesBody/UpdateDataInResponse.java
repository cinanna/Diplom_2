package org.example.responsesBody;

import java.util.List;

public class UpdateDataInResponse {
    private boolean success;
    private List<UserDataInResponse> users;

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
}
