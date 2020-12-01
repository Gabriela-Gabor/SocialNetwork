package repository.file;


import domain.*;
import domain.validators.ValidationException;
import domain.validators.Validator;
import repository.Repository;

import java.util.HashMap;
import java.util.List;

public class InvitatieFile extends AbstractFileRepository<Long, Invitatie>{

    private Repository<Long, Utilizator> repoUser;
    Repository<Tuple<Long,Long>, Prietenie> repoFriends;

    public InvitatieFile(String fileName, Validator<Invitatie> validator, Repository<Long, Utilizator> userFileRepository, Repository<Tuple<Long,Long>,Prietenie> friendsFileRepository) {
        this.repoUser = userFileRepository;
        this.repoFriends=friendsFileRepository;
        super.validator = validator;
        super.entities = new HashMap<Long, Invitatie>();
        this.fileName = fileName;
        super.loadData();
    }

    @Override
    public Invitatie extractEntity(List<String> attributes){
        Utilizator u1=repoUser.findOne(Long.parseLong(attributes.get(1)));
        Utilizator u2=repoUser.findOne(Long.parseLong(attributes.get(2)));
        Status s;
        if(attributes.get(3).equals("pending"))
            s=Status.pending;
        else if(attributes.get(3).equals("approved"))
            s=Status.approved;
        else
            s=Status.rejected;
        Invitatie invitatie=new Invitatie(u1,u2);
        invitatie.setId(Long.parseLong(attributes.get(0)));
        invitatie.setStatus(s);
        return invitatie;
    }

    @Override
    protected String createEntityAsString(Invitatie entity) {
        return entity.getId()+";"+entity.getU1().getId()+";"+entity.getU2().getId()+";"+entity.getStatus();
    }


    @Override
    public Invitatie save(Invitatie entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");

        long id1=entity.getU1().getId();
        long id2=entity.getU2().getId();
        for(Invitatie invitatie:findAll()){
            if((invitatie.getU1().getId()==id1 || invitatie.getU1().getId()==id2) &&(invitatie.getU2().getId()==id1 || invitatie.getU2().getId()==id2)){
                throw new ValidationException("Invitatia deja exista");
            }
        }
        Tuple<Long,Long> idComplementar=new Tuple<>(id2,id1);
        Tuple<Long,Long> id=new Tuple<>(id1,id2);
        Prietenie complementar=repoFriends.findOne(idComplementar);
        Prietenie p=repoFriends.findOne(id);
        //verificam daca cei doi sunt prieteni deja
        if(complementar!=null || p!=null)
            throw new ValidationException("Utilizatorii sunt deja prieteni");

       return super.save(entity);



    }



}
