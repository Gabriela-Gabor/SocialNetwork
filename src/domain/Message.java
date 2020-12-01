package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class Message extends Entity<Long> {

    private Utilizator from;
    private List<Utilizator> to;
    private String message;
    private LocalDateTime data;
    private Message reply;

    public Message(Utilizator from, List<Utilizator> to, String message, LocalDateTime data) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        reply = null;
    }

    public Utilizator getFrom() {
        return from;
    }

    public List<Utilizator> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Message getReply() { return reply; }

    public void setReply(Message reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        String toString = "";
        if (to.isEmpty()) {
            toString = "null";
        } else {
            for (Utilizator u : getTo())
                toString += u.getId() + ",";
        }
        if (reply == null)
            return "Message{" + "From= " + from.getId() + ",To= " + toString + " message= " + message +
                    ", date= " + data.format((DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))) + '}';

        return "Message{" + "From= " + from.getId() + ",To= " + toString + " message= " + message + ", date= " +
                data.format((DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))) + " } -> Replymessage= " + reply.toString();

    }


}
