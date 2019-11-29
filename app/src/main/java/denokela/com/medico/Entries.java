package denokela.com.medico;


import java.io.Serializable;

public class Entries implements Serializable {

    public Entries(String Name, Integer Percent) {
        this.Name = Name;
        this.Percent = Percent;
    }

    private String Name;
    private Integer Percent;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getPercent() {
        return Percent;
    }

    public void setPercent(Integer percent) {
        Percent = percent;
    }

}
