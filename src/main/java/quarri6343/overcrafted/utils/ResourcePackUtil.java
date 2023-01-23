package quarri6343.overcrafted.utils;

import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCResourcePackData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * リソースパックを扱う関数を集めたクラス
 */
public class ResourcePackUtil {

    public static boolean isResourcePackReloading = false;

    /**
     * リソースパックを再ダウンロードさせ必要ならプレイヤーに再ダウンロードさせる
     */
    public static void reloadResourcePack() {
        new BukkitRunnable() {

            @Override
            public void run() {
                isResourcePackReloading = true;

                try {
                    downloadFile(OCResourcePackData.packURL, OCResourcePackData.packPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                OCResourcePackData.packHash = HashCalculator.getfileHash(OCResourcePackData.packPath, HashCalculator.SHA_1);
                isResourcePackReloading = false;
            }
        }.runTaskAsynchronously(Overcrafted.getInstance());
    }

    /**
     * ファイルをダウンロード
     *
     * @param link      リンク
     * @param directory 場所
     * @throws IOException
     */
    public static void downloadFile(String link, String directory) throws IOException {
        URL url = new URL(link);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(directory);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }
}
