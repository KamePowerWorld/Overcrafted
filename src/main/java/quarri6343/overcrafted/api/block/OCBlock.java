package quarri6343.overcrafted.api.block;

import lombok.Getter;
import org.bukkit.Material;
import quarri6343.overcrafted.Overcrafted;

public class OCBlock implements IOCBlock{

    @Getter
    private final Material material;
    
    public OCBlock(Material material){
        if(!material.isBlock())
            Overcrafted.getInstance().getLogger().severe("ブロッククラスのベースにアイテムは登録できません");
        
        this.material = material;
    }
}
