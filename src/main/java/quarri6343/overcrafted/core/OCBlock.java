package quarri6343.overcrafted.core;

import lombok.Getter;
import org.bukkit.Material;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.block.IOCBlock;

public class OCBlock implements IOCBlock {

    /**
     * 機能を追加したいバニラブロック
     */
    @Getter
    private final Material material;
    
    public OCBlock(Material material){
        if(!material.isBlock())
            Overcrafted.getInstance().getLogger().severe("ブロッククラスのベースにアイテムは登録できません");
        
        this.material = material;
    }
}
