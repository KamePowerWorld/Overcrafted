package quarri6343.overcrafted.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ファイルのハッシュ値を計算する
 * https://qiita.com/yasushi-jp/items/ac8c7ead98794aed9905
 */
public class HashCalculator {


    /**
     * MD2アルゴリズム
     */
    public static final String MD2 = "MD2";
    /**
     * MD5アルゴリズム
     */
    public static final String MD5 = "MD5";
    /**
     * SHA-1アルゴリズム
     */
    public static final String SHA_1 = "SHA-1";
    /**
     * SHA-256アルゴリズム
     */
    public static final String SHA_256 = "SHA-256";
    /**
     * SHA-512アルゴリズム
     */
    public static final String SHA_512 = "SHA-512";

    /**
     * ファイルのハッシュ値（文字列）を返す
     *
     * @param filePath      ファイルパス
     * @param algorithmName アルゴリズム
     * @return ハッシュ値（文字列）
     */
    public static String getfileHash(String filePath, String algorithmName) {

        Path path = Paths.get(filePath);

        byte[] hash = null;

        // アルゴリズム取得
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try (
                // 入力ストリームの生成
                DigestInputStream dis = new DigestInputStream(
                        new BufferedInputStream(Files.newInputStream(path)), md)) {

            // ファイルの読み込み
            while (dis.read() != -1) {
            }

            // ハッシュ値の計算
            hash = md.digest();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // ハッシュ値（byte）を文字列に変換し返却
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            String hex = String.format("%02x", b);
            sb.append(hex);
        }
        return sb.toString();
    }
}
