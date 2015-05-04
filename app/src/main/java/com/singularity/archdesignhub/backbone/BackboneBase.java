package com.singularity.archdesignhub.backbone;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.singularity.archdesignhub.backend.entities.agentApi.AgentApi;
import com.singularity.archdesignhub.backend.entities.propertyApi.PropertyApi;
import com.singularity.archdesignhub.backend.entities.userApi.UserApi;

import java.io.IOException;

/**
 * Created by Frederick on 5/3/2015.
 */
public class BackboneBase {
    protected AgentApi agentApi;
    protected PropertyApi propertyApi;
    protected UserApi userApi;

    public BackboneBase() {
        init();
    }

    //Load all APIs
    private void init() {
        if (agentApi == null) {  // Only do this once
            AgentApi.Builder builder = new AgentApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            agentApi = builder.build();
        }
        if (propertyApi == null) {  // Only do this once
            PropertyApi.Builder builder = new PropertyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            propertyApi = builder.build();
        }
        if (userApi == null) {  // Only do this once
            UserApi.Builder builder = new UserApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            userApi = builder.build();
        }
    }
}
