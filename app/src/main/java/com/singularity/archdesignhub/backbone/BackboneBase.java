package com.singularity.archdesignhub.backbone;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.singularity.archdesignhub.backend.entities.agentApi.AgentApi;
import com.singularity.archdesignhub.backend.entities.commentApi.CommentApi;
import com.singularity.archdesignhub.backend.entities.contactApi.ContactApi;
import com.singularity.archdesignhub.backend.entities.eventApi.EventApi;
import com.singularity.archdesignhub.backend.entities.imageApi.ImageApi;
import com.singularity.archdesignhub.backend.entities.messageApi.MessageApi;
import com.singularity.archdesignhub.backend.entities.propertyApi.PropertyApi;
import com.singularity.archdesignhub.backend.entities.userApi.UserApi;

/**
 * Created by Frederick on 5/3/2015.
 * Backbone class to intitialise the APIs
 */
public class BackboneBase {
    protected AgentApi agentApi;
    protected PropertyApi propertyApi;
    protected UserApi userApi;
    protected ImageApi imageApi;
    protected CommentApi commentApi;
    protected EventApi eventApi;
    protected ContactApi contactApi;
    protected MessageApi messageApi;

    public BackboneBase() {
        init();
    }

    //Load all APIs
    private void init() {
        if(messageApi ==null){
            MessageApi.Builder builder = new MessageApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null);
            messageApi = builder.build();
        }

        if (contactApi == null) {  // Only do this once
            ContactApi.Builder builder = new ContactApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null);
            contactApi = builder.build();
        }
        if (eventApi == null) {  // Only do this once
            EventApi.Builder builder = new EventApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null);
            eventApi = builder.build();
        }
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
