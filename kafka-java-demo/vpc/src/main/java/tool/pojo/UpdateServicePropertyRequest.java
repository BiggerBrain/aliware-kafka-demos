package tool.pojo;

import java.util.List;

public class UpdateServicePropertyRequest {
    public String Action = "UpdateServicePropertyInternal";
    public String Version = "2017-03-12";
    public String Region;
    public List<VpcOssServicePropertyRequest> ServicePropertyRequestSet;
}
