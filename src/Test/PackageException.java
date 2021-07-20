package Test;

public class PackageException extends Exception{
    public IllegalPackageException(String message) {
        super(message);
    }

    public StolenPackageException(String message) {
        super(message);
    }
}
