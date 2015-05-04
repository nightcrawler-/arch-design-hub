package com.singularity.archdesignhub.backbone;

import android.content.ContentValues;

import com.singularity.archdesignhub.backend.entities.agentApi.model.Agent;
import com.singularity.archdesignhub.backend.entities.propertyApi.model.Property;
import com.singularity.archdesignhub.data.CassiniContract;

import java.io.IOException;
import java.util.List;


/**
 * Created by Frederick on 4/23/2015.
 */
public class Backbone extends BackboneBase {

    private static Backbone backbone = null;

    private Backbone() {
        super();
    }

    public static Backbone getInstance() {
        if (backbone == null)
            backbone = new Backbone();
        return backbone;
    }


    public List<Property> getListings() throws IOException {
        return propertyApi.list().execute().getItems();
    }

    public List<Agent> getAgents() throws IOException {
        return agentApi.list().execute().getItems();
    }


    private ContentValues getAgentContentValues(Agent agent) {
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
