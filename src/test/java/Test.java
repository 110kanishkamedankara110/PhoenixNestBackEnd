import com.phoenix.model.App;
import com.phoenix.model.AppDetails;
import com.phoenix.model.Category;
import com.phoenix.model.Screenshot;
import com.phoenix.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.LinkedList;
import java.util.List;

public class Test {
    public static void main(String[] args) {

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        App a2 = s.find(App.class, "com.phoenix.noteApp");
        App a3 = s.find(App.class, "cvcvcvcv");


        Category c = s.find(Category.class, "1");
        Category c2 = s.find(Category.class, "2");
        Category c3 = s.find(Category.class, "3");

        List<Category> l = new LinkedList();
        l.add(c);
        l.add(c2);


        AppDetails a = new AppDetails();



        a.setApp(a2);
        a.setDescription("assddsadada");

        a.setCategories(l);
        c.setAppDetails(a);
        c2.setAppDetails(a);

        a2.setAppDetails(a);


        Transaction ta = s.beginTransaction();

        try {
            s.merge(a2);
            ta.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
