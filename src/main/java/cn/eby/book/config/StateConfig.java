package cn.eby.book.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 朱胜伟
 * 书状态
 */
public class StateConfig {
    public static Map<Integer,String> map;
    static {
        map = new HashMap<>();
        map.put(-1,"不允许外借");
        map.put(0,"在馆");
        map.put(1,"已外借");
        map.put(2,"已续借");
        map.put(3,"已预借");
        map.put(4,"已丢失,可连接管理员操作");
        map.put(5,"已报损,可连接管理员操作");
        map.put(6,"损坏不可用,可连接管理员操作");
        map.put(7,"丢失撤销");

        map.put(8,"此书在数据库已删除，可连接管理员操作");
        map.put(9,"借阅成功");

    }
}
