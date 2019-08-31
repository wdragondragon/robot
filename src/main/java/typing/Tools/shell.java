package typing.Tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class shell {
    public static String getShellExecuted(String cmnd) {
        InputStream in = null;
        InputStreamReader insr = null;
        BufferedReader br = null;
        StringBuffer buff = new StringBuffer();
        String[] commond = {"/bin/sh","-c",cmnd};
        try {
            Process process = Runtime.getRuntime().exec(commond, null, null);
            process.waitFor();
            in = process.getInputStream();
            insr = new InputStreamReader(in, "GBK");
            br = new BufferedReader(insr);
            while (br.ready()) {
                String line = br.readLine();
                buff.append(line + "\r\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buff.toString();
    }
}
