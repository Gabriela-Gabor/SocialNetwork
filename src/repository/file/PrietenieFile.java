package repository.file;


import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import domain.validators.ValidationException;
import domain.validators.Validator;
import repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public class PrietenieFile extends AbstractFileRepository<Tuple<Long,Long>, Prietenie>{

    private Repository<Long, Utilizator> repoUser;

    public PrietenieFile(String filename, Repository<Long,Utilizator> userFileRepository, Validator<Prietenie> validator)
    {
        super(filename,validator);
        repoUser=userFileRepository;
        loadPrietenii();

    }

    @Override
    public Prietenie save(Prietenie entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");

        long id1=entity.getId().getLeft();
        long id2=entity.getId().getRight();
        Tuple<Long,Long> idComplementar=new Tuple<>(id2,id1);
        Prietenie complementar=findOne(idComplementar);
        //verificam daca nu exista id-ul complementar ( nu putem avea prietenia 2,3 si prietenia 3,2)
        if(complementar!=null)
            throw new ValidationException("Prietenie existenta");
        //verificam daca utilizatorii pentru care cream o prietenie exista
        if(repoUser.findOne(id1)!=null && repoUser.findOne(id2)!=null)
        {
            return super.save(entity);
        }
        else
            return entity;



    }



    @Override
    public Prietenie extractEntity(List<String> attributes){
        //TODO: implement method
       // DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime data= LocalDateTime.parse(attributes.get(2));
        Prietenie prietenie=new Prietenie(data);
        Tuple<Long,Long> id=new Tuple<Long,Long>((Long.parseLong(attributes.get(0))),Long.parseLong((attributes.get(1))));
        prietenie.setId(id);
        return prietenie;

    }

    @Override
    protected String createEntityAsString(Prietenie entity) {
        return entity.getId().getLeft()+";"+entity.getId().getRight()+";"+entity.getDate();
    }

    public void loadPrietenii()
    {
        for(Prietenie p:findAll()){
            Utilizator u1= repoUser.findOne(p.getId().getLeft());
            Utilizator u2= repoUser.findOne(p.getId().getRight());
            u1.addFriend(u2);
            u2.addFriend(u1);
        }
    }
}
