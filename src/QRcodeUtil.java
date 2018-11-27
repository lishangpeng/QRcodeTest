import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成二维码的工具类
 * author:lsp
 */
public class QRcodeUtil {
    public static void main(String[] args) {
        //链接地址
        //当用户点击删除的时候记录删除的时间
        //进入这个网站的时候过滤器进行判断，当前用户是否已经删除，如果已经删除就显示失效
        //user是数据库中查询
        String user = "lsp";
        String url = "https://www.cnblogs.com/lspa/?user="+user;
        String path = "D:/";
//        String path = FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "testQrcode";
        String fileName = "temp2.jpg";
        createQrCode(url, path, fileName);
    }

    public static String createQrCode(String url, String path, String fileName) {
        try {
            Map<EncodeHintType, String> hints = new HashMap<>();
            //设置为utf-8
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            //二维码的生成
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
            File file = new File(path, fileName);
            //生成地址
            if (file.exists() || ((file.getParentFile().exists() || file.getParentFile().mkdirs()) && file.createNewFile())) {
                //输出二维码
                writeToFile(bitMatrix, "jpg", file);
                System.out.println("搞定：" + file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
}
