package xin.justcsl.autocheckin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class BodyUtil {

    public String dateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(dateTimeFormatter);
    }

    public String updateBody() {
        JSONObject jsonObject = JSON.parseObject(Contants.UPDATE_JSON);

        JSONObject entity = jsonObject.getJSONObject("entity");

        entity.put("tjsj", dateTimeToString(LocalDateTime.now()));
        entity.put("tbrq", LocalDate.now().toString());
        return jsonObject.toJSONString();
    }
}
