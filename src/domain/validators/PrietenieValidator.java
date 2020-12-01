package domain.validators;


import domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie> {
    @Override
    public void validate(Prietenie entity) throws ValidationException {

        String msg=new String();
        if(!msg.isEmpty())
            throw new ValidationException(msg);
    }
}
