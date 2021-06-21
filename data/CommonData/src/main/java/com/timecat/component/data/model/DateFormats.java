package com.timecat.component.data.model;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.text.format.DateFormat.getBestDateTimePattern;

import androidx.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormats {
    @NonNull
    private static SimpleDateFormat fromSkeleton(@NonNull String skeleton,
                                                 @NonNull Locale locale) {
        SimpleDateFormat df = new SimpleDateFormat(skeleton, locale);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df;
    }

    @NonNull
    public static SimpleDateFormat fromSkeleton(@NonNull String skeleton) {
        Locale locale = Locale.getDefault();

        if (SDK_INT >= JELLY_BEAN_MR2)
            skeleton = getBestDateTimePattern(locale, skeleton);

        return fromSkeleton(skeleton, locale);
    }

    public static SimpleDateFormat getBackupDateFormat() {
        return fromSkeleton("yyyy-MM-dd HHmmss", Locale.US);
    }

    public static SimpleDateFormat getCSVDateFormat() {
        return fromSkeleton("yyyy-MM-dd", Locale.US);
    }
}
