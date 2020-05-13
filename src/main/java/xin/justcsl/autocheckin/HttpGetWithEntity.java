package xin.justcsl.autocheckin;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * 打卡系统很坑，使用带 body 的 get 请求提交数据，专门创建一个带请求体的 HttpGet 对象
 *
 * @author just
 */
public class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {

    private final static String METHOD_NAME = "GET";

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    HttpGetWithEntity(final String uri) {
        super();
        setURI(URI.create(uri));
    }
}