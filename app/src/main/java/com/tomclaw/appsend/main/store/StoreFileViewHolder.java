package com.tomclaw.appsend.main.store;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomclaw.appsend.R;
import com.tomclaw.appsend.core.GlideApp;
import com.tomclaw.appsend.main.adapter.files.FileViewHolder;
import com.tomclaw.appsend.main.adapter.files.FilesListener;
import com.tomclaw.appsend.main.item.StoreItem;
import com.tomclaw.appsend.util.FileHelper;
import com.tomclaw.appsend.util.LocaleHelper;

import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.tomclaw.appsend.main.item.StoreItem.NOT_INSTALLED;
import static com.tomclaw.appsend.main.ratings.RatingsListener.STATE_FAILED;
import static com.tomclaw.appsend.main.ratings.RatingsListener.STATE_LOADED;
import static com.tomclaw.appsend.main.ratings.RatingsListener.STATE_LOADING;
import static java.util.concurrent.TimeUnit.SECONDS;

public class StoreFileViewHolder extends FileViewHolder<StoreItem> {

    private View itemView;
    private View appCard;
    private ImageView appIcon;
    private TextView appName;
    private TextView appVersion;
    private TextView appSize;
    private TextView appRating;
    private View ratingIcon;
    private TextView appDownloads;
    private View downloadsIcon;
    private TextView appBadge;
    private View badgeNew;
    private View viewProgress;
    private View errorView;
    private View buttonRetry;

    public StoreFileViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        appCard = itemView.findViewById(R.id.app_card);
        appIcon = itemView.findViewById(R.id.app_icon);
        appName = itemView.findViewById(R.id.app_name);
        appVersion = itemView.findViewById(R.id.app_version);
        appSize = itemView.findViewById(R.id.app_size);
        appRating = itemView.findViewById(R.id.app_rating);
        ratingIcon = itemView.findViewById(R.id.rating_icon);
        appDownloads = itemView.findViewById(R.id.app_downloads);
        downloadsIcon = itemView.findViewById(R.id.downloads_icon);
        appBadge = itemView.findViewById(R.id.app_badge);
        badgeNew = itemView.findViewById(R.id.badge_new);
        viewProgress = itemView.findViewById(R.id.item_progress);
        errorView = itemView.findViewById(R.id.error_view);
        buttonRetry = itemView.findViewById(R.id.button_retry);
    }

    @Override
    public void bind(final StoreItem item, boolean isLast, final FilesListener<StoreItem> listener) {
        appCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(item);
            }
        });
        GlideApp.with(itemView.getContext())
                .load(item.getIcon())
                .placeholder(R.drawable.app_placeholder)
                .into(appIcon);

        appName.setText(LocaleHelper.getLocalizedLabel(item));
        appVersion.setText(item.getVersion());
        appSize.setText(FileHelper.formatBytes(itemView.getResources(), item.getSize()));
        boolean isInstalled = (item.getInstalledVersionCode() != NOT_INSTALLED);
        boolean isMayBeUpdated = (item.getVersionCode() > item.getInstalledVersionCode());
        boolean isShowDownloads = item.getDownloads() > 0;
        boolean hasRating = item.getRating() > 0;
        if (isShowDownloads) {
            appDownloads.setText(String.valueOf(item.getDownloads()));
        }
        if (isInstalled && isMayBeUpdated) {
            appBadge.setText(R.string.store_app_update);
        } else if (isInstalled) {
            appBadge.setText(R.string.store_app_installed);
        }
        if (hasRating) {
            appRating.setText(String.valueOf(item.getRating()));
        }
        appRating.setVisibility(hasRating ? VISIBLE : GONE);
        ratingIcon.setVisibility(hasRating ? VISIBLE : GONE);
        appDownloads.setVisibility(isShowDownloads ? VISIBLE : GONE);
        downloadsIcon.setVisibility(isShowDownloads ? VISIBLE : GONE);
        appBadge.setVisibility(isInstalled ? VISIBLE : GONE);

        long appInstallDelay = System.currentTimeMillis() - SECONDS.toMillis(item.getTime());
        boolean isNewApp = appInstallDelay > 0 && appInstallDelay < TimeUnit.DAYS.toMillis(1);
        badgeNew.setVisibility(isNewApp ? VISIBLE : GONE);
        boolean isProgress = false;
        boolean isError = false;
        if (isLast) {
            int result = listener.onNextPage();
            switch (result) {
                case STATE_LOADING:
                    isProgress = true;
                    isError = false;
                    break;
                case STATE_FAILED:
                    isProgress = false;
                    isError = true;
                    break;
                case STATE_LOADED:
                    isProgress = false;
                    isError = false;
                    break;
            }
            viewProgress.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
        }
        viewProgress.setVisibility(isProgress ? View.VISIBLE : View.GONE);
        errorView.setVisibility(isError ? View.VISIBLE : View.GONE);
        if (isError) {
            buttonRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRetry();
                }
            });
        } else {
            buttonRetry.setOnClickListener(null);
        }
    }

}
