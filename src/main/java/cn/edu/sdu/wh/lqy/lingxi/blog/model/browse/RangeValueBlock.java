package cn.edu.sdu.wh.lqy.lingxi.blog.model.browse;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * 带区间的常用数值定义
 */
public class RangeValueBlock {
    /**
     * 浏览量区间定义
     */
    public static final Map<String, RangeValueBlock> HITS_BLOCK;

    /**
     * 字数区间定义
     */
    public static final Map<String, RangeValueBlock> WORDCNT_BLOCK;

    /**
     * 无限制区间
     */
    public static final RangeValueBlock ALL = new RangeValueBlock("*", -1, -1);

    static {
        WORDCNT_BLOCK = ImmutableMap.<String, RangeValueBlock>builder()
                .put("*-1000", new RangeValueBlock("*-1000", -1, 1000))
                .put("1000-3000", new RangeValueBlock("1000-3000", 1000, 3000))
                .put("3000-*", new RangeValueBlock("3000-*", 3000, -1))
                .build();

        HITS_BLOCK = ImmutableMap.<String, RangeValueBlock>builder()
                .put("*-30", new RangeValueBlock("*-30", -1, 30))
                .put("30-50", new RangeValueBlock("30-50", 30, 50))
                .put("50-*", new RangeValueBlock("50-*", 50, -1))
                .build();
    }

    private String key;
    private int min;
    private int max;

    public RangeValueBlock(String key, int min, int max) {
        this.key = key;
        this.min = min;
        this.max = max;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public static RangeValueBlock matchHits(String key) {
        RangeValueBlock block = HITS_BLOCK.get(key);
        if (block == null) {
            return ALL;
        }
        return block;
    }

    public static RangeValueBlock matchWordCnt(String key) {
        RangeValueBlock block = WORDCNT_BLOCK.get(key);
        if (block == null) {
            return ALL;
        }
        return block;
    }

}