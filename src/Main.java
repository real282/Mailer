import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static final String AUSTIN_POWERS = "Austin Powers";
    public static final String WEAPONS = "weapons";
    public static final String BANNED_SUBSTANCE = "banned substance";

    public static void main(String[] args) {
        System.out.println("Старт обрабокти почты!");
        MailMessage mailMessage1 = new MailMessage("me", "you", "TEST");
        MailService spy = new Spy(Logger.getLogger(MailService.class.getName()));
        //MailService spy = new Spy();
        spy.processMail(mailMessage1);
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
    UntrustworthyMailWorker – класс, моделирующий ненадежного работника почты, который вместо того, чтобы передать
    почтовый объект непосредственно в сервис почты, последовательно передает этот объект набору третьих лиц, а затем,
    в конце концов, передает получившийся объект непосредственно экземпляру RealMailService.
    У UntrustworthyMailWorker должен быть конструктор от массива MailService (результат вызова processMail первого
    элемента массива передается на вход processMail второго элемента, и т. д.) и метод getRealMailService,
    который возвращает ссылку на внутренний экземпляр RealMailService, он не приходит масивом в конструкторе и
    не настраивается извне класса.
     */
    public static class UntrustworthyMailWorker implements MailService {

        @Override
        public Sendable processMail(Sendable mail) {
            return null;
        }
    }

    /*
    Spy - шпион, который логгирует о всей почтовой переписке, которая проходит через его руки.
    Объект конструируется от экземпляра Logger, с помощью которого шпион будет сообщать о всех действиях.
    Он следит только за объектами класса MailMessage и пишет в логгер следующие сообщения (в выражениях нужно
    заменить части в фигурных скобках на значения полей почты):
        2.1) Если в качестве отправителя или получателя указан "Austin Powers", то нужно написать в лог сообщение
        с уровнем WARN: Detected target mail correspondence: from {from} to {to} "{message}"
        2.2) Иначе, необходимо написать в лог сообщение с уровнем INFO: Usual correspondence: from {from} to {to}
     */

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
                    LOGGER.log(Level.WARNING, "Detected target mail correspondence: from {0} to {1} \" {2}\"",
                            new Object[]{from, to, message});

                } else {
                    LOGGER.log(Level.INFO, "Usual correspondence: from {0} to {1}",
                            new Object[]{from, to});
                }
            }
            return mail;
        }
    }

    /*
    Thief – вор, который ворует самые ценные посылки и игнорирует все остальное.
    Вор принимает в конструкторе переменную int – минимальную стоимость посылки, которую он будет воровать.
    Также, в данном классе должен присутствовать метод getStolenValue, который возвращает суммарную стоимость
    всех посылок, которые он своровал. Воровство происходит следующим образом: вместо посылки, которая пришла вору,
    он отдает новую, такую же, только с нулевой ценностью и содержимым посылки "stones instead of {content}".
     */
    public static class Thief implements MailService {

        @Override
        public Sendable processMail(Sendable mail) {
            return null;
        }
    }
  /*
  Inspector – Инспектор, который следит за запрещенными и украденными посылками и бьет тревогу в виде исключения,
  если была обнаружена подобная посылка. Если он заметил запрещенную посылку с одним из запрещенных содержимым
  ("weapons" и "banned substance"), то он бросает IllegalPackageException. Если он находит посылку,
  состоящую из камней (содержит слово "stones"), то тревога прозвучит в виде StolenPackageException.
  Оба исключения вы должны объявить самостоятельно в виде непроверяемых исключений.
   */
    public static class Insprctor implements MailService {

        @Override
      public Sendable processMail(Sendable mail) {
          return null;
      }
  }

}
