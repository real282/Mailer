package Test;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainMail {

    public static final String AUSTIN_POWERS = "Austin Powers";
    public static final String WEAPONS = "weapons";
    public static final String BANNED_SUBSTANCE = "banned substance";

    public static void main(String[] args) {
        System.out.println("Старт обрабокти почты!");

        MailMessage mailMessage1 = new MailMessage(AUSTIN_POWERS, "you", "TEST");
        MailService spy = new Spy(Logger.getLogger(MailService.class.getName()));
        MailService thief = new Thief(100);
        MailService inspector = new Inspector();

        MailService untrustworthyMailWorker = new UntrustworthyMailWorker(new MailService[]{spy, thief, inspector});


        MailPackage mailPackage = new MailPackage("meT", "youT", new Package("RAKETA", 100));

        MailPackage mailPackage1 = new MailPackage("meT", "youT", new Package("RAKETA", 1000));
        untrustworthyMailWorker.processMail(mailMessage1);
        untrustworthyMailWorker.processMail(mailPackage1);
    }
    /*
Интерфейс, который задает класс, который может каким-либо образом обработать почтовый объект.
*/
    public static interface MailService {
        Sendable processMail(Sendable mail);
    }

    /*
    Класс, в котором скрыта логика настоящей почты
    */
    public static class RealMailService implements MailService {

        @Override
        public Sendable processMail(Sendable mail) {
            System.out.println("Отработано почтой");
            return mail;
        }
    }

    /*
    UntrustworthyMailWorker – класс, моделирующий ненадежного работника почты, который вместо того,
    чтобы передать почтовый объект непосредственно в сервис почты, последовательно передает этот
    объект набору третьих лиц, а затем, в конце концов, передает получившийся объект непосредственно
    экземпляру RealMailService. У UntrustworthyMailWorker должен быть конструктор от массива MailService
    (результат вызова processMail первого элемента массива передается на вход processMail второго элемента, и т. д.)
    и метод getRealMailService, который возвращает ссылку на внутренний экземпляр RealMailService,
    он не приходит масивом в конструкторе и не настраивается извне класса.
     */
    public static class UntrustworthyMailWorker implements MailService {
        MailService[] mailServices;
        Sendable mailProcessing;
        RealMailService realMailService = new RealMailService();

        public UntrustworthyMailWorker(MailService[] mailServices) {
            this.mailServices = mailServices;
        }

        @Override
        public Sendable processMail(Sendable mail) {
            mailProcessing = mail;
            for (int i = 0; i < mailServices.length; i++) {
                mailProcessing = mailServices[i].processMail(mailProcessing);
            }
            mailProcessing = realMailService.processMail(mailProcessing);
            return mailProcessing;
        }

        public RealMailService getRealMailService() {
            return realMailService;
        }
    }

    public static class Spy implements MailService {
        Logger LOGGER;
        String from = null;
        String to = null;
        String message = null;


        public Spy(Logger LOGGER) {
            this.LOGGER = LOGGER;
        }

        @Override
        public Sendable processMail(Sendable mail) {
            if (mail instanceof MailMessage) {
                message = ((MailMessage) mail).getMessage();
                if (((from = (mail.getFrom())) == AUSTIN_POWERS) | ((to = (mail.getTo())) == AUSTIN_POWERS)) {
                    LOGGER.log(Level.WARNING, "Detected target mail correspondence: from {0} to {1} \"{2}\"",
                            new Object[]{from, to, message});

                } else {
                    LOGGER.log(Level.INFO, "Usual correspondence: from {0} to {1}",
                            new Object[]{from, to});
                }
            }
            return mail;
        }
    }

    public static class Thief implements MailService {
        int price;
        int stolenValue;

        public Thief(int price) {
            this.price = price;
        }

        @Override
        public Sendable processMail(Sendable mail) {
            if (mail instanceof MailPackage) {
                MailPackage mailPackage = (MailPackage) mail;
                Package mPackage = mailPackage.getContent();
                if (mPackage.getPrice() >= price) {
                    stolenValue += mPackage.getPrice();
                    return new MailPackage(mail.getFrom(), mailPackage.getTo(), new Package(String.format("stones instead of %s", mPackage.getContent()), 0));
                }
            }
            return mail;
        }

        public int getStolenValue() {
            return stolenValue;
        }
    }

    public static class Inspector implements MailService {

        @Override
        public Sendable processMail(Sendable mail) {
            if (mail instanceof MailPackage) {
                MailPackage mailPackage = (MailPackage) mail;
                Package aPackage = mailPackage.getContent();
                try {
                    if (aPackage.getContent().contains("stones")) {
                        throw new StolenPackageException("stones");
                    } else if ((aPackage.getContent().contains(WEAPONS)) | aPackage.getContent().contains(BANNED_SUBSTANCE)) {
                        throw new IllegalPackageException("prohibited");
                    }

                } catch (Exception e) {
                    throw e;
                }
            }

            return mail;
        }
    }

    public static class IllegalPackageException extends RuntimeException {
        public IllegalPackageException(String message) {
            super(message);
        }
    }

    public static class StolenPackageException extends RuntimeException {
        public StolenPackageException(String message) {
            super(message);
        }
    }

}
