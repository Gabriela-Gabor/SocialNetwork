package domain;

public class Invitatie extends Entity<Long>{

    private Status status;
    private Utilizator u1;
    private Utilizator u2;

    public Invitatie(Utilizator u1, Utilizator u2) {
        this.u1 = u1;
        this.u2 = u2;
        status=Status.pending;
    }

    public Status getStatus() {
        return status;
    }

    public Utilizator getU1() {
        return u1;
    }

    public Utilizator getU2() {
        return u2;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString(){

        return "Invitatie{id= "+getId()+", De la= "+getU1().getId()+", "+getU1().getFirstName()+" "+getU1().getLastName()
                +", Pentru= "+getU2().getId()+", "+getU2().getFirstName()+" "+getU2().getLastName()+ ", Status= "+getStatus()+" }";
    }
}
