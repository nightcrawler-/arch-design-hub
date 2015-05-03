package com.singularity.archdesignhub.backbone;

import android.content.ContentValues;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.singularity.archdesignhub.backend.entities.agentApi.AgentApi;
import com.singularity.archdesignhub.backend.entities.agentApi.model.Agent;
import com.singularity.archdesignhub.backend.entities.propertyApi.PropertyApi;
import com.singularity.archdesignhub.backend.entities.propertyApi.model.Property;
import com.singularity.archdesignhub.backend.entities.userApi.UserApi;
import com.singularity.archdesignhub.data.CassiniContract;

import java.io.IOException;
import java.util.List;


/**
 * Created by Frederick on 4/23/2015.
 */
public class Backbone {
    private AgentApi agentApi;
    private PropertyApi propertyApi;
    private UserApi userApi;
    private static Backbone backbone = null;

    private Backbone() {
        init();
    }

    public static Backbone getInstance() {
        if (backbone == null)
            backbone = new Backbone();
        return backbone;
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

    public List<Property> getListings() throws IOException {
        return propertyApi.list().execute().getItems();
    }

    public List<Agent> getAgents() throws IOException {
        return agentApi.list().execute().getItems();
    }



    private ContentValues getAgentContentValues(Agent agent){
        ContentValues values = new ContentValues();


        return values;
    }
    private ContentValues getPropertyContentValues(Property property) {
        ContentValues values = new ContentValues();
        values.put(CassiniContract.PropertyEntry.C_AGENT_ID, property.getAgentId());
        values.put(CassiniContract.PropertyEntry.C_AREA, property.getArea());
        values.put(CassiniContract.PropertyEntry.C_BATHROOMS, property.getBathrooms());
        values.put(CassiniContract.PropertyEntry.C_BEDROOMS, property.getBedrooms());
        values.put(CassiniContract.PropertyEntry.C_DESCRIPTION, property.getDescription());
        values.put(CassiniContract.PropertyEntry.C_EMAIL, property.getEmail());
        values.put(CassiniContract.PropertyEntry.C_EXTRA, property.getExtra());
        values.put(CassiniContract.PropertyEntry.C_ID, property.getId());
        values.put(CassiniContract.PropertyEntry.C_INTENT, property.getIntent());
        values.put(CassiniContract.PropertyEntry.C_LATT, property.getLatt());
        values.put(CassiniContract.PropertyEntry.C_LONG, property.getLongi());
        values.put(CassiniContract.PropertyEntry.C_LOCATION, property.getLocation());
        values.put(CassiniContract.PropertyEntry.C_NAME, property.getName());
        values.put(CassiniContract.PropertyEntry.C_MEDIA, property.getMedia());
        values.put(CassiniContract.PropertyEntry.C_TEL, property.getTel());
        values.put(CassiniContract.PropertyEntry.C_TIME, property.getTime());
        values.put(CassiniContract.PropertyEntry.C_TYPE, property.getType());
        values.put(CassiniContract.PropertyEntry.C_VALUE, property.getValue());
        values.put(CassiniContract.PropertyEntry.C_VOLUME, property.getVolume());
        values.put(CassiniContract.PropertyEntry.C_WEBSITE, property.getWebsite());


        return values;

    }

}
