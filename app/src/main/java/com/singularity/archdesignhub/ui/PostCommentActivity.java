package com.singularity.archdesignhub.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.auth.LoginManager;
import com.singularity.archdesignhub.backbone.Backbone;
import com.singularity.archdesignhub.backend.entities.commentApi.model.Comment;
import com.singularity.archdesignhub.backend.entities.userApi.model.User;
import com.singularity.archdesignhub.utils.Utils;

import java.io.IOException;


public class PostCommentActivity extends ActionBarActivity {

    EditText content;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        content = (EditText) findViewById(R.id.editText5);
        progress = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_comment, menu);

        return true;
    }

    private void submitComment() {
        if (!Utils.isLoginDone(this)) {
            Toast.makeText(this, "You need to login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginSelectionActivity.class));
            return;

        }

        String msg = content.getText().toString().trim();
        if (msg.length() == 0) {
            content.setError("Required");
            return;
        }

        User user = LoginManager.getInstance(this).getCachedUser();
        Comment comment = new Comment();
        comment.setTime(System.currentTimeMillis());
        comment.setContent(msg);
        comment.setOwnerName(user.getName());
        comment.setOwnerUrl(user.getImage());

        new PostTask().execute(comment);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            submitComment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class PostTask extends AsyncTask<Comment, Object, Comment> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            content.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Comment doInBackground(Comment... params) {
            try {
                return Backbone.getInstance().publishComment(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Comment comment) {
            super.onPostExecute(comment);
            if (comment != null)
                finish();
            else {
                Toast.makeText(getBaseContext(), "Something went sideways", Toast.LENGTH_SHORT).show();
                content.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }
        }
    }
}
