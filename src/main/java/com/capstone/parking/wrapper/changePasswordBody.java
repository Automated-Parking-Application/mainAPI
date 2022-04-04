package com.capstone.parking.wrapper;

public class changePasswordBody {
    private String password;
    private String newPassword;
    public String getPassword() {
        return this.password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
