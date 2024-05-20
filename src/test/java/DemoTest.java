import com.api.wq.biz.dto.DemoRequestDto;
import com.api.wq.helper.ProtocolCreatorHelper;
import com.api.wq.protocol.IProtocol;
import org.testng.annotations.Test;

public class DemoTest {

    @Test
    public void testApi(){
        DemoRequestDto demoRequestDto = new DemoRequestDto();
        demoRequestDto.setOrgName("");
        IProtocol iProtocol = ProtocolCreatorHelper.createDefaultGetHttp();
        String res = iProtocol.send(demoRequestDto);
        System.out.println(res);
    }
}
