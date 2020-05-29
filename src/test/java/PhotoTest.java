import com.cafuc.graduation.GraduationApplication;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.cafuc.graduation.user.service.IPhotoService;
import com.cafuc.graduation.user.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${photo.analysed_suffix}")
    private String analysedSuffix;

    @Value("${photo.clothes_suffix}")
    private String clothesSuffix;

    @Autowired
    private IUserService userService;

    @Test
    public void test() throws Exception {
        UserPo one = userService.getById(5);
        String replace = one.getAnalysedPhoto().replace(analysedSuffix, clothesSuffix);
        System.out.println(replace);
    }
}
