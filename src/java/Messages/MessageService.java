/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

import java.io.StringReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author gabriel
 */
@ApplicationScoped
@Path("/messages")
public class MessageService {

    @Inject
    private MessageController controller;

    @GET
    @Produces("application/json")
    public JsonArray get() throws SQLException {
        JsonArrayBuilder arr = Json.createArrayBuilder();
        for (Message message : controller.getMessages()) {
            JsonObject json = Json.createObjectBuilder()
                    .add("id", message.getId())
                    .add("title", message.getTitle())
                    .add("contents", message.getContent())
                    .add("author", message.getAuthor())
                    .add("senttime", message.getDate().toString())
                    .build();

            arr.add(json);
        }
        return arr.build();
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public JsonObject getById(@PathParam("id") int id) throws SQLException {
        for (Message message : controller.getMessages()) {
            if (message.getId() == id) {
                JsonObject json = Json.createObjectBuilder()
                        .add("id", message.getId())
                        .add("title", message.getTitle())
                        .add("contents", message.getContent())
                        .add("author", message.getAuthor())
                        .add("senttime", message.getDate().toString())
                        .build();
                return json;
            }
        }

        return null;
    }

    @GET
    @Path("{startDate}/{endDate}")
    @Produces("application/json")
    public JsonArray getByDate(@PathParam("startDate") String minDate, @PathParam("endDate") String maxDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date min = sdf.parse(minDate);
            Date max = sdf.parse(maxDate);
            ArrayList<Message> messagesGotByDate = controller.getMessagesOnDateRange(min, max);

            JsonArrayBuilder arr = Json.createArrayBuilder();
            for (Message message : messagesGotByDate) {
                JsonObject json = Json.createObjectBuilder()
                        .add("id", message.getId())
                        .add("title", message.getTitle())
                        .add("contents", message.getContent())
                        .add("author", message.getAuthor())
                        .add("senttime", message.getDate().toString())
                        .build();

                arr.add(json);
            }
            return arr.build();

        } catch (ParseException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public JsonObject postNewObject(JsonObject jsonParameter) throws SQLException, ClassNotFoundException {

        String title = jsonParameter.getString("title");
        String author = jsonParameter.getString("author");
        String content = jsonParameter.getString("contents");

        Message newMessage = new Message();
        newMessage.setAuthor(author);
        newMessage.setContent(content);
        newMessage.setTitle(title);
        newMessage.setDate(new Date()); //now
        newMessage.setId(controller.getMessages().size() + 1);
        //ID will always be the final position on the list + 1

        controller.setMessage(newMessage);
        controller.addMessage();

        JsonObject json = Json.createObjectBuilder()
                .add("id", newMessage.getId())
                .add("title", newMessage.getTitle())
                .add("contents", newMessage.getContent())
                .add("author", newMessage.getAuthor())
                .add("senttime", newMessage.getDate().toString())
                .build();

        return json;

    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public JsonObject edit(JsonObject jsonParameter) throws SQLException {

        String title = jsonParameter.getString("title");
        String author = jsonParameter.getString("author");
        String content = jsonParameter.getString("contents");
        int id = jsonParameter.getInt("id");
        //Faltou a data, que nao tem no json.get

        Message newMessage = new Message();
        newMessage.setAuthor(author);
        newMessage.setContent(content);
        newMessage.setTitle(title);
        newMessage.setDate(new Date()); //now
        newMessage.setId(id);

        //As the ID is always the position + 1, I can replace
        controller.getMessages().set(controller.getMessages().size() - 1, newMessage);

        JsonObject json = Json.createObjectBuilder()
                .add("id", newMessage.getId())
                .add("title", newMessage.getTitle())
                .add("contents", newMessage.getContent())
                .add("author", newMessage.getAuthor())
                .add("senttime", newMessage.getDate().toString())
                .build();
        return json;
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") int id) throws SQLException {
        Response result;
        ArrayList<Message> targetToDeletionMesage = controller.getMessageById(id);
        controller.getMessages().remove(targetToDeletionMesage);
        result = Response.ok().status(200).build();
        return result;
    }

}
