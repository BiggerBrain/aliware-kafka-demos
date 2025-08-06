package publicnet;

public enum RegionEnum {
    gz("1", "广州"),
    tj("3", "北京(北京一区 原天津)"),
    sh("4", "上海"),
    hk("5", "香港"),
    ca("6", "多伦多"),
    shjr("7", "上海金融"),
    bj("8", "北京"),
    sg("9", "新加坡"),
    szjr("11", "深圳金融"),
    gzopen("12", "广州Open"),
    usw("15", "美国硅谷"),
    cd("16", "成都"),
    de("17", "法兰克福"),
    kr("18", "首尔"),
    cq("19", "重庆"),
    in("21", "印度"),
    use("22", "弗吉尼亚"),
    th("23", "泰国"),
    ru("24", "欧洲东北（原莫斯科）"),
    jp("25", "日本"),
    jnec("31", "济南"),
    hzec("32", "杭州"),
    nj("33", "南京"),
    fzec("34", "福州"),
    whec("35", "武汉"),
    tsn("36", "天津"),
    szx("37", "深圳"),
    tpe("39", "台北"),
    dub("41", "迪拜"),
    la("42", "洛杉矶"),
    sl_sao("43", "巴西圣保罗互联区"),
    syd("44", "悉尼"),
    csec("45", "长沙"),
    bjjr("46", "北京金融"),
    others("47", "其他"),
    sjwec("53", "石家庄"),
    qy("54", "清远"),
    hfeec("55", "合肥"),
    sheec("56", "沈阳"),
    xiyec("57", "西安"),
    xbec("58", "西北"),
    cgoec("71", "郑州"),
    jkt("72", "雅加达"),
    qyxa("73", "清远信安"),
    sao("74", "圣保罗"),
    gy("76", "贵阳"),
    szsycft("77", "深圳深宇财付通"),
    shadc("78", "上海自动驾驶云"),
    szjrtce("79", "深圳金融专区"),
    shjrtce("81", "上海金融专区"),
    ;

    public static void main(String[] args) {
        System.out.println(bjjr.toString());
    }

    private String regionId;
    private String regionName;

    RegionEnum(String regionId, String regionName) {
        this.regionId = regionId;
        this.regionName = regionName;
    }

    public String getRegionId() {
        return regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public static String getRegionNameByRegionId(String regionId) {
        for (RegionEnum region : RegionEnum.values()) {
            if (region.getRegionId().equals(regionId)) {
                return region.getRegionName();
            }
        }
        return null;
    }
}