package xin.justcsl.autocheckin;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Component
public class HttpUtil {

    private final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    private final CookieStore cookieStore = new BasicCookieStore();

    private final CloseableHttpClient httpClient =
            HttpClients.custom().setDefaultCookieStore(cookieStore).build();

    private HtmlUtil htmlUtil;

    private MailService mailService;

    private BodyUtil bodyUtil;

    @Autowired
    public void setHtmlUtil(HtmlUtil htmlUtil) {
        this.htmlUtil = htmlUtil;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setBodyUtil(BodyUtil bodyUtil) {
        this.bodyUtil = bodyUtil;
    }

    /**
     * 进行登录，登录成功返回 true、失败返回 false
     */
    public String login() {
        //不采用 %2F 转码的方式会出问题
        String loginUrl = Contants.LOGIN_URL + "?service=http%3A%2F%2Fehall.just.edu.cn%2Fdefault%2Fwork%2Fjkd%2Fjkxxtb%2Fjkxxcj.jsp";
        HttpGet get = new HttpGet(loginUrl);
        try (CloseableHttpResponse getResponse = httpClient.execute(get)) {
            if (getResponse.getStatusLine().getStatusCode() == 200) {
                String html = EntityUtils.toString(getResponse.getEntity());
                String execution = htmlUtil.getExecution(html);

                HttpPost post = new HttpPost(loginUrl);
                //表单参数
                List<NameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("username", Contants.USERNAME));
                list.add(new BasicNameValuePair("password", Contants.PASSWORD));
                list.add(new BasicNameValuePair("execution", execution));
                list.add(new BasicNameValuePair("_eventId", "submit"));
                list.add(new BasicNameValuePair("loginType", "1"));
                list.add(new BasicNameValuePair("submit", "%E7%99%BB+%E5%BD%95&"));
                HttpEntity entity = new UrlEncodedFormEntity(list);
                post.setEntity(entity);
                //执行发送，获取相应结果
                try (CloseableHttpResponse postResponse = httpClient.execute(post)) {
                    if (postResponse.getStatusLine().getStatusCode() == 302) {
                        String url = postResponse.getHeaders("Location")[0].getValue();
                        if (url.contains("ticket")) {
                            return url;
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            mailService.sendEmail(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 携带登录后返回的 ticket 进行校验、更新 cookie 之后才能向服务器提交数据
     */
    public void checkTicket(String ticketUrl) {
        try {
            if (ticketUrl == null) {
                throw new Exception("ticket为空");
            }
            HttpGet get = new HttpGet(ticketUrl);
            try (CloseableHttpResponse response = httpClient.execute(get)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new Exception("未知错误");
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            mailService.sendEmail(e.getLocalizedMessage());
        }
    }

    /**
     * 提交打卡记录
     */
    public void insertData() {
        HttpGetWithEntity get = new HttpGetWithEntity(Contants.SYSTEM_BASE_URL + '/' + Contants.UPDATE_PARAM);

        String body = bodyUtil.updateBody();
        StringEntity entity = new StringEntity(body, StandardCharsets.UTF_8);
        get.setHeader("Content-type", "application/json");
        get.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(get)) {
            String str = EntityUtils.toString(response.getEntity());
            log.info(str);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            mailService.sendEmail(e.getLocalizedMessage());
        }
    }
}
