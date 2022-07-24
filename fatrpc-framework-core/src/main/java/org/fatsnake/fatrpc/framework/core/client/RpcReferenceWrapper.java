package org.fatsnake.fatrpc.framework.core.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/16 12:17 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class RpcReferenceWrapper<T> {
    private Class<T> aimClass;

    private Map<String, Object> attatchments = new ConcurrentHashMap<>();

    public Class<T> getAimClass() {
        return aimClass;
    }

    public void setAimClass(Class<T> aimClass) {
        this.aimClass = aimClass;
    }

    /**
     * 设置容错策略
     *
     * @param tolerant
     */
    public void setTolerant(String tolerant){
        this.attatchments.put("tolerant",tolerant);
    }

    /**
     * 失败重试次数
     */
    public int getRetry(){
        if(attatchments.get("retry")==null){
            return 0;
        }else {
            return (int) attatchments.get("retry");
        }
    }

    public void setRetry(int retry){
        this.attatchments.put("retry",retry);
    }
    public boolean isAsync(){
        Object r = attatchments.get("async");
        if (r == null || r.equals(false)) {
            return false;
        }
        return Boolean.valueOf(true);
    }

    public void setAsync(boolean async){
        this.attatchments.put("async",async);
    }

    public String getUrl(){
        return String.valueOf(attatchments.get("url"));
    }

    public void setUrl(String url){
        attatchments.put("url",url);
    }

    public String getServiceToken(){
        return String.valueOf(attatchments.get("serviceToken"));
    }

    public void setServiceToken(String serviceToken){
        attatchments.put("serviceToken",serviceToken);
    }

    public String getGroup(){
        return String.valueOf(attatchments.get("group"));
    }

    public void setGroup(String group){
        attatchments.put("group",group);
    }

    public Map<String, Object> getAttatchments() {
        return attatchments;
    }

    public void setAttatchments(Map<String, Object> attatchments) {
        this.attatchments = attatchments;
    }
    public void setTimeOut(int timeOut) {
        attatchments.put("timeOut", timeOut);
    }

    public String getTimeOut() {
        return String.valueOf(attatchments.get("timeOut"));

    }
}
