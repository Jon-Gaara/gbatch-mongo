package com.example.gbatchmongo.helper;

import com.example.gbatchmongo.model.ItemProperties;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SizeComparator implements Comparator<ItemProperties> {
    private static volatile SizeComparator instance = null;

    private final List<String> sortedStringSize = List.of("xxx-small", "xxxs", "xx-small", "xxs", "x-s", "xs", "s", "ys", "small", "m",
            "ym", "medium", "x-medium", "xx-medium", "xxx-medium", "l", "yl", "large", "xl", "xxl",
            "x-large", "xx-large", "xxx-large", "xxxx-large", "xxxxx-large", "xxxxxx-large");

    private final Pattern numberPattern = Pattern.compile("[0-9]+");

    public static SizeComparator getInstance() {
        if (instance == null) {
            synchronized (SizeComparator.class) {
                if (instance == null) {
                    instance = new SizeComparator();
                }
            }
        }
        return instance;
    }

    @Override
    public int compare(ItemProperties o1, ItemProperties o2) {
        String size1 = o1.getSize();
        String size2 = o2.getSize();
        if (Objects.equals(size1, size2)) {
            return 0;
        }
        Matcher matcher1 = this.numberPattern.matcher(size1);
        Matcher matcher2 = this.numberPattern.matcher(size2);
        if (matcher1.find() && matcher2.find()) {
            do {
                int compare = Integer.parseInt(matcher1.group()) - Integer.parseInt(matcher2.group());
                if (compare != 0) {
                    return compare;
                }
            } while (matcher1.find() && matcher2.find());
            return 0;
        }
        return indexOfSize(size1) - indexOfSize(size2);
    }

    private int indexOfSize(String size) {
        if (size == null)
            return -1;
        size = size.toLowerCase();
        Objects.requireNonNull(size);
        Objects.requireNonNull(this.sortedStringSize);
        return this.sortedStringSize.stream().filter(size::startsWith).findFirst().map(this.sortedStringSize::indexOf).orElse(Integer.valueOf(2147483647)).intValue();
    }
}
