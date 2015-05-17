package com.singularity.archdesignhub.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.singularity.archdesignhub.App;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.auth.LoginManager;
import com.singularity.archdesignhub.data.CassiniContract;
import com.singularity.archdesignhub.utils.DefaultImageLoader;
import com.singularity.archdesignhub.utils.Utils;

/**
 * Created by Frederick on 5/13/2015.
 */
public class CommentsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static String[] COMMENTS_COLUMNS = {
            CassiniContract.CommentEntry.C_ID,
            CassiniContract.CommentEntry.C_TIME,
            CassiniContract.CommentEntry.C_RESPONSE_TIME,
            CassiniContract.CommentEntry.C_RESPONSE,
            CassiniContract.CommentEntry.C_RESPONSE_OWNER,
            CassiniContract.CommentEntry.C_COMMENT,
            CassiniContract.CommentEntry.C_OWNER_NAME,
            CassiniContract.CommentEntry.C_OWNER_PIC,
    };

    private static int COMMENTS_LOADER = 123;
    CommentsAdapter adapter;

    public CommentsFragment() {
    }

    public static CommentsFragment newInstance() {
        return new CommentsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_comments_list, container, false);

        ListView list = (ListView) parent.findViewById(android.R.id.list);
        View button = parent.findViewById(R.id.fab);
        adapter = new CommentsAdapter(getActivity());
        list.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginManager.getInstance(getActivity()).getCachedUser().getName() != null) {
                    startActivity(new Intent(getActivity(), PostCommentActivity.class));
                    //open post comment diag
                } else {
                    ;
                    //open sign up, display toast
                }
            }
        });

        return parent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(COMMENTS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = CassiniContract.CommentEntry.CONTENT_URI;
        return new CursorLoader(getActivity(), uri, COMMENTS_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public static class CommentsAdapter extends BaseAdapter {
        LayoutInflater inflater;
        Cursor data;
        Context context;

        public CommentsAdapter(Context context) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        public void setData(Cursor data) {
            this.data = data;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data != null ? data.getCount() : 0;
        }

        @Override
        public Cursor getItem(int position) {
            data.moveToPosition(position);
            return data;
        }

        @Override
        public long getItemId(int position) {
            data.moveToPosition(position);
            return data.getLong(data.getColumnIndex(CassiniContract.CommentEntry.C_ID));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_comment, null);
                convertView.setTag(holder);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView17);
                holder.userName = (TextView) convertView.findViewById(R.id.textView22);
                holder.content = (TextView) convertView.findViewById(R.id.textView23);
                holder.time = (TextView) convertView.findViewById(R.id.textView26);
                holder.replyContent = (TextView) convertView.findViewById(R.id.textView27);
                holder.responderName = (TextView) convertView.findViewById(R.id.textView24);
                holder.replyTime = (TextView) convertView.findViewById(R.id.textView25);
                holder.replyLayout = convertView.findViewById(R.id.layoutReply);


                Utils.applyFonts(convertView, App.getRobotoSlabLight());

                //holder.bottomPadding = convertView.findViewById(R.id.bottomPad);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            data.moveToPosition(position);

            holder.userName.setText(data.getString(data.getColumnIndex(CassiniContract.CommentEntry.C_OWNER_NAME)));
            holder.content.setText(data.getString(data.getColumnIndex(CassiniContract.CommentEntry.C_COMMENT)));
            holder.time.setText(DateUtils.getRelativeTimeSpanString(context, data.getLong(data.getColumnIndex(CassiniContract.CommentEntry.C_TIME)), true));
            holder.replyTime.setText(DateUtils.getRelativeTimeSpanString(context, data.getLong(data.getColumnIndex(CassiniContract.CommentEntry.C_RESPONSE_TIME)), true));
            holder.responderName.setText(data.getString(data.getColumnIndex(CassiniContract.CommentEntry.C_RESPONSE_OWNER)));

            holder.replyContent.setText(data.getString(data.getColumnIndex(CassiniContract.CommentEntry.C_RESPONSE)));
            DefaultImageLoader.getInstance().loadImage(data.getString(data.getColumnIndex(CassiniContract.CommentEntry.C_OWNER_PIC)), holder.imageView);

            if (data.getString(data.getColumnIndex(CassiniContract.CommentEntry.C_RESPONSE)) == null)
                holder.replyLayout.setVisibility(View.GONE);
            else
                holder.replyLayout.setVisibility(View.VISIBLE);

            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            TextView userName, content, time, replyContent, replyTime, responderName;
            View replyLayout;
        }
    }
}
