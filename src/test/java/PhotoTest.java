import com.cafuc.graduation.GraduationApplication;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.cafuc.graduation.user.service.IPhotoService;
import com.cafuc.graduation.user.service.IUserService;
import io.swagger.models.auth.In;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

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

    @Test
    public void testResourceLoader() throws IOException {
        String ymlPath = "/Users/taylor/springboot/graduation/src/main/resources/application.yml";


        FileSystemResourceLoader fs = new FileSystemResourceLoader();
        Resource resource = fs.getResource(ymlPath);

        InputStream inputStream1 = Files.newInputStream(Paths.get(StringUtils.cleanPath(ymlPath)));

        char[] chars = new char[1024];
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream1);
        while (inputStreamReader.read(chars) != -1) {
            for (char c : chars) {
                System.out.print(c);
            }
        }
    }
}
