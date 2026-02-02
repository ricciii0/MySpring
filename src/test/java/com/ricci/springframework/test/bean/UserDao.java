package com.ricci.springframework.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("21322", "Ricci");
        hashMap.put("09022", "Asuka");
        hashMap.put("32243", "Beacon");
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }
}
