package quarri6343.overcrafted.utils;

import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class ResourcePackUtil {
    
    public static boolean isResourcePackReloading = false;
    
    public static void reloadResourcePack(){
        new BukkitRunnable(){

            @Override
            public void run() {
                isResourcePackReloading = true;
                
                try{
                    downloadFile(OCData.resourcePackURL, OCData.resourcePackPath);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                OCData.resourcePackHash = HashCalculator.getfileHash(OCData.resourcePackPath,HashCalculator.SHA_1);
                isResourcePackReloading = false;
            }
        }.runTaskAsynchronously(Overcrafted.getInstance());
    }

    /**
     * ファイルをダウンロード
     * @param link
     * @param directory
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
