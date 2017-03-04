package pl.sda.services;

import org.codehaus.jackson.map.ObjectMapper;
import pl.sda.entities.User;
import pl.sda.pl.sda.storage.Storage;
import pl.sda.servlets.pl.sda.servlets.response.CreatUserResponse;
import pl.sda.servlets.pl.sda.servlets.response.DeleteUserResponse;
import pl.sda.servlets.pl.sda.servlets.response.UpdateUserResponse;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by RENT on 2017-03-04.
 */
public class UserService {
    public CreatUserResponse addUser(String userJson) {
        ObjectMapper mapper = new ObjectMapper();
        CreatUserResponse response = new CreatUserResponse();

        try {
            User user = mapper.readValue(userJson, User.class);
            UUID id = UUID.randomUUID();
            user.setId(id);

            Storage.addUser(user);
            response.setStatus("OK");
            response.setId(id.toString());
        } catch (IOException e) {
            response.setError(e.getMessage());
        }

        return response;
    }

    public DeleteUserResponse removeUserById(String id) {
        DeleteUserResponse result = new DeleteUserResponse();
        result.setMessage("User with ID: " + id + " NOT FOUND");

        if (id != null && !id.isEmpty()) {
            User tempUser = null;
            UUID uuid = UUID.fromString(id);
            for (User user : Storage.getUsers()) {
                if (uuid.equals(user.getId())) {
                    tempUser = user;
                    break;
                }
            }
            Storage.removeUser(tempUser);
            result.setStatus("OK");
            result.setMessage("USER WITH ID: " + id + " WAS REMOVED");
        }
        return result;
    }

    public UpdateUserResponse updateUser(User user) {
        UpdateUserResponse response = new UpdateUserResponse();

        Storage.updateUser(user);
        response.setStatus("OK");
        response.setMessage("USER UPDATE");
        return response;
    }

}
