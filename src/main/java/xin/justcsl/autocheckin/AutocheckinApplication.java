package xin.justcsl.autocheckin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@EnableScheduling
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class AutocheckinApplication {

    @Autowired
    private HttpUtil httpUtil;

    public static void main(String[] args) {
        SpringApplication.run(AutocheckinApplication.class, args);
    }

    /**
     * cron = "0 0 1 * * ?" 表示每天凌晨一点执行这个任务
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduled() {
        //先登录鉴权中心
        String ticket = httpUtil.login();
        //校验 ticket Url并更新 cookie
        httpUtil.checkTicket(ticket);
        //打卡
        httpUtil.insertData();
    }

}
