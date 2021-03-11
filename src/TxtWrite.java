
import java.io.FileWriter;
import java.io.IOException;

public class TxtWrite {
//	private static String filePath = "D:\\poi2.txt";

    public static void saveAsFileWriter(String content,String filePath) {
        FileWriter fwriter = null;
        try {
            // true��ʾ������ԭ�������ݣ����Ǽӵ��ļ��ĺ��档��Ҫ����ԭ�������ݣ�ֱ��ʡ����������ͺ�
            fwriter = new FileWriter(filePath,true);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
