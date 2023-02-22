package quarri6343.overcrafted.api.item;

/**
 * 提出してスコアに変換可能なアイテム
 */
public interface ISubmittableOCItem {

    /**
     * 提出した時のスコア
     * @return スコア
     */
    int getScore();

    /**
     * アイテムを表すunicode
     * @return unicode
     */
    String toMenuUnicode();
}
