import com.cafuc.graduation.GraduationApplication;
import com.cafuc.graduation.user.service.IPhotoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 * test
 * </p>
 *
 * @author shijintao@supconit.com
 * @date 2020/5/25 12:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GraduationApplication.class)
public class PhotoTest {

    @Autowired
    private IPhotoService photoService;

    @Test
    public void test() throws Exception {
        Object o = null;
        boolean b = false && o.getClass() != null ? true : false;
        System.out.println(b);
    }
}
