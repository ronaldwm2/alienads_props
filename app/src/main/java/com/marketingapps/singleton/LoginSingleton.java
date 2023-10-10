package com.marketingapps.singleton;

public class LoginSingleton {
    private static LoginSingleton single_instance = null;

    public boolean isLogin;

    private LoginSingleton()
    {
        isLogin = false;
    }

    // Static method
    // Static method to create instance of Singleton class
    public static synchronized LoginSingleton getInstance()
    {
        if (single_instance == null)
            single_instance = new LoginSingleton();

        return single_instance;
    }

    public Boolean getLogin() {
        return this.isLogin;
    }

    public void setLogin(Boolean val) {
        LoginSingleton.getInstance().isLogin = val;
    }
}
