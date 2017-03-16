package com.tomclaw.appsend.main.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tomclaw.appsend.R;
import com.tomclaw.appsend.main.adapter.BaseItemAdapter;
import com.tomclaw.appsend.main.controller.StoreController;
import com.tomclaw.appsend.main.item.StoreItem;
import com.tomclaw.appsend.util.FileHelper;
import com.tomclaw.appsend.util.LocaleHelper;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import jp.shts.android.library.TriangleLabelView;

/**
 * Created by Solkin on 10.12.2014.
 */
public class StoreItemHolder extends AbstractItemHolder<StoreItem> {

    public static final String ACTION_RETRY = "action_retry";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy");

    private View itemView;
    private View appCard;
    private ImageView appIcon;
    private TextView appName;
    private TextView appVersion;
    private TextView appUpdateTime;
    private TextView appSize;
    private TriangleLabelView badgeNew;
    private View viewProgress;
    private View errorView;
    private View buttonRetry;

    public StoreItemHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        appCard = itemView.findViewById(R.id.app_card);
        appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
        appName = (TextView) itemView.findViewById(R.id.app_name);
        appVersion = (TextView) itemView.findViewById(R.id.app_version);
        appUpdateTime = (TextView) itemView.findViewById(R.id.app_update_time);
        appSize = (TextView) itemView.findViewById(R.id.app_size);
        badgeNew = (TriangleLabelView) itemView.findViewById(R.id.badge_new);
        viewProgress = itemView.findViewById(R.id.item_progress);
        errorView = itemView.findViewById(R.id.error_view);
        buttonRetry = itemView.findViewById(R.id.button_retry);
    }

    @Override
    View getCardView(View itemView) {
        return itemView.findViewById(R.id.app_card);
    }

    public void bind(Context context, final StoreItem item, final boolean isLast, final BaseItemAdapter.BaseItemClickListener<StoreItem> listener) {
        if (listener != null) {
            appCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(item);
                }
            });
            buttonRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onActionClicked(item, ACTION_RETRY);
                }
            });
        }

        Glide.with(context)
                .load(item.getIcon())
                .into(appIcon);

        appName.setText(LocaleHelper.getLocalizedLabel(item));
        appVersion.setText(item.getVersion());
        if (item.getTime() > 0) {
            appUpdateTime.setVisibility(View.VISIBLE);
            appUpdateTime.setText(simpleDateFormat.format(item.getTime()));
        } else {
            appUpdateTime.setVisibility(View.GONE);
        }
        appSize.setText(FileHelper.formatBytes(context.getResources(), item.getSize()));

        long appInstallDelay = System.currentTimeMillis() - item.getTime();
        boolean isNewApp = appInstallDelay > 0 && appInstallDelay < TimeUnit.DAYS.toMillis(1);
        badgeNew.setVisibility(isNewApp ? View.VISIBLE : View.GONE);

        boolean isAppendError = isLast && StoreController.getInstance().isError() && StoreController.getInstance().isAppend();
        errorView.setVisibility(isAppendError ? View.VISIBLE : View.GONE);
        if (isAppendError) {
            viewProgress.setVisibility(View.GONE);
        } else {
            boolean isLoad = isLast && StoreController.getInstance().load(
                    context, item.getAppId(), item.getFilter());
            viewProgress.setVisibility(isLoad ? View.VISIBLE : View.GONE);
        }
    }
}