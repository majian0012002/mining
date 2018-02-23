package com.miller.mining.service;

public interface JedisService {

    public boolean set(String key,String value);

    public String get(String key);

    public boolean expire(String key,long expire);
}
