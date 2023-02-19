package quarri6343.overcrafted.common.data;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

public class OCSoundData {

    /**
     * 補給所からアイテムを取った時の音
     */
    public static final Sound supplyItemSound = Sound.sound(Key.key("block.composter.empty"), Sound.Source.BLOCK, 1f, 1f);

    /**
     * 補給所を開いた音
     */
    public static final Sound openSupplierSound = Sound.sound(Key.key("block.barrel.open"), Sound.Source.BLOCK, 1f, 1f);

    /**
     * 皿のアイテムをカウンターに提出した音
     */
    public static final Sound submitSound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.BLOCK, 1f, 1f);

    /**
     * ゲームが始まるカウントダウンの音
     */
    public static final Sound countDownSound = Sound.sound(Key.key("block.note_block.bass"), Sound.Source.BLOCK, 1f, 1f);

    /**
     * ゲームが始まった時の音
     */
    public static final Sound gameBeginSound = Sound.sound(Key.key("entity.ender_dragon.ambient"), Sound.Source.AMBIENT, 0.25f, 1f);

    /**
     * ゲームオーバーの音
     */
    public static final Sound gameOverSound = Sound.sound(Key.key("entity.generic.explode"), Sound.Source.AMBIENT, 0.25f, 1f);

    /**
     * ゲームに勝利した音
     */
    public static final Sound victorySound = Sound.sound(Key.key("entity.ender_dragon.death"), Sound.Source.BLOCK, 0.25f, 1f);

    /**
     * テーブルからアイテムを取った音
     */
    public static final Sound tablePickUpSound = Sound.sound(Key.key("entity.item.pickup"), Sound.Source.BLOCK, 1f, 1f);

    /**
     * クラフトしている時の音
     */
    public static final Sound craftingSound = Sound.sound(Key.key("ui.stonecutter.take_result"), Sound.Source.BLOCK, 1f, 1f);

    /**
     * 精錬している時の音
     */
    public static final Sound smeltingSound = Sound.sound(Key.key("block.campfire.crackle"), Sound.Source.BLOCK, 1f, 1f);

    /**
     * 焦げている時の音
     */
    public static final Sound scorchingSound = Sound.sound(Key.key("block.blastfurnace.fire_crackle"), Sound.Source.BLOCK, 1f, 1f);

    /**
     * 焦げている時の警告音
     */
    public static final Sound overcraftAlertSound = Sound.sound(Key.key("block.note_block.didgeridoo"), Sound.Source.BLOCK, 1f, 1f);

    /**
     * 洗浄している時の音
     */
    public static final Sound washingSound = Sound.sound(Key.key("entity.villager.work_leatherworker"), Sound.Source.BLOCK, 1f, 1f);
}
