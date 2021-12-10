import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest {

    public static void main(String[] args) {
        String REGEX = "action='(.*?)'";
        String INPUT = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "\t<head>\n" +
                "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "\t\t<script language=javascript>\n" +
                "\t\t\tfunction postdo(){\n" +
                "\t\t\t\tdocument.jhform.submit();\n" +
                "\t\t\t}\n" +
                "\t\t</script>\n" +
                "\t</head>\n" +
                "\t<body onload=\"postdo();\">\n" +
                "\t\t<form name=\"jhform\" action='https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_08_EPAY?CLIENTIP=&BRANCHID=370000000&PAYMAP=0000000000&REGINFO=&CURCODE=01&PROINFO=&ORDERID=202110281044256366&REFERER=&TYPE=1&REMARK1=&REMARK2=&TXCODE=520100&CCB_IBSVersion=V6&GATEWAY=0&MERCHANTID=105000075235505&POSID=056339629&PAYMENT=0.01&MAC=f166aba402aa7854ead44fc77234f611&QRCODE=1&CHANNEL=1' method=\"post\">\n" +
                "\t\t</form>\n" +
                "\t</body>\n" +
                "</html>";
        Pattern p = Pattern.compile(REGEX);
        // 获取 matcher 对象
        Matcher m = p.matcher(INPUT);
        System.out.println(m.find());
        System.out.println(m.group(1));
    }
}
