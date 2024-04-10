package fun.qxfly.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import fun.qxfly.mapper.AliyunDysmsapiMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class AliyunDysmsapi {

    @Autowired
    private AliyunDysmsapiMapper aliyunDysmsapiMapper;

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public int sendCode(String phone) {
        String accessKeyId = System.getProperty("Aliyun_AccessKeyId");
        String accessKeySecret = System.getProperty("Aliyun_AccessKeySecret");
        String signName = System.getProperty("Aliyun_Dysmsapi_SignName");
        String templateCode = System.getProperty("Aliyun_Dysmsapi_TemplateCode");
        try {
            Client client = createClient(accessKeyId, accessKeySecret);
            Random random = new Random();
            int code = random.nextInt(1000, 10000);
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setPhoneNumbers(phone)
                    .setTemplateParam("{\"code\":'" + code + "'}");
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            if (sendSmsResponse.getStatusCode() == 200 && sendSmsResponse.getBody().getCode().equals("OK")) {
                /*发送成功*/
                return code;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 验证验证码，返回值 0、验证码错误，1、验证码正确，-1、验证码不存在
     *
     * @param phone
     * @param code
     * @return
     */
    public int testCode(String phone, Integer code) {
        List<String> listCode = aliyunDysmsapiMapper.getCode(phone);
        if (listCode.size() == 0) return -1;
        for (String s : listCode) {
            if (code.toString().equals(s)) {
                aliyunDysmsapiMapper.deleteCode(phone);
                return 1;
            }
        }
        return 0;
    }
}

