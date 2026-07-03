package com.shinhan.bananaapp.section1;

public class TVFactory {
    public static TV makeTV(String brand){
        TV tv = null;
        if(brand.equals("S")){
            tv = new SamsungTV();
        }else if(brand.equals("LG")){
            tv = new LgTV();
        }else{
            tv = null;
        }
        return tv;
    }
}
