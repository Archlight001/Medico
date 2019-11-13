package denokela.com.medico;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    //Entity Class for the User Database

    public User(String firstName, String lastName, String age){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }


    @PrimaryKey(autoGenerate = true)
    private int Userid;

    @ColumnInfo(name = "FirstName")
    String firstName;

    @ColumnInfo(name = "LastName")
    String lastName;

    @ColumnInfo(name = "Age")
    String age;

    public int getUserid() {
        return Userid;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
