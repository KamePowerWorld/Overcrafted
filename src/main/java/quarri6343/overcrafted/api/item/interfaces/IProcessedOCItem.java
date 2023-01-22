package quarri6343.overcrafted.api.item.interfaces;

import org.bukkit.Material;
import quarri6343.overcrafted.impl.item.OCItems;

public interface IProcessedOCItem {
    
    public OCItems getIngredient();
    
    public ProcessType getType();
    
    public enum ProcessType{
        CRAFTING(Material.CRAFTING_TABLE),
        SMELTING(Material.FURNACE);
        
        private final Material processBlock;
        
        ProcessType(Material processBlock){
            this.processBlock = processBlock;
        }
        
        public Material getProcessBlock(){
            return processBlock;
        }
    }
}
