package com.dhq.dimen.jar;

import com.dhq.dimen.entity.DimenEntity;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * 读取配置文件
 * Created by Administrator on 2017/5/29 0029.
 */
public class PropertyUtils {

    /**
     * 读取配置文件
     */
    public static DimenEntity readProperties() {
        DimenEntity  dimenEntity=new DimenEntity();
        Properties prop = new Properties();
        try {
            //读取属性文件a.properties
            InputStream in = new BufferedInputStream(new FileInputStream("dimen.properties"));
            prop.load(in);     ///加载属性列表

            //UI设计的尺寸大小
            String designPx = prop.getProperty("designPx");
            //目标尺寸大小
            String targetDp = prop.getProperty("targetDp");
            //目标尺寸大小
            String startName = prop.getProperty("startName");
            //目标尺寸大小
            String textName = prop.getProperty("textName");

            dimenEntity.designPx=designPx;
            dimenEntity.targetDp=targetDp;
            dimenEntity.startName=startName;
            dimenEntity.textName=textName;

//            //遍历属性列表
//            Iterator<String> it = prop.stringPropertyNames().iterator();
//            while (it.hasNext()) {
//                String key = it.next();
//                System.out.println(key + ":" + prop.getProperty(key));
//            }
//            in.close();

//            ///保存属性到b.properties文件
//            FileOutputStream oFile = new FileOutputStream("b.properties", true);//true表示追加打开
//            prop.setProperty("phone", "10086");
//            prop.store(oFile, "The New properties file");
//            oFile.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return dimenEntity;
    }


}
