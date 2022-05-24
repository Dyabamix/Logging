import java.util.logging.*;

/**
 * Created by kvu on 16.08.2017.
 */
public class Solution {
    /**
     * проверка почтовой машины к уроку 4.9
     */

    /**
     * Здесь пишите Ваш код
     *              *
     *              *
     *            ******
     *             ****
     * *            **
     */
//Stepik code: start

    // ------======== Thief =======---------
    public static class Thief implements MailService {
        private static int stolenValue;
        private static int minValue;

        public Thief(int m) {
            minValue = m;
        }

        public int getStolenValue() {
            // your code here...
            return stolenValue;
        }

        @Override
        public Sendable processMail(Sendable mail) {
            // "stones instead of {content}"
            Sendable newMail = mail;
            int valuePriceMail;
            if (mail instanceof MailPackage &&
                    (valuePriceMail = ((MailPackage) mail).getContent().getPrice()) >= minValue){
                stolenValue += valuePriceMail;
                newMail = new MailPackage(mail.getFrom(),
                        mail.getTo(),
                        new Package("stones instead of " + ((MailPackage) mail).getContent().getContent(),0));
            }

            return newMail;
        }

    }

    // ------======== Spy =======---------
    public static class Spy implements MailService {
        private final Logger logger;

        public Spy(Logger logger) {
            // your code here...
            this.logger = logger;
        }

        @Override
        public Sendable processMail(Sendable mail) {
            if (mail instanceof MailMessage &&
                    (mail.getFrom().contains("Austin Powers") || mail.getTo().contains("Austin Powers"))){
                logger.log(Level.WARNING, "Detected target mail correspondence: from " +
                        mail.getFrom() +
                        " to " +
                        mail.getTo() +
                        " \"" +
                        ((MailMessage) mail).message +
                        "\"",
                        new Object[]{mail.getFrom(), mail.getTo()});
            } else {
                logger.log(Level.INFO, "Usual correspondence: from " +
                        mail.getFrom() +
                        " to " +
                        mail.getTo());
            }
            return mail;
        }
    }

    // ------======== Inspector =======---------
    public static class IllegalPackageException extends RuntimeException {
        public IllegalPackageException() {
        }

        public IllegalPackageException (String message) {
        }
    }

    public static class StolenPackageException extends RuntimeException {
        public StolenPackageException() {
        }

        public StolenPackageException (String message) {
        }
    }

    public static class Inspector implements MailService {

        @Override
        public Sendable processMail(Sendable mail) { //weapons" и "banned substance
            if (mail instanceof MailPackage &&
                    (((MailPackage) mail).getContent().getContent().contains("weapons") ||
                            ((MailPackage) mail).getContent().getContent().contains("banned substance"))){
                throw new IllegalPackageException();
            } else if (mail instanceof MailPackage &&
                    ((MailPackage) mail).getContent().getContent().contains("stones")){
                throw new StolenPackageException();
            }
            return mail;
        }
    }

    // ------======== Ненадёжный сотрудник =======---------
    public static class UntrustworthyMailWorker implements MailService {
        private static MailService[] workers;
        private static final RealMailService realWorker = new RealMailService();

        public UntrustworthyMailWorker (MailService[] w){
            workers = w;
        }

        public MailService getRealMailService() {
            return realWorker;
        }

        @Override
        public Sendable processMail(Sendable mail) {
            Sendable launchedInACircle = null;

            for (MailService ms : workers){
                launchedInACircle = ms.processMail(mail);
            }

            return realWorker.processMail(launchedInACircle);
        }
    }


    ////Stepik code: end

    /**
     *              **
     *             ****
     *            ******
     *               *
     *               *
     * Запускать на исполнение класс main. он ниже.
     */




    /*
    Интерфейс: сущность, которую можно отправить по почте.
    У такой сущности можно получить от кого и кому направляется письмо.
    */
    public interface Sendable {
        String getFrom();

        String getTo();
    }

    /*
Абстрактный класс,который позволяет абстрагировать логику хранения
источника и получателя письма в соответствующих полях класса.
*/
    public static abstract class AbstractSendable implements Sendable {

        protected final String from;
        protected final String to;

        public AbstractSendable(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String getFrom() {
            return from;
        }

        @Override
        public String getTo() {
            return to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AbstractSendable that = (AbstractSendable) o;

            if (!from.equals(that.from)) return false;
            return to.equals(that.to);
        }

    }

    /*
Письмо, у которого есть текст, который можно получить с помощью метода `getMessage`
*/
    public static class MailMessage extends AbstractSendable {

        private final String message;

        public MailMessage(String from, String to, String message) {
            super(from, to);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            MailMessage that = (MailMessage) o;

            return message != null ? message.equals(that.message) : that.message == null;
        }

    }

    /*
Посылка, содержимое которой можно получить с помощью метода `getContent`
*/
    public static class MailPackage extends AbstractSendable {
        private final Package content;

        public MailPackage(String from, String to, Package content) {
            super(from, to);
            this.content = content;
        }

        public Package getContent() {
            return content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            MailPackage that = (MailPackage) o;

            return content.equals(that.content);
        }

    }

    /*
Класс, который задает посылку. У посылки есть текстовое описание содержимого и целочисленная ценность.
*/
    public static class Package {
        private final String content;
        private final int price;

        public Package(String content, int price) {
            this.content = content;
            this.price = price;
        }

        public String getContent() {
            return content;
        }

        public int getPrice() {
            return price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Package aPackage = (Package) o;

            if (price != aPackage.price) return false;
            return content.equals(aPackage.content);
        }
    }

    /*
Интерфейс, который задает класс, который может каким-либо образом обработать почтовый объект.
*/
    public interface MailService {
        Sendable processMail(Sendable mail);
    }

    /*
    Класс, в котором скрыта логика настоящей почты
    */
    public static class RealMailService implements MailService {

        @Override
        public Sendable processMail(Sendable mail) {
            // Здесь описан код настоящей системы отправки почты.
            return mail;
        }
    }

}
