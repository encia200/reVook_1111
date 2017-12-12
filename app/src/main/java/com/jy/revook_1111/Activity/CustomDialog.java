package com.jy.revook_1111.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.jy.revook_1111.APISearchNaverBook;
import com.jy.revook_1111.FontSetting;
import com.jy.revook_1111.Fragment.SearchFragment;
import com.jy.revook_1111.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by remna on 2017-12-08.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {

    public Activity a;
    public Dialog d;
    public int index;
    private Context mContext;
    private int fragmentIdentifier;
    public static final int SEARCH_FRAGMENT = 1;
    public static final int BOOK_SEARCH_FRAGMENT = 2;

    public CustomDialog(Activity c, int i, int FRAGMENT) {
        super(c);
        this.index = i;
        this.a = c;
        this.fragmentIdentifier = FRAGMENT;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        setTypeFace();
        setBookInfo();

        FloatingActionButton btn_upload_review = (FloatingActionButton) findViewById(R.id.btn_upload_review);
        btn_upload_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditorActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    private static class ViewHolder {
        TextView title;
        TextView author;
        TextView price;
        TextView publisher;
        TextView isbn;
        TextView description;
        ImageView image;
        ProgressBar dialog;
    }

    private void setTypeFace() {
        FontSetting fontSetting = new FontSetting(getContext());
        TextView textView = (TextView) findViewById(R.id.custom_dialog_author);
        textView.setTypeface(fontSetting.getTypeface_Contents());
        textView = (TextView) findViewById(R.id.custom_dialog_price);
        textView.setTypeface(fontSetting.getTypeface_Contents());
        textView = (TextView) findViewById(R.id.custom_dialog_publisher);
        textView.setTypeface(fontSetting.getTypeface_Contents());
        textView = (TextView) findViewById(R.id.custom_dialog_isbn);
        textView.setTypeface(fontSetting.getTypeface_Contents());
        textView = (TextView) findViewById(R.id.custom_dialog_description);
        textView.setTypeface(fontSetting.getTypeface_Contents());
    }

    public void setBookInfo() {
        mContext = getContext();
        final ViewHolder holder = new ViewHolder();

        String title, imgUrl, author, price, publisher, isbn, description;
        title = imgUrl = author = price = publisher = isbn = description = "";

        if (fragmentIdentifier == BOOK_SEARCH_FRAGMENT) {
            title = APISearchNaverBook.bookInfoList.get(index).title;
            imgUrl = APISearchNaverBook.bookInfoList.get(index).imageURL;
            author = APISearchNaverBook.bookInfoList.get(index).author;
            price = APISearchNaverBook.bookInfoList.get(index).price;
            publisher = APISearchNaverBook.bookInfoList.get(index).publisher;
            isbn = APISearchNaverBook.bookInfoList.get(index).isbn;
            description = APISearchNaverBook.bookInfoList.get(index).description;
        } else if (fragmentIdentifier == SEARCH_FRAGMENT) {
            title = SearchFragment.famousNobleList.get(index).title;
            imgUrl = SearchFragment.famousNobleList.get(index).imageURL;
            author = SearchFragment.famousNobleList.get(index).author;
            price = SearchFragment.famousNobleList.get(index).price;
            publisher = SearchFragment.famousNobleList.get(index).publisher;
            isbn = SearchFragment.famousNobleList.get(index).isbn;
            description = SearchFragment.famousNobleList.get(index).description;
        }

        FontSetting fontSetting = new FontSetting(getContext());
        holder.title = (TextView) findViewById(R.id.custom_dialog_title);
        holder.title.setTypeface(fontSetting.getTypeface_Title());
        holder.image = (ImageView) findViewById(R.id.custom_dialog_bookimg);
        holder.dialog = (ProgressBar) findViewById(R.id.custom_dialog_progress);
        holder.author = (TextView) findViewById(R.id.custom_dialog_author_input);
        holder.author.setTypeface(fontSetting.getTypeface_Contents());
        holder.price = (TextView) findViewById(R.id.custom_dialog_price_input);
        holder.price.setTypeface(fontSetting.getTypeface_Contents());
        holder.publisher = (TextView) findViewById(R.id.custom_dialog_publisher_input);
        holder.publisher.setTypeface(fontSetting.getTypeface_Contents());
        holder.isbn = (TextView) findViewById(R.id.custom_dialog_price_isbn_input);
        holder.isbn.setTypeface(fontSetting.getTypeface_Contents());
        holder.description = (TextView) findViewById(R.id.custom_dialog_description_input);
        holder.description.setTypeface(fontSetting.getTypeface_Contents());

        holder.title.setText(title);
        holder.author.setText(author);
        holder.price.setText(price);
        holder.publisher.setText(publisher);
        holder.isbn.setText(isbn);
        holder.description.setText(description);

        if (imgUrl.length() == 0) {
            holder.image.setImageResource(R.drawable.nobookimg);
        }
        //create the imageloader object

        else {
            ImageLoader imageLoader = ImageLoader.getInstance();
            int defaultImage = mContext.getResources().getIdentifier("@drawable/nobookimg", null, mContext.getPackageName());

            //create display options
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(defaultImage)
                    .showImageOnFail(defaultImage)
                    .showImageOnLoading(defaultImage).build();

            //download and display image from url
            imageLoader.displayImage(imgUrl, holder.image, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.dialog.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            holder.dialog.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            holder.dialog.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    }

            );
        }
    }

    public void onClick(View v) {
    }

}