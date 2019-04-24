package atmmachine.infrastructure;

import atmmachine.domain.model.entities.Session;

public class EmailSender {

    private static EmailSender instance;

    public static synchronized EmailSender getInstance() {
        if(instance == null) {
            instance = new EmailSender();
        }
        return instance;
    }

    public void sendEmailForSession(Session session) {

    }

}
