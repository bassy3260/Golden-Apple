package com.aaa.aaa;

public class MemberInfo {

    public String name;
    public int phone_number;
    public String uid;


    //초기화
    public MemberInfo(String name, int phone_number,String uid){
        this.name=name;
        this.phone_number=phone_number;
        this.uid=uid;
    }

    //getter, setter
    public String getName(){
        return this.name;
    }

    public void setName(){
        this.name=name;
    }
    public int getPhone_number(){
        return this.phone_number;
    }

    public void setPhone_number(){
        this.phone_number=phone_number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
