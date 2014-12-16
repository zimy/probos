package me.zimy.probos;

/**
 * @author Dmitriy &lt;Zimy&gt; Yakovlev
 * @since 12/16/14.
 */
public class LRU {
    int size = 4;
    Integer[] cache;
    int[] age;
    int used;

    public LRU(int size) {
        this.size = size;
        cache = new Integer[size];
        age = new int[size];
        for (int i = 0; i < size; i++) {
            age[i] = Integer.MIN_VALUE;
        }
        used = 0;
    }

    public int put(int value) {
        int address = -1;
        for (int i = 0; i < size && address == -1; i++) {
            address = cache[i] != null ? (cache[i] == value ? i : -1) : -1;
        }
        if (address != -1) {
            cache[address] = value;
            return -1;
        } else {
            if (used < size) {
                cache[used] = value;
                return used++;
            } else {
                int max = Integer.MIN_VALUE;
                address = 0;
                for (int i = 0; i < size; i++) {
                    if (age[i] > max) {
                        address = i;
                        max = age[i];
                    }
                    age[i]++;
                }
                cache[address] = value;
                age[address] = Integer.MIN_VALUE;
                return address;
            }
        }
    }

    public Integer[] getCache() {
        return cache;
    }
}
