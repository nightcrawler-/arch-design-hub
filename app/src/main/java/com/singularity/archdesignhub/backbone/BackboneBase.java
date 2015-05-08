package com.singularity.archdesignhub.backbone;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.singularity.archdesignhub.backend.entities.agentApi.AgentApi;
import com.singularity.archdesignhub.backend.entities.commentApi.CommentApi;
import com.singularity.archdesignhub.backend.entities.imageApi.ImageApi;
import com.singularity.archdesignhub.backend.entities.propertyApi.PropertyApi;
import com.singularity.archdesignhub.backend.entities.userApi.UserApi;

/**
 * Created by Frederick on 5/3/2015.
 */
public class BackboneBase {
    protected AgentApi agentApi;
    protected PropertyApi propertyApi;
    protected UserApi userApi;
    protected ImageApi imageApi;
    protected CommentApi commentApi;

    public BackboneBase() {
        init();
    }

    //Load all APIs
    private void init() {
        if (commentApi == null) {  // Only do this once
            CommentApi.Builder builder = new CommentApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null);

            commentApi = builder.build();
        }
        if (imageApi == null) {  // Only do this once
            ImageApi.Builder builder = new ImageApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null);

            imageApi = builder.build();
        }
        if (agentApi == null) {  // Only do this once
            AgentApi.Builder builder = new AgentApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null);

            agentApi = builder.build();
        }
        if (propertyApi == null) {  // Only do this once
            PropertyApi.Builder builder = new PropertyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null);

            propertyApi = builder.build();
        }
        if (userApi == null) {  // Only do this once
            UserApi.Builder builder = new UserApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null);

            userApi = builder.build();
        }
    }
}
