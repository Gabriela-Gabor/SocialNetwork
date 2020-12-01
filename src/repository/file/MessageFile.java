package repository.file;

import domain.Message;
import domain.Utilizator;
import domain.validators.Validator;
import repository.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MessageFile extends AbstractFileRepository<Long, Message> {

    private Repository<Long, Utilizator> repoUser;

    public MessageFile(String filename, Validator<Message> validator, Repository<Long, Utilizator> userFileRepository) {
        this.repoUser = userFileRepository;
        super.validator = validator;
        super.entities = new HashMap<Long, Message>();
        this.fileName = filename;
        super.loadData();

    }

    @Override
    public Message extractEntity(List<String> attributes) {

        List<String> iduri = Arrays.asList(attributes.get(0).split("[,]"));
        Long idUser = Long.parseLong(attributes.get(1));
        //Long idUser = Long.parseLong(attributes.get(1));
        Utilizator u = repoUser.findOne(idUser);
        List<Utilizator> to = new ArrayList<>();
        List<String> attr = Arrays.asList(attributes.get(2).split("[ ]"));
        attr.forEach(x -> to.add(repoUser.findOne(Long.parseLong(x))));
        if (iduri.get(0) != "0") {
            Message m = findOne(Long.parseLong(iduri.get(0)));
            Message replyMessage = new Message(u, to, attributes.get(3), LocalDateTime.parse(attributes.get(4)));
            replyMessage.setReply(m);
            replyMessage.setId(Long.parseLong(iduri.get(1)));
            return replyMessage;
        }

        Message message = new Message(u, to, attributes.get(3), LocalDateTime.parse(attributes.get(4)));
        message.setId(Long.parseLong(iduri.get(1)));
        return message;
    }

    @Override
    protected String createEntityAsString(Message entity) {
        String to = "";
        for (Utilizator u : entity.getTo())
            to = to + u.getId() + " ";

        if(entity.getReply()==null)
            return "0,"+entity.getId() + ";" + entity.getFrom().getId() + ";" + to +";"+ entity.getMessage() +";"+ entity.getData();
        return entity.getReply().getId()+","+entity.getId() + ";" + entity.getFrom().getId() + ";" + to +";"+ entity.getMessage() +";"+ entity.getData();


    }


}

