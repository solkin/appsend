package com.tomclaw.appsend.util;

import android.text.TextUtils;

import com.tomclaw.appsend.main.item.StoreItem;
import com.tomclaw.appsend.main.meta.Category;

import java.util.Locale;
import java.util.Map;

/**
 * Created by solkin on 14.03.17.
 */
public class LocaleHelper {

    public static String getLocalizedLabel(StoreItem item) {
        String label = item.getLabel();
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        Map<String, String> labels = item.getLabels();
        if (labels != null) {
            String localizedLabel = labels.get(country.toLowerCase(locale));
            if (!TextUtils.isEmpty(localizedLabel)) {
                label = localizedLabel;
            }
        }
        return label;
    }

    public static String getLocalizedName(Category category) {
        String name = category.getDefaultName();
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        Map<String, String> names = category.getNames();
        if (names != null) {
            String localizedName = names.get(country.toLowerCase(locale));
            if (!TextUtils.isEmpty(localizedName)) {
                name = localizedName;
            }
        }
        return name;
    }
}
