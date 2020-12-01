package domain.validators;

import domain.Invitatie;

public class InvitatieValidator implements Validator<Invitatie> {
    @Override
    public void validate(Invitatie entity) throws ValidationException {

        String msg=new String();
        if(!msg.isEmpty())
            throw new ValidationException(msg);
    }
}