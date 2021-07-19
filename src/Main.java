public class Main {
    public static void main(String[] args) {

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

    }


}
