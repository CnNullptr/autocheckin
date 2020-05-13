package xin.justcsl.autocheckin;

public class Contants {

    //todo 填写事务中心账户名和密码
    public static final String USERNAME = "";
    public static final String PASSWORD = "";

    /**
     * 登录系统 URL
     */
    public static final String LOGIN_URL = "http://ids2.just.edu.cn/cas/login";

    /**
     * 打卡系统基础 URL
     */
    public static final String SYSTEM_BASE_URL = "http://ehall.just.edu.cn/default/work/jkd/jkxxtb";

    /**
     * UPDATE 命令（或许不叫命令）
     */
    public static final String UPDATE_PARAM = "com.sudytech.portalone.base.db.saveOrUpdate.biz.ext";

    //todo 填写打卡内容的字段，这部分建议使用抓包软件打一次卡看看具体内容
    public static final String UPDATE_JSON = "";
}
