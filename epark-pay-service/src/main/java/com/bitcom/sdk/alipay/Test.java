package com.bitcom.sdk.alipay;

import com.bitcom.sdk.alipay.model.builder.AlipayTradePayRequestBuilder;
import com.bitcom.sdk.alipay.model.result.AlipayF2FPayResult;

public class Test {
    public static void main(String[] args) {
        String APPID = "2015111000745825";

        String private_key_rsa2 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCHjygPQTevD8aj77w5vWAvNyczq1E4us+I7RD1nXVhE5YNvMJ8zdxuCP2UH0YVThXb4Hr3xeSw2rBrxwzuuX5b83qJR7um2Jq+YES2KayEC9mqOQyAnF2dQU2o5eREXf4SHQo1iimvnsP/jHHwPf71EGRyYfiE63RYmb0zkTrHdbS7QGS97k5ZN0vn/rxYfbaHIeU+qRlIRynWbd76FEbMRL+XJzl+weKvgIXIAgSQy5+oFpKIFTMaKgxmwoTGcta3+43yjF56w9yFafyYc2iue+z0qwcYIBIQs+k1/6mYZForG5a10V6tN0H7FlI7gMX1P+QyuHv3zj/PLiDLEDtNAgMBAAECggEAEoh/8ZX5BvYhMkiUc1Bw1gO0WFwTvq3Lsey3ZN0iLyN+YGJuNKHwgRq5kIa2DjOeyXDnXvivS36aoCGWowy5OdkP57Tm8uiqP1zqB/2qmGMGXz9tdifqpJVJCM5lZvNyyQcvXiKFkH0kTsvkx/Ox589V7E14rLOENJjQRTyVzK8YCgl8EySHf0w6DrngDDrIeTcjt/hxoJTis5bYf27RwSWWNGDQOfKbwxv8S7JQUD8qvNdpiJHviG3FZf/73nP6JAvILBncJbRfZWYUEBi7jKr4xTR3s9w9BSrouUC5vbS7wLTTjkG8spRRbVaV/8E/OfYDqopd0szY0V9lbJJUAQKBgQDCkGO7w5LWc4g8AJf8/4pzp/9ofXYU+KbHxFy/DQH+2SZpAfg3wHE9yzbNERNlo/zXggtitTvtKBHwXEOKsYMmF5FTDJYfQxw49gDJHdFWxlJd1XbyCo9SdFdnzwA4hpiI9oZS0J/0fpVenUST49opWgKAIwN3lCRKrfpD1wyu/wKBgQCyXRqoKPf236JztIvPoVOVgA9f+uive1N9QctLLNzC7A02wVNyWamfsyb+5SStev53zjX54ZtT0ivUKW2NU5G66WN7IVUY1HCi0uljTXeuGG7jsDJXjtMCUD/EBrpT23CTVfAdaD3m29xJuBrKgQs2V+Pb0SJ+EfzTWhxayF0hswKBgQCuobjSxBSc5W/HI2VIEPltG8H0QH/nDNNthIMWfA9pdfCy2bC4IcPCig5y+zukxA7iQ/gCLddO4uNPERESgIfNfePTXf20q6C+Q5eq7FeEAlfVnw626uF/SatWIob5NV3duSARXu3jHmblWFVARIgBC6go6MRo5Q+58/QoRfI9cwKBgHjg7lLuyPYPo6okvuftHpdgHYtfoVhkDTYDZAJDmi2kCmSEashBidqIcLIom03S4ChToXMC+eC5vChiFzPDYi05RMlK0sAsIVXH+JHrC2P6fRRj8goy6kBUIJL6OEbing3MHRng7qrJcCap1cZN5EplEDCGMIkeKakXFcZUqgFPAoGAYv0QGb6IaTBHxvIoy9p9MFOWllAZjqx9GEuHcdkvFEHyTKGC0tH+d8bKCLbDZTBj97q0Z1oTyKntrhMVqoV54iEaOMoIBjC0dTAxOwbBFAu5crsHRikJ2AnhrsGPY6naPfu7i9hEQGmxQxThSdO2LcOaSFW0TB6Ww0TPHiZDJsY=";

        String aliPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuc9T9hPOUE+Hd9oM8sRqqjkISh6PA1d/ZeCZyw6cvORSKt9Fv8tw+HKjxzA0iRnsSHAWwTGswUHUdIj231G/KzcWsC5OqErFxc0zDEhHD6txYOjcqMgeCbWQ4sr/GbEnox7AA9wPFcDEIDrL9LJanSHFzKYIilPfoezJSXeITrepc7sUpXiFAUbJk84AT8EZElqPx4nhSVD3eMsp7UBZuUJR8y1Vop9hGWH4kbTAH8BD5+xc7UzQYyv07Fgrd4LKla6MD9vF0VWYQvB0mDW+mT83Cnkb4eC+yVKt924bzDLXROLnPYxilaL/29g50mT3qi0ehNsptuQYqUNkUfUVMwIDAQAB";


        String appid = APPID;
        String privateKey = private_key_rsa2;

        AliPay.initSDKConfiguration(appid, privateKey, aliPubKey);

        String totalMoney = "20000";

        String outTradeNo = "201807071630307611";
        String subject = "停车费";
        String body = "停车费：" + totalMoney + "元";
        String timeoutExpress = "5m";

        String authCode = "283581073346834535";


        try {
            AlipayF2FPayResult result = AliPay.doScanPayBusiness((new AlipayTradePayRequestBuilder())
                    .setAuthCode(authCode).setOutTradeNo(outTradeNo).setSubject(subject).setStoreId("bitcom")
                    .setTotalAmount(totalMoney).setBody(body).setTimeoutExpress(timeoutExpress));

            if (result.isTradeSuccess() == true) {
                System.out.println("success");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}



