package quarri6343.overcrafted.core.object;

/**
 * 設定できる数値の上限と下限があるfloat型
 */
public class RangedFloat {

    private final float min;
    private final float max;

    private float value;

    public RangedFloat(float value, float min, float max) {
        this.min = min;
        this.max = max;
        set(value);
    }

    public boolean isValid(float value) {
        return value >= min && value <= max;
    }

    public void set(float value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException();
        }

        this.value = value;
    }

    public float get() {
        return value;
    }
}
