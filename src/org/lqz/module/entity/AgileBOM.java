package org.lqz.module.entity;

public class AgileBOM {
    private int id,Level;
    private String Number,Description,MaterialDesc;
    private String Version;

    public AgileBOM(int id, int level, String number,  String description ) {
        this.id = id;
        Level = level;
        Number = number;
        Description = description;
    }

    public AgileBOM(int id, int level, String number, String description,String materialdesc,  String version) {
        this.id = id;
        Level = level;
        Number = number;
        Description = description;
        MaterialDesc=materialdesc;
        Version=version;
    }

    public static AgileBOM CreatRowBOMQuery(Object[] row) {
        //要先检查数组长度
        int id = Integer.valueOf((Integer) row[0]);
        int level = Integer.valueOf((String) row[1]);
        String number = (String) row[2];
        String description = (String) row[3];
        String materialdesc = (String) row[4];
        String version = (String) row[5];
        AgileBOM AB = new AgileBOM(id, level, number, description,materialdesc,version);

        return AB;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String toString() {
        //      AgileBOM(id-0, level-1, number-2, part_Class-3, description-4, part_Type-5, PPlatform_Affected-6,
//                design_Type-7, rev-8, LCycle_Phase-9, rev_Date-10, rev_Incorp_Date-11, effect_Date-12)
       // String s="id-"+this.getId()+"  level-"+this.getLevel()+"  number-"+this.getNumber()+"  desc-"+this.getDescription();
        String s=this.getNumber()+"  "+this.getDescription();
        return s;
    }
    public String AgileBOMtoString() {
        String s="ID: "+this.getId()+ "      Level: "+this.getLevel()
                +"\nNumber: "+this.getNumber()
                +"\nDescription: "+this.getDescription()
                +"\nMaterial_Number_Desc: "+this.MaterialDesc
                +"\nNew_Version: "+this.Version;
        return s;
    }
}
