import com.phoenix.model.App;
import com.phoenix.model.AppDetails;
import com.phoenix.model.Screenshot;
import com.phoenix.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Test {
    public static void main(String[] args) {

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        App a = s.find(App.class, "com.phoenix.app123");

        AppDetails appDetails=new AppDetails();
        appDetails.setApp(a);
        appDetails.setDescription("12dsij oisdjfod ofsd ofisdfsdos dusf gir");

        Screenshot ss=new Screenshot();
        ss.setScreenshot("sdfdsfsdfdfff");
        ss.setAppDetails(appDetails);
        Screenshot ss2=new Screenshot();
        ss2.setScreenshot("ss2");
        ss2.setAppDetails(appDetails);
        appDetails.setScreenshots(ss);
        appDetails.setScreenshots(ss2);

        a.setAppDetails(appDetails);

        Transaction ta=s.beginTransaction();

        try{
            s.merge(a);
            ta.commit();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
