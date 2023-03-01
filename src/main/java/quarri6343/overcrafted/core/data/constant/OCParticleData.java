package quarri6343.overcrafted.core.data.constant;

import org.bukkit.Particle;

/**
 * パーティクルのデータクラス
 */
public class OCParticleData {

    /**
     * プレイヤーがクラフトした時のパーティクル
     */
    public static final Particle craftParticle = Particle.SWEEP_ATTACK;

    /**
     * プレイヤーが何かを洗った時のパーティクル
     */
    public static final Particle washingParticle = Particle.WATER_SPLASH;

    /**
     * プレイヤーが精錬した時のパーティクル
     */
    public static final Particle smeltingParticle = Particle.ASH;

    /**
     * プレイヤーが加工しすぎた時のパーティクル
     */
    public static final Particle overcraftParticle = Particle.SMOKE_NORMAL;

    /**
     * プレイヤーが鍛造した時のパーティクル
     */
    public static final Particle forgingParticle = Particle.SMOKE_NORMAL;

    /**
     * プレイヤーがエンチャントした時のパーティクル
     */
    public static final Particle enchantingParticle = Particle.ENCHANTMENT_TABLE;
}
