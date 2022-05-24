import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static final String AUSTIN_POWERS = "Austin Powers";
    public static final String WEAPONS = "weapons";
    public static final String BANNED_SUBSTANCE = "banned substance";

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Solution.class.getName());

        Solution.Inspector inspector = new Solution.Inspector();
        Solution.Spy spy = new Solution.Spy(logger);
        Solution.Thief thief = new Solution.Thief(10000);
        Solution.MailService[] variousWorkers = new Solution.MailService[]{spy, thief, inspector};
        Solution.UntrustworthyMailWorker worker = new Solution.UntrustworthyMailWorker(variousWorkers);

        Solution.AbstractSendable[] correspondence = {
                new Solution.MailMessage("Oxxxymiron", "Гнойный", "Я здесь чисто по фану, поглумиться над слабым\n" +
                        "Ты же вылез из мамы под мой дисс на Бабана...."),
                new Solution.MailMessage("Гнойный", "Oxxxymiron", "....Что? Так болел за Россию, что на нервах терял ганглии.\n" +
                        "Но когда тут проходили митинги, где ты сидел? В Англии!...."),
                new Solution.MailMessage("Жриновский", AUSTIN_POWERS, "Бери пацанов, и несите меня к воде."),
                new Solution.MailMessage(AUSTIN_POWERS, "Пацаны", "Го, потаскаем Вольфовича как Клеопатру"),
                new Solution.MailPackage("берег", "море", new Solution.Package("ВВЖ", 32)),
                new Solution.MailMessage("NASA", AUSTIN_POWERS, "Найди в России ракетные двигатели и лунные stones"),
                new Solution.MailPackage(AUSTIN_POWERS, "NASA", new Solution.Package("рпакетный двигатель ", 2500000)),
                new Solution.MailPackage(AUSTIN_POWERS, "NASA", new Solution.Package("stones", 1000)),
                new Solution.MailPackage("Китай", "КНДР", new Solution.Package("banned substance", 99)),
                new Solution.MailPackage(AUSTIN_POWERS, "ИГИЛ (запрещенная группировка", new Solution.Package("tiny bomb", 9000)),
                new Solution.MailMessage(AUSTIN_POWERS, "Психиатр", "Помогите"),
        };
        Arrays.stream(correspondence).forEach(parcell -> {
            try {
                worker.processMail(parcell);
            } catch (Solution.StolenPackageException e) {
                logger.log(Level.WARNING, "Inspector found stolen package: " + e);
            } catch (Solution.IllegalPackageException e) {
                logger.log(Level.WARNING, "Inspector found illegal package: " + e);
            }
        });
    }
}
