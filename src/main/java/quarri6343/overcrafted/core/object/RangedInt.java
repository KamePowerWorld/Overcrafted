package quarri6343.overcrafted.core.object;

/**
 * 設定できる数値の上限と下限があるint型
 */
public class RangedInt {

    private final int min;
    private final int max;

    private int value;

    public RangedInt(int value, int min, int max) {
        this.min = min;
        this.max = max;
        set(value);
    }

    public boolean isValid(int value) {
        return value >= min && value <= max;
    }

    public void set(int value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException();
        }

        this.value = value;
    }

    public int get() {
        return value;
    }
}
