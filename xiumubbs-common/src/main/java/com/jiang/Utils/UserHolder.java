package com.jiang.Utils;

import com.jiang.entity.dto.SessionWebUserDto;

public class UserHolder {
    private static final ThreadLocal<SessionWebUserDto> tl = new ThreadLocal<>();

    public static void saveUserDto(SessionWebUserDto sessionWebUserDto){
        tl.set(sessionWebUserDto);
    }

    public static SessionWebUserDto getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
