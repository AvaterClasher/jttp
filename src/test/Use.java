package test;
import jttp.Jttp;
import java.io.IOException;

public class Use {
    public static void main(String[] args) throws IOException {
        Jttp jttp = new Jttp();
        jttp.use(Jttp.statics("C:\\Users\\KIIT\\Desktop\\dev\\jttp\\src\\test\\static_files"));
        jttp.listen(() -> System.out.println("Server is listening!"));
    }
}
