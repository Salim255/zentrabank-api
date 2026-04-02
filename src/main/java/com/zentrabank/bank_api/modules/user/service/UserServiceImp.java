package com.zentrabank.bank_api.modules.user.service;

public class UserServiceImp implements UserService {
    @Override
    public String getUser() {
        return "";
    }

    @Override
    public  String updateUser(){
        return "Hello from update user";
    }

    @Override
    public  String deleteUser(){
        return "hello from delete user";
    }
}