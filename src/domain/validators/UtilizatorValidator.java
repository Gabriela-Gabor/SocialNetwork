package domain.validators;

import domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {

        String msg=new String();
        if(entity.getFirstName().isEmpty()) msg+="Prenume invalid!\n";
        if(entity.getLastName().isEmpty()) msg+="Nume invalid!\n";

        if(!msg.isEmpty())
            throw new ValidationException(msg);
    }
}
