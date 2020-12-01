package service;


import domain.*;
import javafx.beans.InvalidationListener;

import repository.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UtilizatorService<RepositoryTuple>  {
    private Repository<Long, Utilizator> repoUtilizator;
    private Repository<Tuple<Long, Long>, Prietenie> repoFriends;
    private Repository<Long, Message> repoMessage;
    private Repository<Long, Invitatie> repoInvitatie;
    private Graf g;

    List<Integer> listaIduri;


    public UtilizatorService(Repository<Long, Utilizator> repoU, Repository<Tuple<Long, Long>, Prietenie> repoF, Repository<Long, Message> repoM, Repository<Long, Invitatie> repoI) {
        this.repoUtilizator = repoU;
        this.repoFriends = repoF;
        this.repoMessage = repoM;
        this.repoInvitatie = repoI;
        g = new Graf(repoUtilizator.size());
        listaIduri = new ArrayList<>();

    }

    public Utilizator addUtilizator(Utilizator messageTask) {
        Utilizator task = repoUtilizator.save(messageTask);
        return task;
    }

    public Utilizator deleteUtilizator(long id) {
        Utilizator sters = repoUtilizator.delete(id);
        List<Utilizator> prieteni = sters.getFriends();
        for (Utilizator u : prieteni) {
            repoFriends.delete(new Tuple<Long, Long>(u.getId(), id));
            repoFriends.delete(new Tuple<Long, Long>(id, u.getId()));
            u.deleteFriend(sters);
        }
        return sters;
    }

    public Utilizator findUtilizator(long id) {
        return repoUtilizator.findOne(id);
    }

    public void prieteniiUtilizator(long id) {

        Utilizator u = repoUtilizator.findOne(id);
        List<Prietenie> prietenii = new ArrayList<>();
        repoFriends.findAll().forEach(prietenii::add);
        prietenii.stream()
                .filter(x -> {
                    return x.getId().getLeft() == id || x.getId().getRight() == id;
                })
                .map(x -> {
                    Utilizator u2;
                    if (x.getId().getLeft() == id)
                        u2 = repoUtilizator.findOne(x.getId().getRight());
                    else
                        u2 = repoUtilizator.findOne(x.getId().getLeft());
                    String s = u2.getFirstName() + "|" + u2.getLastName() + "|" + x.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    return s;
                })
                .forEach(x -> System.out.println(x));


    }

    public void prieteniiUtilizatorData(long id, int month) {

        Utilizator u = repoUtilizator.findOne(id);
        List<Prietenie> prietenii = new ArrayList<>();
        repoFriends.findAll().forEach(prietenii::add);
        prietenii.stream()
                .filter(x -> {
                    return x.getId().getLeft() == id || x.getId().getRight() == id;
                })
                .filter(x -> x.getDate().getMonthValue() == month)
                .map(x -> {
                    Utilizator u2;
                    if (x.getId().getLeft() == id)
                        u2 = repoUtilizator.findOne(x.getId().getRight());
                    else
                        u2 = repoUtilizator.findOne(x.getId().getLeft());
                    String s = u2.getFirstName() + "|" + u2.getLastName() + "|" + x.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    return s;
                })
                .forEach(x -> System.out.println(x));


    }

    public Prietenie addPrietenie(Prietenie p) {
        Utilizator u1 = repoUtilizator.findOne(p.getId().getLeft());
        Utilizator u2 = repoUtilizator.findOne(p.getId().getRight());
        u1.addFriend(u2);
        u2.addFriend(u1);
        Prietenie pr = repoFriends.save(p);
        return pr;
    }

    public Prietenie deletePrietenie(long id1, long id2) {
        Tuple<Long, Long> id = new Tuple<Long, Long>(id1, id2);
        Utilizator u1 = repoUtilizator.findOne(id1);
        Utilizator u2 = repoUtilizator.findOne(id2);
        u1.deleteFriend(u2);
        u2.deleteFriend(u1);
        Prietenie pr = repoFriends.delete(id);
        return pr;
    }

    public void creareGraf() {
        g = new Graf(repoUtilizator.size());
        listaIduri = new ArrayList<>();
        //Pozitia pe care se gasesc in lista id-urile utilizatorilor reprezinta nr nodului
        int index = 0;
        int i, j;
        for (Prietenie p : repoFriends.findAll()) {
            int id1 = p.getId().getLeft().intValue();
            int id2 = p.getId().getRight().intValue();

            //daca id-ul nu este in lista,il adaugam pe pozitia index, iar index va fi valoarea nodului sau
            if (!listaIduri.contains(id1)) {
                i = index;
                index++;
                listaIduri.add(id1);
                //daca id-ul exista deja in lista, nr nodului ramane acelasi , adica pozitia pe care se gaseste id-ul in listaIduri
            } else {
                i = listaIduri.lastIndexOf(id1);
            }
            if (!listaIduri.contains(id2)) {
                j = index;
                index++;
                listaIduri.add(id2);
            } else {
                j = listaIduri.lastIndexOf(id2);
            }
            g.addEdge(i, j);
        }

    }

    public int numarComunitati() {
        creareGraf();
        return g.count();
    }

    public List<Utilizator> comunitateSociabila() {
        creareGraf();
        HashSet<Integer> comunitate = g.sociabila();
        List<Utilizator> utilizatori = new ArrayList<>();
        for (Integer nod : comunitate) {
            int id = listaIduri.get(nod);
            utilizatori.add(repoUtilizator.findOne((long) id));
        }
        return utilizatori;
    }

    public Iterable<Message> getMessages() {
        return repoMessage.findAll();
    }

    public long selectIdMessage() {
        long max = -1;
        for (Message m : repoMessage.findAll()) {
            if (m.getId() > max)
                max = m.getId();
        }
        return max + 1;
    }

    public Message addMessage(long idUtilizator, String iduri, String mesaj, LocalDateTime data) {
        Utilizator from = repoUtilizator.findOne(idUtilizator);
        List<Utilizator> to = new ArrayList<>();
        Arrays.stream(iduri.split("[ ]")).forEach(x -> to.add(repoUtilizator.findOne(Long.parseLong(x))));
        Message newMessage = new Message(from, to, mesaj, data);
        newMessage.setId(selectIdMessage());
        Message message = repoMessage.save(newMessage);
        return message;
    }

    public Message addReplyMessage(long idRaspuns, long idUtilizator, String mesaj, LocalDateTime data) {
        Message raspuns = repoMessage.findOne(idRaspuns);
        Utilizator from = repoUtilizator.findOne(idUtilizator);
        List<Utilizator> to = new ArrayList<>();
        to.add(repoUtilizator.findOne(raspuns.getFrom().getId()));
        Message newReplyMessage = new Message(from, to, mesaj, data);
        newReplyMessage.setId(selectIdMessage());
        newReplyMessage.setReply(raspuns);
        Message message = repoMessage.save(newReplyMessage);
        return message;
    }

    public List<Message> mesajeUtilizatori(long id1, long id2) {
        List<Message> mesaje = new ArrayList<>();
        Utilizator u1 = repoUtilizator.findOne(id1);
        Utilizator u2 = repoUtilizator.findOne(id2);
        for (Message m : repoMessage.findAll()) {
            if (u1 == m.getFrom() || m.getTo().contains(u1) && (u2 == m.getFrom() || m.getTo().contains(u2))) {
                mesaje.add(m);
            }
        }

        Collections.sort(mesaje, new Comparator<Message>() {
            public int compare(Message m1, Message m2) {
                return m1.getData().compareTo(m2.getData());
            }
        });

        return mesaje;

    }

    public Invitatie addInvitatie(long id1, long id2) {
        Utilizator u1 = repoUtilizator.findOne(id1);
        Utilizator u2 = repoUtilizator.findOne(id2);
        Invitatie invitatie = new Invitatie(u1, u2);
        long max = -1;
        for (Invitatie i : repoInvitatie.findAll()) {
            if (i.getId() > max)
                max = i.getId();
        }
        invitatie.setId(max + 1);
        return repoInvitatie.save(invitatie);
    }

    public void raspundeInvitatie(long id, String raspuns) {
        Status s;
        if (raspuns.equals("Respinge"))
            s = Status.rejected;
        else {
            s = Status.approved;
        }
        Invitatie i = repoInvitatie.findOne(id);
        i.setStatus(s);
        repoInvitatie.delete(id);
        repoInvitatie.save(i);
        if (s == Status.approved) {
            LocalDateTime data = LocalDateTime.now();
            Prietenie p = new Prietenie(data);
            long id1 = repoInvitatie.findOne(id).getU1().getId();
            long id2 = repoInvitatie.findOne(id).getU2().getId();
            p.setId(new Tuple<Long, Long>(id1, id2));
            repoFriends.save(p);
        }


    }

    public List<Invitatie> getInvitatiiPrimite(long id) {
        List<Invitatie> invitatiiPrimite = new ArrayList<>();
        for (Invitatie i : repoInvitatie.findAll()) {
            if (i.getU2().getId() == id)
                invitatiiPrimite.add(i);

        }
        return invitatiiPrimite;

    }

    public List<Invitatie> getInvitatii() {
        List<Invitatie> invitatii = new ArrayList<>();
        repoInvitatie.findAll().forEach(invitatii::add);
        return invitatii;

    }

    public Iterable<Utilizator> getAll() {
        return repoUtilizator.findAll();
    }

}
