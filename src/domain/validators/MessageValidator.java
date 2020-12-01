package domain.validators;


import domain.Message;
import domain.Utilizator;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {

        String msg=new String();
        if(entity.getMessage().contains(";"))
            msg+="Mesajul nu poate sa contina ;\n";
        if(entity.getMessage().isEmpty())
            msg+="Mesajul nu poate sa fie null\n";
        if(entity.getReply()!=null) {
            int valid = 0;
            for (Utilizator u : entity.getReply().getTo()) {
                if (u.getId() == entity.getFrom().getId())
                    valid = 1;
            }
            if (valid == 0)
                msg += "Mesajul nu a fost adresat acestui utilizator\n";
        }
        if(!msg.isEmpty())
            throw new ValidationException(msg);
    }
}