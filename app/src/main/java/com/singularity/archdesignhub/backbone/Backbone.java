package com.singularity.archdesignhub.backbone;

import android.content.ContentValues;

import com.singularity.archdesignhub.backend.entities.agentApi.model.Agent;
import com.singularity.archdesignhub.backend.entities.commentApi.model.Comment;
import com.singularity.archdesignhub.backend.entities.contactApi.model.Contact;
import com.singularity.archdesignhub.backend.entities.eventApi.model.Event;
import com.singularity.archdesignhub.backend.entities.imageApi.model.Image;
import com.singularity.archdesignhub.backend.entities.propertyApi.model.Property;
import com.singularity.archdesignhub.backend.entities.userApi.model.User;
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


    public User insert(User user) throws IOException {
        return userApi.insert(user).execute();

    }

    public User updateUser(User user) throws IOException {
        return userApi.update(user.getId(), user).execute();

    }

    public User getUser(User user) throws IOException {
        return userApi.get(user.getId()).execute();
    }

    public Comment publishComment(Comment comment) throws IOException {
        return commentApi.insert(comment).execute();
    }

    public ContentValues[] getImages() throws IOException {
        List<Image> images = imageApi.list().execute().getItems();
        if (images != null) {
            ContentValues[] values = new ContentValues[images.size()];
            for (Image image : images)
                values[images.indexOf(image)] = getImageContentValues(image);

            return values;
        }
        return null;

    }

    public ContentValues[] getAgents() throws IOException {
        List<Agent> agents = agentApi.list().execute().getItems();
        if (agents != null) {
            ContentValues values[] = new ContentValues[agents.size()];
            for (Agent agent : agents)
                values[agents.indexOf(agent)] = getAgentContentValues(agent);
            return values;
        }
        return null;

    }


    public ContentValues[] getPropertyListings() throws IOException {
        List<Property> properties = propertyApi.list().execute().getItems();
        if (properties != null) {
            ContentValues[] listings = new ContentValues[properties.size()];
            for (Property property : properties)
                listings[properties.indexOf(property)] = getPropertyContentValues(property);
            return listings;
        }
        return null;

    }

    public ContentValues[] getEvents() throws IOException {
        List<Event> events = eventApi.list().execute().getItems();
        if (events != null) {
            ContentValues[] values = new ContentValues[events.size()];
            for (Event event : events)
                values[events.indexOf(event)] = getEventContentValues(event);
            return values;
        }
        return null;

    }

    public ContentValues[] getComments() throws IOException {
        List<Comment> comments = commentApi.list().execute().getItems();
        if (comments != null) {
            ContentValues[] values = new ContentValues[comments.size()];
            for (Comment comment : comments)
                values[comments.indexOf(comment)] = getCommentsContentValues(comment);
            return values;
        }
        return null;
    }

    public ContentValues[] getContacts() throws IOException {
        List<Contact> contacts = contactApi.list().execute().getItems();
        if (contacts != null) {
            ContentValues[] values = new ContentValues[contacts.size()];
            for (Contact contact : contacts) {
                values[contacts.indexOf(contact)] = getContactContentValues(contact);
            }
            return values;
        }
        return null;
    }

    private ContentValues getAgentContentValues(Agent agent) {
        ContentValues values = new ContentValues();
        values.put(CassiniContract.AgentEntry.C_ADDRESS, agent.getAddress());
        values.put(CassiniContract.AgentEntry.C_ID, agent.getId());
        values.put(CassiniContract.AgentEntry.C_NAME, agent.getName());
        values.put(CassiniContract.AgentEntry.C_TEL, agent.getTel());
        values.put(CassiniContract.AgentEntry.C_EMAIL, agent.getEmail());
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

    private ContentValues getImageContentValues(Image image) {
        ContentValues values = new ContentValues();
        values.put(CassiniContract.ImageEntry.C_ID, image.getBlobKeyString());
        values.put(CassiniContract.ImageEntry.C_NAME, image.getName());
        values.put(CassiniContract.ImageEntry.C_OWNER_ID, image.getOwnerId());
        values.put(CassiniContract.ImageEntry.C_URL, image.getServingUrl());
        return values;
    }

    private ContentValues getCommentsContentValues(Comment comment) {
        ContentValues values = new ContentValues();
        values.put(CassiniContract.CommentEntry.C_ID, comment.getId());
        values.put(CassiniContract.CommentEntry.C_COMMENT, comment.getContent());
        values.put(CassiniContract.CommentEntry.C_OWNER_ID, comment.getOwnerId());
        values.put(CassiniContract.CommentEntry.C_TIME, comment.getTime());
        values.put(CassiniContract.CommentEntry.C_RESPONSE_TIME, comment.getReplyTime());
        values.put(CassiniContract.CommentEntry.C_RESPONSE, comment.getResponse());
        values.put(CassiniContract.CommentEntry.C_RESPONSE_OWNER, comment.getResponderName());
        values.put(CassiniContract.CommentEntry.C_OWNER_NAME, comment.getOwnerName());
        values.put(CassiniContract.CommentEntry.C_OWNER_PIC, comment.getOwnerUrl());
        return values;
    }

    private ContentValues getEventContentValues(Event event) {
        ContentValues values = new ContentValues();
        values.put(CassiniContract.EventEntry.C_ID, event.getId());
        values.put(CassiniContract.EventEntry.C_DESCRIPTION, event.getDescription());
        values.put(CassiniContract.EventEntry.C_LIKES, event.getLikes());
        values.put(CassiniContract.EventEntry.C_TIME, event.getTime());
        values.put(CassiniContract.EventEntry.C_TITLE, event.getTitle());
        values.put(CassiniContract.EventEntry.C_LOCATION, event.getLocation());
        values.put(CassiniContract.EventEntry.C_LATT, event.getLatt());
        values.put(CassiniContract.EventEntry.C_LONG, event.getLongi());

        return values;
    }

    private ContentValues getContactContentValues(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(CassiniContract.ContactEntry.C_ID, contact.getId());
        values.put(CassiniContract.ContactEntry.C_ADDRESS, contact.getAddress());
        values.put(CassiniContract.ContactEntry.C_EMAIL, contact.getEmail());
        values.put(CassiniContract.ContactEntry.C_LATT, contact.getLatt());
        values.put(CassiniContract.ContactEntry.C_LONG, contact.getLongi());
        values.put(CassiniContract.ContactEntry.C_PHONE, contact.getPhone());
        values.put(CassiniContract.ContactEntry.C_TITLE, contact.getName());
        values.put(CassiniContract.ContactEntry.C_WEBSITE, contact.getWebsite());
        return values;
    }

}
